package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.client.RMI.RMIClientReceiver;
import it.polimi.ingsw.network.client.RMI.RMIClientSender;
import it.polimi.ingsw.network.client.TCP.TCPClientAdapter;
import it.polimi.ingsw.network.client.actions.RMIServerToClientActions;
import it.polimi.ingsw.network.server.actions.RMIClientToServerActions;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.utils.Utils;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

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

        String clientIp = getClientIp(cmd);
        String serverIp = cmd.getOptionValue("s", "localhost"); // default is localhost
        int serverPort = cmd.hasOption("rmi") ? 1099 : 12345;
        int clientPort = Integer.parseInt(cmd.getOptionValue("cp", Integer.toString(serverPort + 1)));

        DEBUG = cmd.hasOption("debug");

        System.out.println(Utils.ANSI_PURPLE + "Client IP: " + clientIp + Utils.ANSI_RESET);

        if (DEBUG)
            System.out.println(Utils.ANSI_PURPLE + "Debug mode enabled" + Utils.ANSI_RESET);

        // Connect with the server
        ClientNetworkControllerMapper networkControllerMapper;
        if (cmd.hasOption("rmi")) {
            logger.info("Starting RMI connection with server. IP: {} on port: {}", serverIp, serverPort);
            logger.info("Listening for RMI connections. IP: {} on port: {}", clientIp, clientPort);
            networkControllerMapper = startRMIClient(serverIp, serverPort, clientIp, clientPort);
        } else {
            logger.info("Starting TCP connection with server. IP: {} on port: {}", serverIp, serverPort);
            networkControllerMapper = startTCPClient(serverIp, serverPort);
        }
        startTui(networkControllerMapper);
    }

    private static String getClientIp(CommandLine cmd) {
        if (cmd.hasOption("localhost")) {
            return "localhost";
        }

        // Get local ip address
        if (cmd.hasOption("lan")) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress("google.com", 80));
                String clientIp = socket.getLocalAddress().getHostAddress();
                socket.close();
                return clientIp;
            } catch (IOException e) {
                throw new RuntimeException("Unable to retrieve your ip address. Please check your connection.");
            }
        }

        // Get public ip address
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://checkip.amazonaws.com"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body().trim();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Unable to retrieve your ip address. Please check your connection.");
        }
    }

    private static void startTui(ClientNetworkControllerMapper networkControllerMapper) {
        TUIViewController tuiController = new TUIViewController(networkControllerMapper);
        // Add the tuiController as a listener to the clientNetworkControllerMapper
        networkControllerMapper.addPropertyChangeListener(tuiController);

        // Start the TUIController
        tuiController.start();
    }

    private static CommandLine parseCommandLineArgs(String[] args) {
        // create Options object
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        // add options
        options.addOption(Option.builder("rmi").longOpt("rmi_mode").desc("Start the client using a RMI connection.").build());
        options.addOption("s", "server_ip", true, "Server IP address.");
        options.addOption("sp", "server_port", true, "Server port number (default is 12345 for TCP and 1099 for RMI).");
        options.addOption("cp", true, "Client port number (default is the same as the server port number).");
        options.addOption("lan", "lan", false, "Start the client in LAN mode.");
        options.addOption("l", "localhost", false, "Start the client in localhost mode");
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

    private static ClientNetworkControllerMapper startTCPClient(String serverIp, int serverPort) {
        try {
            Socket serverSocket = new Socket();
            serverSocket.connect(new InetSocketAddress(serverIp, serverPort), 5000);
            // Create the mapper
            ClientNetworkControllerMapper networkControllerMapper = new ClientNetworkControllerMapper();
            // Pass the mapper to the TCPAdapter in order to be notified when messages arrive.
            TCPClientAdapter clientAdapter = new TCPClientAdapter(serverSocket, networkControllerMapper);
            // Pass the TCPAdapter to the mapper in order to send messages
            networkControllerMapper.setMessageHandler(clientAdapter);
            return networkControllerMapper;
        } catch (SocketTimeoutException e) {
            logger.fatal("Connection timeout when connecting with the server.");
            throw new RuntimeException("Connection timeout when connecting with the server.");
        } catch (IOException e) {
            logger.fatal("Error when connecting with the server.");
            throw new RuntimeException("Error when connecting with the server.");
        }
    }

    private static ClientNetworkControllerMapper startRMIClient(String serverIp, int serverPort, String clientIp, int clientPort) {
        // Getting the registry


        try {
            System.setProperty("java.rmi.server.hostname", clientIp);
            ClientNetworkControllerMapper networkControllerMapper = new ClientNetworkControllerMapper();
            RMIClientReceiver rmiClientReceiver = new RMIClientReceiver(networkControllerMapper);
            RMIServerToClientActions clientStub = (RMIServerToClientActions) UnicastRemoteObject.exportObject(rmiClientReceiver, clientPort);
            //Client as a client, getting the registry
            Registry registry = LocateRegistry.getRegistry(serverIp, serverPort);
            // Looking up the registry for the remote object
            RMIClientToServerActions serverStub = (RMIClientToServerActions) registry.lookup("ClientToServerActions");
            RMIClientSender rmiClientSender = new RMIClientSender(serverStub, clientStub);


            // Path the sender to the mapper
            networkControllerMapper.setMessageHandler(rmiClientSender);
            return networkControllerMapper;

        } catch (RemoteException | NotBoundException e) {
            logger.fatal("Error establishing RMI connection with the server");
            throw new RuntimeException("Error establishing RMI connection with the server");
        }

    }
}

