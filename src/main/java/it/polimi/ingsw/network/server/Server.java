package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.network.server.RMI.RMIServerReceiver;
import it.polimi.ingsw.network.server.TCP.TCPServerAdapter;
import it.polimi.ingsw.network.server.actions.RMIClientToServerActions;
import it.polimi.ingsw.tui.utils.Utils;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
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
    public static long HEARTBEAT_TIMEOUT = 2500; //Static Instance of the timeout for the heartbeat
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
        int TCPPortNumber = Integer.parseInt(cmd.getOptionValue("TCP_P", "12345")); // default is 12345
        int RMIPortNumber = Integer.parseInt(cmd.getOptionValue("RMI_P", "1099"));
        String serverIp;
        if (cmd.hasOption("debug")) {
            System.out.println(Utils.ANSI_PURPLE + "Start the Server in DEBUG mode. This sets the timeout to 10 minutes and prints all log messages" + Utils.ANSI_RESET);
            HEARTBEAT_TIMEOUT = 600000; //if debug, set the timeout to 10 minutes
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

        System.out.println(Utils.ANSI_PURPLE + "ServerApp IP: " + serverIp + Utils.ANSI_RESET);
        /* ***************************************
         * START RMI SERVER
         * ***************************************/
        RMIServerStart(serverNetworkControllerMapper, RMIPortNumber, serverIp);
        /* ***************************************
         * START TCP SERVER
         * ***************************************/
        TCPServerStart(serverNetworkControllerMapper, TCPPortNumber);
    }

    private static CommandLine parseCommandLineArgs(String[] args) {
        Options options = new Options();
        options.addOption("TCP_P", true, "TCP ServerApp Port number (default is 12345).");
        options.addOption("RMI_P", true, "RMI ServerApp Port number (default is 1099).");
        options.addOption("IP", true, "RMI ServerApp server external IP (default is localhost).");
        options.addOption("lan", "Start the server with his lan ip address.");
        options.addOption("debug", "Start the Server in DEBUG mode. This sets the timeout to 10 minutes and prints all log messages");

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

    private static void RMIServerStart(ServerNetworkControllerMapper serverNetworkControllerMapper, Integer RMIPortNumber, String serverIp) {
        try {
            //TODO Conti fixit
            System.setProperty("java.rmi.server.hostname", serverIp);

            RMIServerReceiver rmiServerReceiver = new RMIServerReceiver(serverNetworkControllerMapper);
            RMIClientToServerActions stub = (RMIClientToServerActions) UnicastRemoteObject.exportObject(rmiServerReceiver, RMIPortNumber);
            Registry registry = LocateRegistry.createRegistry(RMIPortNumber);
            registry.bind("ClientToServerActions", stub);
            System.err.println(Utils.ANSI_YELLOW + "Codex Naturalis RMI Server ready on port: " + RMIPortNumber + Utils.ANSI_RESET);

        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    private static void TCPServerStart(ServerNetworkControllerMapper serverNetworkControllerMapper, Integer TCPPortNumber) {
        try (ServerSocket serverSocket = new ServerSocket(TCPPortNumber)) {
            System.out.println(Utils.ANSI_BLUE + "Codex Naturalis TCP Server ready on port: " + TCPPortNumber + Utils.ANSI_RESET);
            while (true) {
                TCPServerAdapter messageHandler = new TCPServerAdapter(serverSocket.accept(), serverNetworkControllerMapper);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + TCPPortNumber);
            System.exit(-1);
        }
    }
}
