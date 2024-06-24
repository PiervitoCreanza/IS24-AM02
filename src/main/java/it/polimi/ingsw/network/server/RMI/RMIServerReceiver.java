package it.polimi.ingsw.network.server.RMI;

import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.actions.RMIServerToClientActions;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.ServerMessageHandler;
import it.polimi.ingsw.network.server.ServerNetworkControllerMapper;
import it.polimi.ingsw.network.server.actions.RMIClientToServerActions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The RMIServerReceiver class is responsible for receiving client actions and notifying the server message handler.
 * It implements the RMIClientToServerActions interface, which defines the methods for handling client actions.
 * It also implements the Observer interface, which allows it to be notified when a change occurs in the ServerMessageHandler.
 */
public class RMIServerReceiver implements RMIClientToServerActions, PropertyChangeListener {

    /**
     * The ServerNetworkControllerMapper object used to map network commands to actions in the game.
     */
    private final ServerNetworkControllerMapper serverNetworkControllerMapper;

    /**
     * The executor service used to execute tasks in parallel.
     */
    private final ExecutorService executor;

    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(RMIServerReceiver.class);

    /**
     * A "strong" reference to the stub.
     * It's used to avoid the stub being garbage collected in some rare cases.
     * Being static the DGC (Distributed Garbage Collector) will not collect it.
     */
    private static RMIServerReceiver serverStrongReference;

    /**
     * Constructs a new RMIServerReceiver object with the specified ServerNetworkControllerMapper.
     *
     * @param serverNetworkControllerMapper the ServerNetworkControllerMapper to be used by the RMIServerReceiver
     */
    public RMIServerReceiver(ServerNetworkControllerMapper serverNetworkControllerMapper) {
        this.serverNetworkControllerMapper = serverNetworkControllerMapper;
        this.executor = Executors.newFixedThreadPool(10);
        serverStrongReference = this;
    }

    /**
     * Notifies the ServerNetworkControllerMapper when a client disconnects.
     * This method is called when the ServerMessageHandler detects a disconnection.
     *
     * @param evt the ServerMessageHandler that detected the disconnection
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("CONNECTION_CLOSED")) {
            logger.warn("Connection lost");
            ServerMessageHandler serverMessageHandler = (ServerMessageHandler) evt.getSource();
            this.executor.submit(() -> serverNetworkControllerMapper.handleDisconnection(serverMessageHandler.getGameName(), serverMessageHandler.getPlayerName()));
        } else {
            logger.error("Unknown property name: {}", evt.getPropertyName());
        }

    }

    /**
     * Retrieves the list of available games.
     * This method is used when a client wants to see all the games that are currently available to join.
     */
    @Override
    public void getGames(RMIServerToClientActions stub) throws RemoteException {
        this.executor.submit(() -> serverNetworkControllerMapper.getGames(instanceRMIServerAdapter(stub)));
        // Debug
        printDebug(PlayerActionEnum.GET_GAMES, "");
    }

    /* All the methods that can be called from a ClientAsAClient on Server */

    /**
     * Creates a new game with the given game name and number of players.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player.
     * @param nPlayers   the number of players in the game.
     */
    @Override
    public void createGame(RMIServerToClientActions stub, String gameName, String playerName, int nPlayers) throws RemoteException {
        this.executor.submit(() -> serverNetworkControllerMapper.createGame(instanceRMIServerAdapter(stub), gameName, playerName, nPlayers));
        // Debug
        printDebug(PlayerActionEnum.CREATE_GAME, "gameName: " + gameName + " playerName: " + playerName + " nPlayers: " + nPlayers);
    }

    /**
     * Leaves the game.
     *
     * @param gameName the name of the game.
     */
    @Override
    public void deleteGame(RMIServerToClientActions stub, String gameName, String playerName) throws RemoteException {
        this.executor.submit(() -> serverNetworkControllerMapper.deleteGame(instanceRMIServerAdapter(stub), gameName, playerName));
        // Debug
        inGamePrintDebug(PlayerActionEnum.DELETE_GAME, gameName, playerName, "");
    }

    /**
     * Joins the game with the given player name.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is joining the game.
     */
    @Override
    public void joinGame(RMIServerToClientActions stub, String gameName, String playerName) throws RemoteException {
        this.executor.submit(() -> serverNetworkControllerMapper.joinGame(instanceRMIServerAdapter(stub), gameName, playerName));
        // Debug
        printDebug(PlayerActionEnum.JOIN_GAME, "gameName: " + gameName + " playerName: " + playerName);
    }

    /**
     * Chooses the color for a player.
     *
     * @param gameName    the name of the game.
     * @param playerName  the name of the player who is choosing the color.
     * @param playerColor the color to be chosen.
     */
    @Override
    public void choosePlayerColor(String gameName, String playerName, PlayerColorEnum playerColor) throws RemoteException {
        this.executor.submit(() -> serverNetworkControllerMapper.choosePlayerColor(gameName, playerName, playerColor));
        // Debug
        inGamePrintDebug(PlayerActionEnum.CHOOSE_PLAYER_COLOR, gameName, playerName, "playerColor: " + playerColor);
    }

    /**
     * Sets the objective for a player.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player whose objective is to be set.
     * @param cardId     the objective card to be set for the player.
     */
    @Override
    public void setPlayerObjective(String gameName, String playerName, int cardId) throws RemoteException {
        this.executor.submit(() -> serverNetworkControllerMapper.setPlayerObjective(gameName, playerName, cardId));
        // Debug
        inGamePrintDebug(PlayerActionEnum.SET_PLAYER_OBJECTIVE, gameName, playerName, "cardId: " + cardId);
    }


