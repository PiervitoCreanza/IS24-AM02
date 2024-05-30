package it.polimi.ingsw.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class SelectPionController {

    @FXML
    private ImageView pionImageView;
    @FXML
    private ToggleGroup colorToggleGroup;

    private Image bluePion;
    private Image yellowPion;
    private Image redPion;
    private Image greenPion;

    @FXML
    public void initialize() {
        // Load images
        bluePion = new Image(Objects.requireNonNull(getClass().getResource("/fonts/CODEX_pion_bleu.png")).toExternalForm());
        yellowPion = new Image(Objects.requireNonNull(getClass().getResource("/fonts/CODEX_pion_jaune.png")).toExternalForm());
        redPion = new Image(Objects.requireNonNull(getClass().getResource("/fonts/CODEX_pion_rouge.png")).toExternalForm());
        greenPion = new Image(Objects.requireNonNull(getClass().getResource("/fonts/CODEX_pion_vert.png")).toExternalForm());

        // Set default image
        pionImageView.setImage(bluePion);

        // Set default selection
        colorToggleGroup.selectToggle(colorToggleGroup.getToggles().get(0));
    }

    public Image getBluePion() {
        return bluePion;
    }

    public Image getYellowPion() {
        return yellowPion;
    }

    public Image getRedPion() {
        return redPion;
    }

    public Image getGreenPion() {
        return greenPion;
    }

    public ImageView getPionImageView() {
        return pionImageView;
    }

    @FXML
    private void handleColorSelection() {
        RadioButton selectedRadioButton = (RadioButton) colorToggleGroup.getSelectedToggle();
        String selectedColor = selectedRadioButton.getText();

        switch (selectedColor) {
            case "Blue":
                pionImageView.setImage(bluePion);
                break;
            case "Yellow":
                pionImageView.setImage(yellowPion);
                break;
            case "Red":
                pionImageView.setImage(redPion);
                break;
            case "Green":
                pionImageView.setImage(greenPion);
                break;
        }
    }

    @FXML
    private void back() {
        // Handle back action
    }

    @FXML
    private void next() {
        // Handle next action
    }
}