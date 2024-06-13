package it.polimi.ingsw.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class ErrorDialog extends Alert {
    public ErrorDialog(Stage stage, AlertType alertType, String title, String message) {
        super(alertType);
        initOwner(stage);
        initStyle(StageStyle.UNDECORATED);
        setHeaderText(title);
        setContentText(message);
        getButtonTypes().clear();
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.LEFT);// Imposta il bottone come bottone di annullamento
        getButtonTypes().add(okButtonType);
        getDialogPane().getStylesheets().add(getClass().getResource("/alertStyles.css").toExternalForm());
        ((Button) getDialogPane().lookupButton(okButtonType)).setOnAction(event -> close());
    }

    public void addButton(String buttonText, EventHandler<ActionEvent> eventHandler) {
        ButtonType buttonType = new ButtonType(buttonText, ButtonBar.ButtonData.LEFT);
        getButtonTypes().add(buttonType);
        ((Button) getDialogPane().lookupButton(buttonType)).setOnAction(eventHandler);
    }

    public void closeAlert() {
        getButtonTypes().add(ButtonType.CLOSE);
        close();
    }
}
