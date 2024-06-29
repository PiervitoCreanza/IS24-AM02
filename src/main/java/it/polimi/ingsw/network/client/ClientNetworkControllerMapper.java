package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.actions.ServerToClientActions;
import it.polimi.ingsw.network.client.connection.Connection;
import it.polimi.ingsw.network.client.message.ChatClientToServerMessage;
import it.polimi.ingsw.network.client.message.DisconnectClientToServerMessage;
import it.polimi.ingsw.network.client.message.gameController.*;
import it.polimi.ingsw.network.client.message.mainController.CreateGameClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.DeleteGameClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.GetGamesClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.JoinGameClientToServerMessage;
import it.polimi.ingsw.network.server.message.ChatServerToClientMessage;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.utils.PropertyChangeNotifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * The ClientNetworkControllerMapper class implements the ServerToClientActions interface.
 * It handles mapping of actions that the client can perform on the server and
 * manages interaction between client and server for game-related operations.
 *
 * <p>This class supports {@link PropertyChangeNotifier} to manage property change events.
 *
 * <p>Instances of this class are responsible for communication with the server,
 * maintaining game state views, and handling messages between client and server.
 *
 * @since 1.0
 */
public class ClientNetworkControllerMapper implements ServerToClientActions, PropertyChangeNotifier, PropertyChangeListener {

    /**
     * Logger for this class, used to log debug and error messages.
     */
    private static final Logger logger = LogManager.getLogger(ClientNetworkControllerMapper.class);

    /**
     * Singleton instance of the ClientNetworkControllerMapper class.
     */

    private static ClientNetworkControllerMapper instance = null;
    /**
     * Support for property change listeners.
     */
    private final PropertyChangeSupport listeners;
    /**
     * The message handler for client-server communication.
     */
    private ClientMessageHandler messageHandler;
    /**
     * The connection for client-server communication.
     */
    private Connection connection;
    /**
     * The view of the game controller, representing client's perspective of game state.
     */
    private GameControllerView view;
    /**
     * The name of the game.
     */
    private String gameName;

    /**
     * The name of the player.
     */
    private String playerName;

    /**
     * Constructs a ClientNetworkControllerMapper instance.
     * Initializes necessary fields and sets up property change support.
     */
    private ClientNetworkControllerMapper() {
        listeners = new PropertyChangeSupport(this);
    }

    /**
     * Returns the singleton instance of ClientNetworkControllerMapper.
     *
     * @return The singleton instance of ClientNetworkControllerMapper.
     */
    public static ClientNetworkControllerMapper getInstance() {
        if (instance == null) {
            instance = new ClientNetworkControllerMapper();
        }
        return instance;
    }

