package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;

/**
 * This interface defines the actions that a player can perform in the game.
 */
interface PlayerActions {

    /**
     * Gets the current game instance.
     *
     * @return the current game instance.
     */
    Game getGame();

    /**
     * Joins the game with the given player name.
     *
     * @param playerName the name of the player who is joining the game.
     */
    void joinGame(String playerName);

    /**
     * Chooses the color for a player.
     *
     * @param playerName  the name of the player who is choosing the color.
     * @param playerColor the color to be chosen.
     */
    void choosePlayerColor(String playerName, PlayerColorEnum playerColor);

    /**
     * Places a card on the game field.
     *
     * @param playerName the name of the player who is placing the card.
     * @param coordinate the coordinate where the card should be placed.
     * @param card       the card to be placed.
     */
    void placeCard(String playerName, Coordinate coordinate, GameCard card);

    /**
     * Draws a card from the game field.
     *
     * @param playerName the name of the player who is drawing the card.
     * @param card       the card to be drawn.
     */
    void drawCardFromField(String playerName, GameCard card);

    /**
     * Draws a card from the resource deck.
     *
     * @param playerName the name of the player who is drawing the card.
     */
    void drawCardFromResourceDeck(String playerName);

    /**
     * Draws a card from the gold deck.
     *
     * @param playerName the name of the player who is drawing the card.
     */
    void drawCardFromGoldDeck(String playerName);

    /**
     * Switches the side of a card.
     *
     * @param playerName the name of the player who is switching the card side.
     * @param card       the card whose side is to be switched.
     */
    void switchCardSide(String playerName, GameCard card);

    /**
     * Sets the objective for a player.
     *
     * @param playerName the name of the player whose objective is to be set.
     * @param card       the objective card to be set for the player.
     */
    void setPlayerObjective(String playerName, ObjectiveCard card);

    /**
     * Sets the connection status of a player.
     *
     * @param playerName  the name of the player whose connection status is to be set.
     * @param isConnected the connection status to be set for the player.
     */
    void setPlayerConnectionStatus(String playerName, boolean isConnected);
}