package it.polimi.ingsw.network.server.actions;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.actions.RMIServerToClientActions;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface defines the actions that a client can perform on the server via RMI.
 * It includes methods for game management such as creating, joining, and leaving games,
 * as well as gameplay actions like placing and drawing cards.
 */
public interface RMIClientToServerActions extends Remote {

    /**
     * Retrieves the list of available games.
     * This method is used when a client wants to see all the games that are currently available to join.
     *
     * @param stub the stub used to call methods on the client's remote object.
     * @throws RemoteException if the remote operation fails.
     */
    void getGames(RMIServerToClientActions stub) throws RemoteException;

    /**
     * Creates a new game with the given game name and number of players.
     *
     * @param stub       the stub used to call methods on the client's remote object.
     * @param gameName   the name of the game.
     * @param playerName the name of the player.
     * @param nPlayers   the number of players in the game.
     * @throws RemoteException if the remote operation fails.
     */
    void createGame(RMIServerToClientActions stub, String gameName, String playerName, int nPlayers) throws RemoteException;

    /**
     * Leaves the game.
     *
     * @param stub       the stub used to call methods on the client's remote object.
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is leaving the game.
     * @throws RemoteException if the remote operation fails.
     */
    void deleteGame(RMIServerToClientActions stub, String gameName, String playerName) throws RemoteException;

    /**
     * Joins the game with the given player name.
     *
     * @param stub       the stub used to call methods on the client's remote object.
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is joining the game.
     * @throws RemoteException if the remote operation fails.
     */
    void joinGame(RMIServerToClientActions stub, String gameName, String playerName) throws RemoteException;

    /**
     * Chooses the color for a player.
     *
     * @param gameName    the name of the game.
     * @param playerName  the name of the player who is choosing the color.
     * @param playerColor the color to be chosen.
     * @throws RemoteException if the remote operation fails.
     */
    void choosePlayerColor(String gameName, String playerName, PlayerColorEnum playerColor) throws RemoteException;

    /**
     * Places a card on the game field.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is placing the card.
     * @param coordinate the coordinate where the card should be placed.
     * @param cardId     the card to be placed.
     * @param isFlipped  whether the card is flipped or not.
     * @throws RemoteException if the remote operation fails.
     */
    void placeCard(String gameName, String playerName, Coordinate coordinate, int cardId, boolean isFlipped) throws RemoteException;

    /**
     * Draws a card from the game field.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     * @param card       the card to be drawn.
     * @throws RemoteException if the remote operation fails.
     */
    void drawCardFromField(String gameName, String playerName, GameCard card) throws RemoteException;

    /**
     * Draws a card from the resource deck.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     * @throws RemoteException if the remote operation fails.
     */
    void drawCardFromResourceDeck(String gameName, String playerName) throws RemoteException;

    /**
     * Draws a card from the gold deck.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     * @throws RemoteException if the remote operation fails.
     */
    void drawCardFromGoldDeck(String gameName, String playerName) throws RemoteException;

    /**
     * Switches the side of a card.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is switching the card side.
     * @param cardId     the card whose side is to be switched.
     * @throws RemoteException if the remote operation fails.
     */
    void switchCardSide(String gameName, String playerName, int cardId) throws RemoteException;

    /**
     * Sets the objective for a player.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player whose objective is to be set.
     * @param cardId     the objective card to be set for the player.
     * @throws RemoteException if the remote operation fails.
     */
    void setPlayerObjective(String gameName, String playerName, int cardId) throws RemoteException;

    /**
     * Sends a chat message from the client to the server.
     * The server will convert it to a ChatServerToClientMessage and send it to all clients excluding the sender.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is sending the chat message.
     * @param message    the chat message to be sent.
     * @param receiver   the receiver of the message. If this is null, the message is not a direct message.
     * @param timestamp  the timestamp of the message.
     * @throws RemoteException if the remote operation fails.
     */
    void chatMessageSender(String gameName, String playerName, String message, String receiver, long timestamp) throws RemoteException;

    /**
     * Sends a heartbeat message to the server.
     * This method is used to check if the connection is still alive.
     *
     * @throws RemoteException if the remote operation fails.
     */
    void heartbeat() throws RemoteException;

    void disconnect(String gameName, String playerName) throws RemoteException;
}