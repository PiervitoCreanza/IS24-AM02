package it.polimi.ingsw.network;

import it.polimi.ingsw.utils.PropertyChangeNotifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is responsible for handling the connection with a client over TCP.
 * It extends Thread, meaning it can run concurrently with other threads.
 */
public class TCPConnectionHandler extends Thread implements PropertyChangeNotifier {
    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(TCPConnectionHandler.class);
    /**
     * Socket object representing the connection to the client.
     */
    private final Socket socket;
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
     * ExecutorService used to run tasks concurrently.
     */
    private final ExecutorService executor;
    /**
     * Listeners that will be notified when a message is received.
     */
    private final PropertyChangeSupport listeners;

    /**
     * Constructor for the TCPConnectionHandler class.
     *
     * @param socket The socket connected to the client.
     */
    public TCPConnectionHandler(Socket socket) {
        super("TCPConnectionHandler");
        this.socket = socket;
        this.listeners = new PropertyChangeSupport(this);
        this.executor = Executors.newSingleThreadExecutor();
        try {
            // Initialize PrintWriter and BufferedReader for reading from and writing to the socket
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.isConnected = new AtomicBoolean(true);

            // Set the timeout to 5 seconds. If no message is received in this time, the socket will throw a SocketTimeoutException.
            // This is useful to detect when the client disconnects.
            this.socket.setSoTimeout(5000);
            logger.debug("TCP connection established with {}:{}", socket.getInetAddress(), socket.getPort());
            heartbeat();
        } catch (IOException e) {
            logger.fatal("Error while getting streams from socket: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to send a heartbeat message to the client every 2.5 seconds.
     */
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
                String receivedMessage;
                if ((receivedMessage = in.readLine()) != null) {
                    if (!"heartbeat".equals(receivedMessage)) {
                        this.executor.submit(() -> this.listeners.firePropertyChange("MESSAGE_RECEIVED", null, receivedMessage));
                    }
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
     * Sends a message to the client.
     * It is synchronized to prevent closing a connection while sending a message.
     *
     * @param message The message to be sent.
     * @throws IOException If an I/O error occurs.
     */
    public synchronized void send(String message) throws IOException {
        this.out.write(message);
        this.out.newLine();
        this.out.flush();
    }

    /**
     * Closes the connection with the client.
     * It is synchronized to prevent closing a connection while sending a message.
     */
    public synchronized void closeConnection() {
        if (!this.socket.isClosed()) {
            try {
                this.isConnected.set(false);
                this.executor.shutdownNow();
                this.socket.close();
                this.listeners.firePropertyChange("CONNECTION_CLOSED", null, null);
            } catch (IOException ignored) {
                // This exception is ignored because we are closing the connection.
            }
        }
    }

    /**
     * Adds a PropertyChangeListener to the listener list.
     * The listener will be notified of property changes.
     *
     * @param listener The PropertyChangeListener to be added
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener from the listener list.
     * The listener will no longer be notified of property changes.
     *
     * @param listener The PropertyChangeListener to be removed
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.removePropertyChangeListener(listener);
    }
}

