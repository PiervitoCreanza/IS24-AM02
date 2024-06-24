package it.polimi.ingsw.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * This class is the controller for the main menu scene.
 */
public class MainMenuSceneController extends Controller implements PropertyChangeListener {

    /**
     * The name of the controller. This is a static variable, meaning it's shared among all instances of this class.
     */
    public static final ControllersEnum NAME = ControllersEnum.MAIN_MENU;

    /**
     * The logger.
     */
    private final static Logger logger = LogManager.getLogger(MainMenuSceneController.class);

    @FXML
    private void requestFocus(MouseEvent event) {
        Button hoveredButton = (Button) event.getTarget();
        hoveredButton.requestFocus();
    }

    /**
     * The initialize method is called when the scene and controller are created.
     */
    @FXML
    private void initialize() {
        logger.debug("Initializing MainMenuSceneController");
    }

    @FXML
    private void createGame() {
        switchScene(ControllersEnum.CREATE_GAME);
    }

    @FXML
    private void joinGame() {
        switchScene(ControllersEnum.GAMES_LIST);
    }

    @FXML
    private void quit() {
        //quit app
        logger.debug("Quit");
        System.exit(0);
    }

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
        // Do not show updates on connected players
        super.setShowConnectedPlayers(false);
        // Clear the list of connected players
        connectedPlayers.clear();
        // Re-enable the connected players updates
        super.setShowConnectedPlayers(true);
    }

    /**
     *
     */
    @Override
    public void beforeUnmount() {
    }
}