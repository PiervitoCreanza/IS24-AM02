package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.client.message.mainController.CreateGameClientMessage;
import it.polimi.ingsw.network.client.message.mainController.GetGamesClientMessage;
import it.polimi.ingsw.network.server.RMIClientActions;
import org.apache.commons.cli.*;

import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Client {
    public static void main(String[] args) {
        String connectionType = null;
        String ipAddress = null;
        int portNumber = 0;
        // create Options object
        Options options = new Options();

        // add options
        options.addOption("c", true, "Connection type (TCP or RMI). This is mandatory.");
        options.addOption("ip", true, "IP address (default is localhost).");
        options.addOption("p", true, "Port number (default is 12345).");

        CommandLineParser parser = new DefaultParser();

        try {
            // parse the command line arguments
            CommandLine cmd = parser.parse(options, args);

            // check mandatory options
            if (!cmd.hasOption("c")) {
                System.err.println("Please specify the connection type (TCP or RMI) using the -c option.");
                return;
            }

            // get values from options
            connectionType = cmd.getOptionValue("c");
            ipAddress = cmd.getOptionValue("ip", "localhost"); // default is localhost
            portNumber = Integer.parseInt(cmd.getOptionValue("p", "12345")); // default is 12345

        } catch (ParseException e) {
            System.err.println("Parsing failed. Reason: " + e.getMessage());
            return;
        }

        switch (connectionType.toLowerCase()) {
            case "tcp" -> {
                System.out.println("Hai avviato una connessione TCP con IP: " + ipAddress + " e porta: " + portNumber);
                startTCPClient(ipAddress, portNumber);
            }
            case "rmi" -> {
                System.out.println("Hai avviato una connessione RMI con IP: " + ipAddress + " e porta: " + portNumber);
                startRMIClient(ipAddress, portNumber);
            }
            default -> {
                System.err.println("Invalid connection type. Please specify either TCP or RMI.");
            }
        }
    }
    private static void startTCPClient(String ipAddress, int portNumber) {
        try {
            System.out.println("Hello, this is the client!");
            Socket serverSocket = new Socket(ipAddress, portNumber);
            // Print configuration
            System.err.println("Starting client  with connection to server at " + ipAddress + " on port " + portNumber);

            ClientCommandMapper clientCommandMapper = new ClientCommandMapper();
            TCPClientAdapter clientAdapter = new TCPClientAdapter(serverSocket, clientCommandMapper);


            clientCommandMapper.setMessageHandler(clientAdapter);
            //Request the list of games
            clientAdapter.sendMessage(new GetGamesClientMessage());
            //Trying to join a non-existing game
            //clientAdapter.sendMessage(new JoinGameClientMessage("game_inesistente", "Marco"));
            //Trying to create a game with a wrong maxPlayers number
            //clientAdapter.sendMessage(new CreateGameClientMessage("pippo", 6, "Marco"));
            // Create a valid game
            clientAdapter.sendMessage(new CreateGameClientMessage("pippo", 3, "Simone"));
            //Request the list of active games again
            clientAdapter.sendMessage(new GetGamesClientMessage());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startRMIClient(String ipAddress, int portNumber) {
        // Getting the registry
        try {
            //Client as a client, getting the registry
            Registry registry = LocateRegistry.getRegistry(ipAddress, portNumber);
            // Looking up the registry for the remote object
            RMIClientActions stub = (RMIClientActions) registry.lookup("ClientActions");
            RMIClientAsAClient clientMessageSender = new RMIClientAsAClient(stub);
            //Client as a server
            RMIClientAsAServer messageHandler = new RMIClientAsAServer();

            //Test
            clientMessageSender.sendMessage(messageHandler, new GetGamesClientMessage());

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void receive(String message) throws RemoteException {
        System.out.println(message);
    }


}

