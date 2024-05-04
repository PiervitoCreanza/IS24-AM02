package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.client.message.ClientMessage;

public interface ClientMessageHandler {
    void sendMessage(ClientMessage message);

    void closeConnection();
}
