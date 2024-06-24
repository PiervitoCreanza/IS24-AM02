package it.polimi.ingsw.gui.components;

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


public class ErrorDialog extends Alert {

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

    public void addButton(String buttonText, EventHandler<ActionEvent> eventHandler) {
        ButtonType buttonType = new ButtonType(buttonText, ButtonBar.ButtonData.LEFT);
        getButtonTypes().add(buttonType);
        ((Button) getDialogPane().lookupButton(buttonType)).setOnAction(eventHandler);
    }

    public void closeAlert() {
        if (!getButtonTypes().contains(ButtonType.CLOSE))
            getButtonTypes().add(ButtonType.CLOSE);
        close();
    }
}
