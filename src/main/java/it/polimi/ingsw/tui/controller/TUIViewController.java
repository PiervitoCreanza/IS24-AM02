package it.polimi.ingsw.tui.controller;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.tui.commandLine.ClientStatusEnum;
import it.polimi.ingsw.tui.view.scene.ScenesEnum;
import it.polimi.ingsw.tui.view.scene.StageManager;

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
     * List of games.
     */
    private ArrayList<GameRecord> gamesList;

    /**
     * Cli parser.
     */
    //private final CLIParser cliParser = new CLIParser();

    /**
     * List of players' cards.
     */
    private GameControllerView gameControllerView;

    /**
     * Constructor for TUIViewController.
     */
    public TUIViewController(ClientNetworkControllerMapper networkController) {
        this.networkController = networkController;
        this.stageManager = new StageManager(this);
    }

    /**
     * Method to start the game.
     */
    public void start() {
        stageManager.showMainMenuScene();

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
     * Method used by a Scene to request a scene change.
     * scene The scene to be selected.
     */
    public void selectScene(ScenesEnum scene) {
        switch (scene) {
            case MAIN_MENU -> {
                stageManager.showMainMenuScene();
            }
            case CREATE_GAME -> {
                stageManager.showCreateGameScene();
            }
            case JOIN_GAME -> {
                stageManager.showJoinGameScene();
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
     * Method to create a new game.
     * It delegates the request to the network controller.
     *
     * @param gameName the name of the game to be created.
     * @param nPlayers the number of players in the game.
     */
    public void createGame(String gameName, String playerName, int nPlayers) {
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
     */
    public void joinGame(int gameID, String playerName) {
        if (gameID < 0 || gameID >= gamesList.size()) {
            System.out.println("Invalid game ID");
            return;
        }
        networkController.joinGame(gamesList.get(gameID).gameName(), playerName);
    }

    /**
     * Method to choose a player color.
     * It delegates the request to the network controller.
     */
    public void choosePlayerColor(PlayerColorEnum playerColor) {
        networkController.choosePlayerColor(gameName, playerName, playerColor);
    }

    /**
     * Method to place a starter card.
     * It delegates the request to the network controller.
     */
    public void placeStarterCard() {
        networkController.placeCard(gameName, playerName, new Coordinate(0, 0), gameControllerView.getCurrentPlayerView().starterCard());
    }

    /**
     * Method to place a card.
     * It delegates the request to the network controller.
     */
    public void placeCard(int cardInt, Coordinate coordinate) {
        if (cardInt < 0 || cardInt >= 3) {
            System.out.println("Invalid card ID");
            return;
        }
        GameCard choosenCard = gameControllerView.getCurrentPlayerView().playerHandView().hand().get(cardInt);


        networkController.placeCard(gameName, playerName, coordinate, choosenCard);
    }

    /**
     * Method to draw a card from the field.
     * It delegates the request to the network controller.
     */
    public void drawCardFromField() {
        networkController.drawCardFromField(gameName, playerName, null);
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
     */
    public void switchCardSide() {
        networkController.switchCardSide(gameName, playerName, null);
    }

    /**
     * Method to set a player's objective.
     * It delegates the request to the network controller.
     */
    public void setPlayerObjective(int objectiveInt) {
        if (objectiveInt < 0 || objectiveInt >= 2) {
            System.out.println("Invalid objective ID");
            return;
        }
        networkController.setPlayerObjective(gameName, playerName, gameControllerView.getCurrentPlayerView().choosableObjectives().get(objectiveInt));
    }

    private boolean isClientTurn() {
        return gameControllerView.getCurrentPlayerView().playerName().equals(playerName);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        String changedProperty = evt.getPropertyName();
        switch (changedProperty) {
            case "GET_GAMES":
                gamesList = (ArrayList<GameRecord>) evt.getNewValue();
                stageManager.showGetGamesScene(gamesList);
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
                    stageManager.showWaitForPlayersScene(updatedView);
                    return;
                }

//                if (isClientTurn()) {
//                    switch (gameStatus) {
//                        case INIT_PLACE_STARTER_CARD -> {
//                            status = ClientStatusEnum.PLACE_STARTER_CARD;
//                        }
//                        case INIT_CHOOSE_PLAYER_COLOR -> {
//                            status = ClientStatusEnum.CHOOSE_PLAYER_COLOR;
//                        }
//                        case INIT_CHOOSE_OBJECTIVE_CARD -> {
//                            status = ClientStatusEnum.CHOOSE_OBJECTIVE_CARD;
//                        }
//                        case PLACE_CARD -> {
//                            status = ClientStatusEnum.PLACE_CARD;
//                        }
//                        case DRAW_CARD -> {
//                            status = ClientStatusEnum.DRAW_CARD;
//                        }
//                    }
//                }

                stageManager.showGameScene(updatedView, playerName);
                break;

            case "ERROR":
                String errorMessage = (String) evt.getNewValue();
                System.out.println("Error: " + errorMessage);
                break;


        }
    }
}
