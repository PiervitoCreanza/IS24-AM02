package it.polimi.ingsw.network.client;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.network.server.RMIClientActions;
import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

public class Client {

    private static final ClientCommandMapper clientCommandMapper = new ClientCommandMapper();

    private static String serverIpAddress;
    private static int serverPortNumber;
    private static String clientIpAddress;
    private static int clientPortNumber;

    public static void main(String[] args) {
        CommandLine cmd = parseCommandLineArgs(args);
        // get values from options
        String connectionType = cmd.getOptionValue("c");
        serverIpAddress = cmd.getOptionValue("ip_s", "localhost"); // default is localhost
        serverPortNumber = Integer.parseInt(cmd.getOptionValue("p_s", (connectionType.equals("TCP") ? "12345" : "1099")));
        clientIpAddress = cmd.getOptionValue("ip_c", "localhost"); // default is localh
        clientPortNumber = Integer.parseInt(cmd.getOptionValue("p_c", Integer.toString(serverPortNumber)));

        switch (connectionType.toLowerCase()) {
            case "tcp" -> {
                System.out.println(Utils.ANSI_BLUE + "Started a TCP connection with IP: " + serverIpAddress + " on port: " + serverPortNumber + Utils.ANSI_RESET);
                startTCPClient();
            }
            case "rmi" -> {
                System.out.println(Utils.ANSI_YELLOW + "Started an RMI connection with IP: " + serverIpAddress + " on port: " + serverPortNumber + Utils.ANSI_RESET);
                startRMIClient();
            }
            default -> System.err.println("Invalid connection type. Please specify either TCP or RMI.");
        }
    }

    private static CommandLine parseCommandLineArgs(String[] args) {
        // create Options object
        Options options = new Options();

        // add options
        options.addOption("c", true, "Connection type (TCP or RMI). This is mandatory.");
        options.addOption("ip", true, "IP address (default is localhost).");
        options.addOption("p", true, "Port number (default is 12345 for TCP and 1099 for RMI).");

        CommandLineParser parser = new DefaultParser();

        try {
            // parse the command line arguments
            CommandLine cmd = parser.parse(options, args);

            // check mandatory options
            if (!cmd.hasOption("c")) {
                System.err.println("Please specify the connection type (TCP or RMI) using the -c option.");
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
            System.out.println("Hello, this is the client!");
            Socket serverSocket = new Socket(serverIpAddress, serverPortNumber);
            // Print configuration
            System.err.println("Starting client  with connection to server at " + serverIpAddress + " on port " + serverPortNumber);
            TCPClientAdapter clientAdapter = new TCPClientAdapter(serverSocket, clientCommandMapper);
            clientCommandMapper.setMessageHandler(clientAdapter);
            printTUIMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startRMIClient() {
        // Getting the registry
        try {
            //TODO Conti fixit
            System.setProperty("java.rmi.server.hostname", clientIpAddress);
            //TODO
            //Client as a server, binding the registry
            ServerActions rmiClientConnectionHandler = new RMIClientConnectionHandler(clientCommandMapper);
            ServerActions clientStub = (ServerActions) UnicastRemoteObject.exportObject(rmiClientConnectionHandler, clientPortNumber);
            Registry clientRegistry = LocateRegistry.createRegistry(clientPortNumber);
            clientRegistry.bind("ServerActions", clientStub);


            //Client as a client, getting the registry
            Registry serverRegistry = LocateRegistry.getRegistry(serverIpAddress, serverPortNumber);
            // Looking up the registry for the remote object
            RMIClientActions serverStub = (RMIClientActions) serverRegistry.lookup("ClientActions");
            RMIClientAdapter rmiClientAdapter = new RMIClientAdapter(serverStub, clientIpAddress, clientPortNumber);
            clientCommandMapper.setMessageHandler(rmiClientAdapter); //Adding stub to the mapper
            printTUIMenu();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void printTUIMenu() {
        //TODO: substitute rmiClientAdapter with value coming from mapper
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = null;
        try {
            while (!Objects.equals(input, "12")) {
                System.out.print("1) Get games\n");
                System.out.print("2) Create game\n");
                System.out.print("3) Delete game\n");
                System.out.print("4) Join game\n");
                System.out.print("5) Choose player color\n");
                System.out.print("6) Set player objective\n");
                System.out.print("7) Place card\n");
                System.out.print("8) Draw card from field\n");
                System.out.print("9) Draw card from resource deck\n");
                System.out.print("10) Draw card from gold deck\n");
                System.out.print("11) Switch the side of a card\n");
                System.out.print("12) Exit\n");
                input = reader.readLine();

                String gameName = "";
                String numPlayers = "";
                String playerName = "";

                switch (input) {
                    case "1" -> {
                        clearConsole();
                        clientCommandMapper.getGames();
                    }
                    case "2" -> {
                        clearConsole();
                        System.out.println("Enter game name to create: ");
                        gameName = reader.readLine();
                        System.out.println("Enter number of players: ");
                        numPlayers = reader.readLine();
                        System.out.println("Enter player name: ");
                        playerName = reader.readLine();
                        clientCommandMapper.createGame(gameName, playerName, Integer.parseInt(numPlayers));
                    }
                    case "3" -> {
                        clearConsole();
                        System.out.println("Enter game name to delete: ");
                        gameName = reader.readLine();
                        System.out.println("Enter player name: ");
                        playerName = reader.readLine();
                        clientCommandMapper.deleteGame(gameName, playerName);
                    }
                    case "4" -> {
                        clearConsole();
                        System.out.println("Enter game name to join: ");
                        gameName = reader.readLine();
                        System.out.println("Enter player name: ");
                        playerName = reader.readLine();
                        clientCommandMapper.joinGame(gameName, playerName);
                    }
                    case "5" -> {
                        clearConsole();
                        clientCommandMapper.choosePlayerColor(gameName, playerName, PlayerColorEnum.RED);
                    }
                    case "6" -> {
                        clearConsole();
                        //TODO: gather arguments on updated VirtualView
                        System.out.print("Not implemented");
                    }
                    case "7" -> {
                        //TODO: gather arguments on updated VirtualView
                        clearConsole();
                        System.out.print("Not implemented");
                    }
                    case "8" -> {
                        //TODO: gather arguments on updated VirtualView
                        clearConsole();

                        System.out.print("Not implemented");
                    }
                    case "9" -> {
                        //TODO: gather arguments on updated VirtualView
                        clearConsole();
                        System.out.print("Not implemented");
                    }
                    case "10" -> {
                        //TODO: gather arguments on updated VirtualView
                        clearConsole();
                        System.out.print("Not implemented");
                    }
                    case "11" -> {
                        //TODO: gather arguments on updated VirtualView
                        clearConsole();
                        System.out.print("Not implemented");
                    }
                    case "12" -> {
                        System.out.println("Exiting...");
                    }
                    default -> System.out.println("Invalid input");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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

