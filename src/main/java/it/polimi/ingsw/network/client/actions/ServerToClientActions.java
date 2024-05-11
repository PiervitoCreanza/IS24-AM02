package it.polimi.ingsw.network.client.actions;

import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;


/**
 * The ServerToClientActions interface defines the actions that a server can perform.
 * These actions are triggered by receiving different types of messages from the client.
 */
public interface ServerToClientActions extends Remote {

    /**
     * This method is called when the server receives a request for the list of games.
     * TODO: add parameters as needed.
     */
    void receiveGameList(HashSet<GameRecord> games) throws RemoteException;

    /**
     * This method is called when the server receives a notification that a game has been deleted.
     * TODO: add parameters as needed.
     */
    void receiveGameDeleted(String message) throws RemoteException;

    /**
     * This method is called when the server receives an updated view message.
     *
     * @param updatedView The updated view message received from the client.
     */
    void receiveUpdatedView(GameControllerView updatedView) throws RemoteException;

    /**
     * This method is called when the server receives an error message.
     *
     * @param errorMessage The error message received from the client.
     */
    void receiveErrorMessage(String errorMessage) throws RemoteException;


    //TODO: metti tutti i campi del chatMessage che servono
    void receiveChatMessage(ServerToClientMessage message) throws RemoteException;
}