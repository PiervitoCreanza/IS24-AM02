package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.MessageHandler;
import it.polimi.ingsw.network.server.message.ServerMessage;

/**
 * The ServerMessageHandler interface defines the methods that a server message handler must implement.
 * A server message handler is used to make RMI and TCP connections interchangeable.
 */
public interface ServerMessageHandler extends MessageHandler<ServerMessage> {
    /**
     * Sends a message to the client.
     *
     * @param message the message to be sent
     */
    @Override
    void sendMessage(ServerMessage message);

    /**
     * Closes the connection to the client.
     */
    @Override
    void closeConnection();
}
