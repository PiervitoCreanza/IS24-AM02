package it.polimi.ingsw.network.server.message;

import static it.polimi.ingsw.network.server.message.ServerActionEnum.RECEIVE_CHAT_MSG;

/**
 * This class represents a chat message that a server sends to a client.
 * It extends the ServerToClientMessage class and adds additional fields related to the chat functionality.
 */
public class ChatServerToClientMessage extends ServerToClientMessage {
    /**
     * The content of the chat message.
     */
    private final String message;

    /**
     * Timestamp in Unix seconds when the message was created.
     */
    private final long timestamp;

    /**
     * The name of the player who sent the message, introduced here because by default standard server messages don't have a player name.
     */
    private final String sender;
    /**
     * Flag to indicate if the message is a direct message.
     */
    private final boolean isDirectMessage;

    /**
     * Constructor for ChatServerToClientMessage.
     * Initializes the chat message with the specified values.
     *
     * @param sender          The sender of the message.
     * @param message         The content of the message.
     * @param timestamp       The timestamp when the message was created.
     * @param isDirectMessage Flag to indicate if the message is a direct message.
     */
    public ChatServerToClientMessage(String sender, String message, long timestamp, boolean isDirectMessage) {
        super(RECEIVE_CHAT_MSG);
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp; // Set the timestamp when the message is created
        this.isDirectMessage = isDirectMessage;
    }

    /**
     * Returns the content of the chat message.
     *
     * @return The content of the chat message.
     */
    @Override
    public String getChatMessage() {
        return message;
    }

    /**
     * Returns the timestamp when the chat message was created.
     *
     * @return The timestamp when the chat message was created.
     */
    @Override
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Returns whether the chat message is a direct message.
     *
     * @return True if the chat message is a direct message, false otherwise.
     */
    @Override
    public boolean isDirectMessage() {
        return isDirectMessage;
    }

    /**
     * Returns the name of the player who sent the message.
     *
     * @return The name of the player who sent the message.
     */
    @Override
    public String getPlayerName() {
        return sender;
    }
}