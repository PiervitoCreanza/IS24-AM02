package it.polimi.ingsw.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.util.ArrayList;

public class ChatSidebarController {

    @FXML
    private TextArea chatDisplay;
    @FXML
    private TextArea messageInput;
    @FXML
    private ComboBox<String> recepient;

    @FXML
    private void initialize() {
        recepient.setValue("To Everyone");
        messageInput.onKeyPressedProperty().set(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                handleSend();
            }
        });
    }

    public void setUsers(ArrayList<String> users) {
        recepient.getItems().clear();
        recepient.getItems().add("To Everyone");
        for (String user : users) {
            recepient.getItems().add("To " + user);
        }
    }

    @FXML
    private void handleSend() {
        String message = messageInput.getText();
        if (!message.isEmpty()) {
            String recipient = recepient.getValue().substring(3);
            chatDisplay.appendText(String.format("[%s]: %s\n", recipient, message));
            messageInput.clear();
        }
    }
}