package it.polimi.ingsw.network.client.connection;

import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.client.TCP.TCPClientAdapter;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.IllegalBlockingModeException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * The TCPConnection class implements the Connection interface using the TCP protocol.
 * It also implements the PropertyChangeListener and PropertyChangeNotifier interfaces to provide property change support.
 * It is responsible for establishing a connection between the client and the server.
 */
public class TCPConnection extends Connection {

    /**
     * Constructor for the TCPConnection class.
     *
     * @param networkControllerMapper The ClientNetworkControllerMapper object that is used to map network controllers.
     * @param serverIp                The IP address of the server to connect to.
     * @param serverPort              The port number of the server to connect to.
     */
    public TCPConnection(ClientNetworkControllerMapper networkControllerMapper, String serverIp, int serverPort) {
        super(networkControllerMapper, serverIp, serverPort);
        logger = LogManager.getLogger(TCPConnection.class);
    }

    /**
     * Establishes a connection.
     * It tries to connect to the server and, if successful, creates a TCPClientAdapter
     * to handle the communication with the server.
     * If the connection fails, it retries up to 5 times with a 5-second delay between each attempt.
     * If it is not possible to connect after 5 attempts, the program will be closed.
     */
    @Override
    protected void connectionSetUp() {
        connectionTrying = new Timer();
        connectionTrying.schedule(new TimerTask() {
            @Override
            public void run() {
                String trying = "Trying to connect to TCP server...";
                logger.warn(trying);
                listeners.firePropertyChange("CONNECTION_TRYING", null, trying);
            }
        }, 500);
        while (attempts <= maxAttempts) {
            try {
                Socket serverSocket = new Socket();
                serverSocket.connect(new InetSocketAddress(serverIp, serverPort), 5000);
                // Pass the mapper to the TCPAdapter in order to be notified when messages arrive.
                TCPClientAdapter clientAdapter = new TCPClientAdapter(serverSocket, networkControllerMapper);
                clientAdapter.addPropertyChangeListener(this);
                // Pass the TCPAdapter to the mapper in order to send messages
                networkControllerMapper.setMessageHandler(clientAdapter);
                break;
            } catch (IOException e) {
                connectionTrying.cancel();
                String retry = "TCP server unreachable, retrying in " + (waitTime / 1000) + " seconds. Attempt " + attempts + " out of " + maxAttempts + "...";
                logger.warn(retry);
                this.listeners.firePropertyChange("CONNECTION_FAILED", null, retry);
            } catch (IllegalBlockingModeException e) {
                logger.fatal("Socket already associated with a channel");
                quit();
            }
            attempts++;
            try {
                TimeUnit.MILLISECONDS.sleep(waitTime);
            } catch (InterruptedException e) {
                logger.debug("Thread interrupted while waiting for the next connection attempt.");
                return;
            }
        }
        if (attempts > maxAttempts) {
            String failed = "Could not connect to the TCP server.";
            logger.fatal(failed);
            this.listeners.firePropertyChange("CONNECTION_FAILED", null, failed);
            return;
        }
        connectionTrying.cancel();
        logger.info("TCP connection established");
        this.listeners.firePropertyChange("CONNECTION_ESTABLISHED", null, "You are now connected to the TCP server.");
    }
}
