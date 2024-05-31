package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.gui.GameCardImage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

// Each player receives a single starterCard at the beginning of the game, and it has to choose which side to use.
public class InitPlaceStarterCardSceneController {

    @FXML
    private StackPane frontImagePane;

    @FXML
    private StackPane backImagePane;

    @FXML
    private ImageView cardFrontImageView;

    @FXML
    private ImageView cardBackImageView;

    private GameCardImage[] gameCardImages;
    private final int currentIndex = 0;
    private String selectedSide = ""; // Variable to track the selected side

    @FXML
    public void initialize() {
        loadCardImages();
        updateCardImages();

        frontImagePane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                System.out.println("Clicked on the front of the card");
                selectedSide = "front"; // Update the selected side
                // Remove the selection from the other card, if present
                backImagePane.getStyleClass().remove("selected-card-image");
                // Add the selection to the current card
                frontImagePane.getStyleClass().add("selected-card-image");
            }
        });

        backImagePane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                System.out.println("Clicked on the back of the card");
                selectedSide = "back"; // Update the selected side
                frontImagePane.getStyleClass().remove("selected-card-image");
                backImagePane.getStyleClass().add("selected-card-image");
            }
        });
    }

    private void loadCardImages() {
        gameCardImages = new GameCardImage[]{
                new GameCardImage(82), // Replace with actual card IDs
        };
    }

    private void updateCardImages() {
        cardFrontImageView.setImage(gameCardImages[currentIndex].getFront().getImage());
        cardBackImageView.setImage(gameCardImages[currentIndex].getBack().getImage());
    }

    @FXML
    private void continueAction() {
        if (selectedSide == null || selectedSide.isEmpty()) {
            // Show error alert
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Selection Error");
            alert.setHeaderText("No Card Side Selected");
            alert.setContentText("Please select a side of the card before continuing.");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/alertStyles.css").toExternalForm());
            alert.showAndWait();
        } else {
            // Print the selected card ID and the selected side to the console
            System.out.println("Selected Card ID: " + gameCardImages[currentIndex].getCardId() + ", Side: " + selectedSide);
            // Reset the styles after continuing
            backImagePane.getStyleClass().remove("selected-card-image");
            frontImagePane.getStyleClass().remove("selected-card-image");
            //Not resetting the selected ID & side, as it is needed for the next scene, it may seem like a bug in the single scene testing but it is not
        }
    }
}
