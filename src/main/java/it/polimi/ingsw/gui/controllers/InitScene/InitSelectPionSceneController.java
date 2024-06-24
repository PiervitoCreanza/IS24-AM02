package it.polimi.ingsw.gui.controllers.InitScene;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.gui.controllers.ControllersEnum;
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

/**
 * This class is a controller for the InitSelectPionScene.
 * It handles the interactions between the user interface and the game logic during the initial selection of pions.
 */
public class InitSelectPionSceneController extends InitScene {
    /**
     * The name of the controller.
     */
    private static final ControllersEnum NAME = ControllersEnum.INIT_SET_PION;
    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(InitSelectPionSceneController.class);

    /**
     * The image view for the pion.
     */
    @FXML
    private ImageView pionImageView;

    /**
     * The map of pion images.
     */
    private final Map<PlayerColorEnum, String> pionImagesMap = Map.of(
            PlayerColorEnum.RED, "/CODEX_pion_rouge.png",
            PlayerColorEnum.BLUE, "/CODEX_pion_bleu.png",
            PlayerColorEnum.GREEN, "/CODEX_pion_vert.png",
            PlayerColorEnum.YELLOW, "/CODEX_pion_jaune.png"
    );

    /**
     * The list of available pion colors.
     */
    private ArrayList<PlayerColorEnum> availableColors;

    /**
     * The current index of the selected pion.
     */
    private int currentIndex = 0;

    /**
     * This method is called when the next pion button is clicked.
     * It updates the current index and the pion image.
     */
    @FXML
    private void nextPion() {
        currentIndex = (currentIndex + 1) % availableColors.size();
        updatePionImage();
    }

    /**
     * This method is called when the previous pion button is clicked.
     * It updates the current index and the pion image.
     */
    @FXML
    private void previousPion() {
        currentIndex = (currentIndex - 1 + availableColors.size()) % availableColors.size();
        updatePionImage();
    }

    /**
     * This method updates the pion image based on the current index.
     */
    private void updatePionImage() {
        PlayerColorEnum selectedColor = availableColors.get(currentIndex);
        Image image = new Image(getClass().getResourceAsStream(pionImagesMap.get(selectedColor)));
        pionImageView.setImage(image);
    }

    /**
     * This method is called when the continue button is clicked.
     * It prints the selected pion color in the console and sends the selected color to the network controller.
     */
    @FXML
    protected void continueAction() {
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
     * It updates the scene based on the game status.
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