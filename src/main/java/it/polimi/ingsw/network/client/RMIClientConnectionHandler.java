package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.client.message.ClientMessage;
import it.polimi.ingsw.network.server.message.ServerMessage;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class RMIClientConnectionHandler implements ClientMessageHandler, RMIServerActions {
    static int RMIport = 1099;

    protected RMIClientConnectionHandler() throws RemoteException {
        super();
    }

    @Override
    public void receiveMessage(ServerMessage message) {

    }

    @Override
    public void sendMessage(ClientMessage message) throws RemoteException {
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
        RMIServerActions stub = null;

        // If a port number is passed as a command line argument, override the default port number.
        if (args.length == 1) {
            System.err.println("Usage: java RMIServer <port number>");
            RMIport = Integer.parseInt(args[0]);
        }

        try {
            // Export the remote object to make it available to receive incoming calls
            // Cast the exported object to the ClientActions interface
            stub = (RMIServerActions) UnicastRemoteObject.exportObject(rmiClientConnectionHandler, RMIport);
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
}


