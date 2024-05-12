package it.polimi.ingsw.network.server.TCP;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.data.ObjectiveCardAdapter;
import it.polimi.ingsw.data.SideGameCardAdapter;
import it.polimi.ingsw.model.card.gameCard.SideGameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.network.TCPConnectionHandler;
import it.polimi.ingsw.network.client.message.ClientToServerMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;
import it.polimi.ingsw.network.server.ServerMessageHandler;
import it.polimi.ingsw.network.server.ServerNetworkControllerMapper;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import it.polimi.ingsw.network.server.message.adapter.ClientToServerMessageAdapter;
import it.polimi.ingsw.utils.Observer;

import java.io.IOException;
import java.net.Socket;

/**
 * The TCPServerAdapter class is responsible for handling TCP server operations.
 * It masks the TCPConnectionHandler in order to make RMI and TCP server operations interchangeable.
 * It implements the Observer and MessageHandler interfaces.
 */
public class TCPServerAdapter implements Observer<String>, ServerMessageHandler {
    /**
     * The ServerNetworkControllerMapper object is used to map network commands to actions in the game.
     */
    private final ServerNetworkControllerMapper serverNetworkControllerMapper;

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

    private boolean isConnectionSaved = false;

    /**
     * Gson instance used for JSON serialization and deserialization.
     * It is configured to:
     * - Enable complex map key serialization.
     * - Register a custom type adapter for SideGameCard objects.
     * - Register a custom type adapter for ObjectiveCard objects.
     * - Register a custom type adapter for ClientToServerMessage objects.
     */
    private final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(SideGameCard.class, new SideGameCardAdapter())
            .registerTypeAdapter(ObjectiveCard.class, new ObjectiveCardAdapter())
            .registerTypeAdapter(ClientToServerMessage.class, new ClientToServerMessageAdapter())
            .create();

    /**
     * Constructs a new TCPServerAdapter object with the specified socket and ServerNetworkControllerMapper.
     *
     * @param socket                        the socket to be used by the TCPConnectionHandler
     * @param serverNetworkControllerMapper the ServerNetworkControllerMapper to be used by the TCPServerAdapter
     */
    public TCPServerAdapter(Socket socket, ServerNetworkControllerMapper serverNetworkControllerMapper) {
        this.serverNetworkControllerMapper = serverNetworkControllerMapper;
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
            if (this.isConnectionSaved) {
                this.isConnectionSaved = false;
                this.serverNetworkControllerMapper.handleDisconnection(this);
            }
            return;
        }

        ClientToServerMessage receivedMessage = this.gson.fromJson(message, ClientToServerMessage.class);
        PlayerActionEnum playerAction = receivedMessage.getPlayerAction();
        // Thanks to polymorphism, the correct method is called based on the playerAction (ClientToServerMessage have all the methods of subclasses, so when we get the right message the methods was overridden)
        switch (playerAction) {
            case GET_GAMES -> serverNetworkControllerMapper.getGames(this);
            case CREATE_GAME ->
                    serverNetworkControllerMapper.createGame(this, receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getNPlayers());
            case DELETE_GAME ->
                    serverNetworkControllerMapper.deleteGame(this, receivedMessage.getGameName(), receivedMessage.getPlayerName());
            case JOIN_GAME ->
                    serverNetworkControllerMapper.joinGame(this, receivedMessage.getGameName(), receivedMessage.getPlayerName());
            case CHOOSE_PLAYER_COLOR ->
                    serverNetworkControllerMapper.choosePlayerColor(receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getPlayerColor());
            case SET_PLAYER_OBJECTIVE ->
                    serverNetworkControllerMapper.setPlayerObjective(receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getObjectiveCard());
            case PLACE_CARD ->
                    serverNetworkControllerMapper.placeCard(receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getCoordinate(), receivedMessage.getGameCard());
            case DRAW_CARD_FROM_FIELD ->
                    serverNetworkControllerMapper.drawCardFromField(receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getGameCard());
            case DRAW_CARD_FROM_RESOURCE_DECK ->
                    serverNetworkControllerMapper.drawCardFromResourceDeck(receivedMessage.getGameName(), receivedMessage.getPlayerName());
            case DRAW_CARD_FROM_GOLD_DECK ->
                    serverNetworkControllerMapper.drawCardFromGoldDeck(receivedMessage.getGameName(), receivedMessage.getPlayerName());
            case SWITCH_CARD_SIDE ->
                    serverNetworkControllerMapper.switchCardSide(receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getGameCard());
            case CHAT_MSG -> serverNetworkControllerMapper.chatMessageSender(receivedMessage);
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
    public void sendMessage(ServerToClientMessage message) {
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

    /**
     * Sets the name of the game associated with the connection.
     *
     * @param gameName the name of the game
     */
    @Override
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public void connectionSaved(boolean hasBeenSaved) {
        this.isConnectionSaved = hasBeenSaved;
    }
}