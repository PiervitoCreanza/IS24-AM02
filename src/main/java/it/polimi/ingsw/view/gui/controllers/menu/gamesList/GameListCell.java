package it.polimi.ingsw.view.gui.controllers.menu.gamesList;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class representing a cell in a list of games in the GUI.
 * It extends the JavaFX ListCell class and manages the interaction between the game cell in the view and the model.
 */
public class GameListCell extends ListCell<String> {
    /**
     * Logger for this class.
     */
    private static final Logger logger = LogManager.getLogger(GameListCell.class);

    /**
     * HBox representing the root of the game cell.
     */
    private HBox root;

    /**
     * Controller for the game cell.
     */
    private GameListCellController controller;

    /**
     * Constructor for the GameListCell class.
     * It initializes the root and the controller by loading them from the GameCell.fxml file.
     */
    public GameListCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GameCell.fxml"));
            root = loader.load();
            logger.debug("GameListCell loaded");
            controller = loader.getController();
        } catch (IOException e) {
            logger.error("Error loading GameListCell", e);
        }
    }

    /**
     * Method to update the item in the game cell.
     * It sets the graphic of the cell to null if the item is empty or null, and to the root otherwise.
     * It also sets the game name and player count in the controller.
     *
     * @param game  the name of the game
     * @param empty a boolean indicating whether the item is empty
     */
    @Override
    protected void updateItem(String game, boolean empty) {
        super.updateItem(game, empty);

        if (empty || game == null) {
            logger.debug("GameListCell empty");
            setGraphic(null);
        } else {
            controller.setGameName(game);
            // You can set the player count here if you have that data
            @SuppressWarnings("unchecked")
            ArrayList<GameRecord> gamesList = (ArrayList<GameRecord>) getScene().getProperties().get("gamesList");
            logger.debug("GamesList: {}", gamesList);
            GameRecord gameRecord = gamesList.stream().filter(g -> g.gameName().equals(game)).findFirst().orElse(null);
            if (gameRecord != null) {
                controller.setPlayersCount(gameRecord.joinedPlayers() + "/" + gameRecord.maxAllowedPlayers() + " players", gameRecord.isFull());
            }
            setGraphic(root);
        }
    }
}