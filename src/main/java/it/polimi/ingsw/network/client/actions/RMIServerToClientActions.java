package it.polimi.ingsw.network.client.actions;

import java.rmi.Remote;
import java.rmi.RemoteException;


//Remote exceptions will be only thrown by RMI methods
//TCP implementation will not throw RemoteException "promise no more, require no less"

/**
 * The RMIServerToClientActions interface defines the actions that a server can perform.
 * These actions are triggered by receiving different types of messages from the client.
 */
public interface RMIServerToClientActions extends Remote, ServerToClientActions {

    /**
     * Sends a heartbeat signal to the server.
     * This method is used to indicate that the client is still active.
     * It throws a RemoteException if the RMI connection encounters an error.
     *
     * @throws RemoteException If an error occurs during the RMI connection.
     */
    void heartbeat() throws RemoteException;
}