package it.polimi.ingsw.network.client;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.server.RMIClientActions;
import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Client {

    private static final ClientCommandMapper clientCommandMapper = new ClientCommandMapper();

    private static String serverIpAddress;
    private static int serverPortNumber;
    private static String clientIpAddress;
    private static int clientPortNumber;

    public static void main(String[] args) {
        CommandLine cmd = parseCommandLineArgs(args);
        boolean lan;
        // get values from options
        String connectionType = cmd.getOptionValue("c");
        lan = cmd.hasOption("lan");
        serverIpAddress = cmd.getOptionValue("ip_s", "localhost"); // default is localhost
        serverPortNumber = Integer.parseInt(cmd.getOptionValue("p_s", (connectionType.equals("TCP") ? "12345" : "1099")));
        if (lan) {
            try {
                clientIpAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (Exception e) {
                clientIpAddress = "localhost";
            }
        } else {
            clientIpAddress = cmd.getOptionValue("ip_c", "localhost");
        }
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
        options.addOption("ip_s", true, "IP address (default is localhost).");
        options.addOption("p_s", true, "Port number (default is 12345 for TCP and 1099 for RMI).");
        options.addOption("ip_c", true, "Client IP address (default is localhost).");
        options.addOption("p_c", true, "Client port number (default is the same as the server port number).");
        options.addOption("lan", "Start the client with his lan ip address.");

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
            System.setProperty("java.rmi.server.hostname", clientIpAddress);

            RMIClientConnectionHandler rmiClientConnectionHandler = new RMIClientConnectionHandler(clientCommandMapper);
            ServerActions clientStub = (ServerActions) UnicastRemoteObject.exportObject(rmiClientConnectionHandler, clientPortNumber);
            //Client as a client, getting the registry
            Registry registry = LocateRegistry.getRegistry(serverIpAddress, serverPortNumber);
            // Looking up the registry for the remote object
            RMIClientActions serverStub = (RMIClientActions) registry.lookup("ClientActions");
            RMIClientAdapter rmiClientAdapter = new RMIClientAdapter(serverStub, clientStub);
            clientCommandMapper.setMessageHandler(rmiClientAdapter); //Adding stub to the mapper
            printTUIMenu();
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printTUIMenu() {
        //TODO: substitute rmiClientAdapter with value coming from mapper
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = null;
        String gameName = "";
        String playerName = "";

        try {
            while (!Objects.equals(input, "13")) {
                System.out.print("1) Get games\n");
                System.out.print("2) Create game\n");
                System.out.print("3) Delete game\n");
                System.out.print("4) Join game\n");
                System.out.print("5) Set StarterCard\n");
                System.out.print("6) Choose player color\n");
                System.out.print("7) Set ObjectiveCard \n");
                System.out.print("8) Place card\n");
                System.out.print("9) Draw card from field\n");
                System.out.print("10) Draw card from resource deck\n");
                System.out.print("11) Draw card from gold deck\n");
                System.out.print("12) Show map\n");
                System.out.print("13) Exit\n");
                input = reader.readLine();

                String finalPlayerName = playerName;
                switch (input) {
                    case "1" -> {
                        clientCommandMapper.getGames();
                    }
                    case "2" -> {
                        String numPlayers = "";
                        System.out.println("Enter game name to create: ");
                        gameName = reader.readLine();
                        System.out.println("Enter number of players: ");
                        numPlayers = reader.readLine();
                        System.out.println("Enter player name: ");
                        playerName = reader.readLine();
                        clientCommandMapper.createGame(gameName, playerName, Integer.parseInt(numPlayers));
                    }
                    case "3" -> {
                        System.out.println("Enter game name to delete: ");
                        gameName = reader.readLine();
                        System.out.println("Enter player name: ");
                        playerName = reader.readLine();
                        clientCommandMapper.deleteGame(gameName, playerName);
                    }
                    case "4" -> {
                        System.out.println("Enter game name to join: ");
                        gameName = reader.readLine();
                        System.out.println("Enter player name: ");
                        playerName = reader.readLine();
                        clientCommandMapper.joinGame(gameName, playerName);
                    }
                    case "5" -> {
                        //TODO: gather arguments on updated VirtualView
                        GameCard starterCard = clientCommandMapper.getView().gameView().playerViews().stream().filter(playerView -> playerView.playerName().equals(finalPlayerName)).findFirst().get().starterCard();
                        ;
                        String choice = "0";
                        boolean side = false;
                        while (!choice.equals("2")) {
                            System.out.println("This is your startercard: " + starterCard.getCurrentSide().getGameItemStore());
                            System.out.println("1) Change side");
                            System.out.println("2) Place card");
                            choice = reader.readLine();
                            switch (choice) {
                                case "1" -> {
                                    side = !side;
                                    starterCard.switchSide();
                                }
                                case "2" -> {
                                    if (side)
                                        clientCommandMapper.switchCardSide(gameName, playerName, starterCard);
                                    clientCommandMapper.placeCard(gameName, playerName, new Coordinate(0, 0), starterCard);
                                }
                            }
                        }
                        System.out.print("Placed!");
                    }
                    case "6" -> {
                        System.out.println("Choose player color: RED, BLUE, GREEN, YELLOW");
                        String choice = reader.readLine();
                        PlayerColorEnum chosenColor;
                        switch (choice.toLowerCase()) {
                            case "red" -> chosenColor = PlayerColorEnum.RED;
                            case "blue" -> chosenColor = PlayerColorEnum.BLUE;
                            case "green" -> chosenColor = PlayerColorEnum.GREEN;
                            case "yellow" -> chosenColor = PlayerColorEnum.YELLOW;
                            default -> throw new IllegalStateException("Unexpected value: " + choice);
                        }
                        clientCommandMapper.choosePlayerColor(gameName, playerName, chosenColor);
                    }
                    case "7" -> {
                        ArrayList<ObjectiveCard> choosableObjectives = clientCommandMapper.getView().gameView().playerViews().stream().filter(playerView -> playerView.playerName().equals(finalPlayerName)).findFirst().get().choosableObjectives();
                        System.out.println("Choose an objective card: ");
                        System.out.println("1) " + choosableObjectives.getFirst());
                        System.out.println("2) " + choosableObjectives.getLast());
                        String choice = reader.readLine();
                        clientCommandMapper.setPlayerObjective(gameName, playerName, choosableObjectives.get(Integer.parseInt(choice) - 1));
                    }
                    case "8" -> {
                        ArrayList<GameCard> hand;
                        String choiceAction = "0";
                        int choiceCard;
                        ArrayList<Boolean> side = new ArrayList<Boolean>(List.of(false, false, false));
                        int x;
                        int y;
                        while (!choiceAction.equals("2")) {
                            hand = printHand(finalPlayerName);
                            System.out.println("1) Change side");
                            System.out.println("2) Place card");
                            choiceAction = reader.readLine();
                            switch (choiceAction) {
                                case "1" -> {
                                    System.out.println("Of which card do you want to change the side?");
                                    choiceCard = Integer.parseInt(reader.readLine()) - 1;
                                    side.set(choiceCard, !side.get(choiceCard));
                                    hand.get(choiceCard).switchSide();
                                    System.out.println(hand.get(choiceCard).getCurrentSide());
                                }
                                case "2" -> {
                                    System.out.println("Of which card do you want to place?");
                                    choiceCard = Integer.parseInt(reader.readLine()) - 1;
                                    System.out.println("Where do you want to place it?");
                                    System.out.println("Coordniate x:");
                                    x = Integer.parseInt(reader.readLine());
                                    System.out.println("Coordniate y:");
                                    y = Integer.parseInt(reader.readLine());
                                    if (side.get(choiceCard))
                                        clientCommandMapper.switchCardSide(gameName, playerName, hand.get(choiceCard));
                                    clientCommandMapper.placeCard(gameName, playerName, new Coordinate(x, y), hand.get(choiceCard));
                                }
                            }
                        }
                        //TODO: gather arguments on updated VirtualView
                    }
                    case "9" -> {
                        ArrayList<GameCard> field = new ArrayList<>(clientCommandMapper.getView().gameView().globalBoardView().fieldGoldCards());
                        field.addAll(clientCommandMapper.getView().gameView().globalBoardView().fieldResourceCards());
                        System.out.println("Choose one of the cards on the field: ");
                        System.out.println("1) " + field.get(0));
                        System.out.println("2) " + field.get(1));
                        System.out.println("3) " + field.get(2));
                        System.out.println("4) " + field.get(3));
                        String choice = reader.readLine();
                        clientCommandMapper.drawCardFromField(gameName, playerName, field.get(Integer.parseInt(choice) - 1));
                    }
                    case "10" -> {
                        GameCard firstResourceCard = clientCommandMapper.getView().gameView().globalBoardView().resourceFirstCard();
                        System.out.println("The first resource card is: " + firstResourceCard.getCardColor());
                        System.out.println("Do you want to draw it? (y/n)");
                        String choice = reader.readLine();
                        if (choice.equalsIgnoreCase("y")) {
                            clientCommandMapper.drawCardFromResourceDeck(gameName, playerName);
                        }
                    }
                    case "11" -> {
                        GameCard firstGoldCard = clientCommandMapper.getView().gameView().globalBoardView().goldFirstCard();
                        System.out.println("The first gold card is: " + firstGoldCard.getCardColor());
                        System.out.println("Do you want to draw it? (y/n)");
                        String choice = reader.readLine();
                        if (choice.equalsIgnoreCase("y")) {
                            clientCommandMapper.drawCardFromGoldDeck(gameName, playerName);
                        }
                    }
                    case "12" -> {
                        HashMap<Coordinate, GameCard> map = clientCommandMapper.getView().gameView().playerViews().stream().filter(playerView -> playerView.playerName().equals(finalPlayerName)).findFirst().get().playerBoardView().playerBoard();
                        printBoardAsMatrix(map);
                    }
                    case "13" -> {
                        System.out.println("Exiting...");
                    }
                    default -> System.out.println("Invalid input");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<GameCard> printHand(String finalPlayerName) {
        int index = 0;
        ArrayList<GameCard> hand = clientCommandMapper.getView().gameView().playerViews().stream().filter(playerView -> playerView.playerName().equals(finalPlayerName)).findFirst().get().playerHandView().hand();
        System.out.println("Here is your hand:");
        for (GameCard gameCard : hand) {
            System.out.println(index + 1 + ")" + gameCard);
            index++;
        }
        return hand;
    }

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

