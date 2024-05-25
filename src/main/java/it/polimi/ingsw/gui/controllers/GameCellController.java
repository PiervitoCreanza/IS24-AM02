// GameCellController.java
package it.polimi.ingsw.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameCellController {

    @FXML
    private Label gameNameLabel;

    @FXML
    private Label playersLabel;

    public void setGameName(String gameName) {
        if (gameName.length() > 35) {
            gameName = gameName.substring(0, 32) + "...";
        }
        gameNameLabel.setText(gameName);
    }

    public void setPlayersCount(String playersCount, boolean isFull) {
        playersLabel.setText(playersCount);
        if (isFull) {
            playersLabel.getStyleClass().add("players-count-full");
        } else {
            playersLabel.getStyleClass().add("players-count-available");
        }
    }
}