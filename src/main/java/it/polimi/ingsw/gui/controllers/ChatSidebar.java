package it.polimi.ingsw.gui.controllers;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ChatSidebar extends VBox {

    private TextArea chatDisplay;
    private TextField messageInput;
    private ComboBox<String> messageType;
    private ComboBox<String> playerList;
    private Button sendButton;

    public ChatSidebar() {
        initialize();
    }

    private void initialize() {
        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.setStyle("-fx-background-color: #DDDDDD;");
        this.setPrefWidth(300);

        // Chat display area
        chatDisplay = new TextArea();
        chatDisplay.setEditable(false);
        chatDisplay.setPrefHeight(400);

        // Message input field
        messageInput = new TextField();
        messageInput.setPromptText("Type your message...");

        // Public/Private message selection
        messageType = new ComboBox<>();
        messageType.getItems().addAll("Public", "Private");
        messageType.setValue("Public");

        // Private message recipient selection
        playerList = new ComboBox<>();
        playerList.getItems().addAll("Player 1", "Player 2", "Player 3");
        playerList.setVisible(false);

        messageType.setOnAction(e -> {
            playerList.setVisible(messageType.getValue().equals("Private"));
        });

        // Send button
        sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            String message = messageInput.getText();
            if (!message.isEmpty()) {
                String recipient = messageType.getValue().equals("Private") ? playerList.getValue() : "All";
                chatDisplay.appendText(String.format("[%s]: %s\n", recipient, message));
                messageInput.clear();
            }
        });

        this.getChildren().addAll(chatDisplay, messageType, playerList, messageInput, sendButton);
    }

    // Getter methods for accessing components if needed
    public TextArea getChatDisplay() {
        return chatDisplay;
    }

    public TextField getMessageInput() {
        return messageInput;
    }

    public ComboBox<String> getMessageType() {
        return messageType;
    }

    public ComboBox<String> getPlayerList() {
        return playerList;
    }

    public Button getSendButton() {
        return sendButton;
    }
}