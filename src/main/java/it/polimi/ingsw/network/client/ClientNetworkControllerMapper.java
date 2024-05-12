package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.actions.ServerToClientActions;
import it.polimi.ingsw.network.client.message.chatMessageClientToServerMessage;
import it.polimi.ingsw.network.client.message.gameController.*;
import it.polimi.ingsw.network.client.message.mainController.CreateGameClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.DeleteGameClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.GetGamesClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.JoinGameClientToServerMessage;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;

import java.util.HashSet;

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


    /**
     * Default constructor for the ClientNetworkControllerMapper class.
     * This constructor does not initialize any fields.
     */
    public ClientNetworkControllerMapper() {
    }

    /* ***************************************
     * METHODS INVOKED BY THE CLIENT ON THE SERVER
     * ***************************************/

    /**
     * Sends a request to the server to get the list of available games.
     */
    void getGames() {
        messageHandler.sendMessage(new GetGamesClientToServerMessage());
    }

    /**
     * Sends a request to the server to create a new game.
     *
     * @param gameName   The name of the game to be created.
     * @param playerName The name of the player creating the game.
     * @param nPlayers   The number of players in the game.
     */
    void createGame(String gameName, String playerName, int nPlayers) {
        messageHandler.sendMessage(new CreateGameClientToServerMessage(gameName, playerName, nPlayers));
    }

    /**
     * Sends a request to the server to delete a game.
     *
     * @param gameName   The name of the game to be deleted.
     * @param playerName The name of the player deleting the game.
     */
    void deleteGame(String gameName, String playerName) {
        messageHandler.sendMessage(new DeleteGameClientToServerMessage(gameName, playerName));
    }

    /**
     * Sends a request to the server for the player to join a game.
     *
     * @param gameName   The name of the game to join.
     * @param playerName The name of the player joining the game.
     */
    void joinGame(String gameName, String playerName) {
        messageHandler.sendMessage(new JoinGameClientToServerMessage(gameName, playerName));
    }

    /**
     * Sends a request to the server for the player to choose a color.
     *
     * @param gameName    The name of the game.
     * @param playerName  The name of the player choosing the color.
     * @param playerColor The chosen color.
     */
    void choosePlayerColor(String gameName, String playerName, PlayerColorEnum playerColor) {
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
    void placeCard(String gameName, String playerName, Coordinate coordinate, GameCard card) {
        messageHandler.sendMessage(new PlaceCardClientToServerMessage(gameName, playerName, coordinate, card));
    }

    /**
     * Sends a request to the server for the player to draw a card from the field.
     *
     * @param gameName   The name of the game.
     * @param playerName The name of the player drawing the card.
     * @param card       The card to be drawn.
     */
    void drawCardFromField(String gameName, String playerName, GameCard card) {
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
    void sendChatMessage(chatMessageClientToServerMessage message) {
        messageHandler.sendMessage(message);
    }

    /* ***************************************
     * METHODS INVOKED BY THE SERVER ON THE CLIENT
     * ***************************************/

    @Override
/**
 * Receives a list of games from the server.
 * This method is called when the server sends a list of games to the client.
 *
 * @param games The list of games received from the server.
 */
    public void receiveGameList(HashSet<GameRecord> games) {
        System.out.println("Received games: " + games);
        //TODO: JavaFx event trigger
    }


    @Override
/**
 * Receives a message from the server that a game has been deleted.
 * This method is called when the server sends a message to the client that a game has been deleted.
 *
 * @param message The message received from the server.
 */
    public void receiveGameDeleted(String message) {
        System.out.println(message);
        //TODO: JavaFx event trigger
    }


    @Override
/**
 * Receives an updated view of the game from the server.
 * This method is called when the server sends an updated view of the game to the client.
 *
 * @param updatedView The updated view of the game received from the server.
 */
    public void receiveUpdatedView(GameControllerView updatedView) {
        this.view = updatedView;
        System.out.println("Received updated view: " + updatedView);
        System.out.println("Current player: " + updatedView.gameView().currentPlayer());
        System.out.println("Current game status: " + updatedView.gameStatus());
        //TODO: JavaFx event trigger
    }

    @Override
/**
 * Receives an error message from the server.
 * This method is called when the server sends an error message to the client.
 *
 * @param errorMessage The error message received from the server.
 */
    public void receiveErrorMessage(String errorMessage) {
        System.out.println("Received error message: " + errorMessage);
    }


    /**
     * Receives a chat message from the server.
     * This method is called when the server sends a chat message to the client.
     *
     * @param message The chat message received from the server.
     */
    public void receiveChatMessage(ServerToClientMessage message) {
        //TODO: JavaFx / TUI event trigger?
        System.out.println("Received chat message: " + message.chatPrint());
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


}
