package it.polimi.ingsw.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SelectPionSceneController {

    @FXML
    private ImageView pionImageView;

    private final String[] pionImages = {
            "/CODEX_pion_bleu.png",
            "/CODEX_pion_jaune.png",
            "/CODEX_pion_rouge.png",
            "/CODEX_pion_vert.png"
    };

    private final String[] pionColors = {
            "Blue",
            "Yellow",
            "Red",
            "Green"
    };

    private int currentIndex = 0;

    @FXML
    public void initialize() {
        updatePionImage();
    }

    @FXML
    private void nextPion() {
        currentIndex = (currentIndex + 1) % pionImages.length;
        updatePionImage();
    }

    @FXML
    private void previousPion() {
        currentIndex = (currentIndex - 1 + pionImages.length) % pionImages.length;
        updatePionImage();
    }

    private void updatePionImage() {
        Image image = new Image(getClass().getResourceAsStream(pionImages[currentIndex]));
        pionImageView.setImage(image);
    }

    @FXML
    private void continueAction() {
        // Print the selected pion color in the console
        System.out.println("Selected Pion Color: " + pionColors[currentIndex]);
    }
}