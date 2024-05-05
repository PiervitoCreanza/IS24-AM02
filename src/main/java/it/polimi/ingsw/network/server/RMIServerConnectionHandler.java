package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.client.ClientMessageHandler;
import it.polimi.ingsw.network.client.RMIServerActions;
import it.polimi.ingsw.network.client.message.ClientMessage;
import it.polimi.ingsw.network.server.message.ServerMessage;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


//todo: 1) receive the messages from the client correctly
public class RMIServerConnectionHandler implements RMIClientActions {
    static int RMIport = 1777;
    private final RMIServerAdapter serverAdapter = null;
    private final MainController mainController = new MainController();

    public static void createRegistry() throws RemoteException {
        //TODO: move the RMIServer Main from here?

        // Create a new instance of RMIServerConnectionHandler
        RMIServerConnectionHandler rmiClientConnectionHandler = new RMIServerConnectionHandler();
        RMIClientActions stub = null;
        try {
            // Export the remote object to make it available to receive incoming calls
            // Cast the exported object to the ClientActions interface
            stub = (RMIClientActions) UnicastRemoteObject.exportObject(rmiClientConnectionHandler, RMIport);
        } catch (RemoteException e) {
            // Print the stack trace for debugging purposes if a RemoteException occurs
            e.printStackTrace();
        }

        // Bind the remote object's stub in the registry
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(RMIport);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            registry.bind("ClientActions", stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
        System.err.println("Server ready"); //add a bit of color to the CLI ;)

        //TODO: if a controller method throws an exception, how do we send it back to the client in RMI?
        //All exceptions are catched by the NetworkCommandMapper and sent back to the client using sendMessage invocatio

    }

    /**
     * Retrieves the list of available games.
     * This method is used when a client wants to see all the games that are currently available to join.
     *
     * @param ip
     * @param port
     */
    @Override
    public void getGames(String ip, int port) throws RemoteException {
        new Thread(() -> {
            System.err.println("È arrivata una chiamata remota getGames() dal client!");
            RMIServerActions stub = createClientStub(ip, port);
            mainController.createGame("PartitonaRMI", "IngConti", 4);
            try {
                stub.receiveMessage(new UpdateViewServerMessage(mainController.getVirtualView("PartitonaRMI")));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
                //TODO: java.rmi.MarshalException: error marshalling arguments; nested exception is:
                //	java.io.NotSerializableException: it.polimi.ingsw.network.server.message.ErrorServerMessage

            }
        }).start();
        //è arrivato un messaggio getGames
        //chiamo un metodo del server che mi manda indietro la lista delle partite
    }


    /**
     * Creates a new game with the given game name and number of players.
     *
     * @param ip
     * @param port
     * @param gameName   the name of the game.
     * @param playerName the name of the player.
     * @param nPlayers   the number of players in the game.
     */
    @Override
    public void createGame(String ip, int port, String gameName, String playerName, int nPlayers) throws RemoteException {

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
     * @param ip
     * @param port
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is joining the game.
     */
    @Override
    public void joinGame(String ip, int port, String gameName, String playerName) throws RemoteException {

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
