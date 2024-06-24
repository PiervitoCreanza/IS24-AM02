package it.polimi.ingsw.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;

/**
 * This class is responsible for controlling the home scene.
 * It handles key and mouse events to connect to the server.
 */
public class HomeSceneController extends Controller {

    /**
     * The name of the controller.
     */
    private static final ControllersEnum NAME = ControllersEnum.HOME;

    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(HomeSceneController.class);

    /**
     * The network controller mapper.
     */
    private boolean connected = false;

    /**
     * The root pane.
     */
    @FXML
    private StackPane root;

    /**
     * Initializes the home scene controller.
     * It sets the focus on the root pane to capture key events.
     */
    @FXML
    private void initialize() {
        // Set the focus on the root pane to capture key events
        Platform.runLater(() -> root.requestFocus());
    }

    /**
     * Connects to the server.
     * It avoids multiple connections.
     */
    private void connectToServer() {
        // Avoid multiple connections
        if (!connected) {
            networkControllerMapper.connect();
            connected = true;
        }
    }

    /**
     * Handles the key pressed event.
     * It logs the key pressed and connects to the server.
     *
     * @param event The key event.
     */
    @FXML
    private void handleKeyPressed(KeyEvent event) {
        logger.debug("Handle key pressed: {}", event.getText());
        connectToServer();
    }

    /**
     * Handles the mouse press event.
     * It logs the mouse click and connects to the server.
     */
    @FXML
    private void handleMousePress() {
        logger.debug("Mouse click detected");
        connectToServer();
    }

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
     * This method is called before switching to a new scene.
     * It should be overridden by the subclasses to perform any necessary operations before switching to a new scene.
     */
    @Override
    public void beforeUnmount() {

    }

    /**
     * This method is called before showing the scene.
     * It should be overridden by the subclasses to perform any necessary operations before showing the scene.
     * If the switchScene was caused by a property change, the event is passed as an argument.
     *
     * @param evt the property change event that caused the switch.
     */
    @Override
    public void beforeMount(PropertyChangeEvent evt) {
        networkControllerMapper.addPropertyChangeListener(this);
    }
}