package it.polimi.ingsw.network.client.message;

import java.time.Instant;

/**
 * This class represents a chat message that a client sends to the server.
 * It extends the ClientToServerMessage class and adds additional fields related to the chat functionality.
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
    private final String receiver;

    /**
     * Constructor for ChatClientToServerMessage.
     * Initializes the chat message with the specified values.
     *
     * @param gameName The name of the game.
     * @param sender   The sender of the message.
     * @param message  The content of the message.
     * @param receiver The receiver of the message. If this is null, the message is not a direct message.
     */
    public ChatClientToServerMessage(String gameName, String sender, String message, String receiver) {
        super(PlayerActionEnum.SEND_CHAT_MSG, gameName, sender);
        this.message = message;
        this.timestamp = Instant.now().getEpochSecond(); // Set the timestamp when the message is created
        this.receiver = receiver;
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
    public String getReceiver() {
        return receiver;
    }

}