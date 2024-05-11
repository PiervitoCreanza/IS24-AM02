package it.polimi.ingsw.network.server.actions;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.actions.RMIServerToClientActions;
import it.polimi.ingsw.network.client.message.ClientToServerMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;


//TODO: DOC
public interface RMIClientToServerActions extends Remote {
    /**
     * Retrieves the list of available games.
     * This method is used when a client wants to see all the games that are currently available to join.
     */
    void getGames(RMIServerToClientActions stub) throws RemoteException;

    /**
     * Creates a new game with the given game name and number of players.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player.
     * @param nPlayers   the number of players in the game.
     */
    void createGame(RMIServerToClientActions stub, String gameName, String playerName, int nPlayers) throws RemoteException;
    //TODO: the adapter here will save the stub into the map in which we'll have both TCP and RMI connection mapping
    //TODO: the adapter will uniform to a ServerMessageHandler the RMI connections' stubs


    /**
     * Leaves the game.
     *
     * @param gameName the name of the game.
     */
    void deleteGame(RMIServerToClientActions stub, String gameName, String playerName) throws RemoteException;

    /**
     * Joins the game with the given player name.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is joining the game.
     */
    void joinGame(RMIServerToClientActions stub, String gameName, String playerName) throws RemoteException;
    //TODO: the adapter here will save the stub into the map in which we'll have both TCP and RMI connection mapping
    //TODO: the adapter will uniform to a ServerMessageHandler the RMI connections' stubs

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

    //TODO: This method is responsible to send a chatMessageClientToServerMessage to the server, convert it to a chatMessageServerToClientMessage and send it to ALL the clients excluding the sender
    void chatMessageSender(ClientToServerMessage message) throws RemoteException;
}
