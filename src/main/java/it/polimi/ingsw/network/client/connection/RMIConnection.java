package it.polimi.ingsw.network.client.connection;

import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.client.RMI.RMIClientReceiver;
import it.polimi.ingsw.network.client.RMI.RMIClientSender;
import it.polimi.ingsw.network.client.actions.RMIServerToClientActions;
import it.polimi.ingsw.network.server.actions.RMIClientToServerActions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.BindException;
import java.net.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * The RMIConnection class implements the Connection interface using the Java RMI technology.
 * It also implements the PropertyChangeListener and PropertyChangeNotifier interfaces to provide property change support.
 * It is responsible for establishing a connection between the client and the server.
 */
public class RMIConnection implements Connection, PropertyChangeListener {

    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(RMIConnection.class);
    /**
     * The network controller mapper.
     */
    private final ClientNetworkControllerMapper networkControllerMapper;
    /**
     * The server IP.
     */
    private final String serverIp;
    /**
     * The server port.
     */
    private final int serverPort;
    /**
     * The client IP.
     */
    private final String clientIp;

    /**
     * Listeners that will be notified when a message is received.
     */
    private final PropertyChangeSupport listeners;
    /**
     * The client port.
     */
    private int clientPort;

    /**
     * Creates a new RMIConnection.
     *
     * @param clientNetworkControllerMapper The network controller mapper
     * @param serverIp                      The server IP
     * @param serverPort                    The server port
     * @param clientIp                      The client IP
     * @param clientPort                    The client port
     */
    public RMIConnection(ClientNetworkControllerMapper clientNetworkControllerMapper, String serverIp, int serverPort, String clientIp, int clientPort) {
        this.networkControllerMapper = clientNetworkControllerMapper;
        this.serverIp = serverIp;
        this.clientIp = clientIp;
        this.serverPort = serverPort;
        this.clientPort = clientPort;
        this.listeners = new PropertyChangeSupport(this);
    }

    /**
     * Establishes a connection.
     * It tries to connect to the server and, if it fails, it retries for a maximum of 5 times
     * with a 5-second delay between each attempt.
     * If it is not possible to connect after 5 attempts, the program will be closed.
     * If the port is already in use, it will try with the next port until it finds an available one.
     * It locates the stub of the server and creates a new client stub that will be sent later to the server.
     */
    @Override
    public void connect() {
        System.setProperty("java.rmi.server.hostname", clientIp);
        int attempts = 1;
        int maxAttempts = 5;
        long waitTime = 5000;
        while (attempts <= maxAttempts) {
            try {
                RMIClientReceiver rmiClientReceiver = new RMIClientReceiver(networkControllerMapper);
                //Client as a client, getting the registry
                Registry registry = LocateRegistry.getRegistry(serverIp, serverPort);
                RMIServerToClientActions clientStub = (RMIServerToClientActions) UnicastRemoteObject.exportObject(rmiClientReceiver, clientPort);
                // Looking up the registry for the remote object
                RMIClientToServerActions serverStub = (RMIClientToServerActions) registry.lookup("ClientToServerActions");
                // Check if the connection works
                serverStub.heartbeat();
                RMIClientSender rmiClientSender = new RMIClientSender(serverStub, clientStub);
                // Add the listener to the sender
                rmiClientSender.addPropertyChangeListener(this);
                // Set the message handler in the mapper
                networkControllerMapper.setMessageHandler(rmiClientSender);
                break;
            } catch (RemoteException e1) {
                if (e1.getCause() != null && e1.getCause().getClass().equals(BindException.class)) {
                    logger.warn("Port {} already in use. Trying with: {}", clientPort, ++clientPort);
                } else if (e1.getCause() != null && e1.getCause().getClass().equals(ConnectException.class)) {
                    logger.warn("Can't call methods on the server. Port forwarding might be needed. Exiting...");
                    quit();
                } else {
                    logger.warn("RMI server unreachable, retrying in {} seconds. Attempt {} out of {}", (waitTime / 1000), attempts, maxAttempts);
                    attempts++;
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e2) {
                        logger.fatal("Error while sleeping");
                        quit();
                    }
                }
            } catch (NotBoundException e) {
                logger.fatal("Error looking up the registry for the remote object");
                quit();
            }
        }
        if (attempts > maxAttempts) {
            logger.fatal("Could not connect to the server. Exiting...");
            quit();
        }
        logger.info("RMI connection established");
        this.listeners.firePropertyChange("CONNECTION_ESTABLISHED", null, null);
    }

    /**
     * Closes the program when it's impossible to establish a connection.
     */
    @Override
    public void quit() {
        System.exit(-1);
    }

    /**
     * This method is called when a property change event is fired.
     * It is responsible for handling the property change event.
     *
     * @param evt The property change event that was fired
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String changedProperty = evt.getPropertyName();
        if (changedProperty.equals("CONNECTION_CLOSED")) {
            connect();
        } else {
            logger.warn("Unknown property change event: {}", changedProperty);
        }
    }

    /**
     * Adds a PropertyChangeListener to the listeners list.
     * The listener will be notified of property changes.
     *
     * @param listener The PropertyChangeListener to be added
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener from the listeners list.
     * The listener will no longer be notified of property changes.
     *
     * @param listener The PropertyChangeListener to be removed
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.removePropertyChangeListener(listener);
    }
}

