package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;

/**
 * This interface defines the actions that a player can perform in the game.
 */
public class GameController implements PlayerActions {
    /**
     * The game instance.
     */
    private final Game game;

    /**
     * Constructor for GameController.
     * Initializes the GameController with the given game name, number of players, and player name.
     *
     * @param gameName   the name of the game.
     * @param nPlayers   the number of players in the game.
     * @param playerName the name of the player.
     */
    public GameController(String gameName, int nPlayers, String playerName) {
        this.game = new Game(gameName, nPlayers, playerName);
    }

    /**
     * Gets the current game instance.
     *
     * @return the current game instance.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Joins the game with the given player name.
     *
     * @param playerName the name of the player who is joining the game.
     */
    public void joinGame(String playerName) {
        game.addPlayer(playerName);
    }

    /**
     * Chooses the color for a player.
     *
     * @param playerName  the name of the player who is choosing the color.
     * @param playerColor the color to be chosen.
     */
    public void choosePlayerColor(String playerName, PlayerColorEnum playerColor) {
        game.choosePlayerColor(playerName, playerColor);
    }

    /**
     * Places a card on the game field.
     *
     * @param playerName the name of the player who is placing the card.
     * @param coordinate the coordinate where the card should be placed.
     * @param card       the card to be placed.
     */
    public void placeCard(String playerName, Coordinate coordinate, GameCard card) {
        game.getPlayer(playerName).getPlayerBoard().setGameCard(coordinate, card);
    }

    /**
     * Draws a card from the game field.
     *
     * @param playerName the name of the player who is drawing the card.
     * @param card       the card to be drawn.
     */
    public void drawCardFromField(String playerName, GameCard card) {
        game.getGlobalBoard().drawCardFromField(card);
        game.getPlayer(playerName).getPlayerHand().addCard(card);
    }

    /**
     * Draws a card from the resource deck.
     *
     * @param playerName the name of the player who is drawing the card.
     */
    public void drawCardFromResourceDeck(String playerName) {
        GameCard drawnCard = game.getGlobalBoard().getResourceDeck().draw();
        game.getPlayer(playerName).getPlayerHand().addCard(drawnCard);
    }

    /**
     * Draws a card from the gold deck.
     *
     * @param playerName the name of the player who is drawing the card.
     */
    public void drawCardFromGoldDeck(String playerName) {
        GameCard drawnCard = game.getGlobalBoard().getGoldDeck().draw();
        game.getPlayer(playerName).getPlayerHand().addCard(drawnCard);
    }

    /**
     * Switches the side of a card.
     *
     * @param playerName the name of the player who is switching the card side.
     * @param card       the card whose side is to be switched.
     */
    public void switchCardSide(String playerName, GameCard card) {
        game.getPlayer(playerName).getPlayerHand().getCards().stream().filter(c -> c.equals(card)).findFirst().ifPresent(GameCard::switchSide);
    }

    /**
     * Sets the objective for a player.
     *
     * @param playerName the name of the player whose objective is to be set.
     * @param card       the objective card to be set for the player.
     */
    public void setPlayerObjective(String playerName, ObjectiveCard card) {
        game.getPlayer(playerName).setPlayerObjective(card);
    }
}
