package it.polimi.ingsw.tui.controller;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.tui.commandLine.ClientStatusEnum;
import it.polimi.ingsw.tui.view.scene.StageManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;

/**
 * The TUIViewController class is the controller for the text user interface.
 * It is responsible for managing the user interface and the network controller.
 */
public class TUIViewController implements PropertyChangeListener {

    /**
     * Instance of StageManager to manage different scenes in the game.
     */
    private final StageManager stageManager = new StageManager();

    /**
     * Instance of ClientNetworkControllerMapper to manage network requests.
     */
    private final ClientNetworkControllerMapper networkController = new ClientNetworkControllerMapper();

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
     * Constructor for TUIViewController.
     */
    public TUIViewController() {

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
     */
    public void joinGame() {
        networkController.joinGame(gameName, playerName);
    }

    /**
     * Method to choose a player color.
     * It delegates the request to the network controller.
     */
    public void choosePlayerColor() {
        networkController.choosePlayerColor(gameName, playerName, null);
    }

    /**
     * Method to place a card.
     * It delegates the request to the network controller.
     */
    public void placeCard() {
        networkController.placeCard(gameName, playerName, null, null);
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
    public void setPlayerObjective() {
        networkController.setPlayerObjective(gameName, playerName, null);
    }


    public void propertyChange(PropertyChangeEvent evt) {
        String changedProperty = evt.getPropertyName();
        switch (changedProperty) {
            case "GET_GAMES":
                HashSet<GameRecord> games = (HashSet<GameRecord>) evt.getNewValue();
                stageManager.showGetGamesScene(games);
                status = ClientStatusEnum.GET_GAMES;
                break;

            case "GAME_DELETED":
                String message = (String) evt.getNewValue();
                System.out.println("Game deleted: " + message);
                break;

            case "UPDATE_VIEW":
                GameControllerView updatedView = (GameControllerView) evt.getNewValue();
                gameStatus = updatedView.gameStatus();

                if (gameStatus == GameStatusEnum.WAIT_FOR_PLAYERS) {
                    stageManager.showWaitForPlayersScene(updatedView);
                    return;
                }

                stageManager.showGameScene(updatedView, playerName);
                break;

            case "ERROR":
                String errorMessage = (String) evt.getNewValue();
                System.out.println("Error: " + errorMessage);
                break;


        }
    }
}
