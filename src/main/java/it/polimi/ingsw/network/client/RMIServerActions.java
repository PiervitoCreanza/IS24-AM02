package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.server.message.ServerMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerActions extends Remote {
    void receiveMessage(ServerMessage message) throws RemoteException;
}
