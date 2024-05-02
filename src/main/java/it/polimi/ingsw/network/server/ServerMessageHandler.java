package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.server.message.ServerMessage;

/**
 * The ServerMessageHandler interface defines the methods that a server message handler must implement.
 * A server message handler is used to make RMI and TCP connections interchangeable.
 */
public interface ServerMessageHandler {
    /**
     * Sends a message to the client.
     *
     * @param message the message to be sent
     */
    void sendMessage(ServerMessage message);

    /**
     * Closes the connection to the client.
     */
    void closeConnection();
}