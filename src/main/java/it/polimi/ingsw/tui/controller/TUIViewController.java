package it.polimi.ingsw.tui.controller;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.client.message.ChatClientToServerMessage;
import it.polimi.ingsw.network.server.message.ChatServerToClientMessage;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.tui.ViewController;
import it.polimi.ingsw.tui.commandLine.CLIReader;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.utils.Utils;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.scene.Scene;
import it.polimi.ingsw.tui.view.scene.SceneBuilder;
import it.polimi.ingsw.tui.view.scene.ScenesEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * The TUIViewController class is the controller for the text user interface.
 * It is responsible for managing the user interface and the network controller.
 */
public class TUIViewController implements PropertyChangeListener, ViewController {

    /**
     * Instance of SceneBuilder to manage different scenes in the game.
     */
    private final SceneBuilder sceneBuilder;

    /**
     * Instance of ClientNetworkControllerMapper to manage network requests.
     */
    private final ClientNetworkControllerMapper networkController;

    /**
     * The logger.
     */
    private final static Logger logger = LogManager.getLogger(TUIViewController.class);

    /**
     * Name of the player.
     */
    private String playerName;

    /**
     * Name of the game.
     */
    private String gameName;

    /**
     * Status of the game, initially set to null.
     */
    private GameStatusEnum gameStatus = null;

    /**
     * A CLIReader used from the client to call handleUserInput from the scene.
     */
    private final CLIReader cliReader;

    /**
     * Current scene displayed.
     */
    private Scene currentScene;

    /**
     * Chat scene.
     */
    private Scene chatScene;

    /**
     * Winner scene.
     */
    private Scene winnerScene;

    /**
     * List of games.
     */
    private ArrayList<GameRecord> gamesList;

    /**
     * List of players' cards.
     */
    private GameControllerView gameControllerView;

    /**
     * List of messages in the chat.
     */
    private final ArrayList<ChatServerToClientMessage> messages = new ArrayList<>();

    /**
     * Boolean to check if the client is in chat scene.
     */
    private boolean isInChat = false;

    /**
     * Boolean to check if the game is over.
     */
    private boolean isGameOver = false;

    /**
     * Boolean to check if the scene should be displayed.
     */
    private boolean dontDisplay = false;

    /**
     * Constructor for TUIViewController.
     *
     * @param networkController the network controller.
     */
    public TUIViewController(ClientNetworkControllerMapper networkController) {
        this.networkController = networkController;
        this.sceneBuilder = new SceneBuilder(this);
        this.cliReader = new CLIReader(this);
        cliReader.start();
    }

    /**
     * Method to show the current scene.
     */
    private void showCurrentScene() {
        if (!isInChat) {
            Utils.clearScreen();
            this.currentScene.display();
        }
    }

    /**
     * Method to show the chat.
     */
    public void showChat() {
        isInChat = true;
        chatScene = sceneBuilder.instanceChatScene(playerName, messages);
        Utils.clearScreen();
        chatScene.display();
    }

    /**
     * Method to close the chat.
     */
    public void closeChat() {
        isInChat = false;
        showCurrentScene();
    }

