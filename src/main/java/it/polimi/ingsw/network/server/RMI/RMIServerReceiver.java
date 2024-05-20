package it.polimi.ingsw.network.server.RMI;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.actions.RMIServerToClientActions;
import it.polimi.ingsw.network.client.message.ClientToServerMessage;
import it.polimi.ingsw.network.client.message.gameController.*;
import it.polimi.ingsw.network.client.message.mainController.CreateGameClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.DeleteGameClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.GetGamesClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.JoinGameClientToServerMessage;
import it.polimi.ingsw.network.server.ServerMessageHandler;
import it.polimi.ingsw.network.server.ServerNetworkControllerMapper;
import it.polimi.ingsw.network.server.actions.RMIClientToServerActions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;

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

    private static final Logger logger = LogManager.getLogger(RMIServerReceiver.class);

    /**
     * Constructs a new RMIServerReceiver object with the specified ServerNetworkControllerMapper.
     *
     * @param serverNetworkControllerMapper the ServerNetworkControllerMapper to be used by the RMIServerReceiver
     */
    public RMIServerReceiver(ServerNetworkControllerMapper serverNetworkControllerMapper) {
        this.serverNetworkControllerMapper = serverNetworkControllerMapper;
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
            new Thread(() -> serverNetworkControllerMapper.handleDisconnection((ServerMessageHandler) evt.getNewValue())).start();
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
        new Thread(() -> serverNetworkControllerMapper.getGames(instanceRMIServerAdapter(stub))).start();
        // Debug
        printDebug(new GetGamesClientToServerMessage());
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
        new Thread(() -> serverNetworkControllerMapper.createGame(instanceRMIServerAdapter(stub), gameName, playerName, nPlayers)).start();
        // Debug
        printDebug(new CreateGameClientToServerMessage(gameName, playerName, nPlayers));
    }

    /**
     * Leaves the game.
     *
     * @param gameName the name of the game.
     */
    @Override
    public void deleteGame(RMIServerToClientActions stub, String gameName, String playerName) throws RemoteException {
        new Thread(() -> serverNetworkControllerMapper.deleteGame(instanceRMIServerAdapter(stub), gameName, playerName)).start();
        // Debug
        printDebug(new DeleteGameClientToServerMessage(gameName, playerName));
    }

    /**
     * Joins the game with the given player name.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is joining the game.
     */
    @Override
    public void joinGame(RMIServerToClientActions stub, String gameName, String playerName) throws RemoteException {
        new Thread(() -> serverNetworkControllerMapper.joinGame(instanceRMIServerAdapter(stub), gameName, playerName)).start();
        // Debug
        printDebug(new JoinGameClientToServerMessage(gameName, playerName));
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
        new Thread(() -> serverNetworkControllerMapper.choosePlayerColor(gameName, playerName, playerColor)).start();
        // Debug
        printDebug(new ChoosePlayerColorClientToServerMessage(gameName, playerName, playerColor));
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
        new Thread(() -> serverNetworkControllerMapper.setPlayerObjective(gameName, playerName, cardId)).start();
        // Debug
        printDebug(new SetPlayerObjectiveClientToServerMessage(gameName, playerName, cardId));
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
        new Thread(() -> serverNetworkControllerMapper.placeCard(gameName, playerName, coordinate, cardId, isFlipped)).start();
        // Debug
        printDebug(new PlaceCardClientToServerMessage(gameName, playerName, coordinate, cardId, isFlipped));
    }

    /**
     * Draws a card from the game field.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     * @param card       the card to be drawn.
     */
    @Override
    public void drawCardFromField(String gameName, String playerName, GameCard card) throws RemoteException {
        new Thread(() -> serverNetworkControllerMapper.drawCardFromField(gameName, playerName, card)).start();
        // Debug
        printDebug(new DrawCardFromFieldClientToServerMessage(gameName, playerName, card));
    }

    /**
     * Draws a card from the resource deck.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     */
    @Override
    public void drawCardFromResourceDeck(String gameName, String playerName) throws RemoteException {
        new Thread(() -> serverNetworkControllerMapper.drawCardFromResourceDeck(gameName, playerName)).start();
        // Debug
        printDebug(new DrawCardFromResourceDeckClientToServerMessage(gameName, playerName));
    }

    /**
     * Draws a card from the gold deck.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     */
    @Override
    public void drawCardFromGoldDeck(String gameName, String playerName) throws RemoteException {
        new Thread(() -> serverNetworkControllerMapper.drawCardFromGoldDeck(gameName, playerName)).start();
        // Debug
        printDebug(new DrawCardFromGoldDeckClientToServerMessage(gameName, playerName));
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
        new Thread(() -> serverNetworkControllerMapper.switchCardSide(gameName, playerName, cardId)).start();
        // Debug
        printDebug(new SwitchCardSideClientToServerMessage(gameName, playerName, cardId));
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

    private void printDebug(ClientToServerMessage message) {
        System.out.println("RMI message received: " + message.getPlayerAction() + " from: " + message.getPlayerName() + " in game: " + message.getGameName());
    }

    /**
     * This method is used to send a chat message from the client to the server.
     * The server will convert it to a ChatServerToClientMessage and send it to all clients excluding the sender.
     *
     * @param gameName   the name of the game
     * @param playerName the name of the player who sent the chat message
     * @param message    the chat message to be sent
     * @param receiver   the receiver of the message if it's a direct message
     * @param timestamp  the timestamp of the message
     * @throws RemoteException if an error occurs during the remote method call
     */
    @Override
    public void chatMessageSender(String gameName, String playerName, String message, String receiver, long timestamp) throws RemoteException {
        new Thread(() -> serverNetworkControllerMapper.sendChatMessage(gameName, playerName, message, receiver, timestamp)).start();
    }

    /**
     * Sends a heartbeat message to the server.
     * This method is used to check if the connection is still alive.
     *
     * @throws RemoteException if the remote operation fails.
     */
    @Override
    public void heartbeat() throws RemoteException {
    }
}
