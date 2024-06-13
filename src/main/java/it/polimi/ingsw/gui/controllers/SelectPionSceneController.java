package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Map;

public class SelectPionSceneController extends Controller {
    private static final ControllersEnum NAME = ControllersEnum.INIT_SET_PION;
    private static final Logger logger = LogManager.getLogger(SelectPionSceneController.class);

    @FXML
    private ImageView pionImageView;

    private final Map<PlayerColorEnum, String> pionImagesMap = Map.of(
            PlayerColorEnum.RED, "/CODEX_pion_rouge.png",
            PlayerColorEnum.BLUE, "/CODEX_pion_bleu.png",
            PlayerColorEnum.GREEN, "/CODEX_pion_vert.png",
            PlayerColorEnum.YELLOW, "/CODEX_pion_jaune.png"
    );

    private ArrayList<PlayerColorEnum> availableColors;

    private int currentIndex = 0;

    @FXML
    private void nextPion() {
        currentIndex = (currentIndex + 1) % availableColors.size();
        updatePionImage();
    }

    @FXML
    private void previousPion() {
        currentIndex = (currentIndex - 1 + availableColors.size()) % availableColors.size();
        updatePionImage();
    }

    private void updatePionImage() {
        PlayerColorEnum selectedColor = availableColors.get(currentIndex);
        Image image = new Image(getClass().getResourceAsStream(pionImagesMap.get(selectedColor)));
        pionImageView.setImage(image);
    }

    @FXML
    private void continueAction() {
        // Print the selected pion color in the console
        System.out.println("Selected Pion Color: " + availableColors.get(currentIndex));
        networkControllerMapper.choosePlayerColor(availableColors.get(currentIndex));
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
        GameControllerView gameControllerView = (GameControllerView) evt.getNewValue();
        availableColors = gameControllerView.gameView().availablePlayerColors();
        updatePionImage();
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        String propertyName = evt.getPropertyName();
        if (propertyName.equals("UPDATE_VIEW")) {
            GameControllerView gameControllerView = (GameControllerView) evt.getNewValue();
            if (gameControllerView.gameStatus() == GameStatusEnum.INIT_CHOOSE_OBJECTIVE_CARD) {
                Platform.runLater(() -> switchScene(ControllersEnum.INIT_SET_OBJECTIVE_CARD, evt));
            }
        }
    }
}