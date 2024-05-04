package it.polimi.ingsw.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server is listening on port 5000");
            while (true) {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(5000); // Set timeout to 5000 milliseconds
                TCPClientConnectionHandler clientHandler = new TCPClientConnectionHandler(socket, false);
                clientHandler.start();
                System.out.println("New client connected");
                // Handle new client in a new thread
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}