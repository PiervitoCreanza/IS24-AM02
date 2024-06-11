package it.polimi.ingsw.gui.controllers.gameSceneController;

import it.polimi.ingsw.network.client.message.ChatClientToServerMessage;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;

import java.util.HashMap;

public class ChatManager {
    private final HashMap<String, StringBuilder> chatMapper;

    public ChatManager() {
        this.chatMapper = new HashMap<>();
    }

    public void addMessage(ServerToClientMessage message) {
        String sender = message.getPlayerName();
        if (!message.isDirectMessage()) {
            sender = "global";
        }

        if (!chatMapper.containsKey(sender)) {
            chatMapper.put(sender, new StringBuilder());
        }
        if (message.isDirectMessage()) {
            chatMapper.get(sender).append(message.getChatMessage()).append("\n");
        } else {
            chatMapper.get(sender).append(String.format("[%s]: ", message.getPlayerName())).append(message.getChatMessage()).append("\n");
        }
    }

    public void addMessage(ChatClientToServerMessage message) {
        String recipient = message.getRecipient();
        if (!chatMapper.containsKey(recipient)) {
            chatMapper.put(recipient, new StringBuilder());
        }
        chatMapper.get(recipient).append("[You]: ").append(message.getMessage()).append("\n");
    }

    public String getMessages(String sender) {
        if (!chatMapper.containsKey(sender)) {
            chatMapper.put(sender, new StringBuilder());
        }
        return chatMapper.get(sender).toString();
    }
}