    /**
     * Places a card on the game field.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is placing the card.
     * @param coordinate the coordinate where the card should be placed.
     * @param cardId     the card to be placed.
     */
    @Override
    public void placeCard(String gameName, String playerName, Coordinate coordinate, int cardId, boolean isFlipped) throws RemoteException {
        this.executor.submit(() -> serverNetworkControllerMapper.placeCard(gameName, playerName, coordinate, cardId, isFlipped));
        // Debug
        inGamePrintDebug(PlayerActionEnum.PLACE_CARD, gameName, playerName, "coordinate: " + coordinate + " cardId: " + cardId + " isFlipped: " + isFlipped);
    }

    /**
     * Draws a card from the game field.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     * @param cardId     the id of the card to be drawn.
     */
    @Override
    public void drawCardFromField(String gameName, String playerName, int cardId) throws RemoteException {
        this.executor.submit(() -> serverNetworkControllerMapper.drawCardFromField(gameName, playerName, cardId));
        // Debug
        inGamePrintDebug(PlayerActionEnum.DRAW_CARD_FROM_FIELD, gameName, playerName, "card: " + cardId);
    }

    /**
     * Draws a card from the resource deck.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     */
    @Override
    public void drawCardFromResourceDeck(String gameName, String playerName) throws RemoteException {
        this.executor.submit(() -> serverNetworkControllerMapper.drawCardFromResourceDeck(gameName, playerName));
        // Debug
        inGamePrintDebug(PlayerActionEnum.DRAW_CARD_FROM_RESOURCE_DECK, gameName, playerName, "");
    }

    /**
     * Draws a card from the gold deck.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     */
    @Override
    public void drawCardFromGoldDeck(String gameName, String playerName) throws RemoteException {
        this.executor.submit(() -> serverNetworkControllerMapper.drawCardFromGoldDeck(gameName, playerName));
        // Debug
        inGamePrintDebug(PlayerActionEnum.DRAW_CARD_FROM_GOLD_DECK, gameName, playerName, "");
    }

    /**
     * Switches the side of a card.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is switching the card side.
     * @param cardId     the card whose side is to be switched.
     */
    @Override
    public void switchCardSide(String gameName, String playerName, int cardId) throws RemoteException {
        this.executor.submit(() -> serverNetworkControllerMapper.switchCardSide(gameName, playerName, cardId));
        // Debug
        inGamePrintDebug(PlayerActionEnum.SWITCH_CARD_SIDE, gameName, playerName, "cardId: " + cardId);
    }

    /**
     * This method is used to send a chat message from the client to the server.
     * The server will convert it to a ChatServerToClientMessage and send it to all clients excluding the sender.
     *
     * @param gameName        the name of the game
     * @param playerName      the name of the player who sent the chat message
     * @param message         the chat message to be sent
     * @param receiver        the receiver of the message if it's a direct message
     * @param timestamp       the timestamp of the message
     * @param isDirectMessage flag to indicate if the message is a direct message
     * @throws RemoteException if an error occurs during the remote method call
     */
    @Override
    public void chatMessageSender(String gameName, String playerName, String message, String receiver, long timestamp, boolean isDirectMessage) throws RemoteException {
        this.executor.submit(() -> serverNetworkControllerMapper.sendChatMessage(gameName, playerName, message, receiver, timestamp, isDirectMessage));
        // Debug
        inGamePrintDebug(PlayerActionEnum.SEND_CHAT_MSG, gameName, playerName, "message: " + message + " receiver: " + receiver + " timestamp: " + timestamp);
    }

    /**
     * Sends a heartbeat message to the server.
     * This method is used to check if the connection is still alive.
     *
     * @throws RemoteException if the remote operation fails.
     */
    @Override
    public void heartbeat() throws RemoteException {
        if (Server.IS_DEBUG) {
            logger.info("Received heartbeat from client");
        }
    }

    /**
     * Disconnects a player from the game.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is disconnecting.
     */
    @Override
    public void disconnect(String gameName, String playerName) throws RemoteException {
        this.executor.submit(() -> serverNetworkControllerMapper.disconnect(gameName, playerName));
        // Debug
        inGamePrintDebug(PlayerActionEnum.DISCONNECT, gameName, playerName, "");
    }

    /**
     * This method creates an instance of RMIServerSender and adds the current object as an observer.
     * It is used to create a new connection to a client.
     *
     * @param stub the stub used to call methods on the client's remote object
     * @return an instance of RMIServerSender
     */
    private RMIServerSender instanceRMIServerAdapter(RMIServerToClientActions stub) {
        RMIServerSender rmiServerSender = new RMIServerSender(stub);
        rmiServerSender.addPropertyChangeListener(this);
        return rmiServerSender;
    }

    /**
     * Prints a debug message with the details of the message received.
     *
     * @param playerAction the action received
     * @param content      the content of the message
     */
    private void printDebug(PlayerActionEnum playerAction, String content) {
        logger.debug("RMI message received: {} {}", playerAction, content);
    }

    /**
     * Prints a debug message with the details of the message received.
     *
     * @param playerAction the action received
     * @param gameName     the name of the game
     * @param playerName   the name of the player
     * @param content      the content of the message
     */
    private void inGamePrintDebug(PlayerActionEnum playerAction, String gameName, String playerName, String content) {
        printDebug(playerAction, "from player: " + playerName + " in game: " + gameName + " " + content);
    }
}
