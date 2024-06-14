// PlayerCell.java
package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.gui.controllers.gamesList.GameListCell;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class PlayerCell extends ListCell<String> {
    private final Logger logger = LogManager.getLogger(GameListCell.class);
    private HBox root;
    private PlayerCellController controller;

    public PlayerCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/playerCell.fxml"));
            root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            logger.error("Error loading PlayerCell", e);
        }
    }

    @Override
    protected void updateItem(String player, boolean empty) {
        super.updateItem(player, empty);

        if (!empty & player != null) {
            if (player.length() > 45) {
                player = player.substring(0, 42) + "...";
            }
            controller.setPlayerName(player);
            setGraphic(root);
        }
    }
}