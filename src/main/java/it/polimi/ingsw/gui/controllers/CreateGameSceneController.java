package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CreateGameSceneController extends Controller implements PropertyChangeListener {
    public static final ControllersEnum NAME = ControllersEnum.CREATE_GAME;
    private final static Logger logger = LogManager.getLogger(CreateGameSceneController.class);
    private ClientNetworkControllerMapper networkControllerMapper;

    @FXML
    private TextField gameTextField;

    @FXML
    private TextField playerTextField;

    private int nPlayers = 0;


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
    public void createGame(ActionEvent actionEvent) {
        String gameName = gameTextField.getText();
        String playerName = playerTextField.getText();
        logger.debug("Create game: {} player: {}", gameName, playerName);
        if (gameName == null || playerName == null || nPlayers == 0)
            showErrorPopup("Please fill all the fields");
        else {
            networkControllerMapper.createGame(gameName, playerName, nPlayers);
            switchScene(ControllersEnum.WAITING_FOR_PLAYER);
        }
    }

    @FXML
    public void back(ActionEvent actionEvent) {
        switchScene(getPreviousLayoutName());
    }

    @FXML
    public void player2(ActionEvent actionEvent) {
        nPlayers = 2;
    }

    @FXML
    public void player3(ActionEvent actionEvent) {
        nPlayers = 3;
    }

    @FXML
    public void player4(ActionEvent actionEvent) {
        nPlayers = 4;
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
        }
    }
}
