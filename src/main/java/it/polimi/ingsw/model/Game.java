package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class that represents a single game in the system.
 */
public class Game {
    private final String gameName;
    private final int nPlayers;
    private final ArrayList<Player> players;
    private final GlobalBoard globalBoard;
    private Player currentPlayer;

    /**
     * Constructor for Game. Initializes a new game with the specified parameters.
     * @param gameName The name of the game.
     * @param nPlayers The maximum number of players in the game.
     * @param playerName The name of the player creating the game, he will also be the first player.
     * @param goldDeck The deck of gold cards for the game.
     * @param resourceDeck The deck of resource cards for the game.
     * @param objectiveDeck The deck of objective cards for the game.
     * @param starterDeck The deck of starter cards for the game.
     */
    public Game(String gameName, int nPlayers, String playerName, ArrayList<GameCard> goldDeck, ArrayList<GameCard> resourceDeck, ArrayList<ObjectiveCard> objectiveDeck, ArrayList<GameCard> starterDeck) {
        Player player = new Player(playerName);
        this.gameName = gameName;
        this.nPlayers = nPlayers;
        this.players = new ArrayList<Player>(nPlayers);
        this.players.add(player);
        this.globalBoard = new GlobalBoard(goldDeck, resourceDeck, objectiveDeck, starterDeck);
        this.currentPlayer = player;
    }

    /**
     * Returns the name of the game.
     * @return The name of the game.
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * Returns the list of players in the game.
     * @return ArrayList of Player objects.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the player with the specified name.
     * @param playerName The name of the player to find.
     * @return The Player object that represents the player with the specified name.
     * @throws IllegalArgumentException if a player with the specified name does not exist.
     */
    public Player getPlayer(String playerName) {
        Optional<Player> chosenPlayer = players.stream().filter(player -> player.getPlayerName().equals(gameName)).findFirst();
        if (chosenPlayer.isEmpty())
            throw new IllegalArgumentException("Player with name \"" + playerName + "\" doesn't exists");
        return chosenPlayer.get();
    }

    /**
     * Returns the global board of the game.
     * @return The GlobalBoard object that represents the global board of the game.
     */
    public GlobalBoard getGlobalBoard() {
        return globalBoard;
    }

    /**
     * Adds a player to the game.
     * @param player The Player object to add to the game.
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Returns the next player in the game and updates the currentPlayer.
     * @return The Player object that represents the next player in the game.
     */
    public Player getNextPlayer() {
        currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % nPlayers);
        return currentPlayer;
    }

    /**
     * Checks if the game has started by checking if enough players have joined the game.
     * @return true if the game has started, false otherwise.
     */
    public boolean isStarted() {
        return (players.size() == nPlayers);
    }

    /**
     * Returns the winner(s) of the game, first it checks who got more points, then,
     * if there's a tie, it checks who has completed more objectives. If it's still a tie
     * it just returns an array containing all winners.
     * @return ArrayList of Player objects that represents the winner(s) of the game.
     */
    public ArrayList<Player> getWinner() {
        HashMap<Player, Integer> tempMap = new HashMap<>(nPlayers);
        players.forEach(player -> tempMap.put(player, 0));
        Store<Player> cardsWon = new Store<>(tempMap);

        ArrayList<ObjectiveCard> objectives = globalBoard.getGlobalObjectives();

        for (Player player : players) {
            PlayerBoard playerBoard = player.getPlayerBoard();
            ObjectiveCard playerObjective = player.getObjectiveCard();
            objectives.add(playerObjective);
            for (ObjectiveCard objective : objectives){
                int pointsWon = objective.getPoints(playerBoard);
                if (pointsWon != 0){
                    cardsWon.increment(player, 1);
                    player.advancePlayerPos(pointsWon);
                }
            }
            objectives.remove(playerObjective);
        }

        ArrayList<Player> winners;
        int highestPlayerPos = players.stream().map(Player::getPlayerPos).max(Integer::compare).orElse(0);
        winners = players.stream().filter(player -> player.getPlayerPos() == highestPlayerPos).collect(Collectors.toCollection(ArrayList::new));

        int maxCardsWon = winners.stream().map(cardsWon::get).max(Integer::compare).orElse(0);

        return winners.stream().filter(player -> cardsWon.get(player) == maxCardsWon).collect(Collectors.toCollection(ArrayList::new));
    }

}
