package it.polimi.ingsw.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;

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
     * If the switchScene was caused by a property change, the event is passed as an argument.
     *
     * @param evt the property change event that caused the switch.
     */
    @Override
    public void beforeMount(PropertyChangeEvent evt) {

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

    }
}