    /**
     * Sets the connection for client-server communication.
     *
     * @param connection The connection instance to be set.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
        connection.addPropertyChangeListener(this);
    }

    /**
     * Sets the message handler for client-server communication.
     *
     * @param messageHandler The message handler to be set.
     */
    public void setMessageHandler(ClientMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     * Retrieves the current view of the game controller.
     *
     * @return The current view of the game controller.
     */
    public GameControllerView getView() {
        return view;
    }

    /**
     * Initiates connection to the server.
     */
    public void connect() {
        connection.connect();
    }

    /**
     * Closes the connection with the server.
     */
    public void closeConnection() {
        view = null;
        if (checkConnectionStatus()) {
            sendDisconnect();
            messageHandler.closeConnection();
        }
    }

    /* ***************************************
     * METHODS INVOKED BY THE CLIENT ON THE SERVER
     * ***************************************/

    /**
     * Requests the list of available games from the server.
     */
    public void getGames() {
        if (checkConnectionStatus()) {
            messageHandler.sendMessage(new GetGamesClientToServerMessage());
        }
    }

    /**
     * Requests creation of a new game on the server.
     *
     * @param gameName   The name of the game to be created.
     * @param playerName The name of the player creating the game.
     * @param nPlayers   The number of players in the game.
     */
    public void createGame(String gameName, String playerName, int nPlayers) {
        this.gameName = gameName;
        this.playerName = playerName;
        if (checkConnectionStatus()) {
            messageHandler.sendMessage(new CreateGameClientToServerMessage(gameName, playerName, nPlayers));
        }
    }

    /**
     * Requests deletion of the current game on the server.
     */
    public void deleteGame() {
        if (checkConnectionStatus()) {
            messageHandler.sendMessage(new DeleteGameClientToServerMessage(gameName, playerName));
        }
    }

    /**
     * Requests to join a game on the server.
     *
     * @param gameName   The name of the game to join.
     * @param playerName The name of the player joining the game.
     */
    public void joinGame(String gameName, String playerName) {
        this.gameName = gameName;
        this.playerName = playerName;
        if (checkConnectionStatus()) {
            messageHandler.sendMessage(new JoinGameClientToServerMessage(gameName, playerName));
        }
    }

    /**
     * Requests to choose a player color on the server.
     *
     * @param playerColor The chosen color.
     */
    public void choosePlayerColor(PlayerColorEnum playerColor) {
        if (checkConnectionStatus()) {
            messageHandler.sendMessage(new ChoosePlayerColorClientToServerMessage(gameName, playerName, playerColor));
        }
    }

    /**
     * Requests to place a card on the server.
     *
     * @param coordinate The coordinate where the card is placed.
     * @param cardId     The card to be placed.
     * @param isFlipped  Flag indicating if the card is flipped.
     */
    public void placeCard(Coordinate coordinate, int cardId, boolean isFlipped) {
        if (checkConnectionStatus()) {
            messageHandler.sendMessage(new PlaceCardClientToServerMessage(gameName, playerName, coordinate, cardId, isFlipped));
        }
    }

    /**
     * Requests to draw a card from the field on the server.
     *
     * @param cardId The ID of the card to be drawn.
     */
    public void drawCardFromField(int cardId) {
        if (checkConnectionStatus()) {
            messageHandler.sendMessage(new DrawCardFromFieldClientToServerMessage(gameName, playerName, cardId));
        }
    }

    /**
     * Requests to draw a card from the resource deck on the server.
     */
    public void drawCardFromResourceDeck() {
        if (checkConnectionStatus()) {
            messageHandler.sendMessage(new DrawCardFromResourceDeckClientToServerMessage(gameName, playerName));
        }
    }

    /**
     * Requests to draw a card from the gold deck on the server.
     */
    public void drawCardFromGoldDeck() {
        if (checkConnectionStatus()) {
            messageHandler.sendMessage(new DrawCardFromGoldDeckClientToServerMessage(gameName, playerName));
        }
    }

    /**
     * Requests to switch the side of a card on the server.
     *
     * @param cardId The ID of the card to switch sides.
     */
    public void switchCardSide(int cardId) {
        if (checkConnectionStatus()) {
            messageHandler.sendMessage(new SwitchCardSideClientToServerMessage(gameName, playerName, cardId));
        }
    }

    /**
     * Requests to set an objective card for the player on the server.
     *
     * @param cardId The ID of the objective card to be set.
     */
    public void setPlayerObjective(int cardId) {
        if (checkConnectionStatus()) {
            messageHandler.sendMessage(new SetPlayerObjectiveClientToServerMessage(gameName, playerName, cardId));
        }
    }

    /**
     * Sends a chat message to the server.
     *
     * @param message The chat message to be sent.
     */
    public void sendChatMessage(ChatClientToServerMessage message) {
        message.setSender(playerName);
        message.setGameName(gameName);
        if (checkConnectionStatus()) {
            messageHandler.sendMessage(message);
        }
    }

    /**
     * Sends a disconnect message to the server.
     * This method is private and used internally.
     */
    private void sendDisconnect() {
        messageHandler.sendMessage(new DisconnectClientToServerMessage(gameName, playerName));
    }

    /**
     * Checks if the message handler is properly set for communication.
     *
     * @return True if the connection is valid; false otherwise.
     */
    private boolean checkConnectionStatus() {
        if (messageHandler == null) {
            logger.warn("MessageHandler was not set in the ClientNetworkControllerMapper class. You can ignore this message if in debug mode.");
            return false;
        }
        return true;
    }

    /* ***************************************
     * METHODS INVOKED BY THE SERVER ON THE CLIENT
     * ***************************************/

    /**
     * Receives a list of games from the server.
     *
     * @param games The list of games received from the server.
     */
    @Override
    public void receiveGameList(ArrayList<GameRecord> games) {
        this.listeners.firePropertyChange("GET_GAMES", null, games);
    }

    /**
     * Receives notification that a game has been deleted by the server.
     *
     * @param message The message received from the server.
     */
    @Override
    public void receiveGameDeleted(String message) {
        this.listeners.firePropertyChange("GAME_DELETED", null, message);
    }

    /**
     * Receives an updated view of the game from the server.
     *
     * @param updatedView The updated view of the game received from the server.
     */
    @Override
    public void receiveUpdatedView(GameControllerView updatedView) {
        this.listeners.firePropertyChange("UPDATE_VIEW", this.view, updatedView);
        this.view = updatedView;
    }

    /**
     * Receives an error message from the server.
     *
     * @param errorMessage The error message received from the server.
     */
    @Override
    public void receiveErrorMessage(String errorMessage) {
        this.listeners.firePropertyChange("ERROR", null, errorMessage);
    }

    /**
     * Receives a chat message from the server.
     *
     * @param playerName The name of the player sending the chat message.
     * @param message    The chat message received from the server.
     * @param timestamp  The timestamp when the message was sent.
     * @param isDirect   Flag indicating if the message is a direct message.
     */
    @Override
    public void receiveChatMessage(String playerName, String message, long timestamp, boolean isDirect) {
        this.listeners.firePropertyChange("CHAT_MESSAGE", null, new ChatServerToClientMessage(playerName, message, timestamp, isDirect));
    }

    /* ***************************************
     * PROPERTY CHANGE NOTIFIER METHODS
     * ***************************************/

    /**
     * Adds a PropertyChangeListener to the listener list.
     * The listener will be notified of property changes.
     *
     * @param listener The PropertyChangeListener to be added.
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener from the listener list.
     * The listener will no longer be notified of property changes.
     *
     * @param listener The PropertyChangeListener to be removed.
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.removePropertyChangeListener(listener);
    }

    /**
     * Processes property change events.
     *
     * @param evt The PropertyChangeEvent object describing the event source and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String changedProperty = evt.getPropertyName();
        switch (changedProperty) {
            case "CONNECTION_ESTABLISHED" ->
                    this.listeners.firePropertyChange("CONNECTION_ESTABLISHED", null, evt.getNewValue());
            case "CONNECTION_FAILED" -> this.listeners.firePropertyChange("CONNECTION_FAILED", null, evt.getNewValue());
            case "CONNECTION_TRYING" -> this.listeners.firePropertyChange("CONNECTION_TRYING", null, evt.getNewValue());
            case "CONNECTION_CLOSED" -> this.view = null;
            default -> logger.warn("Unknown property change event: {}", changedProperty);
        }
    }
}