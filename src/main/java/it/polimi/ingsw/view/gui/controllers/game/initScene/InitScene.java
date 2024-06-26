package it.polimi.ingsw.view.gui.controllers.game.initScene;

import it.polimi.ingsw.view.gui.controllers.Controller;
import it.polimi.ingsw.view.gui.controllers.ControllersEnum;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 * This abstract class represents the initial scene of the game.
 * It extends the Controller class and provides common functionality for all initial scenes.
 */
public abstract class InitScene extends Controller {

    /**
     * The root pane of the scene.
     */
    @FXML
    protected AnchorPane root;

    /**
     * This abstract method is called when the continue action is performed.
     * It should be implemented by the subclasses to provide specific functionality.
     */
    @FXML
    abstract void continueAction();

    /**
     * This method is called when the scene is initialized.
     * It sets up the key press handlers for the root pane.
     */
    @FXML
    protected void initialize() {
        root.onKeyPressedProperty().set(event -> {
            switch (event.getCode()) {
                case ENTER:
                    // Perform the continue action when the enter key is pressed
                    continueAction();
                    break;
                case ESCAPE:
                    // Close the connection and switch to the main menu when the escape key is pressed
                    networkControllerMapper.closeConnection();
                    switchScene(ControllersEnum.MAIN_MENU);
                    break;
            }
        });
    }
}