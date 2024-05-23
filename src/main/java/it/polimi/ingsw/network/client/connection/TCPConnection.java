package it.polimi.ingsw.network.client.connection;

import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.client.TCP.TCPClientAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.IllegalBlockingModeException;

/**
 * The TCPConnection class implements the Connection interface using the TCP protocol.
 * It also implements the PropertyChangeListener and PropertyChangeNotifier interfaces to provide property change support.
 * It is responsible for establishing a connection between the client and the server.
 */
public class TCPConnection implements Connection, PropertyChangeListener {

    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(TCPConnection.class);
    /**
     * The network controller mapper.
     */
    private final ClientNetworkControllerMapper networkControllerMapper;
    /**
     * The server IP.
     */
    private final String serverIp;
    /**
     * Listeners that will be notified when a message is received.
     */
    private final PropertyChangeSupport listeners;
    /**
     * The server port.
     */
    private final int serverPort;

    /**
     * Creates a new TCPConnection.
     *
     * @param networkControllerMapper The network controller mapper
     * @param serverIp                The server IP
     * @param serverPort              The server port
     */
    public TCPConnection(ClientNetworkControllerMapper networkControllerMapper, String serverIp, int serverPort) {
        this.networkControllerMapper = networkControllerMapper;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.listeners = new PropertyChangeSupport(this);
    }

    /**
     * Establishes a connection.
     * It tries to connect to the server and, if successful, creates a TCPClientAdapter
     * to handle the communication with the server.
     * If the connection fails, it retries up to 5 times with a 5-second delay between each attempt.
     * If it is not possible to connect after 5 attempts, the program will be closed.
     */
    @Override
    public void connect() {
        int attempts = 1;
        int maxAttempts = 5;
        long waitTime = 5000;
        while (attempts <= maxAttempts) {
            try {
                Socket serverSocket = new Socket();
                serverSocket.connect(new InetSocketAddress(serverIp, serverPort), 5000);
                // Pass the mapper to the TCPAdapter in order to be notified when messages arrive.
                TCPClientAdapter clientAdapter = new TCPClientAdapter(serverSocket, networkControllerMapper);
                clientAdapter.addPropertyChangeListener(this);
                // Pass the TCPAdapter to the mapper in order to send messages
                networkControllerMapper.setMessageHandler(clientAdapter);
                break;
            } catch (IOException e) {
                logger.warn("TCP server unreachable, retrying in {} seconds. Attempt {} out of {}", (waitTime / 1000), attempts, maxAttempts);
            } catch (IllegalBlockingModeException e) {
                logger.fatal("Socket already associated with a channel");
                quit();
            }
            attempts++;
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                logger.fatal("Error while sleeping");
            }
        }
        if (attempts > maxAttempts) {
            logger.fatal("Could not connect to the server. Exiting...");
            quit();
        }
        logger.info("TCP connection established");
        notify("CONNECTION_ESTABLISHED", null);
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
     * @param listener The PropertyChangeListener to be added.
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener from the listeners list.
     * The listener will no longer be notified of property changes.
     *
     * @param listener The PropertyChangeListener to be removed.
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.removePropertyChangeListener(listener);
    }

    /**
     * Notifies all listeners about the change of a property.
     * The PropertyChangeListeners firePropertyChange methods will be called.
     *
     * @param propertyName The name of the property that was changed
     * @param message      The new value of the property
     */
    @Override
    public void notify(String propertyName, Object message) {
        this.listeners.firePropertyChange(propertyName, null, message);
    }

    /**
     * Notifies all listeners about the change of a property.
     * The PropertyChangeListeners firePropertyChange methods will be called.
     *
     * @param propertyName The name of the property that was changed
     * @param oldMessage   The old value of the property
     * @param message      The new value of the property
     */
    @Override
    public void notify(String propertyName, Object oldMessage, Object message) {
        this.listeners.firePropertyChange(propertyName, oldMessage, message);
    }

}
