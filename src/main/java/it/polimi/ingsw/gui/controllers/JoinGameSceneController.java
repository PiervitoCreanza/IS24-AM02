package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;


public class JoinGameSceneController extends Controller implements PropertyChangeListener {
    public static final ControllersEnum NAME = ControllersEnum.GAMES_LIST;
    private final static Logger logger = LogManager.getLogger(JoinGameSceneController.class);

    @FXML
    private TextField playerTextField;


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
    }

    /**
     * This method is called before switching to a new scene.
     * It should be overridden by the subclasses to perform any necessary operations before switching to a new scene.
     */
    @Override
    public void beforeUnmount() {
        networkControllerMapper.removePropertyChangeListener(this);
    }

    @FXML
    public void joinGame(ActionEvent actionEvent) {
        String gameName = getProperty("gameName");
        String playerName = playerTextField.getText();
        setProperty("playerName", playerName);
        logger.debug("Joining game: {} with player: {}", gameName, playerName);
        if (gameName != null && playerName != null) {
            networkControllerMapper.joinGame(gameName, playerName);
        }
    }

    @FXML
    public void back(ActionEvent actionEvent) {
        switchScene(getPreviousLayoutName());
    }

    private void showErrorPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals("ERROR")) {
            Platform.runLater(() -> showErrorPopup((String) evt.getNewValue()));
            switchScene(getPreviousLayoutName());
        }

        if (evt.getPropertyName().equals("UPDATE_VIEW")) {

            boolean isMyTurn = Objects.equals(getProperty("playerName"), ((GameControllerView) evt.getNewValue()).getCurrentPlayerView().playerName());
            if (!isMyTurn) {
                // TODO: Show wait scene
                logger.debug("Not my turn");
                return;
            }
            GameControllerView gameControllerView = (GameControllerView) evt.getNewValue();
            if (gameControllerView.gameStatus() == GameStatusEnum.WAIT_FOR_PLAYERS) {
                switchScene(ControllersEnum.WAITING_FOR_PLAYER, evt);
                return;
            }
            if (gameControllerView.gameStatus() == GameStatusEnum.INIT_PLACE_STARTER_CARD) {
                switchScene(ControllersEnum.INIT_PLACE_STARTER_CARD, evt);
                return;
            }

            if (gameControllerView.gameStatus() == GameStatusEnum.INIT_CHOOSE_PLAYER_COLOR) {
                Platform.runLater(() -> switchScene(ControllersEnum.INIT_SET_PION, evt));
                return;
            }

            if (gameControllerView.gameStatus() == GameStatusEnum.INIT_CHOOSE_OBJECTIVE_CARD) {
                Platform.runLater(() -> switchScene(ControllersEnum.INIT_SET_OBJECTIVE_CARD, evt));
                return;
            }
            // TODO: Add the other scenes
            switchScene(ControllersEnum.GAME_SCENE, evt);
        }
    }
}
