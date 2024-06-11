// GameListCell.java
package it.polimi.ingsw.gui.controllers.gamesList;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class GameListCell extends ListCell<String> {
    private final Logger logger = LogManager.getLogger(GameListCell.class);
    private HBox root;
    private GameListCellController controller;

    public GameListCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameCell.fxml"));
            root = loader.load();
            logger.debug("GameListCell loaded");
            controller = loader.getController();
        } catch (IOException e) {
            logger.error("Error loading GameListCell", e);
        }
    }

    @Override
    protected void updateItem(String game, boolean empty) {
        super.updateItem(game, empty);

        if (empty || game == null) {
            logger.debug("GameListCell empty");
            setGraphic(null);
        } else {
            controller.setGameName(game);
            // You can set the player count here if you have that data
            ArrayList<GameRecord> gamesList = (ArrayList<GameRecord>) getScene().getProperties().get("gamesList");
            logger.debug("GamesList: " + gamesList);
            GameRecord gameRecord = gamesList.stream().filter(g -> g.gameName().equals(game)).findFirst().orElse(null);
            controller.setPlayersCount(gameRecord.joinedPlayers() + "/" + gameRecord.maxAllowedPlayers() + " players", gameRecord.isFull());
            setGraphic(root);
        }
    }
}