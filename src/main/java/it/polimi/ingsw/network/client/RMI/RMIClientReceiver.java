package it.polimi.ingsw.network.client.RMI;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.client.actions.RMIServerToClientActions;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;

import java.rmi.RemoteException;
import java.util.HashSet;

/**
 * The RMIClientReceiver class is responsible for receiving messages from the server.
 */
public class RMIClientReceiver implements RMIServerToClientActions {

    /**
     * The client command mapper.
     */
    private final ClientNetworkControllerMapper clientNetworkControllerMapper;

    /**
     * Class constructor.
     *
     * @param clientNetworkControllerMapper The client command mapper.
     * @throws RemoteException if the remote operation fails.
     */
    public RMIClientReceiver(ClientNetworkControllerMapper clientNetworkControllerMapper) throws RemoteException {
        this.clientNetworkControllerMapper = clientNetworkControllerMapper;
    }


    /**
     * This method is called when the server receives the game list response.
     *
     * @param games The list of games received from the server.
     */
    @Override
    public void receiveGameList(HashSet<GameRecord> games) throws RemoteException {
        new Thread(() -> {
            //Instance new RMIAdapter(stub)
            clientNetworkControllerMapper.receiveGameList(games);
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
            clientNetworkControllerMapper.receiveGameDeleted(message);
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
            clientNetworkControllerMapper.receiveUpdatedView(updatedView);
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
            clientNetworkControllerMapper.receiveErrorMessage(errorMessage);
        }).start();
        // Debug
        System.out.println("RMI received message: " + errorMessage);
    }

    @Override
    public void heartbeat() throws RemoteException {
        if (Client.DEBUG) {
            System.out.println("Ping received");
        }
    }
}


