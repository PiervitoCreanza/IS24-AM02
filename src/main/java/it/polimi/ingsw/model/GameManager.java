package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Class that represents the game manager, which is responsible for creating, deleting and managing games.
 */
public class GameManager {
    private final ArrayList<Game> games;

    /**
     * Constructor for GameManager. Initializes an empty list of games.
     */
    public GameManager() {
        games = new ArrayList<Game>();
    }

    /**
     * Returns the list of games managed by the GameManager.
     * @return ArrayList of Game objects.
     */
    public ArrayList<Game> getGames() {
        return games;
    }

    /**
     * Creates a new game with the specified parameters and adds it to the list of games.
     * @param gameName The name of the game to be created.
     * @param nPlayers The max number of players that will be in the game
     * @param playerName The name of the player creating the game, it will also be his nickname
     * @param goldDeck The deck of gold cards for the game.
     * @param resourceDeck The deck of resource cards for the game.
     * @param objectiveDeck The deck of objective cards for the game.
     * @param starterDeck The deck of starter cards for the game.
     * @return The created Game object.
     * @throws IllegalArgumentException if a game with the same name already exists.
     */
    public Game createGame(String gameName, int nPlayers, String playerName, ArrayList<GameCard> goldDeck, ArrayList<GameCard> resourceDeck, ArrayList<ObjectiveCard> objectiveDeck, ArrayList<GameCard> starterDeck) {
        if (findGame(gameName).isPresent())
            throw new IllegalArgumentException("A game with the name \"" + gameName + "\" already exists");
        Game game = new Game(gameName, nPlayers, playerName, goldDeck, resourceDeck, objectiveDeck, starterDeck);
        games.add(game);
        return game;
    }

    /**
     * Deletes a game with the specified name from the list of games.
     * @param gameName The name of the game to be deleted.
     * @throws IllegalArgumentException if a game with the specified name does not exist.
     */
    public void deleteGame(String gameName) {
        Optional<Game> chosenGame = findGame(gameName);
        if (chosenGame.isEmpty())
            throw new IllegalArgumentException("A game with the name \"" + gameName + "\" doesn't exists");
        games.remove(chosenGame.get());
    }

    /**
     * Adds a player to a game with the specified name.
     * @param gameName The name of the game to join.
     * @param playerName The name of the player joining the game, it will also be his nickname
     * @return The Game object that represents the game the player joined.
     * @throws IllegalArgumentException if a game with the specified name does not exist or a player with the same name already exists in the game.
     */
    public Game joinGame(String gameName, String playerName) {
        Optional<Game> chosenGame = findGame(gameName);
        if (chosenGame.isEmpty())
            throw new IllegalArgumentException("A game with the name \"" + gameName + "\" doesn't exists");
        if(chosenGame.get().getPlayers().stream().anyMatch(player -> player.getPlayerName().equals(playerName)))
            throw new IllegalArgumentException("A player with the name \"" + playerName + "\" already exists");
        chosenGame.get().addPlayer(new Player(playerName));
        return chosenGame.get();
    }

    /**
     * Finds a game with the specified name.
     * @param gameName The name of the game to find.
     * @return An Optional<Game> that contains the game if it exists, or is empty if it does not.
     */
    private Optional<Game> findGame(String gameName) {
        return games.stream().filter(game -> game.getGameName().equals(gameName)).findFirst();
    }
}
