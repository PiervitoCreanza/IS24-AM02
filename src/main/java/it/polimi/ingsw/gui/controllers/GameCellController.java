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
        gameNameLabel.setText(gameName);
    }

    public void setPlayersCount(String playersCount) {
        playersLabel.setText(playersCount);
    }
}