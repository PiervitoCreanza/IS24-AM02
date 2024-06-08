package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.gui.ObjectiveCardImage;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Objects;

// Each player receives multiple objective cards at the beginning of the game, and it has to choose one.
public class InitSetObjectiveCardSceneController extends Controller {
    private static final ControllersEnum NAME = ControllersEnum.INIT_SET_OBJECTIVE_CARD;
    private static final Logger logger = LogManager.getLogger(InitSetObjectiveCardSceneController.class);
    @FXML
    private StackPane firstImagePane;

    @FXML
    private StackPane secondImagePane;

    @FXML
    private ImageView cardFirstImageView;

    @FXML
    private ImageView cardSecondImageView;

    private ObjectiveCardImage[] gameCardImages;
    private final int currentIndex = 0;
    private String selectedCard = ""; // Variable to track the selected card

    @FXML
    public void initialize() {

        firstImagePane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && !selectedCard.equals("first")) {
                System.out.println("Clicked on the first card");
                selectedCard = "first"; // Update the selected card
                // Remove the selection from the other card, if present
                secondImagePane.getStyleClass().remove("selected-card-image");
                // Add the selection to the current card
                firstImagePane.getStyleClass().add("selected-card-image");
            }
        });

        secondImagePane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && !selectedCard.equals("second")) {
                System.out.println("Clicked on the second card");
                selectedCard = "second"; // Update the selected card
                firstImagePane.getStyleClass().remove("selected-card-image");
                secondImagePane.getStyleClass().add("selected-card-image");
            }
        });
    }

    private void loadCardImages(ObjectiveCard firstCard, ObjectiveCard secondCard) {
        gameCardImages = new ObjectiveCardImage[]{
                new ObjectiveCardImage(firstCard), // Replace with actual card IDs
                new ObjectiveCardImage(secondCard),
        };
        cardFirstImageView.setImage(gameCardImages[currentIndex].getImageView().getImage());
        cardSecondImageView.setImage(gameCardImages[(currentIndex + 1) % gameCardImages.length].getImageView().getImage());
    }

    @FXML
    private void continueAction() {
        if (selectedCard == null || selectedCard.isEmpty()) {
            // Show error alert
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Selection Error");
            alert.setHeaderText("No Card Selected");
            alert.setContentText("Please select a card before continuing.");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/alertStyles.css").toExternalForm());
            alert.showAndWait();
        } else {
            // Print the selected card ID to the console
            int selectedCardId = selectedCard.equals("first") ? gameCardImages[currentIndex].getCardId() : gameCardImages[(currentIndex + 1) % gameCardImages.length].getCardId();
            System.out.println("Selected Card ID: " + selectedCardId);

            networkControllerMapper.setPlayerObjective(selectedCardId);

            // Reset the styles after continuing
            firstImagePane.getStyleClass().remove("selected-card-image");
            secondImagePane.getStyleClass().remove("selected-card-image");
        }
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
        loadCardImages(choosableObjectives.getFirst(), choosableObjectives.get(1));
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (propertyName.equals("UPDATE_VIEW")) {
            GameControllerView gameControllerView = (GameControllerView) evt.getNewValue();
            GameStatusEnum gameStatus = gameControllerView.gameStatus();
            boolean isMyTurn = Objects.equals(gameControllerView.getCurrentPlayerView().playerName(), getProperty("playerName"));
            if (gameStatus == GameStatusEnum.PLACE_CARD && isMyTurn) {
                Platform.runLater(() -> {
                    switchScene(ControllersEnum.GAME_SCENE, evt);
                });
            }
        }
    }
}
