package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.client.connection.Connection;
import it.polimi.ingsw.network.client.connection.RMIConnection;
import it.polimi.ingsw.network.client.connection.TCPConnection;
import it.polimi.ingsw.network.utils.HostIpAddressResolver;
import it.polimi.ingsw.network.utils.RMITimeoutSetter;
import it.polimi.ingsw.view.gui.GUIApp;
import it.polimi.ingsw.view.tui.View;
import it.polimi.ingsw.view.tui.controller.TUIViewController;
import it.polimi.ingsw.view.tui.utils.Utils;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * The Client class is responsible for setting up the client's connection to the server.
 * It handles command line arguments and sets up the client's IP and port number.
 * It also provides methods for starting TCP and RMI clients, parsing command line arguments, and printing the game board as a matrix in the console.
 * This class is part of the network client package.
 */
public class Client {

    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(Client.class);
    /**
     * A flag indicating whether the client is in debug mode.
     * This is a static variable, meaning it's shared among all instances of this class.
     */
    public static boolean DEBUG;

    public static final String defaultServerIp = "161.35.162.219";

    /**
     * The main method of the Client class.
     * It is the entry point of the application and is responsible for setting up the client's connection to the server.
     * It also handles command line arguments and sets up the client's IP and port number.
     *
     * @param args the command line arguments. These can be used to specify the connection type (TCP or RMI), the server and client IP addresses and port numbers, and whether to start the client in debug mode.
     */
    public static void main(String[] args) {
        CommandLine cmd = parseCommandLineArgs(args);

        String serverIp = cmd.getOptionValue("s", defaultServerIp);// default is our server
        String clientIp = HostIpAddressResolver.getCurrentHostIp(cmd);
        if (clientIp.equals("localhost"))
            serverIp = "localhost";

        int serverPort = cmd.hasOption("rmi") ? 1099 : 12345;

        if (cmd.hasOption("sp")) {
            serverPort = Integer.parseInt(cmd.getOptionValue("sp"));
        }

        int clientPort = Integer.parseInt(cmd.getOptionValue("cp", Integer.toString(serverPort + 1)));

        DEBUG = cmd.hasOption("debug");

        System.out.println(Utils.ANSI_PURPLE + "Client IP: " + clientIp + Utils.ANSI_RESET);

        if (DEBUG)
            System.out.println(Utils.ANSI_PURPLE + "Debug mode enabled" + Utils.ANSI_RESET);

        // Create the ViewController
        ClientNetworkControllerMapper networkControllerMapper = ClientNetworkControllerMapper.getInstance();

        View view;

        // Connect with the server
        Connection connection;
        if (cmd.hasOption("rmi")) {
            logger.info("Starting RMI connection with server. IP: {} on port: {}", serverIp, serverPort);
            logger.info("Listening for RMI connections. IP: {} on port: {}", clientIp, clientPort);
            System.setProperty("java.rmi.server.hostname", clientIp);
            RMITimeoutSetter.setRMITimeout(5000);
            connection = new RMIConnection(networkControllerMapper, serverIp, serverPort, clientIp, clientPort);
        } else {
            logger.info("Starting TCP connection with server. IP: {} on port: {}", serverIp, serverPort);
            connection = new TCPConnection(networkControllerMapper, serverIp, serverPort);
        }
        networkControllerMapper.setConnection(connection);
        if (cmd.hasOption(("tui"))) {
            // Set the root logger level to INFO. If in TUI mode the user does not have to see logs.
            updateRootLoggerLevel(Level.INFO);
            view = new TUIViewController(networkControllerMapper);
        } else {
            view = new GUIApp();
        }
        view.launchUI();
    }

    private static void updateRootLoggerLevel(Level level) {
        if (!DEBUG)
            Configurator.setAllLevels(LogManager.getRootLogger().getName(), level);
    }


    private static CommandLine parseCommandLineArgs(String[] args) {
        // create Options object
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        // add options
        options.addOption(Option.builder("rmi").longOpt("rmi_mode").desc("Start the client using a RMI connection.").build());
        options.addOption("s", "server_ip", true, "Server IP address. If not set it will default to " + defaultServerIp + " (The remote server).");
        // Param used by NetworkUtils.getCurrentHostIp() method
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

        if (cmd.hasOption("server_ip") && cmd.hasOption("localhost")) {
            System.err.println("Please specify either server IP or localhost, not both.");
            formatter.printHelp("Client", options);
            System.exit(1);
        }

        if (!cmd.hasOption("server_ip") && (cmd.hasOption("lan") || cmd.hasOption("ip"))) {
            System.err.println("You can't connect to the remote server with a LAN or manually set client IP.");
            formatter.printHelp("Client", options);
            System.exit(1);
        }
        return cmd;
    }
}

