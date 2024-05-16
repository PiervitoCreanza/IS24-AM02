package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.network.server.RMI.RMIServerReceiver;
import it.polimi.ingsw.network.server.TCP.TCPServerAdapter;
import it.polimi.ingsw.network.server.actions.RMIClientToServerActions;
import it.polimi.ingsw.tui.utils.Utils;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static it.polimi.ingsw.tui.utils.Utils.ANSI_PURPLE;
import static it.polimi.ingsw.tui.utils.Utils.ANSI_RESET;

/**
 * This class is responsible for setting up and running the TCP and RMI servers.
 * It listens for incoming client connections and handles each one in a separate thread.
 */
public class Server {
    /**
     * The timeout for the heartbeat in milliseconds.
     * This is the interval at which the server checks if the client is still connected.
     */
    public static long HEARTBEAT_TIMEOUT = 2500;

    /**
     * A flag indicating whether the server is running in debug mode.
     * If true, the server will output additional debug information.
     */
    public static boolean IS_DEBUG = false;

    /**
     * The port number used by the TCP server.
     */
    private static int TCPPortNumber;

    /**
     * The port number used by the RMI server.
     */
    private static int RMIPortNumber;

    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(Server.class);

    /**
     * The RMIServerReceiver class is responsible for receiving client actions and notifying the server message handler.
     * It implements the RMIClientToServerActions interface, which defines the methods for handling client actions.
     * It also implements the Observer interface, which allows it to be notified when a change occurs in the ServerMessageHandler.
     *
     * @param args the command line arguments. These can be used to specify the port numbers for the TCP and RMI servers, the server IP, and whether to start the server in debug mode.
     * @throws RemoteException if an error occurs during the remote method call
     */
    public static void main(String[] args) throws RemoteException {
        /* ***************************************
         * INSTANTIATE MAIN CONTROLLER and NETWORK COMMAND MAPPER
         * ***************************************/
        MainController mainController = new MainController();
        ServerNetworkControllerMapper serverNetworkControllerMapper = new ServerNetworkControllerMapper(mainController);

        /* ***************************************
         * CLI ARGUMENTS PARSING
         * ***************************************/
        CommandLine cmd = parseCommandLineArgs(args);
        TCPPortNumber = Integer.parseInt(cmd.getOptionValue("TCP_P", "12345")); // default is 12345
        RMIPortNumber = Integer.parseInt(cmd.getOptionValue("RMI_P", "1099")); // default is 1099
        String serverIp;
        if (cmd.hasOption("debug")) {
            logger.info(ANSI_PURPLE + "Start the Server in DEBUG mode." + Utils.ANSI_RESET);
            HEARTBEAT_TIMEOUT = 600000; //if debug, set the timeout to 10 minutes
            IS_DEBUG = true;
        }
        if (cmd.hasOption("lan")) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress("google.com", 80));
                serverIp = socket.getLocalAddress().getHostAddress();
                socket.close();
            } catch (Exception e) {
                serverIp = "localhost";
            }
        } else serverIp = cmd.getOptionValue("IP", "localhost");

        logger.info("ServerApp IP: {}", serverIp);
        /* ***************************************
         * START RMI SERVER
         * ***************************************/
        RMIServerStart(serverNetworkControllerMapper, RMIPortNumber, serverIp);
        /* ***************************************
         * START TCP SERVER
         * ***************************************/
        TCPServerStart(serverNetworkControllerMapper, TCPPortNumber);
    }

    /**
     * Parses the command line arguments and returns a CommandLine object.
     *
     * @param args the command line arguments.
     * @return a CommandLine object that can be used to query the command line arguments.
     */
    private static CommandLine parseCommandLineArgs(String[] args) {
        Options options = new Options();
        options.addOption("TCP_P", true, "TCP ServerApp Port number (default is 12345).");
        options.addOption("RMI_P", true, "RMI ServerApp Port number (default is 1099).");
        options.addOption("IP", true, "RMI ServerApp server external IP (default is localhost).");
        options.addOption("lan", "Start the server with his lan ip address.");
        options.addOption("debug", "Start the Server in DEBUG mode.");

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            TCPPortNumber = cmd.hasOption("TCP_P") ? Integer.parseInt(cmd.getOptionValue("TCP_P")) : 12345; // default is 12345
            RMIPortNumber = cmd.hasOption("RMI_P") ? Integer.parseInt(cmd.getOptionValue("RMI_P")) : 1099; // default is 1099

            if (cmd.hasOption("TCP_P") && cmd.hasOption("RMI_P") && Integer.parseInt(cmd.getOptionValue("TCP_P")) == Integer.parseInt(cmd.getOptionValue("RMI_P"))) {
                System.err.println("Please specify different ports for TCP and RMI ServerApps.");
                System.exit(1);
            }
            return cmd;
        } catch (ParseException e) {
            System.err.println("Parsing failed. Reason: " + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    /**
     * Starts the RMI server.
     *
     * @param serverNetworkControllerMapper the server network controller mapper.
     * @param RMIPortNumber                 the RMI port number.
     * @param serverIp                      the server IP.
     */
    private static void RMIServerStart(ServerNetworkControllerMapper serverNetworkControllerMapper, Integer RMIPortNumber, String serverIp) {
        try {
            //TODO Conti fixit
            System.setProperty("java.rmi.server.hostname", serverIp);

            RMIServerReceiver rmiServerReceiver = new RMIServerReceiver(serverNetworkControllerMapper);
            RMIClientToServerActions stub = (RMIClientToServerActions) UnicastRemoteObject.exportObject(rmiServerReceiver, RMIPortNumber);
            Registry registry = LocateRegistry.createRegistry(RMIPortNumber);
            registry.bind("ClientToServerActions", stub);
            logger.info("Codex Naturalis RMI Server ready on port: {}", RMIPortNumber);

            if (IS_DEBUG) {
                inspectRegistry(registry); //Prints all the methods available in the RMI registry
            }

        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the TCP server.
     *
     * @param serverNetworkControllerMapper the server network controller mapper.
     * @param TCPPortNumber                 the TCP port number.
     */
    private static void TCPServerStart(ServerNetworkControllerMapper serverNetworkControllerMapper, Integer TCPPortNumber) {
        try (ServerSocket serverSocket = new ServerSocket(TCPPortNumber)) {
            logger.info("Codex Naturalis TCP Server ready on port: {}", TCPPortNumber);
            while (true) {
                TCPServerAdapter messageHandler = new TCPServerAdapter(serverSocket.accept(), serverNetworkControllerMapper);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + TCPPortNumber);
            System.exit(-1);
        }
    }

    /**
     * Inspects the RMI registry and prints all the methods available.
     *
     * @param registry the RMI registry.
     * @throws RemoteException if a remote communication error occurs.
     */
    private static void inspectRegistry(Registry registry) throws RemoteException {
        // Get the list of names bound in the registry
        String[] boundNames = registry.list();

        logger.info(ANSI_PURPLE + "Inspecting services available in the RMI registry:" + ANSI_RESET);
        for (String name : boundNames) {
            try {
                Remote remoteObject = registry.lookup(name);
                Class<?> objClass = remoteObject.getClass();

                logger.info("Methods available for: {}", name);
                Method[] methods = objClass.getMethods(); // Retrieves all public methods

                for (Method method : methods) {
                    logger.info(method.toString());
                }
            } catch (Exception e) {
                logger.info("Failed to inspect methods for: {}", name);
                e.printStackTrace();
            }
        }
    }
}