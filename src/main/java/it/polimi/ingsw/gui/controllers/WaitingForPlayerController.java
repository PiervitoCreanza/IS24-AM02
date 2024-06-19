package it.polimi.ingsw.gui.controllers;


import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.network.virtualView.PlayerView;
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

public class WaitingForPlayerController extends Controller implements PropertyChangeListener {

    public static final ControllersEnum NAME = ControllersEnum.WAITING_FOR_PLAYER;

    private final static Logger logger = LogManager.getLogger(WaitingForPlayerController.class);

    @FXML
    private ListView<String> playerListView;

    @FXML
    private Text gameName;

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

    private void setUpPlayerListView(GameControllerView updatedView) {
        String gameNameString = updatedView.gameView().gameName();
        if (gameNameString.length() > 26) {
            gameNameString = gameNameString.substring(0, 23) + "...";
        }
        logger.debug("Setting game name" + gameNameString);
        gameName.setText(gameNameString);
        logger.debug("Received player list");
        ArrayList<String> playersList = updatedView.gameView().playerViews().stream().map(PlayerView::playerName).collect(Collectors.toCollection(ArrayList::new));
        GameStatusEnum gameStatus = updatedView.gameStatus();
        if (gameStatus == GameStatusEnum.WAIT_FOR_PLAYERS) {
            waitingMessage.setText("Waiting for players to join...");
        } else {
            String currentPlayer = updatedView.getCurrentPlayerView().playerName();
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

    @FXML
    public void initialize() {
        playerListView.setCellFactory(param -> new PlayerCell());
    }

    @FXML
    public void back(ActionEvent actionEvent) {
        showInfoBox("red", "Disconnecting...", "You are disconnecting from the server.");
        networkControllerMapper.closeConnection();
    }
}
