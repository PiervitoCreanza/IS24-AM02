package it.polimi.ingsw.network.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;

/**
 * This class provides a utility to set the timeout for RMI connections.
 */
public class RMITimeoutSetter {

    /**
     * Sets the timeout for RMI connections.
     *
     * @param timeout the timeout value in milliseconds.
     */
    public static void setRMITimeout(int timeout) {
        try {
            RMISocketFactory.setSocketFactory(new RMISocketFactory() {

                /**
                 * Creates a new Socket with the specified timeout.
                 *
                 * @param host the host name, or null for a loopback address.
                 * @param port the port number.
                 * @return a new Socket.
                 * @throws IOException if an I/O error occurs when creating the socket.
                 */
                public Socket createSocket(String host, int port) throws IOException {
                    Socket socket = new Socket();
                    socket.setSoTimeout(timeout);
                    socket.setSoLinger(false, 0);
                    socket.connect(new InetSocketAddress(host, port), timeout);
                    return socket;
                }

                /**
                 * Creates a new ServerSocket.
                 *
                 * @param port the port number, or 0 to use a port number that is automatically allocated.
                 * @return a new ServerSocket.
                 * @throws IOException if an I/O error occurs when opening the socket.
                 */
                public ServerSocket createServerSocket(int port) throws IOException {
                    return new ServerSocket(port);
                }
            });
        } catch (IOException e) {
            System.err.println("Error setting RMI timeout");
            System.exit(-1);
        }
    }
}
