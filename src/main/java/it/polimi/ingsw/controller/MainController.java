package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.gameController.GameControllerMiddleware;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class that represents the main controller, which is responsible for creating, deleting and managing games.
 */
public class MainController {

    /**
     * Represents the list of game controller middlewares of games currently in progress.
     */
    private final ArrayList<GameControllerMiddleware> gameControllerMiddlewares;

    /**
     * Constructor for MainController. Initializes an empty list of gameControllerMiddlewares.
     */
    public MainController() {
        gameControllerMiddlewares = new ArrayList<>();
    }

    /**
     * Returns the list of games managed by the MainController.
     *
     * @return ArrayList of Game objects.
     */
    public ArrayList<Game> getGames() {
        return gameControllerMiddlewares.stream().map(GameControllerMiddleware::getGame).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns the list of game records of the games managed by the MainController.
     *
     * @return ArrayList of GameRecord objects.
     */
    public HashSet<GameRecord> getGameRecords() {
        return gameControllerMiddlewares.stream().map(GameControllerMiddleware::getGame).map(Game::getGameRecord).collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * Returns the game with the specified name.
     *
     * @param gameName The name of the game to get.
     * @return Game the game with the specified name.
     */
    public GameControllerMiddleware getGameController(String gameName) {
        Optional<GameControllerMiddleware> chosenGameControllerMiddleware = findGame(gameName);
        if (chosenGameControllerMiddleware.isEmpty())
            throw new IllegalArgumentException("A game with the name \"" + gameName + "\" doesn't exists");
        return chosenGameControllerMiddleware.get();
    }

    /**
     * Returns the GameView of the game with the specified name.
     *
     * @param gameName The name of the game to get the GameView of.
     * @return The GameView of the game with the specified name.
     * @throws IllegalArgumentException if a game with the specified name does not exist.
     */
    public GameControllerView getVirtualView(String gameName) {
        Optional<GameControllerMiddleware> chosenGameControllerMiddleware = findGame(gameName);
        if (chosenGameControllerMiddleware.isEmpty())
            throw new IllegalArgumentException("A game with the name \"" + gameName + "\" doesn't exists");
        return chosenGameControllerMiddleware.get().getVirtualView();
    }

    /**
     * Creates a new gameControllerMiddleware with the specified parameters and adds it to the list of games.
     *
     * @param gameName   The name of the game to be created.
     * @param playerName The name of the player creating the game, it will also be his nickname
     * @param nPlayers   The max number of players that will be in the game
     * @return The created Game object.
     * @throws IllegalArgumentException if a game with the same name already exists.
     */
    public Game createGame(String gameName, String playerName, int nPlayers) {
        if (findGame(gameName).isPresent())
            throw new IllegalArgumentException("A game with the name \"" + gameName + "\" already exists");
        GameControllerMiddleware gameControllerMiddleware = new GameControllerMiddleware(gameName, nPlayers, playerName);
        gameControllerMiddlewares.add(gameControllerMiddleware);
        return gameControllerMiddleware.getGame();
    }

    /**
     * Deletes a gameControllerMiddleware with the specified name from the list of gameControllerMiddlewares.
     *
     * @param gameName   The name of the game to be deleted.
     * @param playerName The name of the player deleting the game.
     * @throws IllegalArgumentException if a game with the specified name does not exist.
     */
    public void deleteGame(String gameName, String playerName) {
        Optional<GameControllerMiddleware> chosenGameControllerMiddleware = findGame(gameName);
        if (chosenGameControllerMiddleware.isEmpty())
            throw new IllegalArgumentException("A game with the name \"" + gameName + "\" doesn't exists");
        GameControllerMiddleware gameControllerMiddleware = chosenGameControllerMiddleware.get();
        if (!gameControllerMiddleware.isCreator(playerName)) {
            throw new IllegalArgumentException("Only the creator of the game can delete it");
        }
        gameControllerMiddlewares.remove(gameControllerMiddleware);
    }

    /**
     * Checks if the player can delete the game
     *
     * @param gameName   The name of the game to be deleted.
     * @param playerName The name of the player deleting the game.
     * @throws IllegalArgumentException if the game cannot be deleted.
     */
    public void isGameDeletable(String gameName, String playerName) {
        Optional<GameControllerMiddleware> chosenGameControllerMiddleware = findGame(gameName);
        if (chosenGameControllerMiddleware.isEmpty())
            throw new IllegalArgumentException("A game with the name \"" + gameName + "\" doesn't exists");
        GameControllerMiddleware gameControllerMiddleware = chosenGameControllerMiddleware.get();
        if (!gameControllerMiddleware.isCreator(playerName)) {
            throw new IllegalArgumentException("Only the creator of the game can delete it");
        }
    }

    /**
     * Deletes a gameControllerMiddleware with the specified name from the list of gameControllerMiddlewares.
     *
     * @param gameName The name of the game to be deleted.
     * @throws IllegalArgumentException if a game with the specified name does not exist.
     */
    public void deleteGame(String gameName) {
        Optional<GameControllerMiddleware> chosenGameControllerMiddleware = findGame(gameName);
        if (chosenGameControllerMiddleware.isEmpty())
            throw new IllegalArgumentException("A game with the name \"" + gameName + "\" doesn't exists");
        GameControllerMiddleware gameControllerMiddleware = chosenGameControllerMiddleware.get();
        gameControllerMiddlewares.remove(gameControllerMiddleware);
    }

    /**
     * Adds a player to a game with the specified name.
     *
     * @param gameName   The name of the game to join.
     * @param playerName The name of the player joining the game, it will also be his nickname
     * @return The Game object that represents the game the player joined.
     * @throws IllegalArgumentException if a game with the specified name does not exist or a player with the same name already exists in the game.
     */
    public Game joinGame(String gameName, String playerName) {
        Optional<GameControllerMiddleware> chosenGameControllerMiddleware = findGame(gameName);
        if (chosenGameControllerMiddleware.isEmpty())
            throw new IllegalArgumentException("A game with the name \"" + gameName + "\" doesn't exists");

        chosenGameControllerMiddleware.get().joinGame(playerName);
        return chosenGameControllerMiddleware.get().getGame();
    }

    /**
     * Finds a gameControllerMiddleware with the specified name.
     *
     * @param gameName The name of the game to find.
     * @return An Optional<gameControllerMiddleware> that contains the game if it exists, or is empty if it does not.
     */
    private Optional<GameControllerMiddleware> findGame(String gameName) {
        return gameControllerMiddlewares.stream().filter(gcm -> gcm.getGame().getGameName().equals(gameName)).findFirst();
    }
}
