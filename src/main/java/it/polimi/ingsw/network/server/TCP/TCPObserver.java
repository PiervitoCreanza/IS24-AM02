package it.polimi.ingsw.network.server.TCP;

import it.polimi.ingsw.network.server.Connection;

import java.util.UUID;

public interface TCPObserver {
    void notify(Connection connection, String message);
}
