package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class MainMenuSceneController extends Controller implements PropertyChangeListener {

    /**
     * The name of the controller. This is a static variable, meaning it's shared among all instances of this class.
     */
    public static final ControllersEnum NAME = ControllersEnum.HOME;

    /**
     * The logger.
     */
    private final static Logger logger = LogManager.getLogger(MainMenuSceneController.class);
    private ClientNetworkControllerMapper networkControllerMapper;

    /**
     * The initialize method is called when the scene and controller are created.
     */
    @FXML
    public void initialize() {
        logger.debug("Initializing MainMenuSceneController");
    }

    public void beforeMount() {
        if (this.networkControllerMapper == null) {
            this.networkControllerMapper = getProperty("networkControllerMapper");
            this.networkControllerMapper.addPropertyChangeListener(this);
        }
    }

    @FXML
    public void createGame() {
        // create game
        logger.debug("Game created");
    }

    @FXML
    public void joinGame() {
        switchScene(ControllersEnum.GAMES_LIST);
    }

    @FXML
    public void quit() {
        //quit app
        logger.debug("Quit");
        System.exit(0);
    }


    public void displayDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

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
        String changedProperty = evt.getPropertyName();
        switch (changedProperty) {
            case "CONNECTION_ESTABLISHED":
                logger.debug("Connection established");
                break;
            case "CONNECTION_CLOSED":
                displayDialog("Connection closed", "The connection to the server has been closed.");
                logger.debug("Connection closed");
                break;
        }
    }

    @Override
    public ControllersEnum getName() {
        return NAME;
    }

    /**
     *
     */
    @Override
    public void beforeUnmount() {
        this.networkControllerMapper.removePropertyChangeListener(this);
    }
}