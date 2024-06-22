package it.polimi.ingsw.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;

public class HomeSceneController extends Controller {

    private static final ControllersEnum NAME = ControllersEnum.HOME;

    private static final Logger logger = LogManager.getLogger(HomeSceneController.class);

    private boolean connected = false;

    @FXML
    private StackPane root;


    @FXML
    private void initialize() {
        // Set the focus on the root pane to capture key events
        Platform.runLater(() -> root.requestFocus());
    }

    private void connectToServer() {
        // Avoid multiple connections
        if (!connected) {
            networkControllerMapper.connect();
            connected = true;
        }
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
        logger.debug("Handle key pressed: {}", event.getText());
        connectToServer();
    }

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
