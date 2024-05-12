package it.polimi.ingsw.network.server.actions;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.actions.RMIServerToClientActions;
import it.polimi.ingsw.network.client.message.ClientToServerMessage;

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
     * @param stub the stub used to call methods on the client's remote object
     */
    void getGames(RMIServerToClientActions stub) throws RemoteException;

    /**
     * Creates a new game with the given game name and number of players.
     *
     * @param stub       the stub used to call methods on the client's remote object.
     * @param gameName   the name of the game.
     * @param playerName the name of the player.
     * @param nPlayers   the number of players in the game.
     */
    void createGame(RMIServerToClientActions stub, String gameName, String playerName, int nPlayers) throws RemoteException;

    /**
     * Leaves the game.
     * @param stub the stub used to call methods on the client's remote object.
     * @param gameName the name of the game.
     * @param playerName the name of the player who is leaving the game.
     */
    void deleteGame(RMIServerToClientActions stub, String gameName, String playerName) throws RemoteException;

    /**
     * Joins the game with the given player name.
     *
     * @param stub       the stub used to call methods on the client's remote object.
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is joining the game.
     */
    void joinGame(RMIServerToClientActions stub, String gameName, String playerName) throws RemoteException;

    /**
     * Chooses the color for a player.
     *
     * @param gameName    the name of the game.
     * @param playerName  the name of the player who is choosing the color.
     * @param playerColor the color to be chosen.
     */
    void choosePlayerColor(String gameName, String playerName, PlayerColorEnum playerColor) throws RemoteException;

    /**
     * Places a card on the game field.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is placing the card.
     * @param coordinate the coordinate where the card should be placed.
     * @param card       the card to be placed.
     */
    void placeCard(String gameName, String playerName, Coordinate coordinate, GameCard card) throws RemoteException;

    /**
     * Draws a card from the game field.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     * @param card       the card to be drawn.
     */
    void drawCardFromField(String gameName, String playerName, GameCard card) throws RemoteException;

    /**
     * Draws a card from the resource deck.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     */
    void drawCardFromResourceDeck(String gameName, String playerName) throws RemoteException;

    /**
     * Draws a card from the gold deck.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     */
    void drawCardFromGoldDeck(String gameName, String playerName) throws RemoteException;

    /**
     * Switches the side of a card.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is switching the card side.
     * @param card       the card whose side is to be switched.
     */
    void switchCardSide(String gameName, String playerName, GameCard card) throws RemoteException;

    /**
     * Sets the objective for a player.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player whose objective is to be set.
     * @param card       the objective card to be set for the player.
     */
    void setPlayerObjective(String gameName, String playerName, ObjectiveCard card) throws RemoteException;

    /**
     * Sends a chat message from the client to the server.
     * The server will convert it to a chatMessageServerToClientMessage and send it to all clients excluding the sender.
     *
     * @param message the chat message to be sent.
     */
    void chatMessageSender(ClientToServerMessage message) throws RemoteException;
}