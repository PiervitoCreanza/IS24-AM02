package it.polimi.ingsw.network.client.connection;

import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.client.RMI.RMIClientReceiver;
import it.polimi.ingsw.network.client.RMI.RMIClientSender;
import it.polimi.ingsw.network.client.actions.RMIServerToClientActions;
import it.polimi.ingsw.network.server.actions.RMIClientToServerActions;
import org.apache.logging.log4j.LogManager;

import java.beans.PropertyChangeListener;
import java.net.BindException;
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
public class RMIConnection extends Connection implements PropertyChangeListener {

    /**
     * The client IP.
     */
    private final String clientIp;

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
        super(clientNetworkControllerMapper, serverIp, serverPort);
        this.clientIp = clientIp;
        this.clientPort = clientPort;
        logger = LogManager.getLogger(RMIConnection.class);
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
    protected void connectionSetUp() {
        System.setProperty("java.rmi.server.hostname", clientIp);
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
               /* } else if (e1.getCause() != null && e1.getCause().getClass().equals(ConnectException.class)) {
                    e1.printStackTrace();
                    logger.warn("Can't call methods on the server. Port forwarding might be needed. Exiting...");
                    quit();*/
                } else {
                    String retry = "RMI server unreachable, retrying in " + (waitTime / 1000) + " seconds. Attempt " + attempts + " out of " + maxAttempts + "...";
                    logger.warn(retry);
                    this.listeners.firePropertyChange("CONNECTION_RETRY", null, retry);
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
}

