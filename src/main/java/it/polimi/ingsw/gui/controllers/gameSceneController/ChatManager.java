package it.polimi.ingsw.gui.controllers.gameSceneController;

import it.polimi.ingsw.network.client.message.ChatClientToServerMessage;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;

import java.util.HashMap;

/**
 * This class manages the chat functionality in the game scene.
 * It maintains a mapping between chat senders and their corresponding chat messages.
 */
public class ChatManager {
    /**
     * A map to store chat messages with their senders as keys.
     */
    private final HashMap<String, StringBuilder> chatMapper;

    /**
     * Constructs a new ChatManager.
     * Initializes the chatMapper as a new HashMap.
     */
    public ChatManager() {
        this.chatMapper = new HashMap<>();
    }

    /**
     * Adds a message received from the server to the chat.
     * If the sender is not already in the chatMapper, it adds the sender with a new StringBuilder.
     * Then it appends the message to the StringBuilder associated with the sender.
     *
     * @param message the message received from the server
     */
    public void addMessage(ServerToClientMessage message) {
        String sender = message.getPlayerName();
        if (!message.isDirectMessage()) {
            sender = "global";
        }

        if (!chatMapper.containsKey(sender)) {
            chatMapper.put(sender, new StringBuilder());
        }
        chatMapper.get(sender).append(String.format("[%s]: ", message.getPlayerName())).append(message.getChatMessage()).append("\n");

    }

    /**
     * Adds a message sent by the client to the chat.
     * If the recipient is not already in the chatMapper, it adds the recipient with a new StringBuilder.
     * Then it appends the message to the StringBuilder associated with the recipient.
     *
     * @param message the message sent by the client
     */
    public void addMessage(ChatClientToServerMessage message) {
        String recipient = message.getRecipient();
        if (!chatMapper.containsKey(recipient)) {
            chatMapper.put(recipient, new StringBuilder());
        }
        chatMapper.get(recipient).append("[You]: ").append(message.getMessage()).append("\n");
    }

    /**
     * Retrieves all messages sent by a specific sender.
     * If the sender is not already in the chatMapper, it adds the sender with a new StringBuilder.
     * Then it returns the string representation of the StringBuilder associated with the sender.
     *
     * @param sender the sender whose messages are to be retrieved
     * @return a string containing all messages sent by the sender
     */
    public String getMessages(String sender) {
        if (!chatMapper.containsKey(sender)) {
            chatMapper.put(sender, new StringBuilder());
        }
        return chatMapper.get(sender).toString();
    }
}