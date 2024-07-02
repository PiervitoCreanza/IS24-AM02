package it.polimi.ingsw.network.server.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.controller.gameController.GameControllerMiddleware;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GlobalBoard;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.gameCard.SerializableBooleanProperty;
import it.polimi.ingsw.model.card.gameCard.SideGameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.player.PlayerHand;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.message.adapter.ServerToClientMessageAdapter;
import it.polimi.ingsw.network.server.ServerNetworkControllerMapper;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import it.polimi.ingsw.network.server.message.successMessage.UpdateViewServerToClientMessage;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.network.virtualView.PlayerView;
import it.polimi.ingsw.parsing.adapters.ObjectiveCardAdapter;
import it.polimi.ingsw.parsing.adapters.SerializableBooleanPropertyAdapter;
import it.polimi.ingsw.parsing.adapters.SideGameCardAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Persistence class is responsible for saving and loading games to and from disk.
 * It listens for property changes in the game controllers and saves the game state to disk when needed.
 */
public class Persistence implements PropertyChangeListener {

    /**
     * The main controller.
     */
    private final MainController mainController;

    /**
     * The server network controller mapper.
     */
    private final ServerNetworkControllerMapper serverNetworkControllerMapper;

    /**
     * The executor service for managing persistence tasks.
     */
    private final HashMap<String, ExecutorService> executors;

    /**
     * The logger.
     */
    private final Logger logger = LogManager.getLogger(Persistence.class);

    /**
     * The Gson object for parsing JSON.
     */
    private final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(SerializableBooleanProperty.class, new SerializableBooleanPropertyAdapter())
            .registerTypeAdapter(ServerToClientMessage.class, new ServerToClientMessageAdapter()) // Registering a type adapter for ServerToClientMessage class
            .registerTypeAdapter(SideGameCard.class, new SideGameCardAdapter()) // Registering a type adapter for SideGameCard class
            .registerTypeAdapter(ObjectiveCard.class, new ObjectiveCardAdapter()) // Registering a type adapter for ObjectiveCard class
            .create();

    /**
     * Creates a new Persistence instance. It needs both
     * the main controller and the server network controller
     * mapper to save and load games.
     *
     * @param mainController                The main controller
     * @param serverNetworkControllerMapper The server network controller mapper
     */
    public Persistence(MainController mainController, ServerNetworkControllerMapper serverNetworkControllerMapper) {
        this.mainController = mainController;
        this.serverNetworkControllerMapper = serverNetworkControllerMapper;
        this.executors = new HashMap<>();
        File directory = new File("savedGames");
        if (!directory.exists()) {
            if (!directory.mkdir()) {
                logger.fatal("Failed to create \"savedGames\" directory");
                System.exit(-1);
            }
        }
    }

    /**
     * Method used to check if the file is valid.
     *
     * @param file The file to check
     * @return True if the file is valid, false otherwise
     */
    private boolean checkFile(File file) {
        if (!file.exists()) {
            logger.fatal("File at {} does not exist", file.getPath());
            return false;
        }
        if (file.isDirectory()) {
            logger.fatal("Expected a file but found a directory at {}", file.getPath());
            return false;
        }
        if (!file.isFile()) {
            logger.fatal("Expected a file but found something else at {}", file.getPath());
            return false;
        }
        if (!file.canRead()) {
            logger.fatal("File at {} is not readable", file.getPath());
            return false;
        }
        if (!file.canWrite()) {
            logger.fatal("File at {} is not writable", file.getPath());
            return false;
        }
        if (file.length() == 0) {
            logger.warn("File at {} is empty", file.getPath());
            return false;
        }
        return true;
    }

    /**
     * Returns the hash of the game name.
     *
     * @param gameName The name of the game
     * @return The hash of the game name
     */
    private String getGameHash(String gameName) {
        return String.valueOf(gameName.hashCode());
    }

    /**
     * Returns the hash of the game name.
     *
     * @param gameControllerView The game controller view
     * @return The hash of the game name
     */
    private String getGameHash(GameControllerView gameControllerView) {
        return getGameHash(gameControllerView.gameView().gameName());
    }

    /**
     * This method is responsible for creating a task to save the game state.
     * It first generates a hash of the game name to use as a key for storing
     * the executor service. If an executor service does not already exist for
     * this game, it creates a new single-thread executor service and stores
     * it in the executors map. It then submits a task to this executor service
     * to save the game state.
     *
     * @param gameControllerView The GameControllerView instance representing the current state of the game.
     */
    private void savingTask(GameControllerView gameControllerView) {
        String gameHash = getGameHash(gameControllerView);
        ExecutorService executor;
        if (!this.executors.containsKey(gameHash)) {
            executor = Executors.newSingleThreadExecutor();
            this.executors.put(gameHash, executor);
        } else {
            executor = this.executors.get(gameHash);
        }
        executor.submit(() -> saveGame(gameControllerView));
    }

