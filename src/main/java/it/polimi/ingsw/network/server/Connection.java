package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.server.message.ServerMessage;

public interface Connection {
    void sendMessage(ServerMessage message);
}
