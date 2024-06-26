package it.polimi.ingsw.view.gui.controllers.menu;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.view.gui.components.toast.ToastLevels;
import it.polimi.ingsw.view.gui.controllers.Controller;
import it.polimi.ingsw.view.gui.controllers.ControllersEnum;
import it.polimi.ingsw.view.gui.utils.GUIUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

/**
 * Class representing the controller for the join game scene in the GUI.
 */
public class JoinGameSceneController extends Controller implements PropertyChangeListener {
    /**
     * Name of the controller.
     */
    public static final ControllersEnum NAME = ControllersEnum.GAMES_LIST;

    /**
     * Logger for this class.
     */
    private static final Logger logger = LogManager.getLogger(JoinGameSceneController.class);

    /**
     * Text field for the player name.
     */
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
    private void joinGame(ActionEvent actionEvent) {
        String gameName = getProperty("gameName");
        String playerName = playerTextField.getText();
        setProperty("playerName", playerName);
        logger.debug("Joining game: {} with player: {}", gameName, playerName);
        if (gameName != null && playerName != null) {
            networkControllerMapper.joinGame(gameName, playerName);
        }
    }

    @FXML
    private void back(ActionEvent actionEvent) {
        switchScene(getPreviousLayoutName());
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
        if (evt.getPropertyName().equals("UPDATE_VIEW")) {
            showToast(ToastLevels.SUCCESS, "Game joined", "You joined the game \"" + GUIUtils.truncateString(getProperty("gameName")) + "\" as \"" + GUIUtils.truncateString(getProperty("playerName")) + "\"");
            boolean isMyTurn = Objects.equals(getProperty("playerName"), ((GameControllerView) evt.getNewValue()).getCurrentPlayerView().playerName());
            GameControllerView gameControllerView = (GameControllerView) evt.getNewValue();
            if (gameControllerView.gameStatus() == GameStatusEnum.INIT_PLACE_STARTER_CARD && isMyTurn) {
                switchScene(ControllersEnum.INIT_PLACE_STARTER_CARD, evt);
                return;
            }

            if (gameControllerView.gameStatus() == GameStatusEnum.INIT_CHOOSE_PLAYER_COLOR && isMyTurn) {
                Platform.runLater(() -> switchScene(ControllersEnum.INIT_SET_PION, evt));
                return;
            }

            if (gameControllerView.gameStatus() == GameStatusEnum.INIT_CHOOSE_OBJECTIVE_CARD && isMyTurn) {
                Platform.runLater(() -> switchScene(ControllersEnum.INIT_SET_OBJECTIVE_CARD, evt));
                return;
            }

            if (gameControllerView.gameStatus() == GameStatusEnum.PLACE_CARD || gameControllerView.gameStatus() == GameStatusEnum.DRAW_CARD) {
                Platform.runLater(() -> switchScene(ControllersEnum.GAME_SCENE, evt));
                return;
            }

            Platform.runLater(() -> switchScene(ControllersEnum.WAITING_FOR_PLAYER, evt));
        }
    }

    @FXML
    private void handleKeyPress(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            joinGame(null);
        }
    }
}
