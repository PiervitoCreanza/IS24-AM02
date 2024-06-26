package it.polimi.ingsw.view.gui.controllers.menu.gamesList;

import it.polimi.ingsw.view.gui.utils.GUIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Class representing the controller for a game cell in the GUI.
 * It manages the interaction between the game cell in the view and the model.
 */
public class GameListCellController {

    /**
     * Label representing the game's name.
     */
    @FXML
    private Label gameNameLabel;

    /**
     * Label representing the players' count.
     */
    @FXML
    private Label playersLabel;

    /**
     * Method to set the game's name in the game cell.
     * If the game's name is longer than 35 characters, it truncates it and adds an ellipsis.
     *
     * @param gameName the name of the game
     */
    public void setGameName(String gameName) {
        gameNameLabel.setText(GUIUtils.truncateString(gameName, 35));
    }

    /**
     * Method to set the players' count in the game cell.
     * It also sets the style class of the players' label depending on whether the game is full.
     *
     * @param playersCount the players' count
     * @param isFull       a boolean indicating whether the game is full
     */
    public void setPlayersCount(String playersCount, boolean isFull) {
        playersLabel.setText(playersCount);
        playersLabel.getStyleClass().removeAll("players-count-full", "players-count-available");
        if (isFull) {
            playersLabel.getStyleClass().add("players-count-full");
        } else {
            playersLabel.getStyleClass().add("players-count-available");
        }
    }
}