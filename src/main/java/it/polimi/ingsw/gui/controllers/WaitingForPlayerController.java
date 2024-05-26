package it.polimi.ingsw.gui.controllers;


import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
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

    private ClientNetworkControllerMapper networkControllerMapper;

    @FXML
    private ListView<String> playerListView;

    @FXML
    private Text gameName;

    /**
     * Returns the name of the controller.
     *
     * @return the name of the controller.
     */
    @Override
    public ControllersEnum getName() {
        return NAME;
    }

    @Override
    public void beforeMount() {
        return;
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
        GameStatusEnum gameStatus = updatedView.gameStatus();
        setUpPlayerListView(updatedView);

        logger.debug("WaitingForPlayerController beforeMount");
        if (networkControllerMapper == null) {
            networkControllerMapper = getProperty("networkControllerMapper");
            networkControllerMapper.addPropertyChangeListener(this);
        }

        // Put here interaction with networkControllerMapper
    }

    /**
     * This method is called before switching to a new scene.
     * It should be overridden by the subclasses to perform any necessary operations before switching to a new scene.
     */
    @Override
    public void beforeUnmount() {
        networkControllerMapper.removePropertyChangeListener(this);
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
        if (!"UPDATE_VIEW".equals(changedProperty)) return;
        // Setup variables
        GameControllerView updatedView = (GameControllerView) evt.getNewValue();
        GameStatusEnum gameStatus = updatedView.gameStatus();

        setUpPlayerListView(updatedView);

        if (gameStatus == GameStatusEnum.INIT_PLACE_STARTER_CARD) {
            logger.debug("Show place starter card scene");
            //switchScene(ControllersEnum.PLACE_STARTER_CARD);
        }

    }

    private void setUpPlayerListView(GameControllerView updatedView) {
        GameStatusEnum gameStatus = updatedView.gameStatus();

        if (gameStatus == GameStatusEnum.WAIT_FOR_PLAYERS) {
            String gameNameString = updatedView.gameView().gameName();
            if (gameNameString.length() > 26) {
                gameNameString = gameNameString.substring(0, 23) + "...";
            }
            logger.debug("Setting game name" + gameNameString);
            gameName.setText(gameNameString);
            logger.debug("Received player list");
            ArrayList<String> playersList = updatedView.gameView().playerViews().stream().map(PlayerView::playerName).collect(Collectors.toCollection(ArrayList::new));
            setProperty("playersList", playersList);
            Platform.runLater(() -> {
                playerListView.getItems().clear();
                playerListView.getItems().addAll(playersList);
            });
        }
    }

    @FXML
    public void initialize() {
        playerListView.setCellFactory(param -> new PlayerCell());

    }

    @FXML
    public void back(ActionEvent actionEvent) {
        switchScene(ControllersEnum.MAIN_MENU);
    }
}
