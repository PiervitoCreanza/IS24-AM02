package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.actions.ServerToClientActions;
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * The ClientNetworkControllerMapper class implements the ServerToClientActions interface.
 * It is responsible for mapping the actions that the client can perform on the server.
 * This class is part of the network client package.
 */
public class ClientNetworkControllerMapper implements ServerToClientActions, PropertyChangeNotifier {

    /**
     * The message handler for the client.
     * It is used to send messages from the client to the server.
     */
    private ClientMessageHandler messageHandler;

    /**
     * The view of the game controller.
     * It represents the current state of the game from the client's perspective.
     */
    private GameControllerView view;

    /**
     * The property change support.
     */
    private final PropertyChangeSupport listeners;

    private String gameName;

    private String playerName;


    /**
     * Default constructor for the ClientNetworkControllerMapper class.
     * This constructor does not initialize any fields.
     */
    public ClientNetworkControllerMapper() {
        listeners = new PropertyChangeSupport(this);
    }

    /**
     * Sets the message handler for the client.
     * This method is used to set the message handler that will be used to send messages from the client to the server.
     *
     * @param messageHandler The message handler to be set.
     */
    public void setMessageHandler(ClientMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     * Gets the view of the game controller.
     * This method is used to get the current state of the game from the client's perspective.
     *
     * @return The view of the game controller.
     */
    public GameControllerView getView() {
        return view;
    }

    /**
     * Closes the connection of the current message handler.
     */
    public void closeConnection() {
        view = null;
        messageHandler.closeConnection();
    }

    /* ***************************************
     * METHODS INVOKED BY THE CLIENT ON THE SERVER
     * ***************************************/

    /**
     * Sends a request to the server to get the list of available games.
     */
    public void getGames() {
        messageHandler.sendMessage(new GetGamesClientToServerMessage());
    }

    /**
     * Sends a request to the server to create a new game.
     *
     * @param gameName   The name of the game to be created.
     * @param playerName The name of the player creating the game.
     * @param nPlayers   The number of players in the game.
     */
    public void createGame(String gameName, String playerName, int nPlayers) {
        this.gameName = gameName;
        this.playerName = playerName;
        messageHandler.sendMessage(new CreateGameClientToServerMessage(gameName, playerName, nPlayers));
    }

    /**
     * Sends a request to the server to delete a game.
     *
     * @param gameName   The name of the game to be deleted.
     * @param playerName The name of the player deleting the game.
     */
    public void deleteGame(String gameName, String playerName) {
        messageHandler.sendMessage(new DeleteGameClientToServerMessage(gameName, playerName));
    }

    /**
     * Sends a request to the server for the player to join a game.
     *
     * @param gameName   The name of the game to join.
     * @param playerName The name of the player joining the game.
     */
    public void joinGame(String gameName, String playerName) {
        this.gameName = gameName;
        this.playerName = playerName;
        messageHandler.sendMessage(new JoinGameClientToServerMessage(gameName, playerName));
    }

    /**
     * Sends a request to the server for the player to choose a color.
     *
     * @param gameName    The name of the game.
     * @param playerName  The name of the player choosing the color.
     * @param playerColor The chosen color.
     */
    public void choosePlayerColor(String gameName, String playerName, PlayerColorEnum playerColor) {
        messageHandler.sendMessage(new ChoosePlayerColorClientToServerMessage(gameName, playerName, playerColor));
    }

    /**
     * Sends a request to the server for the player to place a card.
     *
     * @param gameName   The name of the game.
     * @param playerName The name of the player placing the card.
     * @param coordinate The coordinate where the card is placed.
     * @param cardId     The card to be placed.
     * @param isFlipped  Flag to indicate if the card is flipped.
     */
    public void placeCard(String gameName, String playerName, Coordinate coordinate, int cardId, boolean isFlipped) {
        messageHandler.sendMessage(new PlaceCardClientToServerMessage(gameName, playerName, coordinate, cardId, isFlipped));
    }

    /**
     * Sends a request to the server for the player to draw a card from the field.
     *
     * @param gameName   The name of the game.
     * @param playerName The name of the player drawing the card.
     * @param card       The card to be drawn.
     */
    public void drawCardFromField(String gameName, String playerName, GameCard card) {
        messageHandler.sendMessage(new DrawCardFromFieldClientToServerMessage(gameName, playerName, card));
    }

    /**
     * Sends a request to the server for the player to draw a card from the resource deck.
     *
     * @param gameName   The name of the game.
     * @param playerName The name of the player drawing the card.
     */
    public void drawCardFromResourceDeck(String gameName, String playerName) {
        messageHandler.sendMessage(new DrawCardFromResourceDeckClientToServerMessage(gameName, playerName));
    }

    /**
     * Sends a request to the server for the player to draw a card from the gold deck.
     *
     * @param gameName   The name of the game.
     * @param playerName The name of the player drawing the card.
     */
    public void drawCardFromGoldDeck(String gameName, String playerName) {
        messageHandler.sendMessage(new DrawCardFromGoldDeckClientToServerMessage(gameName, playerName));
    }

    /**
     * Sends a request to the server for the player to switch the side of a card.
     *
     * @param gameName   The name of the game.
     * @param playerName The name of the player switching the card side.
     * @param cardId     The card to switch side.
     */
    public void switchCardSide(String gameName, String playerName, int cardId) {
        messageHandler.sendMessage(new SwitchCardSideClientToServerMessage(gameName, playerName, cardId));
    }

    /**
     * Sends a request to the server for the player to set an objective card.
     *
     * @param gameName   The name of the game.
     * @param playerName The name of the player setting the objective card.
     * @param cardId     The objective card to be set.
     */
    public void setPlayerObjective(String gameName, String playerName, int cardId) {
        messageHandler.sendMessage(new SetPlayerObjectiveClientToServerMessage(gameName, playerName, cardId));
    }

    /**
     * Sends a chat message to the server.
     *
     * @param message The chat message to be sent.
     */
    public void sendChatMessage(ChatClientToServerMessage message) {
        messageHandler.sendMessage(message);
    }

    /**
     * Sends a request to the server for the player to disconnect.
     *
     * @param gameName   The name of the game.
     * @param playerName The name of the player disconnecting.
     */
    public void sendDisconnect(String gameName, String playerName) {
        messageHandler.sendMessage(new DisconnectClientToServerMessage(gameName, playerName));
    }

    /* ***************************************
     * METHODS INVOKED BY THE SERVER ON THE CLIENT
     * ***************************************/

    /**
     * Receives a list of games from the server.
     * This method is called when the server sends a list of games to the client.
     *
     * @param games The list of games received from the server.
     */
    @Override
    public void receiveGameList(ArrayList<GameRecord> games) {
        this.listeners.firePropertyChange("GET_GAMES", null, games);
    }


    /**
     * Receives a message from the server that a game has been deleted.
     * This method is called when the server sends a message to the client that a game has been deleted.
     *
     * @param message The message received from the server.
     */
    @Override
    public void receiveGameDeleted(String message) {
        this.listeners.firePropertyChange("GAME_DELETED", null, message);
    }


    /**
     * Receives an updated view of the game from the server.
     * This method is called when the server sends an updated view of the game to the client.
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
     * This method is called when the server sends an error message to the client.
     *
     * @param errorMessage The error message received from the server.
     */
    @Override
    public void receiveErrorMessage(String errorMessage) {
        this.listeners.firePropertyChange("ERROR", null, errorMessage);
    }

    /**
     * Receives a chat message from the server.
     * This method is called when the server sends a chat message to the client.
     *
     * @param playerName The chat message received from the server.
     * @param message    The chat message received from the server.
     * @param timestamp  The timestamp when the message was created.
     * @param isDirect   Flag to indicate if the message is a direct message.
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
