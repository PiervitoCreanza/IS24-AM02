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
import it.polimi.ingsw.network.server.message.ErrorServerMessage;
import it.polimi.ingsw.network.server.message.ServerMessage;
import it.polimi.ingsw.network.server.message.successMessage.ServerActionsEnum;
import it.polimi.ingsw.network.server.message.successMessage.SuccessServerMessage;
import it.polimi.ingsw.network.server.message.successMessage.ViewUpdateMessage;

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
    public void createGame(Connection connection, String gameName, int nPlayers) {
        try {
            Game game = mainController.createGame(gameName, nPlayers, gameName);
            // TODO: Pass data to ServerMessage
            broadcastMessage(gameName, new SuccessServerMessage(ServerActionsEnum.CREATE_GAME));
        } catch (Exception e) {
            broadcastMessage(gameName, new ErrorServerMessage(e.getMessage()));
        }
    }

    @Override
    public void joinGame(Connection connection, String gameName, String playerName) {
        try {
            mainController.joinGame(gameName, playerName);
            gameConnectionMapper.get(gameName).add(connection);
            broadcastMessage(gameName, new ViewUpdateMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            broadcastMessage(gameName, new ErrorServerMessage(e.getMessage()));
        }
    }

    private void broadcastMessage(String gameName, ServerMessage message) {
        for (Connection connection : gameConnectionMapper.get(gameName)) {
            connection.sendMessage(message);
        }
    }
}