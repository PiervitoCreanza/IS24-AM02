package it.polimi.ingsw.gui.controllers.gameSceneController;

import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.client.message.ChatClientToServerMessage;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
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

    public ChatController(Node root, Node chatDisplayButton, ClientNetworkControllerMapper clientNetworkControllerMapper) {
        this.chatDisplay = (TextArea) root.lookup("#chatDisplay");
        this.messageInput = (TextArea) root.lookup("#messageInput");
        this.recepient = (ComboBox<String>) root.lookup("#recepient");
        this.chatDisplayButton = chatDisplayButton;
        this.chatSendButton = (Button) root.lookup("#chatSendButton");
        this.clientNetworkControllerMapper = clientNetworkControllerMapper;

        // Listeners
        chatSendButton.setOnAction(event -> handleSend());
        messageInput.onKeyPressedProperty().set(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                handleSend();
            }
        });
    }

    public void updateUsers(List<String> users, String clientUserName) {
        this.clientUserName = clientUserName;
        recepient.getItems().clear();
        recepient.getItems().add("To Everyone");
        for (String user : users) {
            recepient.getItems().add("To " + user);
        }
        recepient.setValue("To Everyone");
    }

    private void handleSend() {
        String message = messageInput.getText();
        // TODO: Fix broadcast message.
        if (!message.isEmpty()) {
            String recipient = recepient.getValue().substring(3);
            chatDisplay.appendText(String.format("[To %s]: %s\n", recipient, message));
            clientNetworkControllerMapper.sendChatMessage(new ChatClientToServerMessage(null, null, message, recipient));
            messageInput.clear();
        }
    }

    public void handleChatMessage(ServerToClientMessage message) {
        if (!Objects.equals(message.getPlayerName(), clientUserName)) {
            chatDisplay.appendText(String.format("[From %s]: %s\n", message.getPlayerName(), message.getChatMessage()));
        }
    }
}
