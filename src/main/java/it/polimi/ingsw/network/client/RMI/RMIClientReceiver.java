package it.polimi.ingsw.network.client.RMI;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.client.actions.RMIServerToClientActions;
import it.polimi.ingsw.network.server.message.ServerActionEnum;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(RMIClientReceiver.class);

    private static RMIClientReceiver strongReference;

    /**
     * Class constructor.
     *
     * @param clientNetworkControllerMapper The client command mapper.
     */
    public RMIClientReceiver(ClientNetworkControllerMapper clientNetworkControllerMapper) {
        this.clientNetworkControllerMapper = clientNetworkControllerMapper;
        strongReference = this;
    }


    /**
     * This method is called when the server receives the game list response.
     *
     * @param games The list of games received from the server.
     */
    @Override
    public void receiveGameList(ArrayList<GameRecord> games) throws RemoteException {
        new Thread(() -> clientNetworkControllerMapper.receiveGameList(games)).start();
        // Debug
        printDebug(ServerActionEnum.GET_GAMES, "games: " + games.stream().map(GameRecord::gameName).collect(Collectors.toCollection(ArrayList::new)));
    }

    /**
     * This method is called when the server receives a notification that a game has been deleted.
     *
     * @param message The message received from the server.
     */
    @Override
    public void receiveGameDeleted(String message) throws RemoteException {
        new Thread(() -> clientNetworkControllerMapper.receiveGameDeleted(message)).start();
        // Debug
        printDebug(ServerActionEnum.DELETE_GAME, "message: " + message);
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
        printDebug(ServerActionEnum.UPDATE_VIEW, "view: " + updatedView.toString());
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
        printDebug(ServerActionEnum.ERROR_MSG, "error: " + errorMessage);
    }

    /**
     * Receives a chat message from the server and processes it asynchronously.
     * This method is called when the server sends a chat message to the client.
     * It creates a new thread to handle the received message, allowing the client to continue its operations without waiting for the message processing to complete.
     * After processing the message, it prints a debug message to the console.
     *
     * @param playerName The chat message received from the server.
     * @param message    The chat message received from the server.
     * @param timestamp  The timestamp of the message.
     * @param isDirect   Flag to indicate if the message is a direct message.
     * @throws RemoteException If an error occurs during the RMI connection.
     */
    @Override
    public void receiveChatMessage(String playerName, String message, long timestamp, boolean isDirect) throws RemoteException {
        new Thread(() -> clientNetworkControllerMapper.receiveChatMessage(playerName, message, timestamp, isDirect)).start();
        // Debug
        printDebug(ServerActionEnum.RECEIVE_CHAT_MSG, "sender: " + playerName + " message: " + message + " timestamp: " + timestamp + " isDirect: " + isDirect);
    }

    /**
     * Sends a heartbeat signal to the server.
     * This method is used to indicate that the client is still active.
     * It throws a RemoteException if the RMI connection encounters an error.
     *
     * @throws RemoteException If an error occurs during the RMI connection.
     */
    @Override
    public void heartbeat() throws RemoteException {
        if (Client.DEBUG) {
            logger.debug("Ping received");
        }
    }

    /**
     * Prints a debug message to the console.
     * This method is used to log the received messages for debugging purposes.
     *
     * @param serverAction The action performed by the server.
     * @param content      The content of the message.
     */
    private void printDebug(ServerActionEnum serverAction, String content) {
        logger.debug("RMI message received: {} {}", serverAction, content);
    }
}

