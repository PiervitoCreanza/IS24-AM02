package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.message.ClientToServerMessage;
import it.polimi.ingsw.network.server.actions.ClientToServerActions;
import it.polimi.ingsw.network.server.message.ErrorServerToClientMessage;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import it.polimi.ingsw.network.server.message.chatMessageServerToClientMessage;
import it.polimi.ingsw.network.server.message.successMessage.DeleteGameServerToClientMessage;
import it.polimi.ingsw.network.server.message.successMessage.GetGamesServerToClientMessage;
import it.polimi.ingsw.network.server.message.successMessage.UpdateViewServerToClientMessage;

import java.util.HashMap;

/**
 * The ServerNetworkControllerMapper class is responsible for mapping network commands to actions in the game.
 * It implements the ClientToServerActions interface, which defines the actions that a client can perform.
 */
public class ServerNetworkControllerMapper implements ClientToServerActions {

    /**
     * The MainController object is used to control the main aspects of the game.
     */
    private final MainController mainController;


    /**
     * A map that associates game names with sets of ServerMessageHandler objects.
     * This is used to keep track of the connections for each game.
     */
    // Mapping a ServerMessageHandler (TCP or RMI connection via polimorphism) to a player,
    // and then this "playerName:connectionType" is mapped to each game
    private final HashMap<String, HashMap<String, ServerMessageHandler>> gameConnectionMapper;

    /**
     * Constructs a new ServerNetworkControllerMapper object with the specified MainController.
     *
     * @param mainController the MainController to be used by the ServerNetworkControllerMapper
     */
    public ServerNetworkControllerMapper(MainController mainController) {
        this.mainController = mainController;
        this.gameConnectionMapper = new HashMap<>();
    }

    /**
     * Sends a message to all players in a game.
     *
     * @param gameName the name of the game
     * @param message  the message to be sent
     */
    private void broadcastMessage(String gameName, ServerToClientMessage message) {
        for (ServerMessageHandler messageHandler : gameConnectionMapper.get(gameName).values()) {
            messageHandler.sendMessage(message);
        }
    }

    /**
     * Gets the list of available games.
     *
     * @param messageHandler the ServerMessageHandler that will handle the response
     */
    public void getGames(ServerMessageHandler messageHandler) {
        try {
            messageHandler.sendMessage(new GetGamesServerToClientMessage(mainController.getGameRecords()));
        } catch (Exception e) {
            messageHandler.sendMessage(new ErrorServerToClientMessage(e.getMessage()));
        }
    }

    /**
     * Creates a new game with the specified name and number of players.
     *
     * @param messageHandler the ServerMessageHandler that will handle the response
     * @param gameName       the name of the game to be created
     * @param playerName     the name of the player creating the game
     * @param nPlayers       the number of players in the game
     */

