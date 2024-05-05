package it.polimi.ingsw.network.client;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.network.client.message.mainController.CreateGameClientMessage;
import it.polimi.ingsw.network.client.message.mainController.GetGamesClientMessage;
import it.polimi.ingsw.network.client.message.mainController.JoinGameClientMessage;
import it.polimi.ingsw.network.server.RMIClientActions;
import org.apache.commons.cli.*;

import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


//TODO: start RMI ClientAsAServer service
public class Client {
    public static void main(String[] args) {
        CommandLine cmd = parseCommandLineArgs(args);
        ClientCommandMapper clientCommandMapper = new ClientCommandMapper();

        // get values from options
        String connectionType = cmd.getOptionValue("c");
        String ipAddress = cmd.getOptionValue("ip", "localhost"); // default is localhost
        int portNumber = Integer.parseInt(cmd.getOptionValue("p", (connectionType.equals("TCP") ? "12345" : "1099")));


        switch (connectionType.toLowerCase()) {
            case "tcp" -> {
                System.out.println(Utils.ANSI_BLUE + "Started a TCP connection with IP: " + ipAddress + " on port: " + portNumber + Utils.ANSI_RESET);
                startTCPClient(clientCommandMapper, ipAddress, portNumber);
            }
            case "rmi" -> {
                System.out.println(Utils.ANSI_YELLOW + "Started an RMI connection with IP: " + ipAddress + " on port: " + portNumber + Utils.ANSI_RESET);
                startRMIClient(clientCommandMapper, ipAddress, portNumber);
            }
            default -> {
                System.err.println("Invalid connection type. Please specify either TCP or RMI.");
            }
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

    private static void startTCPClient(ClientCommandMapper clientCommandMapper, String ipAddress, int portNumber) {
        try {
            System.out.println("Hello, this is the client!");
            Socket serverSocket = new Socket(ipAddress, portNumber);
            // Print configuration
            System.err.println("Starting client  with connection to server at " + ipAddress + " on port " + portNumber);


            TCPClientAdapter clientAdapter = new TCPClientAdapter(serverSocket, clientCommandMapper);


            clientCommandMapper.setMessageHandler(clientAdapter);
            //Request the list of games
            clientAdapter.sendMessage(new GetGamesClientMessage());
            //Trying to join a non-existing game
            //clientAdapter.sendMessage(new JoinGameClientMessage("game_inesistente", "Marco"));
            //Trying to create a game with a wrong maxPlayers number
            //clientAdapter.sendMessage(new CreateGameClientMessage("pippo", 6, "Marco"));
            // Create a valid game
            clientAdapter.sendMessage(new CreateGameClientMessage("pippo", 3, "Simone"));
            //Request the list of active games again
            clientAdapter.sendMessage(new GetGamesClientMessage());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startRMIClient(ClientCommandMapper clientCommandMapper, String ipAddress, int portNumber) {
        // Getting the registry
        //TODO: save serverStub -> stub chiamato dal client per eseguire metodi remoti
        //TODO: save thisClientStub -> stub esposto al server dal client
        try {
            ServerActions rmiClientConnectionHandler = new RMIClientConnectionHandler(clientCommandMapper);
            //Client as a client, getting the registry
            Registry registry = LocateRegistry.getRegistry(ipAddress, portNumber);
            // Looking up the registry for the remote object
            RMIClientActions serverStub = (RMIClientActions) registry.lookup("ClientActions");
            RMIClientAdapter rmiClientAdapter = new RMIClientAdapter(serverStub, rmiClientConnectionHandler);
            clientCommandMapper.setMessageHandler(rmiClientAdapter); //Adding stub to the mapper

            //Test: should return a list of empty games
            rmiClientAdapter.sendMessage(new GetGamesClientMessage());
            //Trying to join a non-existing game
            rmiClientAdapter.sendMessage(new JoinGameClientMessage("pippo", "s"));
            //Trying to create a game with a wrong maxPlayers number
            rmiClientAdapter.sendMessage(new CreateGameClientMessage("pippo", 6, "Marco"));
            // Create a valid game
            rmiClientAdapter.sendMessage(new CreateGameClientMessage("pippo", 3, "Simone"));
            //Request the list of active games again
            rmiClientAdapter.sendMessage(new GetGamesClientMessage());

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input = null;
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
                String player = "";

                switch (input) {
                    case "1":
                        clientAdapter.sendMessage(new GetGamesClientMessage());
                        break;
                    case "2":
                        System.out.println("Enter game name to create: ");
                        gameName = reader.readLine();
                        System.out.println("Enter number of players: ");
                        numPlayers = reader.readLine();
                        System.out.println("Enter player name: ");
                        player = reader.readLine();
                        clientAdapter.sendMessage(new CreateGameClientMessage(gameName, Integer.parseInt(numPlayers), player));
                        break;
                    case "3":
                        System.out.println("Enter game name to delete: ");
                        gameName = reader.readLine();
                        System.out.println("Enter player name: ");
                        player = reader.readLine();
                        clientAdapter.sendMessage(new DeleteGameClientMessage(gameName, player));
                        break;
                    case "4":
                        System.out.println("Enter game name to join: ");
                        gameName = reader.readLine();
                        System.out.println("Enter player name: ");
                        player = reader.readLine();
                        clientAdapter.sendMessage(new JoinGameClientMessage(gameName, player));
                        break;
                    case "5":
                        clientAdapter.sendMessage(new ChoosePlayerColorClientMessage(PlayerColorEnum.RED, player, gameName));
                        break;
                    case "6":
                        System.out.print("Not implemented");
                        //clientAdapter.sendMessage(new SetPlayerObjectiveClientMessage(gameName, player, ));
                        break;
                    case "7":
                        System.out.print("Not implemented");
                        //clientAdapter.sendMessage(new PlaceCardClientMessage());
                        break;
                    case "8":
                        System.out.print("Not implemented");
                        //clientAdapter.sendMessage(new DrawCardFromFieldClientMessage());
                        break;
                    case "9":
                        System.out.print("Not implemented");
                        //clientAdapter.sendMessage(new DrawCardFromResourceDeckClientMessage());
                        break;
                    case "10":
                        System.out.print("Not implemented");
                        //clientAdapter.sendMessage(new DrawCardFromGoldDeckClientMessage());
                        break;
                    case "11":
                        System.out.print("Not implemented");
                        //clientAdapter.sendMessage(new SwitchCardSideClientMessage());
                        break;
                    case "12":
                        System.out.println("Exiting...");
                        clientAdapter.closeConnection();
                        break;
                    default:
                        System.out.println("Invalid input");
                }
            }


        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void receive(String message) throws RemoteException {
        System.out.println(message);
    }


}

