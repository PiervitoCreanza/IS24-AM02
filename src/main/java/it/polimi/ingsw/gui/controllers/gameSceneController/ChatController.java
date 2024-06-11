package it.polimi.ingsw.gui.controllers.gameSceneController;

import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.client.message.ChatClientToServerMessage;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.util.List;
import java.util.Objects;

public class ChatController {
    private final TextArea chatDisplay;
    private final TextArea messageInput;
    private final ComboBox<String> recepient;
    private final Node chatDisplayButton;
    private final Button chatSendButton;
    private final ClientNetworkControllerMapper clientNetworkControllerMapper;
    private String clientUserName;
    private final ChatManager chatManager;

    public ChatController(Node root, Node chatDisplayButton, ClientNetworkControllerMapper clientNetworkControllerMapper) {
        this.chatDisplay = (TextArea) root.lookup("#chatDisplay");
        this.messageInput = (TextArea) root.lookup("#messageInput");
        this.recepient = (ComboBox<String>) root.lookup("#recepient");
        this.chatDisplayButton = chatDisplayButton;
        this.chatSendButton = (Button) root.lookup("#chatSendButton");
        this.clientNetworkControllerMapper = clientNetworkControllerMapper;
        this.chatManager = new ChatManager();

        // Listeners
        chatSendButton.setOnAction(event -> handleSend());
        messageInput.onKeyPressedProperty().set(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                handleSend();
            }
        });

        recepient.addEventHandler(javafx.event.ActionEvent.ANY, event -> {
            if (recepient.getValue() == null) {
                return;
            }
            updateMessages();
        });

        chatDisplay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(() -> {
                    chatDisplay.setScrollTop(Double.MAX_VALUE);
                });
            }
        });
    }

    private void updateMessages() {
        String recipient = recepient.getValue().substring(3);
        if (recipient.equals("Everyone")) {
            recipient = "global";
        }
        chatDisplay.setText(chatManager.getMessages(recipient));
    }

    public void updateUsers(List<String> users, String clientUserName) {
        this.clientUserName = clientUserName;
        recepient.getItems().clear();
        recepient.getItems().add("To Everyone");
        for (String user : users) {
            recepient.getItems().add("To " + user);
        }
        if (recepient.getValue() == null) {
            recepient.setValue("To Everyone");
        }


    }

    private void handleSend() {
        String message = messageInput.getText();
        if (!message.isEmpty()) {
            String recipient = recepient.getValue().substring(3);
            if (recipient.equals("Everyone")) {
                recipient = "global";
            }
            ChatClientToServerMessage chatClientToServerMessage = new ChatClientToServerMessage(null, null, message, recipient, !recipient.equals("global"));
            clientNetworkControllerMapper.sendChatMessage(chatClientToServerMessage);
            chatManager.addMessage(chatClientToServerMessage);
            updateMessages();
            messageInput.clear();
        }
    }

    public void handleChatMessage(ServerToClientMessage message) {
        if (!Objects.equals(message.getPlayerName(), clientUserName)) {
            chatManager.addMessage(message);
            updateMessages();
            if (!message.isDirectMessage()) {
                recepient.setValue("To Everyone");
                return;
            }
            recepient.setValue("To " + message.getPlayerName());

        }
    }
}
