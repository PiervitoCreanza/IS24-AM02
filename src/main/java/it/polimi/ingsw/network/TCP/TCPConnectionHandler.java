package it.polimi.ingsw.network.TCP;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

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
     * BufferedWriter object used to write to the socket.
     */
    private final BufferedWriter out;

    /**
     * BufferedReader object used to read from the socket.
     */
    private final BufferedReader in;

    /**
     * Boolean value indicating whether the connection is active.
     */
    private final AtomicBoolean isConnected;

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
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.isConnected = new AtomicBoolean(true);

            // Set the timeout to 5 seconds. If no message is received in this time, the socket will throw a SocketTimeoutException.
            // This is useful to detect when the client disconnects.
            //TODO this.socket.setSoTimeout(5000);

            // Start the thread that executes received messages
            notifyReceivedMessages();

            heartbeat();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void heartbeat() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!isConnected.get()) {
                    cancel();
                    return;
                }

                try {
                    send("heartbeat");
                } catch (IOException e) {
                    System.out.println("Client disconnected - detected when pinging");
                    closeConnection();
                    cancel();
                }
            }
        }, 0, 2500);
    }


    /**
     * The main method that will be run when the thread starts.
     * It reads from the socket and echoes back the input line until null (client disconnects).
     */
    public void run() {

        // Read from socket and echo back the input line until null (client disconnects).
        while (this.isConnected.get()) { //while for next JSON
            try {
                String receivedMessage = keepReadingJSON(in);
                if (!receivedMessage.isEmpty()) {
                    receivedMessages.offer(receivedMessage);
                }
            } catch (SocketTimeoutException e) {
                System.out.println("Client disconnected - detected by timeout");
                closeConnection();
            } catch (IOException e) {
                System.out.println("Error reading from socket");
                closeConnection();
            }
        }
    }

    /**
     * Method to avoid TCP fragmentation.
     *
     * @param in The BufferedReader to read from
     * @return The complete JSON string
     */
    private String keepReadingJSON(BufferedReader in) throws IOException {
        StringBuilder allJSON = new StringBuilder();

        String inputLine;
        // While the input buffer is not empty, read the input line.
        while ((inputLine = in.readLine()) != null) {
            // Discard the heartbeat messages.
            if (!"heartbeat".equals(inputLine)) {
                allJSON.append(inputLine);
            }

            // If the JSON string is complete, break the loop.
            if (isJson(allJSON.toString())) {
                break;
            }
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
     * It is synchronized on the observers to prevent race-conditions.
     */
    private void notifyReceivedMessages() {
        // We perform this action in a thread to make it non-blocking and so keep receiving messages.
        // The method will notify each message synchronously.
        new Thread(() -> {
            while (this.isConnected.get()) {
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
     * It is synchronized on the observers to prevent race-conditions.
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
     * It is synchronized on the observers to prevent race-conditions.
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
     * It is synchronized to prevent closing a connection while sending a message.
     *
     * @param message The message to be sent.
     * @throws IOException If an I/O error occurs.
     */
    public synchronized void send(String message) throws IOException {
        out.write(message);
        out.newLine();
        out.flush();
    }


    /**
     * Closes the connection with the client.
     * It is synchronized to prevent closing a connection while sending a message.
     */
    public synchronized void closeConnection() {
        if (!this.socket.isClosed()) {
            try {
                this.isConnected.set(false);
                this.receivedMessages.clear();
                this.receivedMessages.offer("CONNECTION_CLOSED");
                socket.close();
            } catch (IOException e) {
                // TODO: Handle any IOExceptions that might occur
            }
        }

    }
}

