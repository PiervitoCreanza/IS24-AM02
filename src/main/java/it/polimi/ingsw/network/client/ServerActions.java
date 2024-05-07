package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;


//Remote exceptions will be only thrown by RMI methods
//TCP implementation will not throw RemoteException "promise no more, require no less"
/**
 * The ServerActions interface defines the actions that a server can perform.
 * These actions are triggered by receiving different types of messages from the client.
 */
public interface ServerActions extends Remote {

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

    void heartbeat() throws RemoteException;
}