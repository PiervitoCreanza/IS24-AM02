package it.polimi.ingsw.gui.controllers.gameSceneController;

import it.polimi.ingsw.gui.utils.GUIUtils;
import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.client.message.ChatClientToServerMessage;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.util.List;
import java.util.Objects;

/**
 * This class is a controller for the chat functionality in the game scene.
 * It handles the interactions between the user interface and the chat logic.
 */
public class ChatController {

    /**
     * The chat display.
     */
    private final TextArea chatDisplay;

    /**
     * The message input.
     */
    private final TextArea messageInput;

    /**
     * The recipient selection.
     */
    private final ComboBox<String> recipient;

    /**
     * The button to display the chat.
     */
    private final Node chatDisplayButton;

    /**
     * The send button.
     */
    private final Button chatSendButton;

    /**
     * The client network controller mapper.
     */
    private final ClientNetworkControllerMapper clientNetworkControllerMapper;

    /**
     * The username of the client.
     */
    private String clientUserName;

    /**
     * The chat manager.
     */
    private final ChatManager chatManager;

    /**
     * Constructs a new ChatController.
     * Initializes the chat display, message input, recipient selection, chat display button, chat send button,
     * network controller mapper, and chat manager.
     *
     * @param root the root node of the chat interface
     * @param chatDisplayButton the button to display the chat
     * @param clientNetworkControllerMapper the network controller mapper for the client
     */
    public ChatController(Node root, Node chatDisplayButton, ClientNetworkControllerMapper clientNetworkControllerMapper) {
        this.chatDisplay = (TextArea) root.lookup("#chatDisplay");
        this.messageInput = (TextArea) root.lookup("#messageInput");
        this.recipient = (ComboBox<String>) root.lookup("#recepient");
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

        recipient.addEventHandler(javafx.event.ActionEvent.ANY, event -> {
            if (recipient.getValue() == null) {
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

        // Render recipients list as a cellFactory in order to truncate the displayed player name
        recipient.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(GUIUtils.truncateString(item));
                        }
                    }
                };
            }
        });
    }

    /**
     * Updates the chat display with the messages from the selected recipient.
     */
    private void updateMessages() {
        String recipient = this.recipient.getValue().substring(3);
        if (recipient.equals("Everyone")) {
            recipient = "global";
        }
        chatDisplay.setText(chatManager.getMessages(recipient));
    }

    /**
     * Updates the list of users in the recipient selection.
     *
     * @param users the list of users
     * @param clientUserName the username of the client
     */
    public void updateUsers(List<String> users, String clientUserName) {
        this.clientUserName = clientUserName;
        recipient.getItems().clear();
        recipient.getItems().add("To Everyone");
        for (String user : users) {
            recipient.getItems().add("To " + user);
        }
        if (recipient.getValue() == null) {
            recipient.setValue("To Everyone");
        }
    }

    /**
     * Handles the send action.
     * Sends the message input by the user to the selected recipient.
     */
    private void handleSend() {
        String message = messageInput.getText();
        if (!message.isEmpty()) {
            String recipient = this.recipient.getValue().substring(3);
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

    /**
     * Handles a chat message received from the server.
     *
     * @param message the chat message received from the server
     */
    public void handleChatMessage(ServerToClientMessage message) {
        if (!Objects.equals(message.getPlayerName(), clientUserName)) {
            chatManager.addMessage(message);
            updateMessages();
            if (!message.isDirectMessage()) {
                recipient.setValue("To Everyone");
                return;
            }
            recipient.setValue("To " + message.getPlayerName());

        }
    }
}