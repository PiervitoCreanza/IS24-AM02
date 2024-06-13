package it.polimi.ingsw.gui.controllers;

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
    public static final ControllersEnum NAME = ControllersEnum.MAIN_MENU;

    /**
     * The logger.
     */
    private final static Logger logger = LogManager.getLogger(MainMenuSceneController.class);

    /**
     * The initialize method is called when the scene and controller are created.
     */
    @FXML
    public void initialize() {
        logger.debug("Initializing MainMenuSceneController");
    }

    @FXML
    public void createGame() {
        switchScene(ControllersEnum.CREATE_GAME);
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
     *
     */
    @Override
    public void beforeUnmount() {
    }
}