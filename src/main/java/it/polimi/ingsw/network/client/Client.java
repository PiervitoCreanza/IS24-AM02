package it.polimi.ingsw.network.client;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.network.TCP.Observer;
import it.polimi.ingsw.network.TCP.TCPConnectionHandler;

import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        System.out.println("Hello, this is the client!");
        try {
            final TCPConnectionHandler client = new TCPConnectionHandler(new Socket("localhost", 12345));
            Observer<String> observer = new Observer<String>() {
                @Override
                public void notify(String message) {
                    System.out.println("Received message: " + message);
                }
            };

            client.addObserver(observer);
            client.start();
            Parser p = new Parser();
            client.send(p.serializeToJson(p.getGoldDeck()));
            client.send(p.serializeToJson(p.getStarterDeck()));
            client.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
