package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.network.Utils;
import it.polimi.ingsw.network.server.TCP.TCPServerAdapter;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class is responsible for setting up and running the TCP and RMI servers.
 * It listens for incoming client connections and handles each one in a separate thread.
 */
public class Server {
    /**
     * The entry point of the application.
     *
     * @param args the input arguments. If a single argument is provided, it is used as the port number for the server.
     */
    public static void main(String[] args) throws RemoteException {
        /* ***************************************
         * INSTANTIATE MAIN CONTROLLER and NETWORK COMMAND MAPPER
         * ***************************************/
        MainController mainController = new MainController();
        NetworkCommandMapper networkCommandMapper = new NetworkCommandMapper(mainController);

        /* ***************************************
         * CLI ARGUMENTS PARSING
         * ***************************************/
        CommandLine cmd = parseCommandLineArgs(args);
        int TCPPortNumber = Integer.parseInt(cmd.getOptionValue("TCP_P", "12345")); // default is 12345
        int RMIPortNumber = Integer.parseInt(cmd.getOptionValue("RMI_P", "1099"));
        String serverIp = cmd.getOptionValue("IP", "localhost");

        /* ***************************************
         * START RMI SERVER
         * ***************************************/
        RMIServerStart(networkCommandMapper, RMIPortNumber, serverIp);
        /* ***************************************
         * START TCP SERVER
         * ***************************************/
        TCPServerStart(networkCommandMapper, TCPPortNumber);
    }

    private static CommandLine parseCommandLineArgs(String[] args) {
        Options options = new Options();
        options.addOption("TCP_P", true, "TCP ServerApp Port number (default is 12345).");
        options.addOption("RMI_P", true, "RMI ServerApp Port number (default is 1099).");
        options.addOption("IP", true, "RMI ServerApp server external IP (default is localhost).");

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            if (!cmd.hasOption("TCP_P") || !cmd.hasOption("RMI_P")) {
                System.err.println("Please specify both ports for TCP and RMI ServerApps.");
                System.exit(1);
            }
            if (Integer.parseInt(cmd.getOptionValue("TCP_P")) == Integer.parseInt(cmd.getOptionValue("RMI_P"))) {
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

    private static void RMIServerStart(NetworkCommandMapper networkCommandMapper, Integer RMIPortNumber, String serverIp) {
        try {
            //TODO Conti fixit
            System.setProperty("java.rmi.server.hostname", serverIp);

            RMIServerConnectionHandler rmiServerConnectionHandler = new RMIServerConnectionHandler(networkCommandMapper);
            RMIClientActions stub = (RMIClientActions) UnicastRemoteObject.exportObject(rmiServerConnectionHandler, RMIPortNumber);
            Registry registry = LocateRegistry.createRegistry(RMIPortNumber);
            registry.bind("ClientActions", stub);
            System.err.println(Utils.ANSI_YELLOW + "Codex Naturalis RMI Server ready on port: " + RMIPortNumber + Utils.ANSI_RESET);

        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    private static void TCPServerStart(NetworkCommandMapper networkCommandMapper, Integer TCPPortNumber) {
        try (ServerSocket serverSocket = new ServerSocket(TCPPortNumber)) {
            System.out.println(Utils.ANSI_BLUE + "Codex Naturalis TCP Server ready on port: " + TCPPortNumber + Utils.ANSI_RESET);
            while (true) {
                TCPServerAdapter messageHandler = new TCPServerAdapter(serverSocket.accept(), networkCommandMapper);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + TCPPortNumber);
            System.exit(-1);
        }
    }
}
