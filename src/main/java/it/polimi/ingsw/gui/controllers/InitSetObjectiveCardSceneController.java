package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.gui.GameCardImage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

// Each player receives multiple objective cards at the beginning of the game, and it has to choose one.
public class InitSetObjectiveCardSceneController {

    @FXML
    private StackPane firstImagePane;

    @FXML
    private StackPane secondImagePane;

    @FXML
    private ImageView cardFirstImageView;

    @FXML
    private ImageView cardSecondImageView;

    private GameCardImage[] gameCardImages;
    private final int currentIndex = 0;
    private String selectedCard = ""; // Variable to track the selected card

    @FXML
    public void initialize() {
        loadCardImages();
        updateCardImages();

        firstImagePane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                System.out.println("Clicked on the first card");
                selectedCard = "first"; // Update the selected card
                // Remove the selection from the other card, if present
                secondImagePane.getStyleClass().remove("selected-card-image");
                // Add the selection to the current card
                firstImagePane.getStyleClass().add("selected-card-image");
            }
        });

        secondImagePane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                System.out.println("Clicked on the second card");
                selectedCard = "second"; // Update the selected card
                firstImagePane.getStyleClass().remove("selected-card-image");
                secondImagePane.getStyleClass().add("selected-card-image");
            }
        });
    }

    private void loadCardImages() {
        gameCardImages = new GameCardImage[]{
                new GameCardImage(100), // Replace with actual card IDs
                new GameCardImage(101),
        };
    }

    private void updateCardImages() {
        cardFirstImageView.setImage(gameCardImages[currentIndex].getFront().getImage());
        cardSecondImageView.setImage(gameCardImages[(currentIndex + 1) % gameCardImages.length].getFront().getImage());
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
            // Reset the styles after continuing
            firstImagePane.getStyleClass().remove("selected-card-image");
            secondImagePane.getStyleClass().remove("selected-card-image");
        }
    }
}
