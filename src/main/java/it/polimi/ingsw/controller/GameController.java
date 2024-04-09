package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;

/**
 * This interface defines the actions that a player can perform in the game.
 */
public class GameController implements PlayerActions {
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

    public Game getGame() {
        return game;
    }

    public void placeCard(String playerName, Coordinate coordinate, GameCard card) {
        game.getPlayer(playerName).getPlayerBoard().setGameCard(coordinate, card);
    }

    public void drawCardFromField(String playerName, GameCard card) {
        game.getGlobalBoard().drawCardFromField(card);
        game.getPlayer(playerName).getPlayerHand().addCard(card);
    }

    public void drawCardFromResourceDeck(String playerName) {
        GameCard drawnCard = game.getGlobalBoard().getResourceDeck().draw();
        game.getPlayer(playerName).getPlayerHand().addCard(drawnCard);
    }

    public void drawCardFromGoldDeck(String playerName) {
        GameCard drawnCard = game.getGlobalBoard().getGoldDeck().draw();
        game.getPlayer(playerName).getPlayerHand().addCard(drawnCard);
    }

    public void setPlayerObjective(String playerName, ObjectiveCard card) {
        game.getPlayer(playerName).setPlayerObjective(card);
    }

    public void joinGame(String playerName) {
        game.addPlayer(playerName);
    }

    public void switchCardSide(String playerName, GameCard card) {
        game.getPlayer(playerName).getPlayerHand().getCards().stream().filter(c -> c.equals(card)).findFirst().ifPresent(GameCard::switchSide);
    }
}