    public void createGame(ServerMessageHandler messageHandler, String gameName, String playerName, int nPlayers) {
        try {
            mainController.createGame(gameName, playerName, nPlayers);
            messageHandler.setGameName(gameName);
            messageHandler.setPlayerName(playerName);
            messageHandler.connectionSaved(true);

            gameConnectionMapper.put(gameName, new HashMap<>());
            gameConnectionMapper.get(gameName).put(playerName, messageHandler);

            broadcastMessage(gameName, new UpdateViewServerToClientMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            messageHandler.sendMessage(new ErrorServerToClientMessage(e.getMessage()));
        }
    }

    /**
     * Allows a player to join a game.
     *
     * @param messageHandler the ServerMessageHandler that will handle the response
     * @param gameName       the name of the game to join
     * @param playerName     the name of the player joining the game
     */
    public void joinGame(ServerMessageHandler messageHandler, String gameName, String playerName) {
        try {
            mainController.joinGame(gameName, playerName);

            messageHandler.setGameName(gameName);
            messageHandler.setPlayerName(playerName);
            messageHandler.connectionSaved(true);

            gameConnectionMapper.get(gameName).put(playerName, messageHandler);
            broadcastMessage(gameName, new UpdateViewServerToClientMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            messageHandler.sendMessage(new ErrorServerToClientMessage(e.getMessage()));
        }
    }

    /**
     * Leaves the game.
     *
     * @param messageHandler the ServerMessageHandler that will handle the response
     * @param gameName       the name of the game.
     * @param playerName     the name of the player who is leaving the game.
     */
    public void deleteGame(ServerMessageHandler messageHandler, String gameName, String playerName) {
        try {
            mainController.isGameDeletable(gameName, playerName);
            broadcastMessage(gameName, new DeleteGameServerToClientMessage());
            // We close the connections. This will trigger the handleDisconnection method and so the game deletion.
            closeConnections(gameName);
        } catch (Exception e) {
            messageHandler.sendMessage(new ErrorServerToClientMessage(e.getMessage()));
        }
    }

    /**
     * Chooses the color for a player.
     *
     * @param gameName    the name of the game.
     * @param playerName  the name of the player who is choosing the color.
     * @param playerColor the color to be chosen.
     */
    public void choosePlayerColor(String gameName, String playerName, PlayerColorEnum playerColor) {
        try {
            mainController.getGameController(gameName).choosePlayerColor(playerName, playerColor);
            broadcastMessage(gameName, new UpdateViewServerToClientMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerToClientMessage(e.getMessage()));
        }
    }

    /**
     * Sets the objective for a player.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player whose objective is to be set.
     * @param card       the objective card to be set for the player.
     */
    public void setPlayerObjective(String gameName, String playerName, ObjectiveCard card) {
        try {
            mainController.getGameController(gameName).setPlayerObjective(playerName, card);
            broadcastMessage(gameName, new UpdateViewServerToClientMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerToClientMessage(e.getMessage()));
        }
    }

    /**
     * Places a card on the game field.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is placing the card.
     * @param coordinate the coordinate where the card should be placed.
     * @param card       the card to be placed.
     */
    public void placeCard(String gameName, String playerName, Coordinate coordinate, GameCard card) {
        try {
            mainController.getGameController(gameName).placeCard(playerName, coordinate, card);
            broadcastMessage(gameName, new UpdateViewServerToClientMessage(mainController.getVirtualView(gameName)));

            // If the game is over we close the connections and delete the game.
            if (mainController.getGameController(gameName).getGameStatus().equals(GameStatusEnum.GAME_OVER)) {
                closeConnections(gameName);
            }
        } catch (Exception e) {
            gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerToClientMessage(e.getMessage()));
        }
    }

    /**
     * Draws a card from the game field.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     * @param card       the card to be drawn.
     */
    public void drawCardFromField(String gameName, String playerName, GameCard card) {
        try {
            mainController.getGameController(gameName).drawCardFromField(playerName, card);
            broadcastMessage(gameName, new UpdateViewServerToClientMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerToClientMessage(e.getMessage()));
        }
    }

    /**
     * Draws a card from the resource deck.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     */
    public void drawCardFromResourceDeck(String gameName, String playerName) {
        try {
            mainController.getGameController(gameName).drawCardFromResourceDeck(playerName);
            broadcastMessage(gameName, new UpdateViewServerToClientMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerToClientMessage(e.getMessage()));
        }
    }

    /**
     * Draws a card from the gold deck.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     */
    public void drawCardFromGoldDeck(String gameName, String playerName) {
        try {
            mainController.getGameController(gameName).drawCardFromGoldDeck(playerName);
            broadcastMessage(gameName, new UpdateViewServerToClientMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerToClientMessage(e.getMessage()));
        }
    }

    /**
     * Switches the side of a card.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is switching the card side.
     * @param card       the card whose side is to be switched.
     */
    public void switchCardSide(String gameName, String playerName, GameCard card) {
        try {
            mainController.getGameController(gameName).switchCardSide(playerName, card);
            broadcastMessage(gameName, new UpdateViewServerToClientMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerToClientMessage(e.getMessage()));
        }
    }

    /**
     * Handles the disconnection of a client.
     * If all clients in a game have disconnected, the game is deleted.
     *
     * @param messageHandler the ServerMessageHandler that has been disconnected
     */
    public void handleDisconnection(ServerMessageHandler messageHandler) {
        String gameName = messageHandler.getGameName();

        gameConnectionMapper.get(gameName).remove(messageHandler.getPlayerName());
        mainController.getGameController(gameName).setPlayerConnectionStatus(messageHandler.getPlayerName(), false);

        System.out.println("[Server] Player " + messageHandler.getPlayerName() + " disconnected from game " + gameName + ". Remaining players: " + gameConnectionMapper.get(gameName).size());
        // If the game is now empty we delete it.
        if (gameConnectionMapper.get(gameName).isEmpty()) {
            // Delete the game
            mainController.deleteGame(gameName);

            gameConnectionMapper.remove(gameName);
            System.out.println("[Server] Game " + gameName + " deleted.");
            return;
        }
        // If the game was not deleted we update the view for the remaining players.
        broadcastMessage(gameName, new UpdateViewServerToClientMessage(mainController.getVirtualView(gameName)));
    }

    private void closeConnections(String gameName) {
        for (ServerMessageHandler messageHandler : gameConnectionMapper.get(gameName).values()) {
            messageHandler.closeConnection();
        }
    }

    /**
     * @param message
     */
    @Override
    public void sendChatMessage(ClientToServerMessage message) {
        //Extract the gameName from the message
        String gameName = message.getGameName();

        chatMessageServerToClientMessage convertedMessage = new chatMessageServerToClientMessage(message.getPlayerName(), message.getMessage(), message.getReceiver());

        if (convertedMessage.isDirectMessage()) {
            //It is sent only to the receiver and the sender.
            ServerMessageHandler receiverHandler = gameConnectionMapper.get(gameName).get(message.getReceiver());
            if (receiverHandler != null) {
                receiverHandler.sendMessage(convertedMessage);
            } else {
                gameConnectionMapper.get(gameName).get(message.getPlayerName()).sendMessage(new ErrorServerToClientMessage("The player you are trying to send a message to is not in the game."));
            }
        } else {
            //It is broadcasted to any player of the game (even the sender) to avoid code redundancy client-side.
            broadcastMessage(gameName, convertedMessage);
        }


    }
}