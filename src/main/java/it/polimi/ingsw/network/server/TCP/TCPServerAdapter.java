package it.polimi.ingsw.network.server.TCP;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.data.ObjectiveCardAdapter;
import it.polimi.ingsw.data.SideGameCardAdapter;
import it.polimi.ingsw.model.card.gameCard.SideGameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.network.TCP.Observer;
import it.polimi.ingsw.network.TCP.TCPConnectionHandler;
import it.polimi.ingsw.network.adapters.ClientMessageAdapter;
import it.polimi.ingsw.network.client.message.ClientMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;
import it.polimi.ingsw.network.server.NetworkCommandMapper;
import it.polimi.ingsw.network.server.ServerMessageHandler;
import it.polimi.ingsw.network.server.message.ServerMessage;

import java.net.Socket;

/**
 * The TCPServerAdapter class is responsible for handling TCP server operations.
 * It masks the TCPConnectionHandler in order to make RMI and TCP server operations interchangeable.
 * It implements the TCPObserver and ServerMessageHandler interfaces.
 */
public class TCPServerAdapter implements Observer<String>, ServerMessageHandler {
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
        PlayerActionEnum playerAction = receivedMessage.getPlayerAction();
        switch (playerAction) {
            case GET_GAMES -> networkCommandMapper.getGames();
            case CREATE_GAME ->
                    networkCommandMapper.createGame(this, receivedMessage.getGameName(), receivedMessage.getNPlayers(), receivedMessage.getPlayerName());
            case DELETE_GAME -> networkCommandMapper.deleteGame(this, receivedMessage.getGameName());
            case JOIN_GAME ->
                    networkCommandMapper.joinGame(this, receivedMessage.getGameName(), receivedMessage.getPlayerName());
            case CHOOSE_PLAYER_COLOR ->
                    networkCommandMapper.choosePlayerColor(this, receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getPlayerColor());
            case SET_PLAYER_OBJECTIVE ->
                    networkCommandMapper.setPlayerObjective(this, receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getObjectiveCard());
            case PLACE_CARD ->
                    networkCommandMapper.placeCard(this, receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getCoordinate(), receivedMessage.getGameCard());
            case DRAW_CARD_FROM_FIELD ->
                    networkCommandMapper.drawCardFromField(this, receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getGameCard());
            case DRAW_CARD_FROM_RESOURCE_DECK ->
                    networkCommandMapper.drawCardFromResourceDeck(this, receivedMessage.getGameName(), receivedMessage.getPlayerName());
            case DRAW_CARD_FROM_GOLD_DECK ->
                    networkCommandMapper.drawCardFromGoldDeck(this, receivedMessage.getGameName(), receivedMessage.getPlayerName());
            case SWITCH_CARD_SIDE ->
                    networkCommandMapper.switchCardSide(this, receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getGameCard());
            default -> System.out.print("Invalid action");
        }
        System.out.println("Received message: " + message);
    }

    /**
     * Sends a message to the client.
     *
     * @param message the message to be sent
     */
    @Override
    public void sendMessage(ServerMessage message) {
        String serializedMessage = this.gson.toJson(message);
        System.out.println("Sending message: " + serializedMessage);
        //this.clientConnectionHandler.sendMessage(serializedMessage);
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