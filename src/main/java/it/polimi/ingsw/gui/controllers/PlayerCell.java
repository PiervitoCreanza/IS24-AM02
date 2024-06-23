// PlayerCell.java
package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.gui.controllers.gamesList.GameListCell;
import it.polimi.ingsw.gui.utils.GUIUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Class representing a cell in a list of players in the GUI.
 * It extends the JavaFX ListCell class and manages the interaction between the player cell in the view and the model.
 */
public class PlayerCell extends ListCell<String> {
    /**
     * Logger for this class.
     */
    private static final Logger logger = LogManager.getLogger(GameListCell.class);

    /**
     * HBox representing the root of the player cell.
     */
    private HBox root;

    /**
     * Controller for the player cell.
     */
    private PlayerCellController controller;

    /**
     * Constructor for the PlayerCell class.
     * It initializes the root and the controller by loading them from the PlayerCell.fxml file.
     */
    public PlayerCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PlayerCell.fxml"));
            root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            logger.error("Error loading PlayerCell", e);
        }
    }

    /**
     * Method to update the item in the player cell.
     * It sets the graphic of the cell to null if the item is empty or null, and to the root otherwise.
     * If the player's name is longer than 45 characters, it truncates it and adds an ellipsis.
     *
     * @param player the name of the player
     * @param empty  a boolean indicating whether the item is empty
     */
    @Override
    protected void updateItem(String player, boolean empty) {
        super.updateItem(player, empty);

        if (empty || player == null) {
            setGraphic(null);
        } else {
            controller.setPlayerName(GUIUtils.truncateString(player, 45));
            setGraphic(root);
        }
    }
}