package it.polimi.ingsw.network.client;

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

public class TCPConnection implements Connection, PropertyChangeListener {

    private static final Logger logger = LogManager.getLogger(TCPConnection.class);
    private final ClientNetworkControllerMapper networkControllerMapper;
    private final String serverIp;
    private final int serverPort;
    /**
     * Listeners that will be notified when a message is received.
     */
    private final PropertyChangeSupport listeners;

    public TCPConnection(ClientNetworkControllerMapper networkControllerMapper, String serverIp, int serverPort) {
        this.networkControllerMapper = networkControllerMapper;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.listeners = new PropertyChangeSupport(this);
    }

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
        listeners.firePropertyChange("CONNECTION_ESTABLISHED", null, null);
    }

    @Override
    public void quit() {
        System.exit(0);
    }

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
