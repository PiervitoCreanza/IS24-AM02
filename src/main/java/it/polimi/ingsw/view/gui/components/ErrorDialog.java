package it.polimi.ingsw.view.gui.components;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * This class represents a custom error dialog box.
 * It extends the Alert class from JavaFX.
 */
public class ErrorDialog extends Alert {

    /**
     * Constructor for the ErrorDialog class.
     *
     * @param stage The stage on which the dialog will be shown.
     * @param alertType The type of the alert.
     * @param title The title of the dialog.
     * @param message The message to be shown in the dialog.
     * @param isAutoCloseable If true, the dialog will automatically close after 30 seconds.
     */
    public ErrorDialog(Stage stage, AlertType alertType, String title, String message, boolean isAutoCloseable) {
        super(alertType);
        initOwner(stage);
        initStyle(StageStyle.UNDECORATED);
        setHeaderText(title);
        setContentText(message);
        getButtonTypes().clear();

        // Add close button
        ButtonType closeButtonType = new ButtonType("Close", ButtonBar.ButtonData.LEFT);
        getButtonTypes().add(closeButtonType);
        ((Button) getDialogPane().lookupButton(closeButtonType)).setOnAction(event -> close());
        getDialogPane().getStylesheets().add(getClass().getResource("/alertStyles.css").toExternalForm());

        if (isAutoCloseable) {
            // Set auto-close timer after 30 seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(30));
            pause.setOnFinished(event -> close());
            pause.play();
        }
    }

    /**
     * This method adds a new button to the dialog.
     *
     * @param buttonText The text to be shown on the button.
     * @param eventHandler The event handler that will be triggered when the button is clicked.
     */
    public void addButton(String buttonText, EventHandler<ActionEvent> eventHandler) {
        ButtonType buttonType = new ButtonType(buttonText, ButtonBar.ButtonData.LEFT);
        getButtonTypes().add(buttonType);
        ((Button) getDialogPane().lookupButton(buttonType)).setOnAction(eventHandler);
    }

    /**
     * This method closes the dialog.
     * If the dialog does not already have a close button, it adds one before closing.
     */
    public void closeAlert() {
        if (!getButtonTypes().contains(ButtonType.CLOSE))
            getButtonTypes().add(ButtonType.CLOSE);
        close();
    }
}