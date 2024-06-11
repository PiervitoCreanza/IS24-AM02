package it.polimi.ingsw.network.server.TCP;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.data.ObjectiveCardAdapter;
import it.polimi.ingsw.data.SerializableBooleanPropertyDeserializer;
import it.polimi.ingsw.data.SideGameCardAdapter;
import it.polimi.ingsw.model.card.gameCard.SerializableBooleanProperty;
import it.polimi.ingsw.model.card.gameCard.SideGameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.network.TCPConnectionHandler;
import it.polimi.ingsw.network.client.message.ClientToServerMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;
import it.polimi.ingsw.network.server.ServerMessageHandler;
import it.polimi.ingsw.network.server.ServerNetworkControllerMapper;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import it.polimi.ingsw.network.server.message.adapter.ClientToServerMessageAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.Socket;

/**
 * The TCPServerAdapter class is responsible for handling TCP server operations.
 * It masks the TCPConnectionHandler in order to make RMI and TCP server operations interchangeable.
 * It implements the Observer and MessageHandler interfaces.
 */
public class TCPServerAdapter implements ServerMessageHandler, PropertyChangeListener {
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

    /**
     * Indicates whether the connection has been saved.
     */
    private boolean isConnectionSaved = false;

    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(TCPServerAdapter.class);

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
            .registerTypeAdapter(SerializableBooleanProperty.class, new SerializableBooleanPropertyDeserializer())
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
        this.clientConnectionHandler.addPropertyChangeListener(this);
        this.clientConnectionHandler.start();
    }

    /**
     * Notifies the TCPServerAdapter of a message.
     *
     * @param evt the message to be notified
     */
    public void propertyChange(PropertyChangeEvent evt) {
        String changedProperty = evt.getPropertyName();

        switch (changedProperty) {
            case "CONNECTION_CLOSED" -> {
                if (this.isConnectionSaved) {
                    this.isConnectionSaved = false;
                    this.serverNetworkControllerMapper.handleDisconnection(this);
                }
                logger.warn("Connection with client lost.");
            }
            case "MESSAGE_RECEIVED" -> this.receiveMessage((String) evt.getNewValue());
            default -> logger.error("Invalid property change.");
        }
    }

    private void receiveMessage(String message) {
        logger.debug("Received TCP message: {}", message);
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
                    serverNetworkControllerMapper.setPlayerObjective(receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getObjectiveCardId());
            case PLACE_CARD ->
                    serverNetworkControllerMapper.placeCard(receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getCoordinate(), receivedMessage.getGameCardId(), receivedMessage.isFlipped());
            case DRAW_CARD_FROM_FIELD ->
                    serverNetworkControllerMapper.drawCardFromField(receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getGameCardId());
            case DRAW_CARD_FROM_RESOURCE_DECK ->
                    serverNetworkControllerMapper.drawCardFromResourceDeck(receivedMessage.getGameName(), receivedMessage.getPlayerName());
            case DRAW_CARD_FROM_GOLD_DECK ->
                    serverNetworkControllerMapper.drawCardFromGoldDeck(receivedMessage.getGameName(), receivedMessage.getPlayerName());
            case SWITCH_CARD_SIDE ->
                    serverNetworkControllerMapper.switchCardSide(receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getGameCardId());
            case SEND_CHAT_MSG ->
                    serverNetworkControllerMapper.sendChatMessage(receivedMessage.getGameName(), receivedMessage.getPlayerName(), receivedMessage.getMessage(), receivedMessage.getRecipient(), receivedMessage.getTimestamp(), receivedMessage.isDirectMessage());
            case DISCONNECT ->
                    serverNetworkControllerMapper.disconnect(receivedMessage.getGameName(), receivedMessage.getPlayerName());
            default -> logger.error("Invalid action");
        }
    }

    /**
     * Sends a message to the client.
     *
     * @param message the message to be sent
     */
    @Override
    public void sendMessage(ServerToClientMessage message) {
        logger.debug("Sending message: SERVER_ACTION: {}", message.getServerAction());
        String serializedMessage = this.gson.toJson(message);
        logger.trace("Sending message: {}", serializedMessage);
        try {
            this.clientConnectionHandler.send(serializedMessage);
        } catch (IOException e) {
            logger.error("Error sending message: {}", e.getMessage());
        }
    }

    /**
     * Closes the connection.
     */
    @Override
    public void closeConnection() {
        this.clientConnectionHandler.closeConnection();
        logger.debug("Connection closed.");
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