    /**
     * Saves the game to a JSON file. In the savedGames directory.
     * It uses the hash of the game name as the filename to avoid
     * possible problems with special characters.
     *
     * @param view The game controller view to save
     */
    private void saveGame(GameControllerView view) {
        try {
            String gameNameHash = getGameHash(view);
            // Save the game to a JSON file
            FileWriter writer = new FileWriter("savedGames/" + gameNameHash + ".json");
            gson.toJson(view, writer);
            writer.close();
        } catch (IOException e) {
            logger.warn("Saving game: {} to file failed", view.gameView().gameName());
        }
    }

    /**
     * This method is responsible for creating a task to delete the game state.
     * It first generates a hash of the game name to use as a key for getting
     * the executor service. If an executor service exists for this game, it
     * submits a task to this executor service to delete the game state. It
     * then shuts down the executor service and removes it from the executors map.
     *
     * @param gameName The name of the game to delete
     */
    private void deletingTask(String gameName) {
        String gameHash = getGameHash(gameName);
        ExecutorService executor = this.executors.get(gameHash);
        if (executor != null) {
            executor.submit(() -> deleteGame(gameName));
            executor.shutdown();
            this.executors.remove(gameHash);
        }
    }

    /**
     * Deletes the game from the savedGames directory.
     *
     * @param gameName The name of the game to delete
     */
    private void deleteGame(String gameName) {
        String gameNameHash = getGameHash(gameName);
        File file = new File("savedGames/" + gameNameHash + ".json");
        if (checkFile(file)) {
            if (!file.delete()) {
                logger.warn("Failed to delete saved game file: {}", gameNameHash);
            }
        }
    }

    /**
     * Loads the game from the specified JSON file.
     *
     * @param path The path to the JSON file
     */
    public void loadFromFile(String path) {
        File file = new File(path);
        if (checkFile(file))
            restoreModel(file);
    }

