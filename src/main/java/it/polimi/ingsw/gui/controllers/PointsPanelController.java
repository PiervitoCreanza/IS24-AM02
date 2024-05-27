package it.polimi.ingsw.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class PointsPanelController {

    @FXML
    private ImageView medalIcon1;
    @FXML
    private ImageView medalIcon2;
    @FXML
    private ImageView medalIcon3;

    @FXML
    public void initialize() {
        // Set the images for the icons
        String iconPath = "/assets/images/medalIcon.png";
        Image medalImage = new Image(getClass().getResourceAsStream(iconPath));

        medalIcon1.setImage(medalImage);
        medalIcon2.setImage(medalImage);
        medalIcon3.setImage(medalImage);
    }

    @FXML
    private void handlePlayerClick(MouseEvent event) {
        Label clickedLabel = (Label) event.getSource();
        String playerName = clickedLabel.getText();
        // Logica per reindirizzare alla vista del giocatore
        System.out.println("Clicked on player: " + playerName);
    }
}