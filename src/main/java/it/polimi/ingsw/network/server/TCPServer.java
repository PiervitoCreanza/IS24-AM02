package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.network.server.TCP.TCPServerAdapter;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * This class is responsible for setting up and running the TCP server.
 * It listens for incoming client connections and handles each one in a separate thread.
 */
public class TCPServer {
    /**
     * The entry point of the application.
     *
     * @param args the input arguments. If a single argument is provided, it is used as the port number for the server.
     */
    public static void main(String[] args) {
        // Create a new MainController instance. This will be responsible for controlling the main logic of the application.
        MainController mainController = new MainController();

        // Create a new NetworkCommandMapper instance. This will be responsible for mapping network commands.
        NetworkCommandMapper networkCommandMapper = new NetworkCommandMapper(mainController);

        // Set the default port number to 12345.
        int portNumber = 12345;

        // If a port number is passed as a command line argument, override the default port number.
        if (args.length == 1) {
            System.err.println("Usage: java TCPServer <port number>");
            portNumber = Integer.parseInt(args[0]);
        }

        // Try to create a new ServerSocket that listens on the specified port number.
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Codex Naturalis Server is listening on port " + portNumber);

            // The server runs indefinitely, accepting and handling each client connection in a separate thread.
            while (true) {
                // Accept a new client connection. This method blocks until a connection is made.
                // Once a connection is made, a new TCPConnectionHandler is created to handle the connection.
                // The handler is then started, which causes its run method to be called in a separate thread.
                TCPServerAdapter messageHandler = new TCPServerAdapter(serverSocket.accept(), networkCommandMapper);
            }
        } catch (IOException e) {
            // If an IOException occurs (such as if the server fails to bind to the specified port), print an error message and exit the program.
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}