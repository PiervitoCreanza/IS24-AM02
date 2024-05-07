package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.MessageHandler;
import it.polimi.ingsw.network.client.message.ClientToServerMessage;

/**
 * The ClientMessageHandler interface defines the methods that a client message handler must implement.
 * A client message handler is used to make RMI and TCP connections interchangeable.
 */
public interface ClientMessageHandler extends MessageHandler<ClientToServerMessage> {
    /**
     * Sends a message to the client.
     *
     * @param message the message to be sent
     */
    @Override
    void sendMessage(ClientToServerMessage message);

    /**
     * Closes the connection to the client.
     */
    @Override
    void closeConnection();
}
