package it.polimi.ingsw.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Class representing the controller for a player cell in the GUI.
 */
public class PlayerCellController {

    /**
     * Label representing the player's name.
     */
    @FXML
    private Label playerNameLabel;

    /**
     * Method to set the player's name in the player cell.
     *
     * @param playerName the name of the player
     */
    public void setPlayerName(String playerName) {
        playerNameLabel.setText(playerName);
    }
}