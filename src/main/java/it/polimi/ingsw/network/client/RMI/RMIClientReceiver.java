package it.polimi.ingsw.network.client.RMI;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.client.actions.RMIServerToClientActions;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;


//TODO: DOC

/**
 * The RMIClientReceiver class is responsible for receiving messages from the server.
 * Each method is made asyncronous to avoid blocking the server, by calling a new thread.
 * When the thread is launched, the original call is returned, so both client and Server can go on with their work.
 * The call is then completed asynchronously.
 */
public class RMIClientReceiver implements RMIServerToClientActions {

    /**
     * The client command mapper.
     */
    private final ClientNetworkControllerMapper clientNetworkControllerMapper;

    /**
     * The heartbeat timer.
     */
    private Timer heartbeatTimer;

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
        new Thread(() -> clientNetworkControllerMapper.receiveGameList(games)).start();
        // Debug
        System.out.println("RMI received message: " + games);
    }

    /**
     * This method is called when the server receives a notification that a game has been deleted.
     *
     *
     * @param message
     */
    @Override
    public void receiveGameDeleted(String message) throws RemoteException {
        new Thread(() -> clientNetworkControllerMapper.receiveGameDeleted(message)).start();
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
        new Thread(() -> clientNetworkControllerMapper.receiveUpdatedView(updatedView)).start();
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
        new Thread(() -> clientNetworkControllerMapper.receiveErrorMessage(errorMessage)).start();
        // Debug
        System.out.println("RMI received message: " + errorMessage);
    }


    /**
     * Receives a chat message from the server and processes it asynchronously.
     * This method is called when the server sends a chat message to the client.
     * It creates a new thread to handle the received message, allowing the client to continue its operations without waiting for the message processing to complete.
     * After processing the message, it prints a debug message to the console.
     *
     * @param message The chat message received from the server.
     * @throws RemoteException If an error occurs during the RMI connection.
     */
    public void receiveChatMessage(ServerToClientMessage message) throws RemoteException {
        new Thread(() -> {
            clientNetworkControllerMapper.receiveChatMessage(message);
        }).start();
        // Debug
        System.out.println("RMI received message: " + message.toString());
    }

    @Override
    public void heartbeat() throws RemoteException {
        new Thread(() -> {
            // If the timer is already running, cancel it and start a new one
            if (heartbeatTimer != null) {
                heartbeatTimer.cancel();
            }
            // Instance a new timer
            heartbeatTimer = new Timer();
            // Schedule a new timer task. If the client does not receive a heartbeat message within 5 seconds, it will print an error.
            heartbeatTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    throw new RuntimeException("RMI Server Unreachable - detected when pinging");
                }
            }, 5000);
        }).start();

        if (Client.DEBUG) {
            System.out.println("Ping received");
        }
    }

}


