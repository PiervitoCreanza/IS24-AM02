package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface defines the actions that a client can perform in the game.
 */
public interface ClientActions extends Remote {

    /**
     * Retrieves the list of available games.
     * This method is used when a client wants to see all the games that are currently available to join.
     *
     * @param messageHandler the message handler to handle the response.
     */
    void getGames(ServerMessageHandler messageHandler) throws RemoteException;

    /**
     * Creates a new game with the given game name and number of players.
     *
     * @param messageHandler the message handler to handle the response.
     * @param gameName       the name of the game.
     * @param playerName     the name of the player.
     * @param nPlayers       the number of players in the game.
     */
    void createGame(ServerMessageHandler messageHandler, String gameName, String playerName, int nPlayers) throws RemoteException;

    /**
     * Leaves the game.
     *
     * @param messageHandler the message handler to handle the response.
     * @param gameName the name of the game.
     */
    void deleteGame(ServerMessageHandler messageHandler, String gameName) throws RemoteException;

    /**
     * Joins the game with the given player name.
     *
     * @param messageHandler the message handler to handle the response.
     * @param gameName       the name of the game.
     * @param playerName     the name of the player who is joining the game.
     */
    void joinGame(ServerMessageHandler messageHandler, String gameName, String playerName) throws RemoteException;

    /**
     * Chooses the color for a player.
     *
     * @param messageHandler the message handler to handle the response.
     * @param gameName    the name of the game.
     * @param playerName  the name of the player who is choosing the color.
     * @param playerColor the color to be chosen.
     */
    void choosePlayerColor(ServerMessageHandler messageHandler, String gameName, String playerName, PlayerColorEnum playerColor) throws RemoteException;

    /**
     * Places a card on the game field.
     *
     * @param messageHandler the message handler to handle the response.
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is placing the card.
     * @param coordinate the coordinate where the card should be placed.
     * @param card       the card to be placed.
     */
    void placeCard(ServerMessageHandler messageHandler, String gameName, String playerName, Coordinate coordinate, GameCard card) throws RemoteException;

    /**
     * Draws a card from the game field.
     *
     * @param messageHandler the message handler to handle the response.
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     * @param card       the card to be drawn.
     */
    void drawCardFromField(ServerMessageHandler messageHandler, String gameName, String playerName, GameCard card) throws RemoteException;

    /**
     * Draws a card from the resource deck.
     *
     * @param messageHandler the message handler to handle the response.
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     */
    void drawCardFromResourceDeck(ServerMessageHandler messageHandler, String gameName, String playerName) throws RemoteException;

    /**
     * Draws a card from the gold deck.
     *
     * @param messageHandler the message handler to handle the response.
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     */
    void drawCardFromGoldDeck(ServerMessageHandler messageHandler, String gameName, String playerName) throws RemoteException;

    /**
     * Switches the side of a card.
     *
     * @param messageHandler the message handler to handle the response.
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is switching the card side.
     * @param card       the card whose side is to be switched.
     */
    void switchCardSide(ServerMessageHandler messageHandler, String gameName, String playerName, GameCard card) throws RemoteException;

    /**
     * Sets the objective for a player.
     *
     * @param messageHandler the message handler to handle the response.
     * @param gameName   the name of the game.
     * @param playerName the name of the player whose objective is to be set.
     * @param card       the objective card to be set for the player.
     */
    void setPlayerObjective(ServerMessageHandler messageHandler, String gameName, String playerName, ObjectiveCard card) throws RemoteException;
}
