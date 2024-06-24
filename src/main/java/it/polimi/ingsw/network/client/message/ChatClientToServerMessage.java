package it.polimi.ingsw.network.client.message;

import java.time.Instant;

/**
 * This class represents a chat message that a client sends to the server.
 * It extends the ClientToServerMessage class and adds additional fields related to the chat functionality.
 * The message can be a direct message to a specific player or a message to everyone.
 * A message to everyone has the receiver field set to "".
 */
public class ChatClientToServerMessage extends ClientToServerMessage {
    /**
     * The content of the chat message.
     * This is a final variable, meaning it cannot be changed once it has been set.
     */
    private final String message;

    /**
     * Timestamp in Unix seconds when the message was created.
     * This is a final variable, meaning it cannot be changed once it has been set.
     */
    private final long timestamp;
    /**
     * The receiver of the message if it's a direct message.
     * This is a final variable, meaning it cannot be changed once it has been set.
     */
    private final String recipient;

    /**
     * True if the message is a direct message, false otherwise.
     * This is a final variable, meaning it cannot be changed once it has been set.
     */
    private boolean isDirectMessage = false;

    /**
     * Constructor for ChatClientToServerMessage.
     * Initializes the chat message with the specified values.
     *
     * @param gameName        The name of the game.
     * @param sender          The sender of the message.
     * @param message         The content of the message.
     * @param recipient       The receiver of the message.
     * @param isDirectMessage True if the message is a direct message, false otherwise.
     */
    public ChatClientToServerMessage(String gameName, String sender, String message, String recipient, boolean isDirectMessage) {
        super(PlayerActionEnum.SEND_CHAT_MSG, gameName, sender);
        this.message = message;
        this.timestamp = Instant.now().getEpochSecond(); // Set the timestamp when the message is created
        this.recipient = recipient;
        this.isDirectMessage = isDirectMessage;
    }

    /**
     * Set the game name.
     *
     * @param gameName The name of the game.
     */
    public void setGameName(String gameName) {
        super.gameName = gameName;
    }

    /**
     * Set the sender of the chat message.
     *
     * @param sender The sender of the chat message.
     */
    public void setSender(String sender) {
        super.playerName = sender;
    }

    /**
     * Returns the content of the chat message.
     *
     * @return The content of the chat message.
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Returns the timestamp when the chat message was created.
     *
     * @return The timestamp when the chat message was created.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the receiver of the chat message if it's a direct message.
     *
     * @return The receiver of the chat message if it's a direct message, null otherwise.
     */
    @Override
    public String getRecipient() {
        return recipient;
    }

    /**
     * This method is used to check if the chat message is a direct message.
     *
     * @return boolean - Returns true if the chat message is a direct message, false otherwise.
     */
    public boolean isDirectMessage() {
        return isDirectMessage;
    }
}