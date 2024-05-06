package it.polimi.ingsw.network;

/**
 * The MessageHandler interface defines the methods that a server message handler must implement.
 * A server message handler is used to make RMI and TCP connections interchangeable.
 */
public interface MessageHandler<T> {
    /**
     * Sends a message to the client.
     *
     * @param message the message to be sent
     */
    void sendMessage(T message);

    /**
     * Closes the connection to the client.
     */
    void closeConnection();
}