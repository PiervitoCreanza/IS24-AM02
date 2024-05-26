// PlayerCell.java
package it.polimi.ingsw.gui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerCell extends ListCell<String> {
    private final Logger logger = LogManager.getLogger(GameCell.class);
    private HBox root;
    private PlayerCellController controller;

    public PlayerCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/playerCell.fxml"));
            root = loader.load();
            logger.debug("PlayerCell loaded");
            controller = loader.getController();
        } catch (IOException e) {
            logger.error("Error loading PlayerCell", e);
        }
    }

    @Override
    protected void updateItem(String player, boolean empty) {
        super.updateItem(player, empty);

        if (empty || player == null) {
            logger.debug("PlayerCell empty");
            setGraphic(null);
        } else {
            if (player.length() > 52) {
                player = player.substring(0, 49) + "...";
            }
            controller.setPlayerName(player);
            // You can set the player count here if you have that data
            ArrayList<String> playerList = (ArrayList<String>) getScene().getProperties().get("playerList");
            logger.debug("PlayerList: " + playerList);
            setGraphic(root);
        }
    }
}