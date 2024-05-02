package it.polimi.ingsw.network.server.TCP;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.server.Connection;
import it.polimi.ingsw.network.server.NetworkCommandMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

/**
 * This class is responsible for handling the connection with a client over TCP.
 * It extends Thread, meaning it can run concurrently with other threads.
 */
public class TCPClientConnectionHandler extends Thread implements TCPObservable, Connection {
    /**
     * Socket object representing the connection to the client.
     */
    private final Socket socket;
    /**
     * NetworkCommandMapper object used to map network commands.
     */
    private final NetworkCommandMapper networkCommandMapper;

    private final HashSet<TCPObserver> observers = new HashSet<>();

    /**
     * Constructor for the TCPClientConnectionHandler class.
     *
     * @param socket               The socket connected to the client.
     * @param networkCommandMapper The object used to map network commands.
     */
    public TCPClientConnectionHandler(Socket socket, NetworkCommandMapper networkCommandMapper) {
        super("TCPClientConnectionHandler");
        this.socket = socket;
        this.networkCommandMapper = networkCommandMapper;
    }

    /**
     * The main method that will be run when the thread starts.
     * It reads from the socket and echoes back the input line until null (client disconnects).
     */
    public void run() {
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            // Initialize PrintWriter and BufferedReader for reading from and writing to the socket
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            // Handle any IOExceptions that might occur
        }
        String inputLine;

        // Read from socket and echo back the input line until null (client disconnects).
        while (true) { //while for next JSON
            inputLine = keepReadingJSON(in);
            if (inputLine == null || (inputLine.isEmpty())) {
                break;
            }
            //System.out.println(inputLine); // Print JSON to console.
            //out.println(inputLine); //Print to remote
            //out.println(); //Print to remote

            //Gson stops parsing when encounters a newline character
            notifyObservers(inputLine);
            // TODO We need to add a method to send messages
            //String statusMessage = this.networkCommandMapper.parse(inputLine);
            //System.out.println(statusMessage);
            //out.println(statusMessage); //Print to remote
        }

        // Close the socket when done
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to avoid TCP fragmentation.
     *
     * @param in The BufferedReader to read from
     * @return The complete JSON string
     */
    private String keepReadingJSON(BufferedReader in) {
        String inputLine = null;
        String allJSON = "";

        while (true) {
            try {
                inputLine = in.readLine();
                if (inputLine == null) {
                    break;
                }
                allJSON += inputLine;

                if (isJson(allJSON)) {
                    break;
                }
            } catch (IOException e) {
                return null;
            }
        }

        return allJSON;
    }

    /**
     * Checks if a string is a valid JSON string.
     *
     * @param json The string to check.
     * @return true if the string is a valid JSON string, false otherwise.
     */
    private static boolean isJson(String json) {
        try {
            JsonParser.parseString(json);
        } catch (JsonSyntaxException e) {
            return false;
        }
        return true;
    }

    @Override
    public void addObserver(TCPObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(TCPObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String message) {
        for (TCPObserver observer : new ArrayList<>(observers)) {
            observer.notify(this, message);
        }
    }


}
