package it.polimi.ingsw.tui.controller;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.tui.Scene.StageManager;
import it.polimi.ingsw.tui.commandLine.ClientStatusEnum;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;

public class TUIViewController implements PropertyChangeListener {
    private final StageManager stageManager = new StageManager();
    private String playerName;
    private String gameName;

    private ClientStatusEnum status = ClientStatusEnum.MAIN_MENU;
    private GameStatusEnum gameStatus = null;

    /**
     * Constructor for TUIViewController.
     */
    public TUIViewController() {
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

    public void setClientStatus(ClientStatusEnum status) {
        this.status = status;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        String changedProperty = evt.getPropertyName();
        switch (changedProperty) {
            case "GET_GAMES":
                HashSet<GameRecord> games = (HashSet<GameRecord>) evt.getNewValue();
                stageManager.showGetGamesScene(games);
                status = ClientStatusEnum.GET_GAMES;
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
