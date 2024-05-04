package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.server.message.ErrorServerMessage;
import it.polimi.ingsw.network.server.message.ServerMessage;
import it.polimi.ingsw.network.server.message.successMessage.ServerActionsEnum;
import it.polimi.ingsw.network.server.message.successMessage.SuccessServerMessage;
import it.polimi.ingsw.network.server.message.successMessage.ViewUpdateMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * The NetworkCommandMapper class is responsible for mapping network commands to actions in the game.
 * It implements the ClientActions interface, which defines the actions that a client can perform.
 */
public class NetworkCommandMapper implements ClientActions {

    /**
     * The MainController object is used to control the main aspects of the game.
     */
    private final MainController mainController;

    /**
     * A map that associates game names with sets of MessageHandler objects.
     * This is used to keep track of the connections for each game.
     */
    private final HashMap<String, HashSet<MessageHandler>> gameConnectionMapper = new HashMap<>();

    /**
     * Constructs a new NetworkCommandMapper object with the specified MainController.
     *
     * @param mainController the MainController to be used by the NetworkCommandMapper
     */
    public NetworkCommandMapper(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Sends a message to all players in a game.
     *
     * @param gameName the name of the game
     * @param message  the message to be sent
     */
    private void broadcastMessage(String gameName, ServerMessage message) {
        for (MessageHandler messageHandler : gameConnectionMapper.get(gameName)) {
            messageHandler.sendMessage(message);
        }
    }

    /**
     * Gets the list of available games.
     *
     * @param messageHandler the MessageHandler that will handle the response
     */
    @Override
    public void getGames(MessageHandler messageHandler) {
        try {
            ArrayList<Game> games = mainController.getGames();
            // TODO: Pass data to ServerMessage
        } catch (Exception e) {
            messageHandler.sendMessage(new ErrorServerMessage(e.getMessage()));
        }
    }

    /**
     * Creates a new game with the specified name and number of players.
     *
     * @param messageHandler the MessageHandler that will handle the response
     * @param gameName       the name of the game to be created
     * @param nPlayers       the number of players in the game
     */
    @Override
    public void createGame(MessageHandler messageHandler, String gameName, int nPlayers) {
        try {
            gameConnectionMapper.put(gameName, new HashSet<>());
            gameConnectionMapper.get(gameName).add(messageHandler);
            Game game = mainController.createGame(gameName, nPlayers, gameName);
            // TODO: Pass data to ServerMessage
            broadcastMessage(gameName, new SuccessServerMessage(ServerActionsEnum.CREATE_GAME));
        } catch (Exception e) {
            broadcastMessage(gameName, new ErrorServerMessage(e.getMessage()));
        }
    }

    /**
     * Allows a player to join a game.
     *
     * @param messageHandler the MessageHandler that will handle the response
     * @param gameName       the name of the game to join
     * @param playerName     the name of the player joining the game
     */
    @Override
    public void joinGame(MessageHandler messageHandler, String gameName, String playerName) {
        try {
            mainController.joinGame(gameName, playerName);
            gameConnectionMapper.get(gameName).add(messageHandler);
            broadcastMessage(gameName, new ViewUpdateMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            broadcastMessage(gameName, new ErrorServerMessage(e.getMessage()));
        }
    }

    /**
     * Leaves the game.
     *
     * @param messageHandler the class that will send the response back to the client
     * @param gameName       the name of the game.
     */
    @Override
    public void deleteGame(MessageHandler messageHandler, String gameName) {
        try {
            mainController.deleteGame(gameName);
            gameConnectionMapper.remove(gameName);
            // TODO: Send message to all players
        } catch (Exception e) {
            broadcastMessage(gameName, new ErrorServerMessage(e.getMessage()));
        }
    }

    /**
     * Chooses the color for a player.
     *
     * @param messageHandler the class that will send the response back to the client
     * @param gameName       the name of the game.
     * @param playerName     the name of the player who is choosing the color.
     * @param playerColor    the color to be chosen.
     */
    @Override
    public void choosePlayerColor(MessageHandler messageHandler, String gameName, String playerName, PlayerColorEnum playerColor) {
        try {
            mainController.getGameController(gameName).choosePlayerColor(playerName, playerColor);
            broadcastMessage(gameName, new ViewUpdateMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            broadcastMessage(gameName, new ErrorServerMessage(e.getMessage()));
        }
    }

    /**
     * Places a card on the game field.
     *
     * @param messageHandler the class that will send the response back to the client
     * @param gameName       the name of the game.
     * @param playerName     the name of the player who is placing the card.
     * @param coordinate     the coordinate where the card should be placed.
     * @param card           the card to be placed.
     */
    @Override
    public void placeCard(MessageHandler messageHandler, String gameName, String playerName, Coordinate coordinate, GameCard card) {
        try {
            mainController.getGameController(gameName).placeCard(playerName, coordinate, card);
            broadcastMessage(gameName, new ViewUpdateMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            broadcastMessage(gameName, new ErrorServerMessage(e.getMessage()));
        }
    }

    /**
     * Draws a card from the game field.
     *
     * @param messageHandler the class that will send the response back to the client
     * @param gameName       the name of the game.
     * @param playerName     the name of the player who is drawing the card.
     * @param card           the card to be drawn.
     */
    @Override
    public void drawCardFromField(MessageHandler messageHandler, String gameName, String playerName, GameCard card) {
        try {
            mainController.getGameController(gameName).drawCardFromField(playerName, card);
            broadcastMessage(gameName, new ViewUpdateMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            broadcastMessage(gameName, new ErrorServerMessage(e.getMessage()));
        }
    }

    /**
     * Draws a card from the resource deck.
     *
     * @param messageHandler the class that will send the response back to the client
     * @param gameName       the name of the game.
     * @param playerName     the name of the player who is drawing the card.
     */
    @Override
    public void drawCardFromResourceDeck(MessageHandler messageHandler, String gameName, String playerName) {
        try {
            mainController.getGameController(gameName).drawCardFromResourceDeck(playerName);
            broadcastMessage(gameName, new ViewUpdateMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            broadcastMessage(gameName, new ErrorServerMessage(e.getMessage()));
        }
    }

    /**
     * Draws a card from the gold deck.
     *
     * @param messageHandler the class that will send the response back to the client
     * @param gameName       the name of the game.
     * @param playerName     the name of the player who is drawing the card.
     */
    @Override
    public void drawCardFromGoldDeck(MessageHandler messageHandler, String gameName, String playerName) {
        try {
            mainController.getGameController(gameName).drawCardFromGoldDeck(playerName);
            broadcastMessage(gameName, new ViewUpdateMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            broadcastMessage(gameName, new ErrorServerMessage(e.getMessage()));
        }
    }

    /**
     * Switches the side of a card.
     *
     * @param messageHandler the class that will send the response back to the client
     * @param gameName       the name of the game.
     * @param playerName     the name of the player who is switching the card side.
     * @param card           the card whose side is to be switched.
     */
    @Override
    public void switchCardSide(MessageHandler messageHandler, String gameName, String playerName, GameCard card) {
        try {
            mainController.getGameController(gameName).switchCardSide(playerName, card);
            broadcastMessage(gameName, new ViewUpdateMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            broadcastMessage(gameName, new ErrorServerMessage(e.getMessage()));
        }
    }

    /**
     * Sets the objective for a player.
     *
     * @param messageHandler the class that will send the response back to the client
     * @param gameName       the name of the game.
     * @param playerName     the name of the player whose objective is to be set.
     * @param card           the objective card to be set for the player.
     */
    @Override
    public void setPlayerObjective(MessageHandler messageHandler, String gameName, String playerName, ObjectiveCard card) {
        try {
            mainController.getGameController(gameName).setPlayerObjective(playerName, card);
            broadcastMessage(gameName, new ViewUpdateMessage(mainController.getVirtualView(gameName)));
        } catch (Exception e) {
            broadcastMessage(gameName, new ErrorServerMessage(e.getMessage()));
        }
    }
}