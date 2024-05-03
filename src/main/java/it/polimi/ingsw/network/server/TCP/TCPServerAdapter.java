package it.polimi.ingsw.network.server.TCP;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.data.ObjectiveCardAdapter;
import it.polimi.ingsw.data.SideGameCardAdapter;
import it.polimi.ingsw.model.card.gameCard.SideGameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.network.TCP.TCPConnectionHandler;
import it.polimi.ingsw.network.TCP.TCPObserver;
import it.polimi.ingsw.network.adapters.ClientMessageAdapter;
import it.polimi.ingsw.network.client.message.ClientMessage;
import it.polimi.ingsw.network.server.NetworkCommandMapper;
import it.polimi.ingsw.network.server.ServerMessageHandler;
import it.polimi.ingsw.network.server.message.ServerMessage;

import java.net.Socket;

/**
 * The TCPServerAdapter class is responsible for handling TCP server operations.
 * It masks the TCPConnectionHandler in order to make RMI and TCP server operations interchangeable.
 * It implements the TCPObserver and ServerMessageHandler interfaces.
 */
public class TCPServerAdapter implements TCPObserver, ServerMessageHandler {
    /**
     * The NetworkCommandMapper object is used to map network commands to actions in the game.
     */
    private final NetworkCommandMapper networkCommandMapper;

    /**
     * The TCPConnectionHandler object is used to handle TCP client connections.
     */
    private final TCPConnectionHandler clientConnectionHandler;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(SideGameCard.class, new SideGameCardAdapter())
            .registerTypeAdapter(ObjectiveCard.class, new ObjectiveCardAdapter())
            .registerTypeAdapter(ClientMessage.class, new ClientMessageAdapter())
            .create();

    /**
     * Constructs a new TCPServerAdapter object with the specified socket and NetworkCommandMapper.
     *
     * @param socket               the socket to be used by the TCPConnectionHandler
     * @param networkCommandMapper the NetworkCommandMapper to be used by the TCPServerAdapter
     */
    public TCPServerAdapter(Socket socket, NetworkCommandMapper networkCommandMapper) {
        this.networkCommandMapper = networkCommandMapper;
        this.clientConnectionHandler = new TCPConnectionHandler(socket);
        this.clientConnectionHandler.addObserver(this);
        this.clientConnectionHandler.start();
    }

    /**
     * Notifies the TCPServerAdapter of a message.
     *
     * @param message the message to be notified
     */
    @Override
    public void notify(String message) {
        ClientMessage receivedMessage = this.gson.fromJson(message, ClientMessage.class);
        /*
        switch (playerAction){
            case GETGAMES -> networkCommandMapper.getGames();
            case CREATEGAME -> networkCommandMapper.createGame(this, receivedMessage.);
            case JOINGAME -> networkCommandMapper.joinGame();
            case CHOOSEPLAYERCOLOR -> networkCommandMapper.choosePlayerColor();
            case SETPLAYEROBJECTIVE -> networkCommandMapper.setPlayerObjective();
            case PLACECARD -> networkCommandMapper.placeCard();
            case DRAWCARDFROMFIELD -> networkCommandMapper.drawCardFromField();
            case DRAWCARDFROMRESOURCEDECK -> networkCommandMapper.drawCardFromResourceDeck();
            case DRAWCARDFROMGOLDDECK -> networkCommandMapper.drawCardFromGoldDeck();
            case SWITCHCARDSIDE -> networkCommandMapper.switchCardSide();
        }

            case
        System.out.println("Received message: " + message);
         */
        //TODO: Deserialize and call the right method on the networkCommandMapper
    }

    /**
     * Sends a message to the server.
     *
     * @param message the message to be sent
     */
    @Override
    public void sendMessage(ServerMessage message) {
        //TODO: Serialize message and send it to the client
    }

    /**
     * Closes the connection.
     */
    @Override
    public void closeConnection() {
        // TODO: Close the connection
    }
}