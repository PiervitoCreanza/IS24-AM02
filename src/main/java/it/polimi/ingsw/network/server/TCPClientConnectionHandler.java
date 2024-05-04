package it.polimi.ingsw.network.server;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.TCP.Observer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;

/**
 * This class is responsible for handling the connection with a client over TCP.
 * It extends Thread, meaning it can run concurrently with other threads.
 */
public class TCPClientConnectionHandler extends Thread {
    /**
     * Socket object representing the connection to the client.
     */
    private final Socket socket;

    /**
     * Set of observers that will be notified when a message is received.
     */
    private final HashSet<Observer<String>> observers = new HashSet<>();

    /**
     * PrintWriter object used to write to the socket.
     */
    private PrintWriter out;

    /**
     * BufferedReader object used to read from the socket.
     */
    private BufferedReader in;

    /**
     * Boolean value indicating whether the connection is active.
     */
    private boolean isConnected = false;

    /**
     * Constructor for the TCPConnectionHandler class.
     *
     * @param socket The socket connected to the client.
     */
    public TCPClientConnectionHandler(Socket socket, boolean activatePinging) {
        super("TCPConnectionHandler");
        this.socket = socket;

        try {
            // Initialize PrintWriter and BufferedReader for reading from and writing to the socket
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.isConnected = true;

            // Set the timeout to 4 seconds. If no message is received in this time, the socket will throw a SocketTimeoutException.
            // This is useful to detect when the client disconnects.
            this.socket.setSoTimeout(5000);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The main method that will be run when the thread starts.
     * It reads from the socket and echoes back the input line until null (client disconnects).
     */
    public void run() {
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
            //ChosenCardMessage parsedMessage = chosenCardMessageFromJson(inputLine);
            System.out.println(inputLine);
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
                System.out.println("Error reading from socket");
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
}