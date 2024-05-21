package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.network.NetworkUtils;
import it.polimi.ingsw.network.server.RMI.RMIServerReceiver;
import it.polimi.ingsw.network.server.TCP.TCPServerAdapter;
import it.polimi.ingsw.network.server.actions.RMIClientToServerActions;
import it.polimi.ingsw.tui.utils.Utils;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
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
        int TCPPort = Integer.parseInt(cmd.getOptionValue("tp", "12345")); // default is 12345
        int RMIPort = Integer.parseInt(cmd.getOptionValue("rp", "1099")); // default is 1099
        if (cmd.hasOption("debug")) {
            logger.info(ANSI_PURPLE + "Start the Server in DEBUG mode." + Utils.ANSI_RESET);
            HEARTBEAT_TIMEOUT = 600000; //if debug, set the timeout to 10 minutes
            IS_DEBUG = true;
        }
        String serverIp = NetworkUtils.getCurrentHostIp(cmd);

        logger.info("Server IP: {}", serverIp);
        /* ***************************************
         * START RMI SERVER
         * ***************************************/
        RMIServerStart(serverNetworkControllerMapper, RMIPort, serverIp);
        /* ***************************************
         * START TCP SERVER
         * ***************************************/
        TCPServerStart(serverNetworkControllerMapper, TCPPort);
    }

    /**
     * Parses the command line arguments and returns a CommandLine object.
     *
     * @param args the command line arguments.
     * @return a CommandLine object that can be used to query the command line arguments.
     */
    private static CommandLine parseCommandLineArgs(String[] args) {
        Options options = new Options();
        options.addOption("tp", "tcp_port", true, "TCP ServerApp Port number (default is 12345).");
        options.addOption("rp", "rmi_port", true, "RMI ServerApp Port number (default is 1099).");
        //TODO
        options.addOption("ip", true, "RMI ServerApp server external IP (default is localhost).");
        options.addOption("l", "localhost", false, "Start the server with his localhost ip address");
        options.addOption("lan", "Start the server with his lan ip address.");
        options.addOption("debug", "Start the Server in DEBUG mode.");
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("tp") && cmd.hasOption("rp") && Integer.parseInt(cmd.getOptionValue("tp")) == Integer.parseInt(cmd.getOptionValue("rp"))) {
                logger.fatal("Please specify different ports for TCP and RMI ServerApps.");
                System.exit(1);
            }
            return cmd;
        } catch (ParseException e) {
            logger.fatal("Parsing failed. Reason: {}", e.getMessage());
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
            logger.fatal("Failed to start RMI Server on port: {}", RMIPortNumber);
            System.exit(1);
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
                new TCPServerAdapter(serverSocket.accept(), serverNetworkControllerMapper);
            }
        } catch (IOException e) {
            logger.fatal("Could not listen on port {}", TCPPortNumber);
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

        logger.debug(ANSI_PURPLE + "Inspecting services available in the RMI registry:" + ANSI_RESET);
        for (String name : boundNames) {
            try {
                Remote remoteObject = registry.lookup(name);
                Class<?> objClass = remoteObject.getClass();

                logger.debug("Methods available for: {}", name);
                Method[] methods = objClass.getMethods(); // Retrieves all public methods

                for (Method method : methods) {
                    logger.debug(method.toString());
                }
            } catch (Exception e) {
                logger.fatal("Failed to inspect methods for: {} \n Error: {}", name, e);
            }
        }
    }
}