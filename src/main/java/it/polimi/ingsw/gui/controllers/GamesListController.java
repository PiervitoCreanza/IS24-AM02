package it.polimi.ingsw.gui.controllers;


import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class GamesListController extends Controller implements PropertyChangeListener {
    public static final ControllersEnum NAME = ControllersEnum.GAMES_LIST;
    private final static Logger logger = LogManager.getLogger(GamesListController.class);
    private ClientNetworkControllerMapper networkControllerMapper;
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
    public void beforeMount() {
        logger.debug("GamesListController beforeMount");
        if (networkControllerMapper == null) {
            networkControllerMapper = getProperty("networkControllerMapper");
            networkControllerMapper.addPropertyChangeListener(this);
        }

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
        String changedProperty = evt.getPropertyName();
        if (!"GET_GAMES".equals(changedProperty)) return;

        logger.debug("Received games list");
        ArrayList<GameRecord> gamesList = (ArrayList<GameRecord>) evt.getNewValue();
        Platform.runLater(() -> {
            gameListView.getItems().clear();
            gameListView.getItems().addAll(gamesList.stream().map(GameRecord::gameName).toList());
        });
    }

    public void refreshList(ActionEvent actionEvent) {
        networkControllerMapper.getGames();
    }

    public void joinGame(ActionEvent actionEvent) {
        networkControllerMapper.joinGame(gameListView.getSelectionModel().getSelectedItem(), "GUI player");
    }

    public void createGame(ActionEvent actionEvent) {
    }

    public void quit(ActionEvent actionEvent) {
    }
}
