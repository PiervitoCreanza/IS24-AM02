package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.MainController;

import java.io.IOException;
import java.net.ServerSocket;

public class TCPServer {
    public static void main(String[] args) {
        //Creating MainController
        MainController mainController = new MainController();
        NetworkMessageDispatcher networkMessageDispatcher = new NetworkMessageDispatcher(mainController);
        int portNumber = 12345;


        if (args.length == 1) {
            System.err.println("Usage: java TCPServer <port number>");
            portNumber = Integer.parseInt(args[0]);
        }

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Echo Server is listening on port " + portNumber);

            // Server runs indefinitely, accepting and handling each client connection in a separate thread.
            while (true) {
                TCPClientConnectionHandler h = new TCPClientConnectionHandler(serverSocket.accept(), networkMessageDispatcher);
                h.start();

            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}