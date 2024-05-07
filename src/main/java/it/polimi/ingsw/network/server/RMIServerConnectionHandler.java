package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.TCP.Observer;
import it.polimi.ingsw.network.client.ServerActions;

import java.rmi.RemoteException;

public class RMIServerConnectionHandler implements RMIClientActions, Observer<ServerMessageHandler> {

    private final NetworkCommandMapper networkCommandMapper;

    public RMIServerConnectionHandler(NetworkCommandMapper networkCommandMapper) {
        this.networkCommandMapper = networkCommandMapper;
    }

    @Override
    public void notify(ServerMessageHandler handler) {
        new Thread(() -> {
            networkCommandMapper.handleDisconnection(handler);
        }).start();
    }

    /**
     * Retrieves the list of available games.
     * This method is used when a client wants to see all the games that are currently available to join.
     *
     */
    @Override
    public void getGames(ServerActions stub) throws RemoteException {
        new Thread(() -> {
            //Instance new RMIAdapter(stub)
            networkCommandMapper.getGames(istanceRMIServerAdapter(stub));
        }).start();
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
    public void createGame(ServerActions stub, String gameName, String playerName, int nPlayers) throws RemoteException {
        new Thread(() -> {
            networkCommandMapper.createGame(istanceRMIServerAdapter(stub), gameName, playerName, nPlayers);
        }).start();
    }

    /**
     * Leaves the game.
     *
     * @param gameName the name of the game.
     */
    @Override
    public void deleteGame(ServerActions stub, String gameName, String playerName) throws RemoteException {
        new Thread(() -> {
            networkCommandMapper.deleteGame(istanceRMIServerAdapter(stub), gameName, playerName);
        }).start();
    }

    /**
     * Joins the game with the given player name.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is joining the game.
     */
    @Override
    public void joinGame(ServerActions stub, String gameName, String playerName) throws RemoteException {
        new Thread(() -> {
            networkCommandMapper.joinGame(istanceRMIServerAdapter(stub), gameName, playerName);
        }).start();
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
        new Thread(() -> {
            networkCommandMapper.choosePlayerColor(gameName, playerName, playerColor);
        }).start();
    }

    /**
     * Sets the objective for a player.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player whose objective is to be set.
     * @param card       the objective card to be set for the player.
     */
    @Override
    public void setPlayerObjective(String gameName, String playerName, ObjectiveCard card) throws RemoteException {
        new Thread(() -> {
            networkCommandMapper.setPlayerObjective(gameName, playerName, card);
        }).start();
    }

    /**
     * Places a card on the game field.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is placing the card.
     * @param coordinate the coordinate where the card should be placed.
     * @param card       the card to be placed.
     */
    @Override
    public void placeCard(String gameName, String playerName, Coordinate coordinate, GameCard card) throws RemoteException {
        new Thread(() -> {
            networkCommandMapper.placeCard(gameName, playerName, coordinate, card);
        }).start();
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
        new Thread(() -> {
            networkCommandMapper.drawCardFromField(gameName, playerName, card);
        }).start();
    }

    /**
     * Draws a card from the resource deck.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     */
    @Override
    public void drawCardFromResourceDeck(String gameName, String playerName) throws RemoteException {
        new Thread(() -> {
            networkCommandMapper.drawCardFromResourceDeck(gameName, playerName);
        }).start();
    }

    /**
     * Draws a card from the gold deck.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     */
    @Override
    public void drawCardFromGoldDeck(String gameName, String playerName) throws RemoteException {
        new Thread(() -> {
            networkCommandMapper.drawCardFromGoldDeck(gameName, playerName);
        }).start();
    }

    /**
     * Switches the side of a card.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is switching the card side.
     * @param card       the card whose side is to be switched.
     */
    @Override
    public void switchCardSide(String gameName, String playerName, GameCard card) throws RemoteException {
        new Thread(() -> {
            networkCommandMapper.switchCardSide(gameName, playerName, card);
        }).start();
    }

    private RMIServerAdapter istanceRMIServerAdapter(ServerActions stub) {
        RMIServerAdapter rmiServerAdapter = new RMIServerAdapter(stub);
        rmiServerAdapter.addObserver(this);
        return rmiServerAdapter;
    }
}
