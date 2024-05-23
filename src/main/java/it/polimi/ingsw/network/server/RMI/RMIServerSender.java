package it.polimi.ingsw.network.server.RMI;

import it.polimi.ingsw.network.client.actions.RMIServerToClientActions;
import it.polimi.ingsw.network.server.ServerMessageHandler;
import it.polimi.ingsw.network.server.message.ServerActionEnum;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import it.polimi.ingsw.utils.PropertyChangeNotifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The RMIServerSender class is responsible for sending messages to the client over RMI.
 * It implements the ServerMessageHandler interface, which defines the methods for handling server messages.
 * It also implements the Observable interface, which allows it to notify observers when a change occurs.
 */
public class RMIServerSender implements ServerMessageHandler, PropertyChangeNotifier {
    /**
     * The stub used to call methods on the client's remote object.
     */
    private final RMIServerToClientActions stub;

    /**
     * The listeners that are notified when a change occurs.
     */
    private final PropertyChangeSupport listeners;

    /**
     * The name of the player associated with the connection.
     */
    private String playerName;

    /**
     * The name of the game associated with the connection.
     */
    private String gameName;

    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(RMIServerSender.class);

    // This variable is used to check if the connection has been saved by the ServerNetworkControllerMapper.
    // The heartbeat will start only after the connection has been saved.
    // If an error occurs during the heartbeat, or while sending a message, this parameter will be set to false.
    // The closeConnection method will be called only one time and will notify the observers.
    /**
     * A flag indicating whether the client is connected.
     */
    private final AtomicBoolean isConnectionSaved = new AtomicBoolean(false);

    /**
     * Constructs a new RMIServerSender object with the specified stub.
     *
     * @param stub the stub used to call methods on the client's remote object
     */
    public RMIServerSender(RMIServerToClientActions stub) {
        this.stub = stub;
        this.listeners = new PropertyChangeSupport(this);
    }

    /**
     * This method is used to check if the client is still connected.
     * It sends a heartbeat message to the client every 2.5 seconds.
     * If the client does not respond, it is considered disconnected.
     */
    private void heartbeat() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!isConnectionSaved.get()) {
                    cancel();
                    return;
                }
                try {
                    stub.heartbeat();
                } catch (RemoteException e) {
                    logger.warn("RMI Client: {} disconnected. Detected while pinging.", playerName);
                    closeConnection();
                    cancel();
                }
            }
        }, 0, 2500);
    }

    /**
     * Sends a message to the client.
     *
     * @param message the message to be sent
     */
    @Override
    public void sendMessage(ServerToClientMessage message) {
        ServerActionEnum serverAction = message.getServerAction();
        try {
            switch (serverAction) {
                case UPDATE_VIEW -> stub.receiveUpdatedView(message.getView());
                case DELETE_GAME -> stub.receiveGameDeleted(message.getSuccessDeleteMessage());
                case GET_GAMES -> stub.receiveGameList(message.getGames());
                case ERROR_MSG -> stub.receiveErrorMessage(message.getErrorMessage());
                case RECEIVE_CHAT_MSG ->
                        stub.receiveChatMessage(message.getPlayerName(), message.getChatMessage(), message.getTimestamp(), message.isDirectMessage());
                default -> logger.error("Invalid action");
            }
        } catch (RemoteException e) {
            logger.warn("RMI Client: {} disconnected. Detected while sending message", playerName);
            closeConnection();
            return;
        }

        // Debug
        logger.debug("RMI message sent: SERVER_ACTION: {}", message.getServerAction());
    }

    /**
     * Gets the name of the player associated with the connection.
     *
     * @return the name of the player
     */
    @Override
    public String getPlayerName() {
        return this.playerName;
    }

    /**
     * Sets the name of the player associated with the connection.
     *
     * @param playerName the name of the player
     */
    @Override
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Gets the name of the game associated with the connection.
     *
     * @return the name of the game
     */
    @Override
    public String getGameName() {
        return this.gameName;
    }

    /**
     * Sets the name of the game associated with the connection.
     *
     * @param gameName the name of the game
     */
    @Override
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public void connectionSaved(boolean hasBeenSaved) {
        this.isConnectionSaved.set(hasBeenSaved);
        this.heartbeat(); //Comment here if you want to disable the heartbeat
    }

    /**
     * Closes the connection to the client.
     * This method is called when the client is disconnected or when an error occurs.
     */
    @Override
    public void closeConnection() {
        if (isConnectionSaved.getAndSet(false)) {
            this.listeners.firePropertyChange("CONNECTION_CLOSED", null, null);
        }
    }

    /**
     * Adds a PropertyChangeListener to the listener list.
     * The listener will be notified of property changes.
     *
     * @param listener The PropertyChangeListener to be added
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener from the listener list.
     * The listener will no longer be notified of property changes.
     *
     * @param listener The PropertyChangeListener to be removed
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.removePropertyChangeListener(listener);
    }
}
