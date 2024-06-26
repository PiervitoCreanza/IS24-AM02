package it.polimi.ingsw.view.gui.controllers.menu.waitingForPlayer;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.network.virtualView.PlayerView;
import it.polimi.ingsw.view.gui.components.toast.ToastLevels;
import it.polimi.ingsw.view.gui.controllers.Controller;
import it.polimi.ingsw.view.gui.controllers.ControllersEnum;
import it.polimi.ingsw.view.gui.utils.GUIUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * This class is a controller for the WaitingForPlayer scene.
 * It handles the interactions between the user interface and the game logic while waiting for players.
 */
public class WaitingForPlayerController extends Controller implements PropertyChangeListener {
    /**
     * The name of the controller. This is a static variable, meaning it's shared among all instances of this class.
     */
    public static final ControllersEnum NAME = ControllersEnum.WAITING_FOR_PLAYER;
    /**
     * The logger.
     */
    private final static Logger logger = LogManager.getLogger(WaitingForPlayerController.class);

    /**
     * The player list view.
     */
    @FXML
    private ListView<String> playerListView;

    /**
     * The game name.
     */
    @FXML
    private Text gameName;

    /**
     * The waiting message.
     */
    @FXML
    private Text waitingMessage;

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
     * In this case, it sets up the player list view based on the updated game view.
     *
     * @param evt The property change event that triggered this method.
     */
    @Override
    public void beforeMount(PropertyChangeEvent evt) {
        String changedProperty = evt.getPropertyName();
        if (!"UPDATE_VIEW".equals(changedProperty)) return;

        GameControllerView updatedView = (GameControllerView) evt.getNewValue();
        setUpPlayerListView(updatedView);

        logger.debug("WaitingForPlayerController beforeMount");

        // Put here interaction with networkControllerMapper
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
     * It updates the player list view and switches the scene based on the game status.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        String changedProperty = evt.getPropertyName();
        if (!"UPDATE_VIEW".equals(changedProperty)) return;
        // Setup variables
        GameControllerView updatedView = (GameControllerView) evt.getNewValue();
        GameStatusEnum gameStatus = updatedView.gameStatus();

        setUpPlayerListView(updatedView);

        if (gameStatus == GameStatusEnum.INIT_PLACE_STARTER_CARD && updatedView.isMyTurn(getProperty("playerName"))) {
            logger.debug("Show place starter card scene");
            switchScene(ControllersEnum.INIT_PLACE_STARTER_CARD, evt);
        }

        if (gameStatus == GameStatusEnum.PLACE_CARD || gameStatus == GameStatusEnum.DRAW_CARD) {
            logger.debug("Show place workers scene");
            switchScene(ControllersEnum.GAME_SCENE, evt);
        }

    }

    /**
     * This method sets up the player list view.
     * It updates the game name, waiting message, and player list based on the updated game view.
     *
     * @param updatedView The updated game view.
     */
    private void setUpPlayerListView(GameControllerView updatedView) {
        String gameNameString = GUIUtils.truncateString(updatedView.gameView().gameName());

        logger.debug("Setting game name" + gameNameString);
        gameName.setText(gameNameString);
        logger.debug("Received player list");
        ArrayList<String> playersList = updatedView.gameView().playerViews().stream().map(PlayerView::playerName).collect(Collectors.toCollection(ArrayList::new));
        GameStatusEnum gameStatus = updatedView.gameStatus();
        if (gameStatus == GameStatusEnum.WAIT_FOR_PLAYERS) {
            waitingMessage.setText("Waiting for players to join...");
        } else {
            String currentPlayer = GUIUtils.truncateString(updatedView.getCurrentPlayerView().playerName());
            switch (gameStatus) {
                case INIT_PLACE_STARTER_CARD:
                    waitingMessage.setText("Waiting for " + currentPlayer + " to place the starter card...");
                    break;
                case INIT_CHOOSE_PLAYER_COLOR:
                    waitingMessage.setText("Waiting for " + currentPlayer + " to choose the color...");
                    break;
                case INIT_CHOOSE_OBJECTIVE_CARD:
                    waitingMessage.setText("Waiting for " + currentPlayer + " to choose the objective card...");
                    break;
                case GAME_PAUSED:
                    waitingMessage.setText("Game paused...");
                    break;
                default:
                    waitingMessage.setText("Waiting...");
            }
        }
        Platform.runLater(() -> {
            playerListView.getItems().clear();
            playerListView.getItems().addAll(playersList);
        });
    }

    /**
     * This method is called when the scene is initialized.
     * It sets the cell factory for the player list view.
     */
    @FXML
    public void initialize() {
        playerListView.setCellFactory(param -> new PlayerCell());
    }

    /**
     * This method is called when the back button is clicked.
     * It shows a toast message and closes the connection.
     *
     * @param actionEvent The action event that triggered this method.
     */
    @FXML
    public void back(ActionEvent actionEvent) {
        showToast(ToastLevels.ERROR, "Disconnecting...", "You are disconnecting from the server.");
        networkControllerMapper.closeConnection();
    }
}