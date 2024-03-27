package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.utils.store.Store;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class that represents a single game in the system.
 */
public class Game {

    /**
     * Represents the name of the game.
     * This is a unique identifier for each game instance.
     */
    private final String gameName;

    /**
     * Represents the maximum number of players in the game.
     * This is the total number of players that can join a game.
     */
    private final int nPlayers;

    /**
     * Represents the list of players in the game.
     * This is an ArrayList that contains all the Player objects that are currently participating in the game.
     */
    private final ArrayList<Player> players;

    /**
     * Represents the winners of the game.
     * This is an ArrayList that contains the Player objects who won the game.
     * The winners are determined by the calculateWinners() method.
     */
    private ArrayList<Player> winners;

    /**
     * Represents the global board of the game.
     * This is a GlobalBoard object that contains all the global elements of the game, such as the decks and objectives.
     */
    private final GlobalBoard globalBoard;

    /**
     * Represents the current player in the game.
     * This is a Player object that represents the player whose turn it is to play.
     */
    private Player currentPlayer;

    /**
     * Constructor for Game. Initializes a new game with the specified parameters.
     *
     * @param gameName   The name of the game.
     * @param nPlayers   The maximum number of players in the game.
     * @param playerName The name of the player creating the game, he will also be the first player.
     * @throws NullPointerException if the gameName is null.
     * @throws IllegalArgumentException if the number of players is not between 2 and 4.
     */
    public Game(String gameName, int nPlayers, String playerName) {
        this.gameName = Objects.requireNonNull(gameName, "The game name can't be NULL");
        if(nPlayers < 2 || nPlayers > 4)
            throw new IllegalArgumentException("Players must be between 2-4");
        this.nPlayers = nPlayers;
        this.players = new ArrayList<>(nPlayers);
        this.globalBoard = new GlobalBoard();
        this.addPlayer(playerName);
        this.currentPlayer = players.getFirst();
    }

    /**
     * Returns the name of the game.
     *
     * @return The name of the game.
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * Returns the list of players in the game.
     *
     * @return ArrayList of Player objects.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the player with the specified name.
     *
     * @param playerName The name of the player to find.
     * @return The Player object that represents the player with the specified name.
     * @throws NullPointerException if the playerName is null.
     * @throws IllegalArgumentException if a player with the specified name does not exist.
     */
    public Player getPlayer(String playerName) {
        Objects.requireNonNull(playerName, "The player name can't be NULL");
        Optional<Player> chosenPlayer = players.stream().filter(player -> player.getPlayerName().equals(gameName)).findFirst();
        if (chosenPlayer.isEmpty())
            throw new IllegalArgumentException("Player with name \"" + playerName + "\" doesn't exists");
        return chosenPlayer.get();
    }

    /**
     * Returns the global board of the game.
     *
     * @return The GlobalBoard object that represents the global board of the game.
     */
    public GlobalBoard getGlobalBoard() {
        return globalBoard;
    }

    /**
     * Adds a new player to the game.
     * This method draws two objective cards from the global board's objective deck,
     * draws a starter card from the global board's starter deck and creates a new
     * Player object with the specified name, objective cards and starter card.
     * The new player is added to the list of players in the game.
     *
     * @param playerName The name of the player to be added.
     * @throws NullPointerException if the playerName is null.
     * @throws IllegalArgumentException if a player with the same name already exists.
     */
    public void addPlayer(String playerName) {
        Objects.requireNonNull(playerName, "The player name can't be NULL");
        if (players.stream().map(Player::getPlayerName).anyMatch(name -> name.equals(playerName)))
            throw new IllegalArgumentException("A player with the same name, already exists");

        ArrayList<ObjectiveCard> drawnObjectives = new ArrayList<>(List.of(globalBoard.getObjectiveDeck().draw(), globalBoard.getObjectiveDeck().draw()));
        GameCard starterCard = globalBoard.getStarterDeck().draw();
        players.add(new Player(playerName, drawnObjectives, starterCard));
    }

    /**
     * Returns the current player in the game.
     *
     * @return The Player object that represents the current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the next player in the game.
     * This method updates the currentPlayer variable to the next player in the list of players.
     */
    public void setNextPlayer() {
        currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % nPlayers);
    }

    /**
     * Checks if the game has started by checking if enough players have joined the game.
     *
     * @return true if the game has started, false otherwise.
     */
    public boolean isStarted() {
        return (players.size() == nPlayers);
    }

    /**
     * This method checks if any player's position is greater than or equal to 20.
     * If so, the game can start its ending phase.
     *
     * @return true if a player has more than 20 points, false otherwise.
     */
    public boolean isOver() {
        return players.stream().anyMatch(player -> player.getPlayerPos() >= 20);
    }

    /**
     * Calculate the winner(s) of the game, first it checks who got more points, then,
     * if there's a tie, it checks who has completed more objectives. If it's still a tie
     * it just returns an array containing all winners. This array is then saved in the
     * winners attribute.
     */
    public void calculateWinners() {
        //Initialize a store that will contain for each player the number of objective cards he won.
        HashMap<Player, Integer> tempMap = new HashMap<>(nPlayers);
        players.forEach(player -> tempMap.put(player, 0));
        Store<Player> cardsWon = new Store<>(tempMap);

        ArrayList<ObjectiveCard> objectives = globalBoard.getGlobalObjectives();

        for (Player player : players) {
            PlayerBoard playerBoard = player.getPlayerBoard();
            ObjectiveCard playerObjective = player.getObjectiveCard();
            objectives.add(playerObjective);
            for (ObjectiveCard objective : objectives) {
                int pointsWon = objective.getPoints(playerBoard);
                //If pointsWon aren't 0, we won an objective, so we increase the counter and advance the player.
                if (pointsWon != 0) {
                    cardsWon.increment(player, 1);
                    player.advancePlayerPos(pointsWon);
                }
            }
            //We remove the current player objective, so only the two global objectives remain.
            objectives.remove(playerObjective);
        }
        //A player wins if he has the highest score, if there's a tie the player who has completed more objectives wins.
        //If they both completed the same number of objectives, they are both winners.

        //We calculate the highest score reached by a player.
        int highestPlayerScore = players.stream().map(Player::getPlayerPos).max(Integer::compare).orElse(0);
        //We filter by players with the highest score, we can't use a simple max() because a tie is possible.
        ArrayList<Player> tempWinners = players.stream().filter(player -> player.getPlayerPos() == highestPlayerScore).collect(Collectors.toCollection(ArrayList::new));

        //We calculate the maximum number of objective cards won by players, it is needed to resolve the tie
        int maxCardsWon = tempWinners.stream().map(cardsWon::get).max(Integer::compare).orElse(0);
        //We filter by players with the maxCardsWon, and we save the array, a tie is still possible.
        this.winners = tempWinners.stream().filter(player -> cardsWon.get(player) == maxCardsWon).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns the winners of the game.
     * This method retrieves the winners variable which represents the list of players who won the game.
     * The winners are determined by the calculateWinners() method.
     *
     * @return ArrayList of Player objects that represents the winners of the game.
     */
    public ArrayList<Player> getWinners() {
        return winners;
    }
}
