package it.polimi.ingsw.network.client.TCP;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.data.ObjectiveCardAdapter;
import it.polimi.ingsw.data.SideGameCardAdapter;
import it.polimi.ingsw.model.card.gameCard.SideGameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.network.TCPConnectionHandler;
import it.polimi.ingsw.network.client.ClientMessageHandler;
import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.client.message.ClientToServerMessage;
import it.polimi.ingsw.network.client.message.adapter.ServerToClientMessageAdapter;
import it.polimi.ingsw.network.server.message.ServerActionEnum;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import it.polimi.ingsw.tui.utils.Utils;
import it.polimi.ingsw.utils.Observer;

import java.io.IOException;
import java.net.Socket;

/**
 * The TCPClientAdapter class is responsible for handling TCP client operations.
 * It masks the TCPConnectionHandler in order to make RMI and TCP server operations interchangeable.
 * It implements the TCPObserver and ClientMessageHandler interfaces.
 * It is UNIQUE to every RMI player.
 */
public class TCPClientAdapter implements Observer<String>, ClientMessageHandler {
    /**
     * The ServerNetworkControllerMapper object is used to map network commands to actions in the game.
     */
    private final ClientNetworkControllerMapper clientNetworkControllerMapper;

    /**
     * The TCPConnectionHandler object is used to handle TCP client connections.
     */
    private final TCPConnectionHandler serverConnectionHandler;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ServerToClientMessage.class, new ServerToClientMessageAdapter())
            .registerTypeAdapter(SideGameCard.class, new SideGameCardAdapter())
            .registerTypeAdapter(ObjectiveCard.class, new ObjectiveCardAdapter())
            .create();

    /**
     * Constructs a new TCPClientAdapter object with the specified socket and ClientNetworkControllerMapper.
     *
     * @param socket                        the socket to be used by the TCPConnectionHandler
     * @param clientNetworkControllerMapper the clientNetworkControllerMapper to be used by the TCPClientAdapter
     */
    public TCPClientAdapter(Socket socket, ClientNetworkControllerMapper clientNetworkControllerMapper) {
        this.clientNetworkControllerMapper = clientNetworkControllerMapper;
        this.serverConnectionHandler = new TCPConnectionHandler(socket);
        this.serverConnectionHandler.addObserver(this);
        this.serverConnectionHandler.start();
    }

    /**
     * Notifies the TCPClientAdapter of a message.
     *
     * @param message the message to be notified
     */
    @Override
    public void notify(String message) {
        if ("CONNECTION_CLOSED".equals(message)) {
            System.out.println("Connection with server lost.");
            return;
        }
        ServerToClientMessage receivedMessage = this.gson.fromJson(message, ServerToClientMessage.class);
        ServerActionEnum serverAction = receivedMessage.getServerAction();
        switch (serverAction) {
            case UPDATE_VIEW -> clientNetworkControllerMapper.receiveUpdatedView(receivedMessage.getView());
            case DELETE_GAME ->
                    clientNetworkControllerMapper.receiveGameDeleted(receivedMessage.getSuccessDeleteMessage());
            case GET_GAMES -> clientNetworkControllerMapper.receiveGameList(receivedMessage.getGames());
            case ERROR_MSG -> clientNetworkControllerMapper.receiveErrorMessage(receivedMessage.getErrorMessage());
            default -> System.out.print("Invalid action");
        }
        // Debug
        System.out.println(Utils.ANSI_CYAN + "TCP received message: " + message + Utils.ANSI_RESET);
    }

    /**
     * Sends a message to the server.
     *
     * @param message the message to be sent
     */
    @Override
    public void sendMessage(ClientToServerMessage message) {
        String serializedMessage = this.gson.toJson(message);
        try {
            this.serverConnectionHandler.send(serializedMessage);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        // Debug
        System.out.println("Sending message: " + serializedMessage);
    }

    /**
     * Closes the connection.
     */
    @Override
    public void closeConnection() {
        this.serverConnectionHandler.closeConnection();
        // Debug
        System.out.println("Close the connection.");
    }


}