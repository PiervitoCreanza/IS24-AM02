package it.polimi.ingsw.network.server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TestClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            socket.setSoTimeout(5000); // Set timeout to 5000 milliseconds
            System.out.println("Connected to the server");
            // Handle server communication here
            while (true) {
                socket.getInputStream().read();
                System.out.println("Received message from server");
            }
        } catch (SocketTimeoutException ex) {
            System.out.println("Connection timed out: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Client exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}