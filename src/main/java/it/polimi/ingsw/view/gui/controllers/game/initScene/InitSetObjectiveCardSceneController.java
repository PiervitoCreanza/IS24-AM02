package it.polimi.ingsw.view.gui.controllers.game.initScene;

import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.view.gui.components.GuiCardFactory;
import it.polimi.ingsw.view.gui.controllers.ControllersEnum;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

/**
 * This class is a controller for the InitSetObjectiveCardScene.
 * It handles the interactions between the user interface and the game logic during the initial selection of objective cards.
 */
public class InitSetObjectiveCardSceneController extends InitScene {
    /**
     * The name of the controller.
     */
    private static final ControllersEnum NAME = ControllersEnum.INIT_SET_OBJECTIVE_CARD;
    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(InitSetObjectiveCardSceneController.class);
    /**
     * The first choosable card.
     */
    private final SimpleObjectProperty<ObjectiveCard> firstChoosableCard = new SimpleObjectProperty<>();
    /**
     * The second choosable card.
     */
    private final SimpleObjectProperty<ObjectiveCard> secondChoosableCard = new SimpleObjectProperty<>();
    /**
     * The index of the current card.
     */
    private final int currentIndex = 0;
    /**
     * The first image pane.
     */
    @FXML
    private StackPane firstImagePane;
    /**
     * The second image pane.
     */
    @FXML
    private StackPane secondImagePane;
    /**
     * The image view for the first card.
     */
    @FXML
    private ImageView cardFirstImageView;
    /**
     * The image view for the second card.
     */
    @FXML
    private ImageView cardSecondImageView;
    /**
     * The selected card.
     */
    private String selectedCard = ""; // Variable to track the selected card

    /**
     * This method is called when the scene is initialized.
     * It sets up the click handlers for the image panes and the listeners for the choosable cards.
     */
    @FXML
    public void initialize() {
        // Call the default initialization method
        super.initialize();

        firstImagePane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && !selectedCard.equals("first")) {
                handleFirstImageClick();
            }
        });

        secondImagePane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && !selectedCard.equals("second")) {
                handleSecondImageClick();
            }
        });

        firstChoosableCard.addListener((observable, oldValue, newValue) ->
                updateGameCardImage(newValue, cardFirstImageView)
        );

        secondChoosableCard.addListener((observable, oldValue, newValue) ->
                updateGameCardImage(newValue, cardSecondImageView)
        );
    }

    /**
     * This method is called when the first image pane is clicked.
     * It updates the selected card and the style of the image panes.
     */
    private void handleFirstImageClick() {
        selectedCard = "first"; // Update the selected card
        secondImagePane.getStyleClass().remove("selected-card-image");
        firstImagePane.getStyleClass().add("selected-card-image");
    }

    /**
     * This method is called when the second image pane is clicked.
     * It updates the selected card and the style of the image panes.
     */
    private void handleSecondImageClick() {
        selectedCard = "second"; // Update the selected card
        firstImagePane.getStyleClass().remove("selected-card-image");
        secondImagePane.getStyleClass().add("selected-card-image");
    }

    /**
     * This method updates the image of the game card.
     *
     * @param objectiveCard the objective card to display.
     * @param imageView     the image view to update.
     */
    private void updateGameCardImage(ObjectiveCard objectiveCard, ImageView imageView) {
        Image objectiveCardImage = GuiCardFactory.createImage(objectiveCard);
        imageView.setImage(objectiveCardImage);
    }

    /**
     * This method is called when the continue button is clicked.
     * It prints the selected card ID to the console and sends the selected card ID to the network controller.
     */
    @FXML
    protected void continueAction() {
        // Print the selected card ID to the console
        int selectedCardId = selectedCard.equals("first") ? firstChoosableCard.get().getCardId() : secondChoosableCard.get().getCardId();
        logger.debug("Selected Card ID: {}", selectedCardId);

        networkControllerMapper.setPlayerObjective(selectedCardId);
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
        // If this scene is being mounted it means that the player has to choose an objective card
        // The card is located in the gameControllerView passed in the event.

        // Load the objective cards
        String playerName = getProperty("playerName");
        GameControllerView gameControllerView = (GameControllerView) evt.getNewValue();
        ArrayList<ObjectiveCard> choosableObjectives = gameControllerView.getPlayerViewByName(playerName).choosableObjectives();
        logger.debug("Loading card ids: {},  {}", choosableObjectives.getFirst().getCardId(), choosableObjectives.get(1).getCardId());
        firstChoosableCard.set(choosableObjectives.getFirst());
        secondChoosableCard.set(choosableObjectives.get(1));

        // Set the default selected card
        handleFirstImageClick();
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
        GameControllerView updatedView = (GameControllerView) evt.getNewValue();
        if (propertyName.equals("UPDATE_VIEW") && !updatedView.isMyTurn(getProperty("playerName"))) {
            //
            Platform.runLater(() ->
                    switchScene(ControllersEnum.GAME_SCENE, evt)
            );
        }
    }
}