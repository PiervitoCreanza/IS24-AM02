package it.polimi.ingsw.network.client.actions;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


/**
 * The ServerToClientActions interface defines the actions that a server can perform.
 * These actions are triggered by receiving different types of messages from the client.
 */
public interface ServerToClientActions extends Remote {

    /**
     * This method is called when the server receives a request for the list of games.
     *
     * @param games The list of games to be sent to the client.
     */
    void receiveGameList(ArrayList<GameRecord> games) throws RemoteException;

    /**
     * This method is called when the server receives a notification that a game has been deleted.
     *
     * @param message The message received from the client.
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

    /**
     * This method is called when the server receives a chat message.
     * It is responsible for handling the received chat message from the client.
     *
     * @param playerName The chat message received from the client.
     * @param message    The chat message received from the client.
     * @param timestamp  The timestamp of the message.
     * @param isDirect   Flag to indicate if the message is a direct message.
     * @throws RemoteException If an error occurs during the RMI connection.
     */
    void receiveChatMessage(String playerName, String message, long timestamp, boolean isDirect) throws RemoteException;
}