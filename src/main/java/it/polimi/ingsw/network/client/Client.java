package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.RMI.RMIClientReceiver;
import it.polimi.ingsw.network.client.RMI.RMIClientSender;
import it.polimi.ingsw.network.client.TCP.TCPClientAdapter;
import it.polimi.ingsw.network.client.actions.RMIServerToClientActions;
import it.polimi.ingsw.network.client.message.ChatClientToServerMessage;
import it.polimi.ingsw.network.server.actions.RMIClientToServerActions;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.utils.Utils;
import org.apache.commons.cli.*;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Client class is responsible for setting up the client's connection to the server.
 * It handles command line arguments and sets up the client's IP and port number.
 * It also provides methods for starting TCP and RMI clients, parsing command line arguments, and printing the game board as a matrix in the console.
 * This class is part of the network client package.
 */
public class Client {

    /**
     * The ClientNetworkControllerMapper object used to map network commands to actions in the game.
     * This is a static final instance which means it's a constant throughout the application.
     */
    private static final ClientNetworkControllerMapper CLIENT_NETWORK_CONTROLLER_MAPPER = new ClientNetworkControllerMapper();
    private static final TUIViewController tuiController = new TUIViewController();

    /**
     * A flag indicating whether the client is in debug mode.
     * This is a static variable, meaning it's shared among all instances of this class.
     */
    public static boolean DEBUG;
    /**
     * The IP address of the server that the client will connect to.
     * This is a static variable, meaning it's shared among all instances of this class.
     */
    private static String serverIpAddress;
    /**
     * The port number of the server that the client will connect to.
     * This is a static variable, meaning it's shared among all instances of this class.
     */
    private static int serverPortNumber;
    /**
     * The IP address of the client.
     * This is a static variable, meaning it's shared among all instances of this class.
     */
    private static String clientIpAddress;
    /**
     * The port number of the client.
     * This is a static variable, meaning it's shared among all instances of this class.
     */
    private static int clientPortNumber;

