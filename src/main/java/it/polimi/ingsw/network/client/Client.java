package it.polimi.ingsw.network.client;

import it.polimi.ingsw.gui.GUIApp;
import it.polimi.ingsw.network.NetworkUtils;
import it.polimi.ingsw.network.client.connection.Connection;
import it.polimi.ingsw.network.client.connection.RMIConnection;
import it.polimi.ingsw.network.client.connection.TCPConnection;
import it.polimi.ingsw.tui.View;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.utils.Utils;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Client class is responsible for setting up the client's connection to the server.
 * It handles command line arguments and sets up the client's IP and port number.
 * It also provides methods for starting TCP and RMI clients, parsing command line arguments, and printing the game board as a matrix in the console.
 * This class is part of the network client package.
 */
public class Client {

    /**
     * A flag indicating whether the client is in debug mode.
     * This is a static variable, meaning it's shared among all instances of this class.
     */
    public static boolean DEBUG;

    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(Client.class);

    /**
     * The main method of the Client class.
     * It is the entry point of the application and is responsible for setting up the client's connection to the server.
     * It also handles command line arguments and sets up the client's IP and port number.
     *
     * @param args the command line arguments. These can be used to specify the connection type (TCP or RMI), the server and client IP addresses and port numbers, and whether to start the client in debug mode.
     */
    public static void main(String[] args) {
        CommandLine cmd = parseCommandLineArgs(args);

        String clientIp = NetworkUtils.getCurrentHostIp(cmd);
        String serverIp = cmd.getOptionValue("s", "localhost"); // default is localhost
        int serverPort = cmd.hasOption("rmi") ? 1099 : 12345;
        int clientPort = Integer.parseInt(cmd.getOptionValue("cp", Integer.toString(serverPort + 1)));

        DEBUG = cmd.hasOption("debug");

        System.out.println(Utils.ANSI_PURPLE + "Client IP: " + clientIp + Utils.ANSI_RESET);

        if (DEBUG)
            System.out.println(Utils.ANSI_PURPLE + "Debug mode enabled" + Utils.ANSI_RESET);

        // Create the ViewController
        ClientNetworkControllerMapper networkControllerMapper = new ClientNetworkControllerMapper();

        View view;

        // Connect with the server
        Connection connection;
        if (cmd.hasOption("rmi")) {
            logger.info("Starting RMI connection with server. IP: {} on port: {}", serverIp, serverPort);
            logger.info("Listening for RMI connections. IP: {} on port: {}", clientIp, clientPort);
            connection = new RMIConnection(networkControllerMapper, serverIp, serverPort, clientIp, clientPort);
        } else {
            logger.info("Starting TCP connection with server. IP: {} on port: {}", serverIp, serverPort);
            connection = new TCPConnection(networkControllerMapper, serverIp, serverPort);
        }

        if (cmd.hasOption(("tui"))) {
            view = instanceTUI(networkControllerMapper, connection);
        } else {
            view = instanceGUI(networkControllerMapper, connection);
        }
        view.launchUI();
    }

    private static TUIViewController instanceTUI(ClientNetworkControllerMapper networkControllerMapper, Connection connection) {
        TUIViewController tuiController = new TUIViewController(networkControllerMapper, connection);
        // Add the tuiController as a listener to the clientNetworkControllerMapper
        networkControllerMapper.addPropertyChangeListener(tuiController);
        return tuiController;
    }

    private static GUIApp instanceGUI(ClientNetworkControllerMapper networkControllerMapper, Connection connection) {
        GUIApp guiApp = new GUIApp();
        guiApp.instanceGUI(networkControllerMapper, connection);
        return guiApp;
    }

    private static CommandLine parseCommandLineArgs(String[] args) {
        // create Options object
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        // add options
        options.addOption(Option.builder("rmi").longOpt("rmi_mode").desc("Start the client using a RMI connection.").build());
        options.addOption("s", "server_ip", true, "Server IP address.");
        options.addOption("ip", "client_ip", true, "Client IP address.");
        options.addOption("sp", "server_port", true, "Server port number (default is 12345 for TCP and 1099 for RMI).");
        options.addOption("cp", true, "Client port number (default is server port number + 1).");
        options.addOption("lan", "lan", false, "Start the client in LAN mode.");
        options.addOption("l", "localhost", false, "Start the client in localhost mode");
        options.addOption("tui", "tui_mode", false, "Start the client in TUI mode.");
        options.addOption("debug", "Start the client in debug mode.");
        options.addOption("h", "help", false, "Print this message.");

        CommandLine cmd = null;
        try {
            // parse the command line arguments
            cmd = new DefaultParser().parse(options, args);
        } catch (ParseException e) {
            System.err.println("Parsing failed. Reason: " + e.getMessage());
            formatter.printHelp("Client", options);
            System.exit(1);
        }

        if (cmd.hasOption("h")) {
            formatter.printHelp("Client", options);
            System.exit(0);
        }

        // check user args
        if (cmd.hasOption("lan") && cmd.hasOption("localhost")) {
            System.err.println("Please specify either LAN or localhost mode, not both.");
            formatter.printHelp("Client", options);
            System.exit(1);
        }

        // If the connection is not on localhost a server ip is required.
        if (!cmd.hasOption("s") && !cmd.hasOption("localhost")) {
            System.err.println("Please specify the server ip");
            formatter.printHelp("Client", options);
            System.exit(1);
        }

        return cmd;
    }
}

