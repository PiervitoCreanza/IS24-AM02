package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.RepresentedGame;
import it.polimi.ingsw.network.client.message.ClientMessage;
import it.polimi.ingsw.network.messages.ChosenCardMessage;
import it.polimi.ingsw.network.server.TCP.TCPClientConnectionHandler;
import it.polimi.ingsw.network.server.message.ClientCommandMessage;
import it.polimi.ingsw.network.messages.CreateGameMessage;
import it.polimi.ingsw.network.messages.JoinGameMessage;
import it.polimi.ingsw.network.server.message.ServerMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;


public class NetworkCommandMapper implements ClientActions {

    private final MainController mainController;

    private final HashSet<Connection> connections = new HashSet<>();

    private final HashMap<String, HashSet<Connection>> gameConnectionMapper = new HashMap<>();


    public NetworkCommandMapper(MainController mainController) {
        this.mainController = mainController;
    }


    private static ClientCommandMessage jsonToMessageObjBuilder(String jsonString) {
        // Attempt to convert the JSON string to a CreateGameMessage object.
        CreateGameMessage createGameMessage = null;
        try {
            createGameMessage = CreateGameMessage.createGameMessageFromJson(jsonString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (createGameMessage != null) {
            return createGameMessage;
        }

        // Attempt to convert the JSON string to a JoinGameMessage object.
        JoinGameMessage joinGameMessage = null;
        try {
            joinGameMessage = JoinGameMessage.joinGameFromJson(jsonString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (joinGameMessage != null) {
            return joinGameMessage;
        }

        // Attempt to convert the JSON string to a ChosenCardMessage object.
        ChosenCardMessage chosenCardMessage = ChosenCardMessage.chosenCardMessageFromJson(jsonString);
        return chosenCardMessage;
    }


    public String parse(String jsonString) {
        ClientCommandMessage parsedMessage = jsonToMessageObjBuilder(jsonString);
        System.out.println(parsedMessage); // Print the type of the parsed message to the console.

        if (parsedMessage == null) {
            return "{\"message\" : \"ko, WRONG\"}";
        }

        // Perform an action based on the type of the parsed message.
        // We use 'instanceof' to determine the type of the parsed message. This approach is also used for RMI.
        // It's important to check child classes BEFORE the parent class due to inheritance in 'CreateGameMessage'.
        if (parsedMessage instanceof CreateGameMessage msg) {
            try {
                Game game = mainController.createGame(msg.getGameName(), msg.nPlayers, msg.getPlayerName());
                return new RepresentedGame(game).toJSON();
            } catch (Exception e) {
                return answerMsgToJSON(e.getMessage());
            }
        } else if (parsedMessage instanceof JoinGameMessage msg) {
            try {
                mainController.joinGame(msg.getGameName(), msg.getPlayerName());
            } catch (Exception e) {
                return answerMsgToJSON(e.getMessage());
            }
        } else if (parsedMessage instanceof ChosenCardMessage msg) {
            return answerMsgToJSON("chosenCardMessage");
        }

        return "{\"message\" : \"ok\"}";
    }


    private static String answerMsgToJSON(String answer) {
        return "{\"message\" : \"" + answer + "\"}";
    }

    public void addConnection(TCPClientConnectionHandler connection) {
        connections.add(connection);
    }

    public void removeConnection(TCPClientConnectionHandler connection) {
        connections.remove(connection);
    }

    @Override
    public ServerMessage createGame(Connection connection, String gameName, int nPlayers) {
        try {
            Game game = mainController.createGame(gameName, nPlayers, gameName);
            // TODO: Pass data to ServerMessage
            return new ServerMessage();
        } catch (Exception e) {
            return new ServerMessage();
        }
    }

    @Override
    public ServerMessage joinGame(Connection connection, String gameName, String playerName) {
        try {
            mainController.joinGame(gameName, playerName);
            gameConnectionMapper.get(gameName).add(connection);
            return new ServerMessage();
        } catch (Exception e) {
            return new ServerMessage();
        }
    }

    private void broadcastMessage(String gameName, ServerMessage message) {
        for (Connection connection : gameConnectionMapper.get(gameName)) {
            connection.sendMessage(message);
        }
    }

    public 
}