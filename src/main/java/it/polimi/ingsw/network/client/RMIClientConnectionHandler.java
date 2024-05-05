package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;

//TODO: here methods should be launched once the VirtualView is received from the server
//TODO: who physically takes the RemoteObject here?
//getGames, virtualView, errorMessage
//TODO: then CORRECT GUI updates will be called based on the received VirtualView
public class RMIClientConnectionHandler extends UnicastRemoteObject implements ServerActions {
    private final ClientCommandMapper clientCommandMapper;

    public RMIClientConnectionHandler(ClientCommandMapper clientCommandMapper) throws RemoteException {
        this.clientCommandMapper = clientCommandMapper;
    }


    /**
     * This method is called when the server receives a request for the list of games.
     * TODO: add parameters as needed.
     *
     * @param games
     */
    @Override
    public void receiveGameList(HashSet<GameRecord> games) throws RemoteException {
        new Thread(() -> {
            //Instance new RMIAdapter(stub)
            clientCommandMapper.receiveGameList(games);
        }).start();
        // Debug
        System.out.println("RMI received message: " + games);
    }

    /**
     * This method is called when the server receives a notification that a game has been deleted.
     * TODO: add parameters as needed.
     *
     * @param message
     */
    @Override
    public void receiveGameDeleted(String message) throws RemoteException {
        new Thread(() -> {
            //Instance new RMIAdapter(stub)
            clientCommandMapper.receiveGameDeleted(message);
        }).start();
        // Debug
        System.out.println("RMI received message: " + message);
    }

    /**
     * This method is called when the server receives an updated view message.
     *
     * @param updatedView The updated view message received from the client.
     */
    @Override
    public void receiveUpdatedView(GameControllerView updatedView) throws RemoteException {
        new Thread(() -> {
            //Instance new RMIAdapter(stub)
            clientCommandMapper.receiveUpdatedView(updatedView);
        }).start();
        // Debug
        System.out.println("RMI received message: " + updatedView);
    }

    /**
     * This method is called when the server receives an error message.
     *
     * @param errorMessage The error message received from the client.
     */
    @Override
    public void receiveErrorMessage(String errorMessage) throws RemoteException {
        new Thread(() -> {
            clientCommandMapper.receiveErrorMessage(errorMessage);
        }).start();
        // Debug
        System.out.println("RMI received message: " + errorMessage);
    }

    @Override
    public void heartbeat() throws RemoteException {
        System.out.println("Ping received");
    }
}


