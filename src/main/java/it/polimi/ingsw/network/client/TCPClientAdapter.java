package it.polimi.ingsw.network.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.Utils;
import it.polimi.ingsw.data.ObjectiveCardAdapter;
import it.polimi.ingsw.data.SideGameCardAdapter;
import it.polimi.ingsw.model.card.gameCard.SideGameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.network.TCP.Observer;
import it.polimi.ingsw.network.TCP.TCPConnectionHandler;
import it.polimi.ingsw.network.adapters.ServerMessageAdapter;
import it.polimi.ingsw.network.client.message.ClientMessage;
import it.polimi.ingsw.network.server.message.ServerActionEnum;
import it.polimi.ingsw.network.server.message.ServerMessage;

import java.io.IOException;
import java.net.Socket;

/**
 * The TCPClientAdapter class is responsible for handling TCP client operations.
 * It masks the TCPConnectionHandler in order to make RMI and TCP server operations interchangeable.
 * It implements the TCPObserver and ClientMessageHandler interfaces.
 */
public class TCPClientAdapter implements Observer<String>, ClientMessageHandler {
    /**
     * The NetworkCommandMapper object is used to map network commands to actions in the game.
     */
    private final ClientCommandMapper clientCommandMapper;

    /**
     * The TCPConnectionHandler object is used to handle TCP client connections.
     */
    private final TCPConnectionHandler serverConnectionHandler;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ServerMessage.class, new ServerMessageAdapter())
            .registerTypeAdapter(SideGameCard.class, new SideGameCardAdapter())
            .registerTypeAdapter(ObjectiveCard.class, new ObjectiveCardAdapter())
            .create();

    /**
     * Constructs a new TCPClientAdapter object with the specified socket and ClientCommandMapper.
     *
     * @param socket              the socket to be used by the TCPConnectionHandler
     * @param clientCommandMapper the clientCommandMapper to be used by the TCPClientAdapter
     */
    public TCPClientAdapter(Socket socket, ClientCommandMapper clientCommandMapper) {
        this.clientCommandMapper = clientCommandMapper;
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
        ServerMessage receivedMessage = this.gson.fromJson(message, ServerMessage.class);
        ServerActionEnum serverAction = receivedMessage.getServerAction();
        switch (serverAction) {
            case UPDATE_VIEW -> clientCommandMapper.receiveUpdatedView(receivedMessage.getView());
            case DELETE_GAME -> clientCommandMapper.receiveGameDeleted(receivedMessage.getSuccessDeleteMessage());
            case GET_GAMES -> clientCommandMapper.receiveGameList(receivedMessage.getGames());
            case ERROR_MSG -> clientCommandMapper.receiveErrorMessage(receivedMessage.getErrorMessage());
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
    public void sendMessage(ClientMessage message) {
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