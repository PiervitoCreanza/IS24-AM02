package it.polimi.ingsw.network.client;

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
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIConnection implements Connection, PropertyChangeListener {

    private static final Logger logger = LogManager.getLogger(RMIConnection.class);
    private final ClientNetworkControllerMapper networkControllerMapper;
    private final String serverIp;
    private final int serverPort;
    private final String clientIp;
    /**
     * Listeners that will be notified when a message is received.
     */
    private final PropertyChangeSupport listeners;
    private int clientPort;

    public RMIConnection(ClientNetworkControllerMapper clientNetworkControllerMapper, String serverIp, int serverPort, String clientIp, int clientPort) {
        this.networkControllerMapper = clientNetworkControllerMapper;
        this.serverIp = serverIp;
        this.clientIp = clientIp;
        this.serverPort = serverPort;
        this.clientPort = clientPort;
        this.listeners = new PropertyChangeSupport(this);
    }

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
                RMIClientSender rmiClientSender = new RMIClientSender(serverStub, clientStub);
                // Add the listener to the sender and receiver
                rmiClientReceiver.addPropertyChangeListener(rmiClientSender);
                rmiClientSender.addPropertyChangeListener(this);
                // Path the sender to the mapper
                networkControllerMapper.setMessageHandler(rmiClientSender);
                break;
            } catch (RemoteException e1) {
                if (e1.getCause() != null && e1.getCause().getClass().equals(BindException.class)) {
                    logger.warn("Port already in use: {}. Trying with: {}", clientPort, ++clientPort);
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
        listeners.firePropertyChange("CONNECTION_ESTABLISHED", null, null);
    }

    @Override
    public void quit() {
        System.exit(0);
    }

    /**
     * This method gets called when a bound property is changed.
     * Is Synchronized because it can be notified by the heartbeat from the sender and the receiver
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
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
     * @param listener The PropertyChangeListener to be added.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener from the listeners list.
     * The listener will no longer be notified of property changes.
     *
     * @param listener The PropertyChangeListener to be removed.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.removePropertyChangeListener(listener);
    }
}

