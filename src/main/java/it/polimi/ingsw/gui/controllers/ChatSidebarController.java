package it.polimi.ingsw.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatSidebarController {

    @FXML
    private TextArea chatDisplay;
    @FXML
    private TextField messageInput;
    @FXML
    private ComboBox<String> messageType;
    @FXML
    private ComboBox<String> playerList;

    @FXML
    private void initialize() {
        messageType.setOnAction(e -> {
            playerList.setVisible(messageType.getValue().equals("Private"));
        });
    }

    @FXML
    private void handleSend() {
        String message = messageInput.getText();
        if (!message.isEmpty()) {
            String recipient = messageType.getValue().equals("Private") ? playerList.getValue() : "All";
            chatDisplay.appendText(String.format("[%s]: %s\n", recipient, message));
            messageInput.clear();
        }
    }
}