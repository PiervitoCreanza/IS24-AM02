package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.server.message.ErrorServerMessage;
import it.polimi.ingsw.network.server.message.ServerMessage;
import it.polimi.ingsw.network.server.message.successMessage.DeleteGameServerMessage;
import it.polimi.ingsw.network.server.message.successMessage.GetGamesServerMessage;
import it.polimi.ingsw.network.server.message.successMessage.UpdateViewServerMessage;

import java.util.HashMap;

/**
 * The NetworkCommandMapper class is responsible for mapping network commands to actions in the game.
 * It implements the ClientActions interface, which defines the actions that a client can perform.
 */
public class NetworkCommandMapper {

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
     * Constructs a new NetworkCommandMapper object with the specified MainController.
     *
     * @param mainController the MainController to be used by the NetworkCommandMapper
     */
    public NetworkCommandMapper(MainController mainController) {
        this.mainController = mainController;
        this.gameConnectionMapper = new HashMap<>();
    }

    /**
     * Sends a message to all players in a game.
     *
     * @param gameName the name of the game
     * @param message  the message to be sent
     */
    private void broadcastMessage(String gameName, ServerMessage message) {
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
            messageHandler.sendMessage(new GetGamesServerMessage(mainController.getGameRecords()));
        } catch (Exception e) {
            messageHandler.sendMessage(new ErrorServerMessage(e.getMessage()));
        }
    }

    /**
     * Creates a new game with the specified name and number of players.
     *
     * @param messageHandler the ServerMessageHandler that will handle the response
     * @param gameName       the name of the game to be created
     * @param nPlayers       the number of players in the game
     */

    public void createGame(ServerMessageHandler messageHandler, String gameName, String playerName, int nPlayers) {
        try {
            mainController.createGame(gameName, playerName, nPlayers);
            //TODO Why these properties weren't set in create game? @Pier
            messageHandler.setGameName(gameName);
            messageHandler.setPlayerName(playerName);

            gameConnectionMapper.put(gameName, new HashMap<>());
            gameConnectionMapper.get(gameName).put(playerName, messageHandler);

            broadcastMessage(gameName, new UpdateViewServerMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            messageHandler.sendMessage(new ErrorServerMessage(e.getMessage()));
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
            gameConnectionMapper.get(gameName).put(playerName, messageHandler);
            broadcastMessage(gameName, new UpdateViewServerMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            messageHandler.sendMessage(new ErrorServerMessage(e.getMessage()));
        }
    }

    /**
     * Leaves the game.
     *
     * @param gameName the name of the game.
     */
    public void deleteGame(ServerMessageHandler messageHandler, String gameName) {
        try {
            //TODO : Need player name of the Host
            mainController.deleteGame(gameName);
            broadcastMessage(gameName, new DeleteGameServerMessage());
            // TODO: Close connections
            gameConnectionMapper.remove(gameName);
        } catch (Exception e) {
            messageHandler.sendMessage(new ErrorServerMessage(e.getMessage()));
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
            broadcastMessage(gameName, new UpdateViewServerMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerMessage(e.getMessage()));
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
            broadcastMessage(gameName, new UpdateViewServerMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerMessage(e.getMessage()));
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
            broadcastMessage(gameName, new UpdateViewServerMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerMessage(e.getMessage()));
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
            broadcastMessage(gameName, new UpdateViewServerMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerMessage(e.getMessage()));
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
            broadcastMessage(gameName, new UpdateViewServerMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerMessage(e.getMessage()));
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
            broadcastMessage(gameName, new UpdateViewServerMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerMessage(e.getMessage()));
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
            broadcastMessage(gameName, new UpdateViewServerMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            gameConnectionMapper.get(gameName).get(playerName).sendMessage(new ErrorServerMessage(e.getMessage()));
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
            mainController.deleteGame(gameName);
            gameConnectionMapper.remove(gameName);
            System.out.println("[Server] Game " + gameName + " deleted.");
        }
    }
}