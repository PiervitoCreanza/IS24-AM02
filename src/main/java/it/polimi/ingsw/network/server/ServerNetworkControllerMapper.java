package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.controller.gameController.GameControllerMiddleware;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.server.actions.ClientToServerActions;
import it.polimi.ingsw.network.server.message.ChatServerToClientMessage;
import it.polimi.ingsw.network.server.message.ErrorServerToClientMessage;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import it.polimi.ingsw.network.server.message.successMessage.DeleteGameServerToClientMessage;
import it.polimi.ingsw.network.server.message.successMessage.GetGamesServerToClientMessage;
import it.polimi.ingsw.network.server.message.successMessage.UpdateViewServerToClientMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

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
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(ServerNetworkControllerMapper.class);


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
     * Gets the list of available games.
     *
     * @param messageHandler the ServerMessageHandler that will handle the response
     */
    @Override
    public void getGames(ServerMessageHandler messageHandler) {
        synchronized (gameConnectionMapper) {
            try {
                messageHandler.sendMessage(new GetGamesServerToClientMessage(mainController.getGameRecords()));
            } catch (Exception e) {
                messageHandler.sendMessage(new ErrorServerToClientMessage(e.getMessage()));
            }
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
    @Override
    public void createGame(ServerMessageHandler messageHandler, String gameName, String playerName, int nPlayers) {
        synchronized (gameConnectionMapper) {
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
    }

    /**
     * Allows a player to join a game.
     *
     * @param messageHandler the ServerMessageHandler that will handle the response
     * @param gameName       the name of the game to join
     * @param playerName     the name of the player joining the game
     */
    @Override
    public void joinGame(ServerMessageHandler messageHandler, String gameName, String playerName) {
        synchronized (gameConnectionMapper) {
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
    }

    /**
     * Leaves the game.
     *
     * @param messageHandler the ServerMessageHandler that will handle the response
     * @param gameName       the name of the game.
     * @param playerName     the name of the player who is leaving the game.
     */
    @Override
    public void deleteGame(ServerMessageHandler messageHandler, String gameName, String playerName) {
        synchronized (gameConnectionMapper) {
            try {
                mainController.isGameDeletable(gameName, playerName);
                broadcastMessage(gameName, new DeleteGameServerToClientMessage());
                // We close the connections. This will trigger the handleDisconnection method and so the game deletion.
                closeConnections(gameName);
            } catch (Exception e) {
                messageHandler.sendMessage(new ErrorServerToClientMessage(e.getMessage()));
            }
        }
    }

    /**
     * Chooses the color for a player.
     *
     * @param gameName    the name of the game.
     * @param playerName  the name of the player who is choosing the color.
     * @param playerColor the color to be chosen.
     */
    @Override
    public void choosePlayerColor(String gameName, String playerName, PlayerColorEnum playerColor) {
        synchronized (gameConnectionMapper.get(gameName)) {
            try {
                mainController.getGameController(gameName).choosePlayerColor(playerName, playerColor);
                broadcastMessage(gameName, new UpdateViewServerToClientMessage(mainController.getVirtualView(gameName)));
            } catch (Exception e) {
                gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerToClientMessage(e.getMessage()));
            }
        }
    }

    /**
     * Sets the objective for a player.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player whose objective is to be set.
     * @param cardId     the objective card to be set for the player.
     */
    @Override
    public void setPlayerObjective(String gameName, String playerName, int cardId) {
        synchronized (gameConnectionMapper.get(gameName)) {
            try {
                mainController.getGameController(gameName).setPlayerObjective(playerName, cardId);
                broadcastMessage(gameName, new UpdateViewServerToClientMessage(mainController.getVirtualView(gameName)));
            } catch (Exception e) {
                gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerToClientMessage(e.getMessage()));
            }
        }
    }

    /**
     * Places a card on the game field.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is placing the card.
     * @param coordinate the coordinate where the card should be placed.
     * @param cardId     the card to be placed.
     * @param isFlipped  whether the card is flipped or not.
     */
    @Override
    public void placeCard(String gameName, String playerName, Coordinate coordinate, int cardId, boolean isFlipped) {
        synchronized (gameConnectionMapper.get(gameName)) {
            try {
                GameControllerMiddleware gameController = mainController.getGameController(gameName);
                if (isFlipped) {
                    gameController.switchCardSide(playerName, cardId);
                }
                gameController.placeCard(playerName, coordinate, cardId);

                broadcastMessage(gameName, new UpdateViewServerToClientMessage(mainController.getVirtualView(gameName)));

            } catch (Exception e) {
                gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerToClientMessage(e.getMessage()));
            }
        }
    }

    /**
     * Draws a card from the game field.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     * @param card       the card to be drawn.
     */
    @Override
    public void drawCardFromField(String gameName, String playerName, GameCard card) {
        synchronized (gameConnectionMapper.get(gameName)) {
            try {
                mainController.getGameController(gameName).drawCardFromField(playerName, card);
                broadcastMessage(gameName, new UpdateViewServerToClientMessage(mainController.getVirtualView(gameName)));
            } catch (Exception e) {
                gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerToClientMessage(e.getMessage()));
            }
        }
    }

    /**
     * Draws a card from the resource deck.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     */
    @Override
    public void drawCardFromResourceDeck(String gameName, String playerName) {
        synchronized (gameConnectionMapper.get(gameName)) {
            try {
                mainController.getGameController(gameName).drawCardFromResourceDeck(playerName);
                broadcastMessage(gameName, new UpdateViewServerToClientMessage(mainController.getVirtualView(gameName)));
            } catch (Exception e) {
                gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerToClientMessage(e.getMessage()));
            }
        }
    }

    /**
     * Draws a card from the gold deck.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     */
    @Override
    public void drawCardFromGoldDeck(String gameName, String playerName) {
        synchronized (gameConnectionMapper.get(gameName)) {
            try {
                mainController.getGameController(gameName).drawCardFromGoldDeck(playerName);
                broadcastMessage(gameName, new UpdateViewServerToClientMessage(mainController.getVirtualView(gameName)));
            } catch (Exception e) {
                gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerToClientMessage(e.getMessage()));
            }
        }
    }

    /**
     * Switches the side of a card.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is switching the card side.
     * @param cardId     the card whose side is to be switched.
     */
    @Override
    public void switchCardSide(String gameName, String playerName, int cardId) {
        synchronized (gameConnectionMapper.get(gameName)) {
            try {
                mainController.getGameController(gameName).switchCardSide(playerName, cardId);
                broadcastMessage(gameName, new UpdateViewServerToClientMessage(mainController.getVirtualView(gameName)));
            } catch (Exception e) {
                gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerToClientMessage(e.getMessage()));
            }
        }
    }

    /**
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is sending the chat message.
     * @param message    the chat message to be sent.
     * @param receiver   the receiver of the message. If this is null, the message is not a direct message.
     * @param timestamp  the timestamp of the message.
     */
    @Override
    public void sendChatMessage(String gameName, String playerName, String message, String receiver, long timestamp) {
        synchronized (gameConnectionMapper.get(gameName)) {
            // The message is converted to a ChatServerToClientMessage and sent to all clients excluding the sender.
            ChatServerToClientMessage convertedMessage = new ChatServerToClientMessage(playerName, message, timestamp, !receiver.isEmpty());

            if (convertedMessage.isDirectMessage()) {
                //It is sent only to the receiver and the sender.
                ServerMessageHandler receiverHandler = gameConnectionMapper.get(gameName).get(receiver);
                ServerMessageHandler senderHandler = gameConnectionMapper.get(gameName).get(playerName);
                if (receiverHandler != null) {
                    receiverHandler.sendMessage(convertedMessage);
                    if (!receiver.equals(playerName))
                        senderHandler.sendMessage(convertedMessage);
                } else {
                    senderHandler.sendMessage(new ErrorServerToClientMessage("The player you are trying to send a message to is not in the game."));
                }
            } else {
                //It is broadcast to any player of the game (even the sender) to avoid code redundancy client-side.
                broadcastMessage(gameName, convertedMessage);
            }
        }
    }

    /**
     * Disconnects a player from a game.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is disconnecting.
     */
    @Override
    public void disconnect(String gameName, String playerName) {
        synchronized (gameConnectionMapper.get(gameName)) {
            gameConnectionMapper.get(gameName).get(playerName).closeConnection();
        }
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
     * Closes all connections for a game.
     *
     * @param gameName the name of the game
     */
    private void closeConnections(String gameName) {
        for (ServerMessageHandler messageHandler : gameConnectionMapper.get(gameName).values()) {
            messageHandler.closeConnection();
        }
    }

    /**
     * Handles the disconnection of a client.
     * If all clients in a game have disconnected, the game is deleted.
     *
     * @param gameName   the name of the game
     * @param playerName the name of the player who disconnected
     */
    public void handleDisconnection(String gameName, String playerName) {
        synchronized (gameConnectionMapper.get(gameName)) {

            gameConnectionMapper.get(gameName).remove(playerName);
            mainController.getGameController(gameName).setPlayerConnectionStatus(playerName, false);

            logger.debug("Player {} disconnected from game {}. Remaining players: {}", playerName, gameName, gameConnectionMapper.get(gameName).size());

            if (gameConnectionMapper.get(gameName).size() == 1) {
                startLastPlayerTimeout(gameName);
            }
            // If the game is now empty we delete it.
            if (gameConnectionMapper.get(gameName).isEmpty()) {
                // Delete the game
                mainController.deleteGame(gameName);

                gameConnectionMapper.remove(gameName);
                logger.debug("Game {} deleted.", gameName);
                return;
            }
            // If the game was not deleted we update the view for the remaining players.
            broadcastMessage(gameName, new UpdateViewServerToClientMessage(mainController.getVirtualView(gameName)));
        }
    }

    /**
     * Starts a timer that will delete the game if there is only one player left in it.
     *
     * @param gameName the name of the game
     */
    private void startLastPlayerTimeout(String gameName) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // If after 30 seconds there is still only one player in the game we close the connections and delete the game.
                synchronized (gameConnectionMapper.get(gameName)) {
                    HashMap<String, ServerMessageHandler> gameConnections = gameConnectionMapper.get(gameName);
                    if (gameConnections != null && gameConnections.size() == 1) {
                        broadcastMessage(gameName, new DeleteGameServerToClientMessage());
                        closeConnections(gameName);
                    }
                }
            }
        }, 30000); // Delay in milliseconds
    }
}