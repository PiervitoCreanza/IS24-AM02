package it.polimi.ingsw.gui.controllers.InitScene;

import it.polimi.ingsw.gui.controllers.Controller;
import it.polimi.ingsw.gui.controllers.ControllersEnum;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public abstract class InitScene extends Controller {

    @FXML
    protected AnchorPane root;

    @FXML
    abstract void continueAction();

    @FXML
    protected void initialize() {
        root.onKeyPressedProperty().set(event -> {
            switch (event.getCode()) {
                case ENTER:
                    continueAction();
                    break;
                case ESCAPE:
                    networkControllerMapper.sendDisconnect();
                    networkControllerMapper.closeConnection();
                    switchScene(ControllersEnum.MAIN_MENU);
                    break;
            }
        });
    }
}
