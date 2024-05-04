package it.polimi.ingsw.network.TCP_utils;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is responsible for handling the connection with a client over TCP.
 * It extends Thread, meaning it can run concurrently with other threads.
 */
public class TCPConnectionHandler extends Thread implements Observable<String> {
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
     * Queue of received messages.
     */
    private final LinkedBlockingQueue<String> receivedMessages = new LinkedBlockingQueue<>();

    /**
     * Constructor for the TCPConnectionHandler class.
     *
     * @param socket The socket connected to the client.
     */
    public TCPConnectionHandler(Socket socket) {
        super("TCPConnectionHandler");
        this.socket = socket;

        try {
            // Initialize PrintWriter and BufferedReader for reading from and writing to the socket
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.isConnected = true;
            // Set the timeout to 4 seconds
            this.socket.setSoTimeout(4000);

            notifyReceivedMessages();
        } catch (IOException e) {
            // Handle any IOExceptions that might occur
        }
    }


    /**
     * The main method that will be run when the thread starts.
     * It reads from the socket and echoes back the input line until null (client disconnects).
     */
    public void run() {
        String json;

        // Read from socket and echo back the input line until null (client disconnects).
        while (isConnected) { //while for next JSON
            json = keepReadingJSON(in);
            if (!json.isEmpty()) {
                // The queue accepts at least Integer.MAX_VALUE messages. If the queue is full, the message will be dropped.
                receivedMessages.offer(json);
            }
        }
        closeConnection();
    }

    /**
     * Method to avoid TCP fragmentation.
     *
     * @param in The BufferedReader to read from
     * @return The complete JSON string
     */
    private String keepReadingJSON(BufferedReader in) {
        StringBuilder allJSON = new StringBuilder();

        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null && !isJson(allJSON.toString())) {
                if (!"pong".equals(inputLine)) {
                    allJSON.append(inputLine);
                }
            }
        } catch (Exception e) {
            isConnected = false;
        }

        return allJSON.toString();
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

    /**
     * Method to notify the observers of received messages.
     */
    private void notifyReceivedMessages() {
        // We perform this action in a thread to make it non-blocking and so keep receiving messages.
        // The method will notify each message synchronously.
        new Thread(() -> {
            while (isConnected) {
                String message = receivedMessages.poll();
                if (message != null) {
                    synchronized (observers) {
                        observers.forEach(observer -> observer.notify(message));
                    }

                }

            }
        }).start();

    }

    /**
     * Adds an observer to the observable.
     *
     * @param observer the observer to be added
     */
    @Override
    public void addObserver(Observer<String> observer) {
        synchronized (observers) {
            observers.add(observer);
        }

    }

    /**
     * Removes an observer from the observable.
     *
     * @param observer the observer to be removed
     */
    @Override
    public void removeObserver(Observer<String> observer) {
        synchronized (observers) {
            observers.remove(observer);
        }

    }

    /**
     * Sends a message to the client.
     *
     * @param message The message to be sent.
     */
    public void sendMessage(String message) {
        try {
            out.println(message);
            out.flush();
        } catch (Exception e) {
            isConnected = false;
        }
    }


    /**
     * Closes the connection with the client.
     */
    public void closeConnection() {
        try {
            socket.close();
            isConnected = false;
        } catch (IOException e) {
            // Handle any IOExceptions that might occur
        }
    }
}
