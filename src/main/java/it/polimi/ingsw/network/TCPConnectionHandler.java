package it.polimi.ingsw.network;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is responsible for handling the connection with a client over TCP.
 * It extends Thread, meaning it can run concurrently with other threads.
 */
public class TCPConnectionHandler extends Thread {
    /**
     * Socket object representing the connection to the client.
     */
    private final Socket socket;

    /**
     * Listeners that will be notified when a message is received.
     */
    private final PropertyChangeSupport listeners;

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
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(TCPConnectionHandler.class);

    /**
     * Constructor for the TCPConnectionHandler class.
     *
     * @param socket The socket connected to the client.
     */
    public TCPConnectionHandler(Socket socket) {
        super("TCPConnectionHandler");
        this.socket = socket;
        this.listeners = new PropertyChangeSupport(this);
        try {
            // Initialize PrintWriter and BufferedReader for reading from and writing to the socket
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.isConnected = new AtomicBoolean(true);

            // Set the timeout to 5 seconds. If no message is received in this time, the socket will throw a SocketTimeoutException.
            // This is useful to detect when the client disconnects.
            this.socket.setSoTimeout(5000);
            logger.debug("TCP connection established with {}:{}", socket.getInetAddress(), socket.getPort());

            // Start the thread that executes received messages
            notifyReceivedMessages();

            heartbeat();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a PropertyChangeListener to the listeners list.
     * The listener will be notified of property changes.
     *
     * @param listener The PropertyChangeListener to be added.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener from the listeners list.
     * The listener will no longer be notified of property changes.
     *
     * @param listener The PropertyChangeListener to be removed.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.removePropertyChangeListener(listener);
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
                    logger.warn("TCP disconnected - detected when pinging");
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
                logger.warn("TCP disconnected - detected by timeout");
                closeConnection();
            } catch (IOException e) {
                logger.warn("TCP disconnected - detected while reading from socket");
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
                    listeners.firePropertyChange("MESSAGE_RECEIVED", null, message);
                }

            }
        }).start();

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
                // TODO: Improve code
                this.isConnected.set(false);
                this.receivedMessages.clear();
                listeners.firePropertyChange("CONNECTION_CLOSED", null, null);
                socket.close();
            } catch (IOException ignored) {
                // This exception is ignored because we are closing the connection.
            }
        }
    }
}

