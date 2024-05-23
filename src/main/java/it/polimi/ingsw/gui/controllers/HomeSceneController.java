package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;


public class HomeSceneController extends Controller implements PropertyChangeListener {

    /**
     * The name of the controller. This is a static variable, meaning it's shared among all instances of this class.
     */
    public static final ControllersEnum NAME = ControllersEnum.HOME;

    /**
     * The logger.
     */
    private final static Logger logger = LogManager.getLogger(HomeSceneController.class);
    @FXML
    private StackPane rootPane;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private Button quitButton;
    private ClientNetworkControllerMapper networkControllerMapper;

    /**
     * The initialize method is called when the scene and controller are created.
     */
    @FXML
    public void initialize() {
        logger.debug("Initializing HomeSceneController");

        if (backgroundImage == null) {
            logger.debug("backgroundImage is not initialized!");
        } else {
            // Bind the background image size to the size of the StackPane
            backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
            backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());
            logger.debug("backgroundImage initialized successfully");
        }

    }

    public void beforeMount() {
        if (this.networkControllerMapper == null) {
            this.networkControllerMapper = getProperty("networkControllerMapper");
            this.networkControllerMapper.addPropertyChangeListener(this);
        }
    }

    public void createGame() {
        // create game
        logger.debug("Game created");

    }

    public void joinGame() {
        // join game
        logger.debug("Game joined");
    }

    public void quit() {
        //quit app
        quitButton.setOnMouseClicked(mouseEvent -> {
            quitButton.getStyleClass().add("button-clicked");
            quitButton.setOnMouseReleased(mouseEvent2 -> quitButton.getStyleClass().remove("button-clicked"));
            logger.debug("GUIApp quit");
            Platform.exit();
        });
    }

    public void switchToGamesView(ActionEvent event) throws IOException {
        switchScene(ControllersEnum.GAMES_LIST);
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
        // TODO

    }
}