    /**
     * The main method of the Client class.
     * It is the entry point of the application and is responsible for setting up the client's connection to the server.
     * It also handles command line arguments and sets up the client's IP and port number.
     *
     * @param args the command line arguments. These can be used to specify the connection type (TCP or RMI), the server and client IP addresses and port numbers, and whether to start the client in debug mode.
     */
    public static void main(String[] args) {
        CommandLine cmd = parseCommandLineArgs(args);
        boolean lan;
        // get values from options
        String connectionType = cmd.getOptionValue("c");
        lan = cmd.hasOption("lan");
        serverIpAddress = cmd.getOptionValue("ip_s", "localhost"); // default is localhost
        serverPortNumber = Integer.parseInt(cmd.getOptionValue("p_s", (connectionType.equals("TCP") ? "12345" : "1099")));
        DEBUG = cmd.hasOption("debug");
        if (lan) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress("google.com", 80));
                clientIpAddress = socket.getLocalAddress().getHostAddress();
                socket.close();

            } catch (Exception e) {
                clientIpAddress = "localhost";
            }
        } else {
            clientIpAddress = cmd.getOptionValue("ip_c", "localhost");
        }
        clientPortNumber = Integer.parseInt(cmd.getOptionValue("p_c", Integer.toString(serverPortNumber + 1)));

        System.out.println(Utils.ANSI_PURPLE + "Client IP: " + clientIpAddress + Utils.ANSI_RESET);
        if (DEBUG)
            System.out.println(Utils.ANSI_PURPLE + "Debug mode enabled" + Utils.ANSI_RESET);
        switch (connectionType.toLowerCase()) {
            case "tcp" -> {
                System.out.println(Utils.ANSI_BLUE + "Started a TCP connection with IP: " + serverIpAddress + " on port: " + serverPortNumber + Utils.ANSI_RESET);
                startTCPClient();
            }
            case "rmi" -> {
                System.out.println(Utils.ANSI_YELLOW + "Started an RMI connection with IP: " + serverIpAddress + " on port: " + serverPortNumber + Utils.ANSI_RESET);
                System.out.println(Utils.ANSI_YELLOW + "Waiting RMI request on IP: " + clientIpAddress + " on port: " + clientPortNumber + Utils.ANSI_RESET);
                startRMIClient();
            }
        }


    }

    private static void startTui() {
        CLIParser cliParser = new CLIParser(CLIENT_NETWORK_CONTROLLER_MAPPER, tuiController);
        cliParser.addPropertyChangeListener(tuiController);
        cliParser.start();
        CLIENT_NETWORK_CONTROLLER_MAPPER.addPropertyChangeListener(tuiController);
    }

    private static CommandLine parseCommandLineArgs(String[] args) {
        // create Options object
        Options options = new Options();

        // add options
        options.addOption("c", true, "Connection type (TCP or RMI). This is mandatory.");
        options.addOption("ip_s", true, "IP address (default is localhost).");
        options.addOption("p_s", true, "Port number (default is 12345 for TCP and 1099 for RMI).");
        options.addOption("ip_c", true, "Client IP address (default is localhost).");
        options.addOption("p_c", true, "Client port number (default is the same as the server port number).");
        options.addOption("lan", "Start the client with his lan ip address.");
        options.addOption("debug", "Start the client in debug mode.");

        CommandLineParser parser = new DefaultParser();

        try {
            // parse the command line arguments
            CommandLine cmd = parser.parse(options, args);

            // check mandatory options
            if (!cmd.hasOption("c")) {
                System.err.println("Please specify the connection type (TCP or RMI) using the -c option.");
                System.exit(1);
            }

            // check if the value of -c is either "TCP" or "RMI"
            String connectionType = cmd.getOptionValue("c").toLowerCase();
            if (!connectionType.equals("tcp") && !connectionType.equals("rmi")) {
                System.err.println("Invalid value for -c. Please specify either TCP or RMI.");
                System.exit(1);
            }

            return cmd;
        } catch (ParseException e) {
            System.err.println("Parsing failed. Reason: " + e.getMessage());
            System.exit(1);
        }


        return null;
    }

    private static void startTCPClient() {
        try {
            Socket serverSocket = new Socket(serverIpAddress, serverPortNumber);
            // Print configuration
            System.err.println("Starting client  with connection to server at " + serverIpAddress + " on port " + serverPortNumber);
            TCPClientAdapter clientAdapter = new TCPClientAdapter(serverSocket, CLIENT_NETWORK_CONTROLLER_MAPPER);
            CLIENT_NETWORK_CONTROLLER_MAPPER.setMessageHandler(clientAdapter);
            startTui();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startRMIClient() {
        // Getting the registry
        try {
            System.setProperty("java.rmi.server.hostname", clientIpAddress);

            RMIClientReceiver rmiClientReceiver = new RMIClientReceiver(CLIENT_NETWORK_CONTROLLER_MAPPER);
            RMIServerToClientActions clientStub = (RMIServerToClientActions) UnicastRemoteObject.exportObject(rmiClientReceiver, clientPortNumber);
            //Client as a client, getting the registry
            Registry registry = LocateRegistry.getRegistry(serverIpAddress, serverPortNumber);
            // Looking up the registry for the remote object
            RMIClientToServerActions serverStub = (RMIClientToServerActions) registry.lookup("ClientToServerActions");
            RMIClientSender rmiClientSender = new RMIClientSender(serverStub, clientStub);
            CLIENT_NETWORK_CONTROLLER_MAPPER.setMessageHandler(rmiClientSender); //Adding stub to the mapper
            startTui();
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }


    private static ArrayList<GameCard> printHand(String finalPlayerName) {
        int index = 0;
        ArrayList<GameCard> hand = CLIENT_NETWORK_CONTROLLER_MAPPER.getView().gameView().playerViews().stream().filter(playerView -> playerView.playerName().equals(finalPlayerName)).findFirst().get().playerHandView().hand();
        System.out.println("Here is your hand:");
        for (GameCard gameCard : hand) {
            System.out.println(index + 1 + ")" + gameCard);
            index++;
        }
        return hand;
    }

    /**
     * Prints the game board as a matrix in the console.
     * The game board is represented as a HashMap where the keys are coordinates and the values are game cards.
     * An 'x' is printed for each coordinate that contains a game card, and a space is printed for each coordinate that does not contain a game card.
     *
     * @param map the game board represented as a HashMap where the keys are coordinates and the values are game cards.
     */
    public static void printBoardAsMatrix(HashMap<Coordinate, GameCard> map) {
        double maxX = 0;
        double maxY = 0;

        // Determine the size of the matrix
        for (Coordinate coordinate : map.keySet()) {
            if (coordinate.getX() > maxX) {
                maxX = coordinate.getX();
            }
            if (coordinate.getY() > maxY) {
                maxY = coordinate.getY();
            }
        }

        // Print the matrix
        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                Coordinate currentCoordinate = new Coordinate(x, y);
                if (map.containsKey(currentCoordinate)) {
                    System.out.print("x ");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }

    private static void clearConsole() {
        String os = System.getProperty("os.name").toLowerCase();

        try {
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                System.out.print(Utils.ANSI_CLEAR_UNIX);
            } else {
                // Fallback to printing new lines if OS detection failed
                for (int i = 0; i < 100; i++) {
                    System.out.println();
                }
            }
        } catch (Exception e) {
            // Handle any exceptions
        }

    }
}