    /**
     * Loads all the games from the savedGames directory.
     */
    public void loadAll() {
        File folder = new File("savedGames");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (checkFile(file))
                    restoreModel(file);
            }
        }
    }

    /**
     * Restores the game model from the specified JSON file.
     *
     * @param file The JSON file to restore the game from
     */
    private void restoreModel(File file) {
        try {
            // Parse the JSON file for restoring th game status
            FileReader reader = new FileReader(file);
            GameControllerView gameControllerView = gson.fromJson(reader, GameControllerView.class);
            reader.close();
            // Add the game to the connections hashmap
            String gameName = gameControllerView.gameView().gameName();
            serverNetworkControllerMapper.addGameToMapper(gameName);

            // Create the game
            boolean first = true;
            // Add all the players to the game
            for (PlayerView playerView : gameControllerView.gameView().playerViews()) {
                if (first) {
                    mainController.createGame(gameName, playerView.playerName(), gameControllerView.gameView().playerViews().size());
                    first = false;
                } else {
                    mainController.joinGame(gameName, playerView.playerName());
                }
            }
            // Set the game controller middleware
            GameControllerMiddleware gameControllerMiddleware = mainController.getGameController(gameName);
            // Persistence will receive a notification if the game needs to be deleted
            gameControllerMiddleware.addPropertyChangeListener(this);
            gameControllerMiddleware.setSavedGameStatus(gameControllerView.gameStatus());
            gameControllerMiddleware.setGameStatus(GameStatusEnum.WAIT_FOR_PLAYERS);
            gameControllerMiddleware.setLoadedFromDisk(true);
            gameControllerMiddleware.setRemainingRoundsToEndGame(gameControllerView.remainingRoundsToEndGame());
            gameControllerMiddleware.setLastRound(gameControllerView.isLastRound());

            // Set the game
            Game game = mainController.getGameController(gameName).getGame();
            game.setCurrentPlayer(gameControllerView.getCurrentPlayerView().playerName());
            // Initialize the globalboard
            GlobalBoard globalBoard = game.getGlobalBoard();
            globalBoard.setGlobalObjectives(gameControllerView.gameView().globalBoardView().globalObjectives());
            globalBoard.getFieldResourceCards().forEach(card -> globalBoard.getResourceDeck().addCard(card));
            globalBoard.getFieldGoldCards().forEach(card -> globalBoard.getGoldDeck().addCard(card));
            globalBoard.getFieldResourceCards().clear();
            globalBoard.getFieldGoldCards().clear();
            gameControllerView.gameView().globalBoardView().fieldResourceCards().forEach(card -> globalBoard.getFieldResourceCards().add(card));
            gameControllerView.gameView().globalBoardView().fieldGoldCards().forEach(card -> globalBoard.getFieldGoldCards().add(card));
            GameCard resourceFirstCard = gameControllerView.gameView().globalBoardView().resourceFirstCard();
            GameCard goldFirstCard = gameControllerView.gameView().globalBoardView().goldFirstCard();
            if (resourceFirstCard != null) {
                globalBoard.getResourceDeck().removeCard(resourceFirstCard.getCardId());
            }
            if (goldFirstCard != null) {
                globalBoard.getGoldDeck().removeCard(goldFirstCard.getCardId());
            }

            // Initialize the players
            for (Player player : game.getPlayers()) {
                PlayerView playerView = gameControllerView.getPlayerViewByName(player.getPlayerName());
                player.setConnected(false);
                player.setChoosableObjectives(playerView.choosableObjectives());
                PlayerBoard playerBoard = player.getPlayerBoard();
                playerBoard.setStarterCard(playerView.starterCard());
                // Check if the player has placed the starter card
                if (playerView.playerBoardView().playerBoard().get(new Coordinate(0, 0)) == null) {
                    continue;
                }
                // Set player board
                playerBoard.setPlayerBoard(playerView.playerBoardView().playerBoard());
                // Set player hand
                PlayerHand playerHand = player.getPlayerHand();
                playerView.playerHandView().hand().forEach(card -> {
                    playerHand.addCard(card);
                    globalBoard.getGoldDeck().removeCard(card.getCardId());
                    globalBoard.getResourceDeck().removeCard(card.getCardId());
                });
                // Check if the player has chosen a color
                if (playerView.color() == null) {
                    continue;
                }
                // Set color
                player.setPlayerColor(playerView.color());
                // Check if the player has chosen an objective
                if (playerView.objectiveCard() == null) {
                    continue;
                }
                // Set objective
                player.setPlayerObjective(playerView.objectiveCard().getCardId());
                // Set player points
                player.advancePlayerPos(playerView.playerPos());
                // Set game items
                playerBoard.setGameItems(playerView.playerBoardView().gameItemStore());
                playerView.playerBoardView().playerBoard().values().forEach(card -> {
                    globalBoard.getGoldDeck().removeCard(card.getCardId());
                    globalBoard.getResourceDeck().removeCard(card.getCardId());
                });
                // Set card placement index
                HashMap<Coordinate, GameCard> savedPlayerboard = playerView.playerBoardView().playerBoard();
                savedPlayerboard.keySet().forEach(coordinate -> playerBoard.getGameCard(coordinate).get().setPlacementIndex(savedPlayerboard.get(coordinate).getPlacementIndex()));
            }
            // Add the first cards to the global board
            globalBoard.getResourceDeck().getCards().addFirst(resourceFirstCard);
            globalBoard.getGoldDeck().getCards().addFirst(goldFirstCard);
            // Start the reconnect timer
            gameControllerMiddleware.startReconnectTimer();
        } catch (JsonSyntaxException e) {
            logger.warn("File {} is not a valid JSON file", file.getName());
        } catch (IOException e) {
            logger.warn("Reading file {} failed", file.getName());
        }
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String property = evt.getPropertyName();
        switch (property) {
            case "SAVE" -> {
                GameControllerView gameControllerView = (GameControllerView) evt.getNewValue();
                GameStatusEnum gameStatus = gameControllerView.gameStatus();
                if (gameStatus.equals(GameStatusEnum.GAME_OVER)) {
                    deletingTask(gameControllerView.gameView().gameName());
                    break;
                }
                if (!gameStatus.equals(GameStatusEnum.WAIT_FOR_PLAYERS) && !gameStatus.equals(GameStatusEnum.GAME_PAUSED)) {
                    savingTask(gameControllerView);
                }
            }
            case "DELETE" -> {
                String gameName = (String) evt.getNewValue();
                serverNetworkControllerMapper.deleteGame(gameName);
                deletingTask(gameName);
            }
            case "START_GAME" -> {
                GameControllerView gameControllerView = (GameControllerView) evt.getNewValue();
                String gameName = gameControllerView.gameView().gameName();
                serverNetworkControllerMapper.broadcastMessage(gameName, new UpdateViewServerToClientMessage(gameControllerView));
            }
            default -> logger.warn("Unknown property change event: {}", property);
        }
    }
}
