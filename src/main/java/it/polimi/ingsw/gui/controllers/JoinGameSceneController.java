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


public class JoinGameSceneController extends Controller implements PropertyChangeListener {
    public static final ControllersEnum NAME = ControllersEnum.GAMES_LIST;
    private final static Logger logger = LogManager.getLogger(JoinGameSceneController.class);
    private ClientNetworkControllerMapper networkControllerMapper;

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
    public void joinGame(ActionEvent actionEvent) {
        String gameName = getProperty("gameName");
        String playerName = playerTextField.getText();
        logger.debug("Joining game: {} with player: {}", gameName, playerName);
        if (gameName != null && playerName != null)
            networkControllerMapper.joinGame(gameName, playerName);
    }

    public void quit(ActionEvent actionEvent) {
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
    }
}
