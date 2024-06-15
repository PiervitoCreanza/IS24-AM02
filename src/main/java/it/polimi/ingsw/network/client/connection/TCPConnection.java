package it.polimi.ingsw.network.client.connection;

import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.client.TCP.TCPClientAdapter;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.IllegalBlockingModeException;

/**
 * The TCPConnection class implements the Connection interface using the TCP protocol.
 * It also implements the PropertyChangeListener and PropertyChangeNotifier interfaces to provide property change support.
 * It is responsible for establishing a connection between the client and the server.
 */
public class TCPConnection extends Connection {

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
                String retry = "TCP server unreachable, retrying in " + (waitTime / 1000) + " seconds. Attempt " + attempts + " out of " + maxAttempts + "...";
                logger.warn(retry);
                this.listeners.firePropertyChange("CONNECTION_RETRY", null, retry);
            } catch (IllegalBlockingModeException e) {
                logger.fatal("Socket already associated with a channel");
                quit();
            }
            attempts++;
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                logger.fatal("Error while sleeping");
            }
        }
        if (attempts > maxAttempts) {
            logger.fatal("Could not connect to the server. Exiting...");
            quit();
        }
        logger.info("TCP connection established");
        this.listeners.firePropertyChange("CONNECTION_ESTABLISHED", null, null);
    }
}