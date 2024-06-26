package it.polimi.ingsw.view.gui.components.toast;

import it.polimi.ingsw.view.gui.utils.GUIUtils;
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

    /**
     * Logger for the Toast class, used to log debug and error messages.
     */
    private static final Logger logger = LogManager.getLogger(Toast.class);

    /**
     * VBox object representing the toast notification.
     */
    private final VBox toastBox;

    /**
     * Constructs a new Toast.
     *
     * @param toastlevel       the level of the toast. It determines the style of the toast.
     * @param toastTitle       the title of the toast.
     * @param toastDescription the description of the toast.
     */
    public Toast(ToastLevels toastlevel, String toastTitle, String toastDescription) {
        super();

        // Load the toast FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Toast.fxml"));

        try {
            toastBox = loader.load();
        } catch (IOException e) {
            logger.fatal("Error loading toast FXML");
            throw new RuntimeException(e);
        }

        // Set the toast color. Supported colors are "green" and "red".
        toastBox.getStyleClass().add(toastlevel.getCssClassName());

        // Set the title and message of the toast
        Label title = (Label) toastBox.lookup("#toastTitle");
        Label messageLabel = (Label) toastBox.lookup("#toastMessage");
        title.setText(GUIUtils.truncateString(toastTitle, 35));
        messageLabel.setText(GUIUtils.truncateString(toastDescription, 70));
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