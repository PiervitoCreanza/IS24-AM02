package it.polimi.ingsw.view.gui.controllers.menu.gamesList;


import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.view.gui.controllers.Controller;
import it.polimi.ingsw.view.gui.controllers.ControllersEnum;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * Class representing the controller for the games list scene in the GUI.
 */
public class GamesListController extends Controller implements PropertyChangeListener {
    /**
     * Name of the controller.
     */
    public static final ControllersEnum NAME = ControllersEnum.GAMES_LIST;

    /**
     * Logger for this class.
     */
    private static final Logger logger = LogManager.getLogger(GamesListController.class);

    /**
     * The list view for the games.
     */
    @FXML
    private ListView<String> gameListView;

    /**
     * Returns the name of the controller.
     *
     * @return the name of the controller.
     */
    @Override
    public ControllersEnum getName() {
        return NAME;
    }

    /**
     * This method is called before showing the scene.
     * It should be overridden by the subclasses to perform any necessary operations before showing the scene.
     */
    @Override
    public void beforeMount(PropertyChangeEvent evt) {
        logger.debug("GamesListController beforeMount");
        networkControllerMapper.getGames();
    }

    /**
     * This method is called before switching to a new scene.
     * It should be overridden by the subclasses to perform any necessary operations before switching to a new scene.
     */
    @Override
    public void beforeUnmount() {

    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        String changedProperty = evt.getPropertyName();
        if (!"GET_GAMES".equals(changedProperty)) return;

        logger.debug("Received games list");
        ArrayList<GameRecord> gamesList = (ArrayList<GameRecord>) evt.getNewValue();
        setProperty("gamesList", gamesList);
        Platform.runLater(() -> {
            gameListView.getItems().clear();
            gameListView.getItems().addAll(gamesList.stream().map(GameRecord::gameName).toList());
        });
    }

    @FXML
    private void initialize() {
        gameListView.setCellFactory(param -> new GameListCell());
        gameListView.setOnMouseClicked(event -> {
            String gameName = gameListView.getSelectionModel().getSelectedItem();
            if (gameName != null)
                joinGame(gameName);
        });
    }

    @FXML
    private void refreshList(ActionEvent actionEvent) {
        networkControllerMapper.getGames();
    }

    @FXML
    private void joinGame(String gameName) {
        setProperty("gameName", gameName);
        switchScene(ControllersEnum.JOIN_GAME);
    }

    @FXML
    private void createGame(ActionEvent actionEvent) {
        switchScene(ControllersEnum.CREATE_GAME);
    }

    @FXML
    private void back(ActionEvent actionEvent) {
        switchScene(ControllersEnum.MAIN_MENU);
    }
}
