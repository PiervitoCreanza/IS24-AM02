package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;

/**
 * This interface defines the actions that a player can perform in the game.
 */
public interface PlayerActions {

    /**
     * Gets the current game instance.
     *
     * @return the current game instance.
     */
    public Game getGame();

    /**
     * Joins the game with the given player name.
     *
     * @param playerName the name of the player who is joining the game.
     */
    public void joinGame(String playerName);

    /**
     * Places a card on the game field.
     *
     * @param playerName the name of the player who is placing the card.
     * @param coordinate the coordinate where the card should be placed.
     * @param card       the card to be placed.
     */
    public void placeCard(String playerName, Coordinate coordinate, GameCard card);

    /**
     * Draws a card from the game field.
     *
     * @param playerName the name of the player who is drawing the card.
     * @param card       the card to be drawn.
     */
    public void drawCardFromField(String playerName, GameCard card);

    /**
     * Draws a card from the resource deck.
     *
     * @param playerName the name of the player who is drawing the card.
     */
    public void drawCardFromResourceDeck(String playerName);

    /**
     * Draws a card from the gold deck.
     *
     * @param playerName the name of the player who is drawing the card.
     */
    public void drawCardFromGoldDeck(String playerName);

    /**
     * Switches the side of a card.
     *
     * @param card the card whose side is to be switched.
     */
    public void switchCardSide(String playerName, GameCard card);

    /**
     * Sets the objective for a player.
     *
     * @param playerName the name of the player whose objective is to be set.
     * @param card       the objective card to be set for the player.
     */
    public void setPlayerObjective(String playerName, ObjectiveCard card);
}