package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.server.message.ServerMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

//TODO: here methods should be launched once the VirtualView is received from the server
//TODO: who physically takes the RemoteObject here?
//getGames, virtualView, errorMessage
//TODO: then CORRECT GUI updates will be called based on the received VirtualView
public class RMIClientAsAServer extends UnicastRemoteObject implements RMIServerActions {

    public RMIClientAsAServer() throws RemoteException {
    }

    @Override
    public void receiveMessage(ServerMessage message) {
        System.err.println("Ho ricevuto un messaggio dal Server! " + message);
        System.out.println(message.getView());
        System.out.println(message.getView().gameView().globalBoardView().fieldGoldCards().getFirst().getCurrentSide().getClass());
        System.out.println(message.getView().gameView().globalBoardView().fieldGoldCards().getLast().getCurrentSide().getClass());
        System.out.println(message.getView().gameView().globalBoardView().fieldResourceCards().getFirst().getCurrentSide().getClass());
        System.out.println(message.getView().gameView().globalBoardView().fieldResourceCards().getLast().getCurrentSide().getClass());
    }

    /*public void createRegistry() throws RemoteException {
        //TODO: move the RMIServer Main from here?

        // Create a new instance of RMIServerConnectionHandler
        RMIClientAsAServer RMI_S_to_C_ConnectionHandler = new RMIClientAsAServer(RMIport);
        RMIServerActions stub = null;
        try {
            // Export the remote object to make it available to receive incoming calls
            // Cast the exported object to the ClientActions interface
            stub = (RMIServerActions) UnicastRemoteObject.exportObject(RMI_S_to_C_ConnectionHandler, RMIport);
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
            registry.bind("ServerActions", stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
        System.err.println("CLIENT AS A Server ready"); //add a bit of color to the CLI ;)

        //TODO: if a controller method throws an exception, how do we send it back to the client in RMI?
        //All exceptions are catched by the NetworkCommandMapper and sent back to the client using sendMessage invocatio

    }*/

    /*
    public static void main(String[] args) throws RemoteException {
        //TODO: move the RMIServer Main from here?

        // Create a new instance of RMIClientAsAServer
        RMIClientAsAServer rmiClientAsAServer = new RMIClientAsAServer();
        RMIServerActions stub = null;

        // If a port number is passed as a command line argument, override the default port number.
        if (args.length == 1) {
            System.err.println("Usage: java RMIServer <port number>");
            RMIport = Integer.parseInt(args[0]);
        }

        try {
            // Export the remote object to make it available to receive incoming calls
            // Cast the exported object to the ClientActions interface
            stub = (RMIServerActions) UnicastRemoteObject.exportObject(rmiClientAsAServer, RMIport);
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
     */
}


