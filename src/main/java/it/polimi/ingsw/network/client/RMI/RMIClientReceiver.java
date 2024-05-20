package it.polimi.ingsw.network.client.RMI;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.client.actions.RMIServerToClientActions;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


//TODO: DOC

/**
 * The RMIClientReceiver class is responsible for receiving messages from the server.
 * Each method is made asyncronous to avoid blocking the server, by calling a new thread.
 * When the thread is launched, the original call is returned, so both client and Server can go on with their work.
 * The call is then completed asynchronously.
 */
public class RMIClientReceiver implements RMIServerToClientActions {

    /**
     * The client command mapper.
     */
    private final ClientNetworkControllerMapper clientNetworkControllerMapper;

    /**
     * Listeners that will be notified when a message is received.
     */
    private final PropertyChangeSupport listeners;
    /**
     * The heartbeat timer.
     */
    private Timer heartbeatTimer;

    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(RMIClientReceiver.class);

    /**
     * Class constructor.
     *
     * @param clientNetworkControllerMapper The client command mapper.
     */
    public RMIClientReceiver(ClientNetworkControllerMapper clientNetworkControllerMapper) {
        this.clientNetworkControllerMapper = clientNetworkControllerMapper;
        this.listeners = new PropertyChangeSupport(this);
    }


    /**
     * This method is called when the server receives the game list response.
     *
     * @param games The list of games received from the server.
     */
    @Override
    public void receiveGameList(ArrayList<GameRecord> games) throws RemoteException {
        new Thread(() -> clientNetworkControllerMapper.receiveGameList(games)).start();
        // Debug
        logger.debug("RMI received message: {}", games);
    }

    /**
     * This method is called when the server receives a notification that a game has been deleted.
     *
     * @param message The message received from the server.
     */
    @Override
    public void receiveGameDeleted(String message) throws RemoteException {
        new Thread(() -> clientNetworkControllerMapper.receiveGameDeleted(message)).start();
        // Debug
        logger.debug("RMI received message: {}", message);
    }

    /**
     * This method is called when the server receives an updated view message.
     *
     * @param updatedView The updated view message received from the client.
     */
    @Override
    public void receiveUpdatedView(GameControllerView updatedView) throws RemoteException {
        new Thread(() -> clientNetworkControllerMapper.receiveUpdatedView(updatedView)).start();
        // Debug
        logger.debug("RMI received message: " + updatedView);
    }

    /**
     * This method is called when the server receives an error message.
     *
     * @param errorMessage The error message received from the client.
     */
    @Override
    public void receiveErrorMessage(String errorMessage) throws RemoteException {
        new Thread(() -> clientNetworkControllerMapper.receiveErrorMessage(errorMessage)).start();
        // Debug
        logger.debug("RMI received message: {}", errorMessage);
    }


    /**
     * Receives a chat message from the server and processes it asynchronously.
     * This method is called when the server sends a chat message to the client.
     * It creates a new thread to handle the received message, allowing the client to continue its operations without waiting for the message processing to complete.
     * After processing the message, it prints a debug message to the console.
     *
     * @param playerName The chat message received from the server.
     * @param message    The chat message received from the server.
     * @param timestamp  The timestamp of the message.
     * @param isDirect   Flag to indicate if the message is a direct message.
     * @throws RemoteException If an error occurs during the RMI connection.
     */
    @Override
    public void receiveChatMessage(String playerName, String message, long timestamp, boolean isDirect) throws RemoteException {
        new Thread(() -> clientNetworkControllerMapper.receiveChatMessage(playerName, message, timestamp, isDirect)).start();
        // Debug
        logger.debug("RMI received message: {}", playerName);
    }

    @Override
    public void heartbeat() throws RemoteException {
        new Thread(() -> {
            // If the timer is already running, cancel it and start a new one
            if (heartbeatTimer != null) {
                heartbeatTimer.cancel();
            }
            // Instance a new timer
            heartbeatTimer = new Timer();
            // Schedule a new timer task. If the client does not receive a heartbeat message within 5 seconds, it will print an error.
            heartbeatTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    closeConnection();
                }
            }, 5000);
        }).start();

        if (Client.DEBUG) {
            logger.debug("Ping received");
        }
    }

    /**
     * Closes the connection to the client.
     */
    public void closeConnection() {
        this.listeners.firePropertyChange("CONNECTION_CLOSED", null, null);
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


