package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.client.message.ClientMessage;

public interface ClientMessageHandler {
    void sendMessage(ClientMessage message);
    void closeConnection();
}
