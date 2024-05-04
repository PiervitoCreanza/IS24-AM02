package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.server.message.ServerMessage;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class RMIClientConnectionHandler implements ServerMessageHandler, ClientActions {
    static int RMIport = 1099;

    protected RMIClientConnectionHandler() throws RemoteException {
        super();
    }

    @Override
    public void getGames() throws RemoteException {
        //TODO: implement method
    }

    @Override
    public void createGame(ServerMessageHandler messageHandler, String gameName, int nPlayers) throws RemoteException {
        //TODO: implement method
    }

    @Override
    public void deleteGame(ServerMessageHandler messageHandler, String gameName) throws RemoteException {
        //TODO: implement method
    }

    @Override
    public void joinGame(ServerMessageHandler messageHandler, String gameName, String playerName) throws RemoteException {
        //TODO: implement method
    }

    @Override
    public void choosePlayerColor(ServerMessageHandler messageHandler, String gameName, String playerName, PlayerColorEnum playerColor) throws RemoteException {
        //TODO: implement method
    }

    @Override
    public void placeCard(ServerMessageHandler messageHandler, String gameName, String playerName, Coordinate coordinate, GameCard card) throws RemoteException {
        //TODO: implement method
    }

    @Override
    public void drawCardFromField(ServerMessageHandler messageHandler, String gameName, String playerName, GameCard card) throws RemoteException {
        //TODO: implement method
    }

    @Override
    public void drawCardFromResourceDeck(ServerMessageHandler messageHandler, String gameName, String playerName) throws RemoteException {
        //TODO: implement method
    }

    @Override
    public void drawCardFromGoldDeck(ServerMessageHandler messageHandler, String gameName, String playerName) throws RemoteException {
        //TODO: implement method
    }

    @Override
    public void switchCardSide(ServerMessageHandler messageHandler, String gameName, String playerName, GameCard card) throws RemoteException {
        //TODO: implement method
    }

    @Override
    public void setPlayerObjective(ServerMessageHandler messageHandler, String gameName, String playerName, ObjectiveCard card) throws RemoteException {
        //TODO: implement method
    }

    @Override
    public void sendMessage(ServerMessage message) throws RemoteException {
        //TODO: implement method
        //SERVER to CLIENT invocation

    }

    @Override
    public void closeConnection() throws RemoteException {
        //TODO: implement method
    }


    public static void main(String[] args) throws RemoteException {
        //TODO: move the RMIServer Main from here?

        // Create a new instance of RMIClientConnectionHandler
        RMIClientConnectionHandler rmiClientConnectionHandler = new RMIClientConnectionHandler();
        ClientActions stub = null;

        // If a port number is passed as a command line argument, override the default port number.
        if (args.length == 1) {
            System.err.println("Usage: java RMIServer <port number>");
            RMIport = Integer.parseInt(args[0]);
        }

        try {
            // Export the remote object to make it available to receive incoming calls
            // Cast the exported object to the ClientActions interface
            stub = (ClientActions) UnicastRemoteObject.exportObject(rmiClientConnectionHandler, RMIport);
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

    }
}


