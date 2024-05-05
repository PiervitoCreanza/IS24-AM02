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

import java.io.IOException;
import java.net.Socket;

/**
 * The TCPServerAdapter class is responsible for handling TCP server operations.
 * It masks the TCPConnectionHandler in order to make RMI and TCP server operations interchangeable.
 * It implements the Observer and MessageHandler interfaces.
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

    /**
     * The name of the game.
     */
    private String gameName;

    /**
     * The name of the player.
     */
    private String playerName;

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
        // Handle client disconnection
        if ("CONNECTION_CLOSED".equals(message)) {
            networkCommandMapper.handleDisconnection(this);
            return;
        }

        ClientMessage receivedMessage = this.gson.fromJson(message, ClientMessage.class);
        PlayerActionEnum playerAction = receivedMessage.getPlayerAction();
        // Thanks to polymorphism, the correct method is called based on the playerAction (ClientMessage have all the methods of subclasses, so when we get the right message the methods was overridden)
        switch (playerAction) {
            case GET_GAMES -> networkCommandMapper.getGames(this);
            case CREATE_GAME ->
                    networkCommandMapper.createGame(this, receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getNPlayers());
            case DELETE_GAME -> networkCommandMapper.deleteGame(this, receivedMessage.getGameName());
            case JOIN_GAME ->
                    networkCommandMapper.joinGame(this, receivedMessage.getGameName(), receivedMessage.getPlayerName());
            case CHOOSE_PLAYER_COLOR ->
                    networkCommandMapper.choosePlayerColor(receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getPlayerColor());
            case SET_PLAYER_OBJECTIVE ->
                    networkCommandMapper.setPlayerObjective(receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getObjectiveCard());
            case PLACE_CARD ->
                    networkCommandMapper.placeCard(receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getCoordinate(), receivedMessage.getGameCard());
            case DRAW_CARD_FROM_FIELD ->
                    networkCommandMapper.drawCardFromField(receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getGameCard());
            case DRAW_CARD_FROM_RESOURCE_DECK ->
                    networkCommandMapper.drawCardFromResourceDeck(receivedMessage.getGameName(), receivedMessage.getPlayerName());
            case DRAW_CARD_FROM_GOLD_DECK ->
                    networkCommandMapper.drawCardFromGoldDeck(receivedMessage.getGameName(), receivedMessage.getPlayerName());
            case SWITCH_CARD_SIDE ->
                    networkCommandMapper.switchCardSide(receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getGameCard());
            default -> System.err.print("Invalid action");
        }
        // Debug
        System.out.println("TCP received message: " + message);
    }

    /**
     * Sends a message to the client.
     *
     * @param message the message to be sent
     */
    @Override
    public void sendMessage(ServerMessage message) {
        String serializedMessage = this.gson.toJson(message);
        try {
            this.clientConnectionHandler.send(serializedMessage);
        } catch (IOException e) {
            System.out.println("Error sending message: ");
            return;
        }

        // Debug
        System.out.println("TCP message sent: " + serializedMessage);
    }

    /**
     * Closes the connection.
     */
    @Override
    public void closeConnection() {
        this.clientConnectionHandler.closeConnection();
        // Debug
        System.out.println("Connection closed.");
    }

    /**
     * Gets the name of the player associated with the connection.
     */
    @Override
    public String getPlayerName() {
        return this.playerName;
    }

    /**
     * Sets the name of the player associated with the connection.
     *
     * @param playerName the name of the player
     */
    @Override
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    /**
     * Gets the name of the game associated with the connection.
     *
     * @return the name of the game
     */
    @Override
    public String getGameName() {
        return this.gameName;
    }

    @Override
    public void heartbeat() {
        //TODO Unify with RMI?
        // We need this method in rmi because
        // we start pinging only when the connection is "saved"
        // is there any way to make it similar on TCP?
    }

    /**
     * Sets the name of the game associated with the connection.
     *
     * @param gameName the name of the game
     */
    @Override
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}