// GameCellController.java
package it.polimi.ingsw.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PlayerCellController {

    @FXML
    private Label playerNameLabel;

    public void setPlayerName(String playerName) {
        playerNameLabel.setText(playerName);
    }
}