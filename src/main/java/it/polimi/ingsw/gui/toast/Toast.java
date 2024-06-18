package it.polimi.ingsw.gui.toast;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * This class represents a Toast notification in the application.
 * It extends VBox, which means it can be added to any container that accepts nodes.
 */
public class Toast extends VBox {

    private static final Logger logger = LogManager.getLogger(Toast.class);
    private final VBox toastBox;

    /**
     * Constructs a new Toast.
     *
     * @param color            the color of the toast. Supported colors are "green", "red", "brown" and "yellow.
     * @param toastTitle       the title of the toast.
     * @param toastDescription the description of the toast.
     */
    public Toast(String color, String toastTitle, String toastDescription) {
        super();

        if (!color.equals("green") && !color.equals("red") && !color.equals("yellow") && !color.equals("brown")) {
            logger.error("Invalid color for toast: " + color);
            throw new IllegalArgumentException("Invalid color for toast: " + color);
        }

        // Load the toast FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Toast.fxml"));

        try {
            toastBox = loader.load();
        } catch (IOException e) {
            logger.fatal("Error loading toast FXML");
            throw new RuntimeException(e);
        }

        // Set the toast color. Supported colors are "green" and "red".
        toastBox.getStyleClass().add(color);

        // Set the title and message of the toast
        Label title = (Label) toastBox.lookup("#toastTitle");
        Label messageLabel = (Label) toastBox.lookup("#toastMessage");
        title.setText(toastTitle);
        messageLabel.setText(toastDescription);
    }

    /**
     * Returns the VBox node of the toast.
     *
     * @return the VBox node of the toast.
     */
    public VBox getToastNode() {
        return toastBox;
    }
}