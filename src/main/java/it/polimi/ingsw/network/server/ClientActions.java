package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * The ClientActions interface extends the Remote interface to enable remote method invocation (RMI)
 * capabilities across different network protocols. This interface is crucial for ensuring methods
 * within the client-side operations can appropriately handle RemoteExceptions as required by Java RMI.
 *
 * Key Points:
 * - The interface extends Remote, mandating all methods to be capable of throwing RemoteExceptions.
 * - Adheres to the "require no more, promise no less" principle whereby if the parent interface
 *   declares certain exceptions, the implementing classes (IMPLEMENTATION) may opt to throw any subset of these.
 * - The RMI implementation is strict in enforcing RemoteExceptions, ensuring full RMI compatibility.
 * - The TCP adaptation (e.g., in TCPServerAdapter) demonstrates the flexibility of the interface, as
 *   methods under TCP do not throw RemoteExceptions, reflecting the non-RMI requirements.
 *
 * TODO: Verify and ensure that all methods are implemented with correct exception handling in the TCP context.
 * TODO: Validate and test RMI compatibility and behavior under various network conditions.
 * TODO: Document each method detailing its specific role, expected inputs, outputs, and potential side effects.
 *
 * Example Implementations:
 * - RMIClientConnectionHandler implements ClientActions for RMI, strictly throwing RemoteExceptions where required.
 * - TCPServerAdapter implements ClientActions for TCP, omitting RemoteExceptions to suit the protocol's needs.
 */
public interface ClientActions extends Remote {

    /**
     * Retrieves the list of available games.
     * This method is used when a client wants to see all the games that are currently available to join.
     */
    void getGames() throws RemoteException;

    /**
     * Creates a new game with the given game name and number of players.
     *
     * @param gameName the name of the game.
     * @param nPlayers the number of players in the game.
     */
    void createGame(ServerMessageHandler messageHandler, String gameName, int nPlayers) throws RemoteException;

    /**
     * Leaves the game.
     *
     * @param gameName the name of the game.
     */
    void deleteGame(ServerMessageHandler messageHandler, String gameName) throws RemoteException;

    /**
     * Joins the game with the given player name.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is joining the game.
     */
    void joinGame(ServerMessageHandler messageHandler, String gameName, String playerName) throws RemoteException;

    /**
     * Chooses the color for a player.
     *
     * @param gameName    the name of the game.
     * @param playerName  the name of the player who is choosing the color.
     * @param playerColor the color to be chosen.
     */
    void choosePlayerColor(ServerMessageHandler messageHandler, String gameName, String playerName, PlayerColorEnum playerColor) throws RemoteException;

    /**
     * Places a card on the game field.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is placing the card.
     * @param coordinate the coordinate where the card should be placed.
     * @param card       the card to be placed.
     */
    void placeCard(ServerMessageHandler messageHandler, String gameName, String playerName, Coordinate coordinate, GameCard card) throws RemoteException;

    /**
     * Draws a card from the game field.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     * @param card       the card to be drawn.
     */
    void drawCardFromField(ServerMessageHandler messageHandler, String gameName, String playerName, GameCard card) throws RemoteException;

    /**
     * Draws a card from the resource deck.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     */
    void drawCardFromResourceDeck(ServerMessageHandler messageHandler, String gameName, String playerName) throws RemoteException;

    /**
     * Draws a card from the gold deck.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     */
    void drawCardFromGoldDeck(ServerMessageHandler messageHandler, String gameName, String playerName) throws RemoteException;

    /**
     * Switches the side of a card.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is switching the card side.
     * @param card       the card whose side is to be switched.
     */
    void switchCardSide(ServerMessageHandler messageHandler, String gameName, String playerName, GameCard card) throws RemoteException;

    /**
     * Sets the objective for a player.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player whose objective is to be set.
     * @param card       the objective card to be set for the player.
     */
    void setPlayerObjective(ServerMessageHandler messageHandler, String gameName, String playerName, ObjectiveCard card) throws RemoteException;
}
