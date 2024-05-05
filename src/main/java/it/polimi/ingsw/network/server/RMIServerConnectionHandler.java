package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.RMIServerActions;
import it.polimi.ingsw.network.server.message.successMessage.UpdateViewServerMessage;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class RMIServerConnectionHandler implements RMIClientActions {

    private final RMIServerAdapter serverAdapter = null;
    private final MainController mainController = new MainController();

    public void instanceRMIServerConnectionHandler(int portNumber, NetworkCommandMapper networkCommandMapper) throws RemoteException {

        // Create a new instance of RMIServerConnectionHandler
        RMIServerConnectionHandler rmiClientConnectionHandler = new RMIServerConnectionHandler();
        // Stub for the remote methods that a ClientAsAClient can call on the Server
        RMIClientActions stub = null;
        try {
            // Make stub available to calls from the RMI Client
            // Cast the exported object to the ClientActions interface
            //rmiClientConnectionHandler contains the effective implementation of the methods that the client can call
            //stub is the object that the client will use to call the methods
            stub = (RMIClientActions) UnicastRemoteObject.exportObject(rmiClientConnectionHandler, portNumber);
        } catch (RemoteException e) {
            // Print the stack trace for debugging purposes if a RemoteException occurs
            e.printStackTrace();
        }

        // Bind the remote object's stub in the registry
        Registry registry = null;
        try {
            //Create the registry on the specified port
            registry = LocateRegistry.createRegistry(portNumber);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            //Bind the stub to the registry
            registry.bind("ClientActions", stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
        System.err.println("Server ready");
    }



    private RMIServerActions createClientStub(String ip, int port) {
        Registry registry = null;
        RMIServerActions stub;
        try {
            registry = LocateRegistry.getRegistry(ip, port);
            stub = (RMIServerActions) registry.lookup("ServerActions");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return stub;
    }

    /**
     * Retrieves the list of available games.
     * This method is used when a client wants to see all the games that are currently available to join.
     *
     */
    @Override
    public void getGames(RMIServerActions stub) throws RemoteException {
        new Thread(() -> {
            System.err.println("È arrivata una chiamata remota getGames() dal client!");
            mainController.createGame("PartitonaRMI", "IngConti", 4);
            try {
                stub.receiveMessage(new UpdateViewServerMessage(mainController.getVirtualView("PartitonaRMI")));
            } catch (RemoteException e) {
                throw new RuntimeException(e);

            }
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
    public void createGame(RMIClientActions stub, String gameName, String playerName, int nPlayers) throws RemoteException {

    }

    /**
     * Leaves the game.
     *
     * @param gameName the name of the game.
     */
    @Override
    public void deleteGame(String gameName) throws RemoteException {

    }

    /**
     * Joins the game with the given player name.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is joining the game.
     */
    @Override
    public void joinGame(RMIClientActions stub, String gameName, String playerName) throws RemoteException {

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

    }

    /**
     * Draws a card from the resource deck.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     */
    @Override
    public void drawCardFromResourceDeck(String gameName, String playerName) throws RemoteException {

    }

    /**
     * Draws a card from the gold deck.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     */
    @Override
    public void drawCardFromGoldDeck(String gameName, String playerName) throws RemoteException {

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

    }

    /**
     * Retrieves the list of available games.
     * This method is used when a client wants to see all the games that are currently available to join.
     *
     * @param messageHandler the message handler to handle the response.
     */
    @Override
    public void getGames(ServerMessageHandler messageHandler) throws RemoteException {

    }

    /**
     * Creates a new game with the given game name and number of players.
     *
     * @param messageHandler the message handler to handle the response.
     * @param gameName       the name of the game.
     * @param playerName     the name of the player.
     * @param nPlayers       the number of players in the game.
     */
    @Override
    public void createGame(ServerMessageHandler messageHandler, String gameName, String playerName, int nPlayers) throws RemoteException {

    }

    /**
     * Leaves the game.
     *
     * @param messageHandler the message handler to handle the response.
     * @param gameName       the name of the game.
     */
    @Override
    public void deleteGame(ServerMessageHandler messageHandler, String gameName) throws RemoteException {

    }

    /**
     * Joins the game with the given player name.
     *
     * @param messageHandler the message handler to handle the response.
     * @param gameName       the name of the game.
     * @param playerName     the name of the player who is joining the game.
     */
    @Override
    public void joinGame(ServerMessageHandler messageHandler, String gameName, String playerName) throws RemoteException {

    }

    /**
     * Chooses the color for a player.
     *
     * @param messageHandler the message handler to handle the response.
     * @param gameName       the name of the game.
     * @param playerName     the name of the player who is choosing the color.
     * @param playerColor    the color to be chosen.
     */
    @Override
    public void choosePlayerColor(ServerMessageHandler messageHandler, String gameName, String playerName, PlayerColorEnum playerColor) throws RemoteException {

    }

    /**
     * Places a card on the game field.
     *
     * @param messageHandler the message handler to handle the response.
     * @param gameName       the name of the game.
     * @param playerName     the name of the player who is placing the card.
     * @param coordinate     the coordinate where the card should be placed.
     * @param card           the card to be placed.
     */
    @Override
    public void placeCard(ServerMessageHandler messageHandler, String gameName, String playerName, Coordinate coordinate, GameCard card) throws RemoteException {

    }

    /**
     * Draws a card from the game field.
     *
     * @param messageHandler the message handler to handle the response.
     * @param gameName       the name of the game.
     * @param playerName     the name of the player who is drawing the card.
     * @param card           the card to be drawn.
     */
    @Override
    public void drawCardFromField(ServerMessageHandler messageHandler, String gameName, String playerName, GameCard card) throws RemoteException {

    }

    /**
     * Draws a card from the resource deck.
     *
     * @param messageHandler the message handler to handle the response.
     * @param gameName       the name of the game.
     * @param playerName     the name of the player who is drawing the card.
     */
    @Override
    public void drawCardFromResourceDeck(ServerMessageHandler messageHandler, String gameName, String playerName) throws RemoteException {

    }

    /**
     * Draws a card from the gold deck.
     *
     * @param messageHandler the message handler to handle the response.
     * @param gameName       the name of the game.
     * @param playerName     the name of the player who is drawing the card.
     */
    @Override
    public void drawCardFromGoldDeck(ServerMessageHandler messageHandler, String gameName, String playerName) throws RemoteException {

    }

    /**
     * Switches the side of a card.
     *
     * @param messageHandler the message handler to handle the response.
     * @param gameName       the name of the game.
     * @param playerName     the name of the player who is switching the card side.
     * @param card           the card whose side is to be switched.
     */
    @Override
    public void switchCardSide(ServerMessageHandler messageHandler, String gameName, String playerName, GameCard card) throws RemoteException {

    }

    /**
     * Sets the objective for a player.
     *
     * @param messageHandler the message handler to handle the response.
     * @param gameName       the name of the game.
     * @param playerName     the name of the player whose objective is to be set.
     * @param card           the objective card to be set for the player.
     */
    @Override
    public void setPlayerObjective(ServerMessageHandler messageHandler, String gameName, String playerName, ObjectiveCard card) throws RemoteException {

    }
}
