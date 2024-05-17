package it.polimi.ingsw.tui.controller;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.tui.commandLine.CLIReader;
import it.polimi.ingsw.tui.commandLine.ClientStatusEnum;
import it.polimi.ingsw.tui.view.scene.Displayable;
import it.polimi.ingsw.tui.view.scene.ScenesEnum;
import it.polimi.ingsw.tui.view.scene.StageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * The TUIViewController class is the controller for the text user interface.
 * It is responsible for managing the user interface and the network controller.
 */
public class TUIViewController implements PropertyChangeListener {

    /**
     * Instance of StageManager to manage different scenes in the game.
     */
    private final StageManager stageManager;

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
     * Status of the client, initially set to MAIN_MENU.
     */
    private ClientStatusEnum status = ClientStatusEnum.MAIN_MENU;

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
    private Displayable currentScene;

    /**
     * List of games.
     */
    private ArrayList<GameRecord> gamesList;

    /**
     * List of players' cards.
     */
    private GameControllerView gameControllerView;

    /**
     * Constructor for TUIViewController.
     *
     * @param networkController the network controller.
     */
    public TUIViewController(ClientNetworkControllerMapper networkController) {
        this.networkController = networkController;
        this.stageManager = new StageManager(this);
        this.cliReader = new CLIReader(this);
        cliReader.start();
    }

    /**
     * Method to start the game.
     */
    public void start() {
        this.currentScene = stageManager.showMainMenuScene();
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
    public ClientStatusEnum getClientStatus() {
        return status;
    }

    /**
     * Method to handle user input of the current scene.
     *
     * @return the game status.
     */
    public void handleUserInput(String input) {
        this.currentScene.handleUserInput(input);
    }

    /**
     * Method used by a UserInputScene to request a scene change.
     * scene The scene to be selected.
     */
    public void selectScene(ScenesEnum scene) {
        switch (scene) {
            case MAIN_MENU -> {
                this.currentScene = stageManager.showMainMenuScene();
            }
            case CREATE_GAME -> {
                this.currentScene = stageManager.showCreateGameScene();
            }
            case JOIN_GAME -> {
                this.currentScene = stageManager.showJoinGameScene();
            }
        }
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
            this.currentScene = stageManager.showGetGamesScene(gamesList);
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

    private boolean isClientTurn() {
        return gameControllerView.getCurrentPlayerView().playerName().equals(playerName);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        String changedProperty = evt.getPropertyName();
        switch (changedProperty) {
            case "GET_GAMES":
                gamesList = (ArrayList<GameRecord>) evt.getNewValue();
                this.currentScene = stageManager.showGetGamesScene(gamesList);
                status = ClientStatusEnum.GET_GAMES;
                break;

            case "GAME_DELETED":
                String message = (String) evt.getNewValue();
                System.out.println("Game deleted: " + message);
                break;

            case "UPDATE_VIEW":
                GameControllerView updatedView = (GameControllerView) evt.getNewValue();
                gameStatus = updatedView.gameStatus();

                gameControllerView = updatedView;
                if (gameStatus == GameStatusEnum.WAIT_FOR_PLAYERS) {
                    this.currentScene = stageManager.showWaitForPlayersScene(updatedView);
                    return;
                }

                if (playerName.equals(updatedView.getCurrentPlayerView().playerName())) {
                    switch (gameStatus) {
                        case INIT_PLACE_STARTER_CARD:
                            this.currentScene = stageManager.showInitPlaceStarterCardScene(updatedView.getCurrentPlayerView().starterCard());
                            break;
                        case INIT_CHOOSE_PLAYER_COLOR:
                            this.currentScene = stageManager.showInitChoosePlayerColorScene(updatedView.gameView().availablePlayerColors());
                            break;
                        case INIT_CHOOSE_OBJECTIVE_CARD:
                            this.currentScene = stageManager.showInitSetObjectiveCardScene(updatedView.getCurrentPlayerView().choosableObjectives());
                            break;
                        case DRAW_CARD:
                            this.currentScene = stageManager.showDrawCardScene(updatedView.getCurrentPlayerView().playerBoardView().playerBoard(), updatedView.gameView().globalBoardView().globalObjectives(), updatedView.getCurrentPlayerView().objectiveCard(), updatedView.getCurrentPlayerView().playerHandView().hand(), updatedView.gameView().globalBoardView());
                            break;
                        case PLACE_CARD:
                            this.currentScene = stageManager.showPlaceCardScene(updatedView.getCurrentPlayerView().playerBoardView().playerBoard(), updatedView.gameView().globalBoardView().globalObjectives(), updatedView.getCurrentPlayerView().objectiveCard(), updatedView.getCurrentPlayerView().playerHandView().hand(), updatedView.gameView().playerViews());
                            break;
                    }
                }
                break;

            case "ERROR":
                String errorMessage = (String) evt.getNewValue();
                System.out.println("Error: " + errorMessage);
                this.currentScene.display();
                break;


        }
    }
}
