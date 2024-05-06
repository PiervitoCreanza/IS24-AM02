package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.client.message.mainController.CreateGameClientMessage;
import it.polimi.ingsw.network.client.message.mainController.GetGamesClientMessage;
import it.polimi.ingsw.network.client.message.mainController.JoinGameClientMessage;

import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        System.out.println("Hello, this is the client!");
        // Default values for IP address and port number
        String ipAddress = "localhost";
        int portNumber = 12345;

        // Check command line arguments
        if (args.length > 2) {
            System.err.println("Usage: java TCPServer [<port number>] [<IP address>]");
            return;
        }

        // If a port number is provided as the first argument
        if (args.length >= 1) {
            try {
                portNumber = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Error: Invalid port number " + args[0]);
                return;
            }
        }

        // If an IP address is provided as the second argument
        if (args.length == 2) {
            ipAddress = args[1];
        }




        try {

            Socket serverSocket = new Socket(ipAddress, portNumber);
            // Print configuration
            System.err.println("Starting client  with connection to server at " + ipAddress + " on port " + portNumber);

            ClientCommandMapper clientCommandMapper = new ClientCommandMapper();
            TCPClientAdapter clientAdapter = new TCPClientAdapter(serverSocket, clientCommandMapper);
            clientCommandMapper.setMessageHandler(clientAdapter);
            //Request the list of games
            clientAdapter.sendMessage(new GetGamesClientMessage());
            //Trying to join a non-existing game
            clientAdapter.sendMessage(new JoinGameClientMessage("game_inesistente", "Marco"));
            //Trying to create a game with a wrong maxPlayers number
            clientAdapter.sendMessage(new CreateGameClientMessage("pippo", 6, "Marco"));
            // Create a valid game
            clientAdapter.sendMessage(new CreateGameClientMessage("pippo", 3, "Simone"));
            //Request the list of active games again
            clientAdapter.sendMessage(new GetGamesClientMessage());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