    /**
     * Method to set the player name.
     *
     * @param playerName the player name.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Method to set the game name.
     *
     * @param gameName the game name.
     */
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }


    /**
     * Method to get the client status.
     *
     * @return the client status.
     */
    public Scene getCurrentScene() {
        return currentScene;
    }

    /**
     * Method to handle user input of the current scene.
     */
    public void handleUserInput(String input) {
        if (isInChat) {
            chatScene.handleUserInput(input);
            return;
        }
        if (isGameOver) {
            winnerScene.handleUserInput(input);
            return;
        }
        this.currentScene.handleUserInput(input);
    }

    /**
     * Method used by a UserInputScene to request a scene change.
     * scene The scene to be selected.
     */
    public void selectScene(ScenesEnum scene) {
        switch (scene) {
            case MAIN_MENU -> this.currentScene = sceneBuilder.instanceMainMenuScene();
            case CREATE_GAME -> this.currentScene = sceneBuilder.instanceCreateGameScene();
            case JOIN_GAME -> this.currentScene = sceneBuilder.instanceJoinGameScene();
        }
        showCurrentScene();
    }

    /**
     * Method to get the list of games.
     * It delegates the request to the network controller.
     */
    public void getGames() {
        networkController.getGames();
    }

    /**
     * Method to get the player name.
     *
     * @return the player name.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Method to create a new game.
     * It delegates the request to the network controller.
     *
     * @param gameName the name of the game to be created.
     * @param nPlayers the number of players in the game.
     */
    public void createGame(String gameName, String playerName, int nPlayers) {
        this.playerName = playerName;
        this.gameName = gameName;
        networkController.createGame(gameName, playerName, nPlayers);
    }

    /**
     * Method to delete a game.
     * It delegates the request to the network controller.
     */
    public void deleteGame() {
        networkController.deleteGame(gameName, playerName);
    }

    /**
     * Method to join a game.
     * It delegates the request to the network controller.
     *
     * @param gameID     the game ID to be joined.
     * @param playerName the player name.
     */
    public void joinGame(int gameID, String playerName) {
        if (gameID < 0 || gameID >= gamesList.size()) {
            this.currentScene = sceneBuilder.instanceGetGamesScene(gamesList);
            showCurrentScene();
            new DrawArea("Invalid game ID", ColorsEnum.RED).println();
            return;
        }
        this.gameName = gamesList.get(gameID).gameName();
        this.playerName = playerName;
        networkController.joinGame(gameName, playerName);
    }

    /**
     * Method to choose a player color.
     * It delegates the request to the network controller.
     *
     * @param playerColor the player color to be chosen.
     */
    public void choosePlayerColor(PlayerColorEnum playerColor) {
        networkController.choosePlayerColor(gameName, playerName, playerColor);
    }

    /**
     * Method to place a starter card.
     * It delegates the request to the network controller.
     *
     * @param starterCardId the starter card to be placed.
     * @param isFlipped     the side of the card.
     */
    public void placeStarterCard(int starterCardId, boolean isFlipped) {
        networkController.placeCard(gameName, playerName, new Coordinate(0, 0), starterCardId, isFlipped);
    }

    /**
     * Method to place a card.
     * It delegates the request to the network controller.
     *
     * @param choosenCardId the card to be placed.
     * @param coordinate    the coordinate where the card is placed.
     * @param isFlipped     the side of the card.
     */
    public void placeCard(int choosenCardId, Coordinate coordinate, boolean isFlipped) {
        networkController.placeCard(gameName, playerName, coordinate, choosenCardId, isFlipped);
    }

    /**
     * Method to draw a card from the field.
     * It delegates the request to the network controller.
     *
     * @param gameCard the card to be drawn.
     */
    public void drawCardFromField(GameCard gameCard) {
        networkController.drawCardFromField(gameName, playerName, gameCard);
    }

    /**
     * Method to draw a card from the resource deck.
     * It delegates the request to the network controller.
     */
    public void drawCardFromResourceDeck() {
        networkController.drawCardFromResourceDeck(gameName, playerName);
    }

    /**
     * Method to draw a card from the gold deck.
     * It delegates the request to the network controller.
     */
    public void drawCardFromGoldDeck() {
        networkController.drawCardFromGoldDeck(gameName, playerName);
    }

    /**
     * Method to switch the side of a card.
     * It delegates the request to the network controller.
     *
     * @param gameCardId the card to be switched.
     */
    public void switchCardSide(int gameCardId) {
        networkController.switchCardSide(gameName, playerName, gameCardId);
    }

    /**
     * Method to set a player's objective.
     * It delegates the request to the network controller.
     *
     * @param objectiveCardId the objective card to be set.
     */
    public void setPlayerObjective(int objectiveCardId) {
        networkController.setPlayerObjective(gameName, playerName, objectiveCardId);
    }

    /**
     * Method to send a chat message.
     * It delegates the request to the network controller.
     *
     * @param message  the message to be sent.
     * @param receiver the receiver of the message.
     */
    public void sendMessage(String message, String receiver) {
        networkController.sendChatMessage(new ChatClientToServerMessage(gameName, playerName, message, receiver));
    }

    /**
     * Method to pause the game.
     * It delegates the request to the network controller.
     */
    public void sendDisconnect() {
        networkController.sendDisconnect(gameName, playerName);
    }

    /**
     * Method to resume the game.
     * It delegates the request to the network controller.
     */
    private boolean isClientTurn() {
        return gameControllerView.getCurrentPlayerView().playerName().equals(playerName);
    }

    /**
     * Method to check if the player board has changed.
     *
     * @param oldView     the old view.
     * @param updatedView the updated view.
     * @return true if the player board has changed, false otherwise.
     */
    private boolean isPlayerBoardChanged(GameControllerView oldView, GameControllerView updatedView) {
        if (oldView == null) {
            return true;
        }
        // If the previous status was Game_Paused
        if (oldView.gameStatus().equals(GameStatusEnum.GAME_PAUSED) && !updatedView.gameStatus().equals(GameStatusEnum.GAME_PAUSED)) {
            return true;
        }
        // If the player has changed
        if (!updatedView.getCurrentPlayerView().playerName().equals(oldView.getCurrentPlayerView().playerName())) {
            return true;
        }
        // If the player board has changed
        if (!updatedView.getCurrentPlayerView().playerBoardView().playerBoard().equals(oldView.getCurrentPlayerView().playerBoardView().playerBoard())) {
            return true;
        }
        logger.debug("Player board did not change");
        return false;
    }

    /**
     * Method to check if the player view has changed.
     *
     * @param oldView     the old view.
     * @param updatedView the updated view.
     * @return true if the player view has changed, false otherwise.
     */
    private boolean isPlayerViewChanged(GameControllerView oldView, GameControllerView updatedView) {
        if (oldView == null) {
            return true;
        }

        // If the status has changed
        if (!updatedView.gameStatus().equals(oldView.gameStatus())) {
            return true;
        }

        // If the current player view has changed
        if (!updatedView.getCurrentPlayerView().equals(oldView.getCurrentPlayerView())) {
            return true;
        }

        logger.debug("Player view did not change");
        return false;
    }

    /**
     * Method to check if the game is over.
     *
     * @return true if the game is over, false otherwise.
     */
    public void setIsGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    /**
     * Method to close the connection.
     */
    public void closeConnection() {
        networkController.closeConnection();
    }

    /**
     * Method to handle property change events.
     * Manages the current scene based on the event.
     *
     * @param evt the property change event.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        dontDisplay = false;
        String changedProperty = evt.getPropertyName();
        switch (changedProperty) {
            case "CONNECTION_ESTABLISHED":
                this.currentScene = sceneBuilder.instanceMainMenuScene();
                break;

            case "GET_GAMES":
                gamesList = (ArrayList<GameRecord>) evt.getNewValue();
                this.currentScene = sceneBuilder.instanceGetGamesScene(gamesList);
                break;

            case "UPDATE_VIEW":
                GameControllerView updatedView = (GameControllerView) evt.getNewValue();
                GameControllerView oldView = (GameControllerView) evt.getOldValue();
                gameStatus = updatedView.gameStatus();

                gameControllerView = updatedView;
                if (gameStatus == GameStatusEnum.WAIT_FOR_PLAYERS) {
                    this.currentScene = sceneBuilder.instanceWaitForPlayersScene(updatedView);
                    break;
                }

                if (gameStatus == GameStatusEnum.GAME_PAUSED) {
                    this.currentScene = sceneBuilder.instanceGamePausedScene();
                    break;
                }

                if (gameStatus == GameStatusEnum.GAME_OVER) {
                    this.winnerScene = sceneBuilder.instanceWinnerScene(updatedView);
                    this.currentScene = winnerScene;
                    break;
                }

                // If the client is the current player and the view has changed. Prevent re-rendering the scene if the view has not changed.
                if (isClientTurn() && isPlayerViewChanged(oldView, updatedView)) {
                    switch (gameStatus) {
                        case INIT_PLACE_STARTER_CARD:
                            this.currentScene = sceneBuilder.instanceInitPlaceStarterCardScene(updatedView.getCurrentPlayerView().starterCard());
                            break;
                        case INIT_CHOOSE_PLAYER_COLOR:
                            this.currentScene = sceneBuilder.instanceInitChoosePlayerColorScene(updatedView.gameView().availablePlayerColors());
                            break;
                        case INIT_CHOOSE_OBJECTIVE_CARD:
                            this.currentScene = sceneBuilder.instanceInitSetObjectiveCardScene(updatedView.getCurrentPlayerView().choosableObjectives());
                            break;
                        case DRAW_CARD:
                            this.currentScene = sceneBuilder.instanceDrawCardScene(updatedView.getCurrentPlayerView().playerBoardView().playerBoard(), updatedView.gameView().globalBoardView().globalObjectives(), updatedView.getCurrentPlayerView().objectiveCard(), updatedView.getCurrentPlayerView().playerHandView().hand(), updatedView.gameView().globalBoardView(), updatedView.isLastRound(), updatedView.remainingRoundsToEndGame());
                            break;
                        case PLACE_CARD:
                            this.currentScene = sceneBuilder.instancePlaceCardScene(updatedView.getCurrentPlayerView().playerBoardView().playerBoard(), updatedView.gameView().globalBoardView().globalObjectives(), updatedView.getCurrentPlayerView().objectiveCard(), updatedView.getCurrentPlayerView().playerHandView().hand(), updatedView.gameView().playerViews(), updatedView.isLastRound(), updatedView.remainingRoundsToEndGame());
                            break;
                    }
                    break;
                }

                if (!isClientTurn() && isPlayerBoardChanged(oldView, updatedView)) {
                    this.currentScene = sceneBuilder.instanceOtherPlayerTurnScene(updatedView.getCurrentPlayerView().playerName(), updatedView.getCurrentPlayerView().color(), updatedView.getCurrentPlayerView().playerBoardView().playerBoard(), updatedView.isLastRound(), updatedView.remainingRoundsToEndGame());
                    break;
                }
                dontDisplay = true;
                break;

            case "CHAT_MESSAGE":
                messages.add((ChatServerToClientMessage) evt.getNewValue());
                if (isInChat)
                    showChat();
                dontDisplay = true;
                break;

            case "GAME_DELETED":
                closeConnection();
                dontDisplay = true;
                break;

            case "ERROR":
                String errorMessage = (String) evt.getNewValue();
                if (isInChat)
                    showChat();
                else
                    showCurrentScene();
                new DrawArea("\n" + errorMessage, ColorsEnum.RED).println();
                dontDisplay = true;
                break;
        }
        if (!dontDisplay && !isGameOver)
            showCurrentScene();
    }
}
