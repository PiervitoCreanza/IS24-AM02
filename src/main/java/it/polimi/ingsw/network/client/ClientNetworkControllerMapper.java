package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.actions.ServerToClientActions;
import it.polimi.ingsw.network.client.message.ChatClientToServerMessage;
import it.polimi.ingsw.network.client.message.gameController.*;
import it.polimi.ingsw.network.client.message.mainController.CreateGameClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.DeleteGameClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.GetGamesClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.JoinGameClientToServerMessage;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import static it.polimi.ingsw.tui.utils.Utils.ANSI_BLUE;
import static it.polimi.ingsw.tui.utils.Utils.ANSI_RESET;

/**
 * The ClientNetworkControllerMapper class implements the ServerToClientActions interface.
 * It is responsible for mapping the actions that the client can perform on the server.
 * This class is part of the network client package.
 */
public class ClientNetworkControllerMapper implements ServerToClientActions {

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

    private final PropertyChangeSupport support;


    /**
     * Default constructor for the ClientNetworkControllerMapper class.
     * This constructor does not initialize any fields.
     */
    public ClientNetworkControllerMapper() {
        support = new PropertyChangeSupport(this);
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
     * @param card       The card to be placed.
     */
    public void placeCard(String gameName, String playerName, Coordinate coordinate, GameCard card) {
        messageHandler.sendMessage(new PlaceCardClientToServerMessage(gameName, playerName, coordinate, card));
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
    void drawCardFromResourceDeck(String gameName, String playerName) {
        messageHandler.sendMessage(new DrawCardFromResourceDeckClientToServerMessage(gameName, playerName));
    }

    /**
     * Sends a request to the server for the player to draw a card from the gold deck.
     *
     * @param gameName   The name of the game.
     * @param playerName The name of the player drawing the card.
     */
    void drawCardFromGoldDeck(String gameName, String playerName) {
        messageHandler.sendMessage(new DrawCardFromGoldDeckClientToServerMessage(gameName, playerName));
    }

    /**
     * Sends a request to the server for the player to switch the side of a card.
     *
     * @param gameName   The name of the game.
     * @param playerName The name of the player switching the card side.
     * @param card       The card to switch side.
     */
    void switchCardSide(String gameName, String playerName, GameCard card) {
        messageHandler.sendMessage(new SwitchCardSideClientToServerMessage(gameName, playerName, card));
    }

    /**
     * Sends a request to the server for the player to set an objective card.
     *
     * @param gameName   The name of the game.
     * @param playerName The name of the player setting the objective card.
     * @param card       The objective card to be set.
     */
    void setPlayerObjective(String gameName, String playerName, ObjectiveCard card) {
        messageHandler.sendMessage(new SetPlayerObjectiveClientToServerMessage(gameName, playerName, card));
    }

    /**
     * Sends a chat message to the server.
     *
     * @param message The chat message to be sent.
     */
    void sendChatMessage(ChatClientToServerMessage message) {
        messageHandler.sendMessage(message);
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
        System.out.println("Received games: " + games);
        notify("GET_GAMES", games);
        //TODO: JavaFx event trigger
    }



/**
 * Receives a message from the server that a game has been deleted.
 * This method is called when the server sends a message to the client that a game has been deleted.
 *
 * @param message The message received from the server.
 */
@Override
    public void receiveGameDeleted(String message) {
        System.out.println(message);
        notify("GAME_DELETED", message);
        //TODO: JavaFx event trigger
    }



/**
 * Receives an updated view of the game from the server.
 * This method is called when the server sends an updated view of the game to the client.
 *
 * @param updatedView The updated view of the game received from the server.
 */
@Override
    public void receiveUpdatedView(GameControllerView updatedView) {
        this.view = updatedView;
        notify("UPDATE_VIEW", updatedView);
        //PlayerBoardComponent playerBoardComponent = new PlayerBoardComponent(updatedView.gameView().getViewByPlayer(updatedView.gameView().currentPlayer()).playerBoardView().playerBoard());
        //System.out.println(playerBoardComponent);
//        System.out.println("Received updated view: " + updatedView);
//        System.out.println("Current player: " + updatedView.gameView().currentPlayer());
//        System.out.println("Current game status: " + updatedView.gameStatus());
        //TODO: JavaFx event trigger
    }


/**
 * Receives an error message from the server.
 * This method is called when the server sends an error message to the client.
 *
 * @param errorMessage The error message received from the server.
 */
@Override
    public void receiveErrorMessage(String errorMessage) {
        System.out.println("Received error message: " + errorMessage);
        notify("ERROR", errorMessage);

    }

    /**
     * Receives a chat message from the server.
     * This method is called when the server sends a chat message to the client.
     *
     * @param playerName The chat message received from the server.
     * @param message    The chat message received from the server.
     * @param receiver   The receiver of the message if it's a direct message.
     * @param timestamp  The timestamp when the message was created.
     * @param isDirect   Flag to indicate if the message is a direct message.
     */
    public void receiveChatMessage(String playerName, String message, String receiver, long timestamp, boolean isDirect) {
        //TODO: JavaFx / TUI event trigger?
        System.out.println("Received chat message: " + chatPrint(playerName, message, receiver, timestamp, isDirect));

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
     * Prints a chat message with the appropriate colors.
     *
     * @param sender          The sender of the message.
     * @param message         The content of the message.
     * @param receiver        The receiver of the message if it's a direct message.
     * @param timestamp       The timestamp when the message was created.
     * @param isDirectMessage Flag to indicate if the message is a direct message.
     * @return A string representation of the chat message.
     */
    private String chatPrint(String sender, String message, String receiver, long timestamp, boolean isDirectMessage) {
        // If it's a direct message, print it in blue with the appropriate fields
        if (isDirectMessage)
            return ANSI_BLUE + sender + "(to " + receiver + "): " + message + " (" + getFormattedTimestamp(timestamp) + ")" + ANSI_RESET;
        // If it's not a direct message, print it as a normal message
        return sender + ": " + message + " (" + getFormattedTimestamp(timestamp) + ")";
    }

    /**
     * Converts a Unix timestamp into a formatted string.
     *
     * @param timestamp The Unix timestamp to convert.
     * @return The formatted string representation of the timestamp.
     */
    private String getFormattedTimestamp(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return dateTime.format(formatter);
    }

    /**
     * Adds a PropertyChangeListener to the ClientNetworkCommandMapper.
     *
     * @param listener the PropertyChangeListener to be added.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener from the ClientNetworkCommandMapper.
     *
     * @param listener the PropertyChangeListener to be removed.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    /**
     * Notifies of a message.
     *
     * @param message the message to be sent.
     */
    private void notify(String propertyName, Object message) {
        support.firePropertyChange(propertyName, null, message);
    }
}
