package it.polimi.ingsw.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class InfoBox extends VBox {

    private final VBox box;

    public InfoBox(String color, String toastTitle, String toastDescription) {
        super();
        // Load the toast FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Toast.fxml"));

        try {
            box = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        // Set the toast color. Supported colors are "green" and "red".
        box.getStyleClass().add(color);

        // Set the title and message of the toast
        Label title = (Label) box.lookup("#toastTitle");
        Label messageLabel = (Label) box.lookup("#toastMessage");
        title.setText(toastTitle);
        messageLabel.setText(toastDescription);
    }

    public VBox getToastNode() {
        return box;
    }
}
