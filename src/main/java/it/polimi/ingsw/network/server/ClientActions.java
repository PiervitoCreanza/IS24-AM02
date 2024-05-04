package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.message.ClientMessage;

/**
 * This interface defines the actions that a client can perform in the game.
 */
public interface ClientActions {

    /**
     * Retrieves the list of available games.
     * This method is used when a client wants to see all the games that are currently available to join.
     *
     * @param messageHandler the message handler to handle the response.
     */
    void getGames(MessageHandler<ClientMessage> messageHandler);

    /**
     * Creates a new game with the given game name and number of players.
     *
     * @param gameName the name of the game.
     * @param playerName the name of the player.
     * @param nPlayers the number of players in the game.
     */
    void createGame(MessageHandler<ClientMessage> messageHandler, String gameName, String playerName, int nPlayers);

    /**
     * Leaves the game.
     *
     * @param gameName the name of the game.
     */
    void deleteGame(MessageHandler<ClientMessage> messageHandler, String gameName);

    /**
     * Joins the game with the given player name.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is joining the game.
     */
    void joinGame(MessageHandler<ClientMessage> messageHandler, String gameName, String playerName);

    /**
     * Chooses the color for a player.
     *
     * @param gameName    the name of the game.
     * @param playerName  the name of the player who is choosing the color.
     * @param playerColor the color to be chosen.
     */
    void choosePlayerColor(MessageHandler<ClientMessage> messageHandler, String gameName, String playerName, PlayerColorEnum playerColor);

    /**
     * Places a card on the game field.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is placing the card.
     * @param coordinate the coordinate where the card should be placed.
     * @param card       the card to be placed.
     */
    void placeCard(MessageHandler<ClientMessage> messageHandler, String gameName, String playerName, Coordinate coordinate, GameCard card);

    /**
     * Draws a card from the game field.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     * @param card       the card to be drawn.
     */
    void drawCardFromField(MessageHandler<ClientMessage> messageHandler, String gameName, String playerName, GameCard card);

    /**
     * Draws a card from the resource deck.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     */
    void drawCardFromResourceDeck(MessageHandler<ClientMessage> messageHandler, String gameName, String playerName);

    /**
     * Draws a card from the gold deck.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is drawing the card.
     */
    void drawCardFromGoldDeck(MessageHandler<ClientMessage> messageHandler, String gameName, String playerName);

    /**
     * Switches the side of a card.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player who is switching the card side.
     * @param card       the card whose side is to be switched.
     */
    void switchCardSide(MessageHandler<ClientMessage> messageHandler, String gameName, String playerName, GameCard card);

    /**
     * Sets the objective for a player.
     *
     * @param gameName   the name of the game.
     * @param playerName the name of the player whose objective is to be set.
     * @param card       the objective card to be set for the player.
     */
    void setPlayerObjective(MessageHandler<ClientMessage> messageHandler, String gameName, String playerName, ObjectiveCard card);
}
