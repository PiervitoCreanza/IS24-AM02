package it.polimi.ingsw.network.client.message;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static it.polimi.ingsw.tui.utils.Utils.ANSI_BLUE;
import static it.polimi.ingsw.tui.utils.Utils.ANSI_RESET;

/**
 * This class represents a chat message that a client sends to the server.
 * It extends the ClientToServerMessage class and adds additional fields related to the chat functionality.
 */
public class chatMessageClientToServerMessage extends ClientToServerMessage {
    private final String message;  // The content of the chat message
    private final long timestamp;  // Timestamp in Unix seconds when the message was created
    private boolean isDirectMessage;  // Flag to indicate if the message is a direct message
    private final String receiver;  // The receiver of the message if it's a direct message

    /**
     * Constructor for chatMessageClientToServerMessage.
     * Initializes the chat message with the specified values.
     *
     * @param gameName The name of the game.
     * @param sender   The sender of the message.
     * @param message  The content of the message.
     * @param receiver The receiver of the message. If this is null, the message is not a direct message.
     */
    public chatMessageClientToServerMessage(String gameName, String sender, String message, String receiver) {
        super(PlayerActionEnum.CHAT_MSG, gameName, sender);
        this.message = message;
        this.timestamp = Instant.now().getEpochSecond(); // Set the timestamp when the message is created
        this.isDirectMessage = !receiver.isEmpty();
        this.receiver = receiver;
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
     * Returns whether the chat message is a direct message.
     *
     * @return True if the chat message is a direct message, false otherwise.
     */
    public boolean isDirectMessage() {
        return isDirectMessage;
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


    /**
     * Overrides the toString method for the chatMessageClientToServerMessage class.
     * If the message is a direct message, it will be printed in blue with the sender, receiver, message content, and timestamp.
     * If the message is not a direct message, it will be printed with the sender, message content, and timestamp.
     *
     * @return A string representation of the chat message.
     */
    public String prettyPrint() {
        // If it's a direct message, print it in blue with the appropriate fields
        if (isDirectMessage)
            return ANSI_BLUE + this.playerName + "(to " + receiver + "): " + message + " (" + getFormattedTimestamp() + ")" + ANSI_RESET;
        // If it's not a direct message, print it as a normal message
        return this.playerName + ": " + message + " (" + getFormattedTimestamp() + ")";
    }

    /**
     * This method converts the Unix timestamp of the chat message into a formatted string.
     * The format of the string is "HH:mm:ss".
     *
     * @return A string representation of the timestamp in "HH:mm:ss" format.
     */
    public String getFormattedTimestamp() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return dateTime.format(formatter);
    }

}