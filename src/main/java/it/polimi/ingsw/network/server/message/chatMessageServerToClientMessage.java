package it.polimi.ingsw.network.server.message;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static it.polimi.ingsw.network.server.message.ServerActionEnum.CHAT_MSG;
import static it.polimi.ingsw.tui.utils.Utils.ANSI_BLUE;
import static it.polimi.ingsw.tui.utils.Utils.ANSI_RESET;

/**
 * This class represents a chat message that a server sends to a client.
 * It extends the ServerToClientMessage class and adds additional fields related to the chat functionality.
 */
public class chatMessageServerToClientMessage extends ServerToClientMessage {
    private final String message;  // The content of the chat message
    private final long timestamp;  // Timestamp in Unix seconds when the message was created
    private boolean isDirectMessage;  // Flag to indicate if the message is a direct message
    private final String receiver;  // The receiver of the message if it's a direct message

    private final String sender; // The name of the player who sent the message, introduced here because by default standard server messages don't have a player name

    /**
     * Constructor for chatMessageServerToClientMessage.
     * Initializes the chat message with the specified values.
     *
     * @param sender   The sender of the message.
     * @param message  The content of the message.
     * @param receiver The receiver of the message. If this is null, the message is not a direct message.
     */
    public chatMessageServerToClientMessage(String sender, String message, String receiver) {
        super(CHAT_MSG);
        this.sender = sender;
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
    public String getReceiver() {
        return receiver;
    }

    /**
     * Returns a string representation of the chat message.
     *
     * @return A string representation of the chat message.
     */
    @Override
    public String chatPrint() {
        // If it's a direct message, print it in blue with the appropriate fields
        if (isDirectMessage)
            return ANSI_BLUE + this.sender + "(to " + receiver + "): " + message + " (" + getFormattedTimestamp() + ")" + ANSI_RESET;
        // If it's not a direct message, print it as a normal message
        return this.sender + ": " + message + " (" + getFormattedTimestamp() + ")";
    }


    // Method to return a formatted timestamp (only hour, minutes, seconds)
    public String getFormattedTimestamp() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return dateTime.format(formatter);
    }

}