package it.polimi.ingsw.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HomeSceneController extends Controller {

    public static final ControllersEnum NAME = ControllersEnum.HOME;

    private static final Logger logger = LogManager.getLogger(HomeSceneController.class);


    @FXML
    public void initialize() {
    }

    // TODO: Make it work
    @FXML
    public void handleKeyPressed(KeyEvent event) {
        logger.debug("Handle key pressed: {}", event.getText());
        switchScene(ControllersEnum.MAIN_MENU);
    }

    public void handleMousePress() {
        logger.debug("Mouse click detected");
        switchScene(ControllersEnum.MAIN_MENU);
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
     * This method is called before showing the scene.
     * It should be overridden by the subclasses to perform any necessary operations before showing the scene.
     */
    @Override
    public void beforeMount() {

    }

    /**
     * This method is called before switching to a new scene.
     * It should be overridden by the subclasses to perform any necessary operations before switching to a new scene.
     */
    @Override
    public void beforeUnmount() {

    }
}
