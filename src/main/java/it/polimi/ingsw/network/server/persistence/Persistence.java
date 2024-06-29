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

public class Persistence implements PropertyChangeListener {

    private final MainController mainController;

    private final ServerNetworkControllerMapper serverNetworkControllerMapper;

    private final ExecutorService executor;

    private final Logger logger = LogManager.getLogger(Persistence.class);

    private final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(SerializableBooleanProperty.class, new SerializableBooleanPropertyAdapter())
            .registerTypeAdapter(ServerToClientMessage.class, new ServerToClientMessageAdapter()) // Registering a type adapter for ServerToClientMessage class
            .registerTypeAdapter(SideGameCard.class, new SideGameCardAdapter()) // Registering a type adapter for SideGameCard class
            .registerTypeAdapter(ObjectiveCard.class, new ObjectiveCardAdapter()) // Registering a type adapter for ObjectiveCard class
            .create();

    public Persistence(MainController mainController, ServerNetworkControllerMapper serverNetworkControllerMapper) {
        this.mainController = mainController;
        this.serverNetworkControllerMapper = serverNetworkControllerMapper;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void loadFromFile(String path) {
        File file = new File(path);
        checkFile(file);
        restoreModel(file);
    }
    
    public void loadAll() {
        File folder = new File("savedGames");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                checkFile(file);
                restoreModel(file);
            }
        }
    }

    private void checkFile(File file) {
        if (!file.exists()) {
            logger.fatal("File does not exist");
            System.exit(-1);
        }
        if (file.isDirectory()) {
            logger.fatal("File is a directory");
            System.exit(-1);
        }
        if (!file.isFile()) {
            logger.fatal("File is not an actual file");
            System.exit(-1);
        }
        if (!file.canRead()) {
            logger.fatal("File is not readable");
            System.exit(-1);
        }
    }

    private void saveGame(GameControllerView view) {
        try {
            // Calculate the hash of the game name to use as a filename
            // Avoiding possible problems with special characters
            String gameNameHash = String.valueOf(view.gameView().gameName().hashCode());
            File directory = new File("savedGames");
            if (!directory.exists()) {
                if (!directory.mkdir()) {
                    logger.fatal("Failed to create \"savedGames\" directory");
                    System.exit(-1);
                }
            }
            // Save the game to a JSON file
            FileWriter writer = new FileWriter("savedGames/" + gameNameHash + ".json");
            gson.toJson(view, writer);
            writer.close();
        } catch (IOException e) {
            logger.warn("Saving game: {} to file failed", view.gameView().gameName());
        }
    }

    private void deleteGame(String gameName) {
        String gameNameHash = String.valueOf(gameName.hashCode());
        File file = new File("savedGames/" + gameNameHash + ".json");
        checkFile(file);
        if (!file.delete()) {
            logger.warn("Failed to delete saved game file: {}", gameName);
        }
    }

    private void restoreModel(File file) {
        try {
            // Parse the JSON file for restoring th game status
            FileReader reader = new FileReader(file);
            GameControllerView gameControllerView = gson.fromJson(reader, GameControllerView.class);
            reader.close();
            // Add the game to the connections hashmap
            String gameName = gameControllerView.gameView().gameName();
            serverNetworkControllerMapper.getGameConnectionMapper().put(gameName, new HashMap<>());

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
                player.forceSetPlayerObjective(playerView.objectiveCard());
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
                    this.executor.submit(() -> deleteGame(gameControllerView.gameView().gameName()));
                    break;
                }
                if (!gameStatus.equals(GameStatusEnum.WAIT_FOR_PLAYERS) && !gameStatus.equals(GameStatusEnum.GAME_PAUSED)) {
                    this.executor.submit(() -> saveGame(gameControllerView));
                }
            }
            case "DELETE" -> {
                this.executor.submit(() -> {
                    String gameName = (String) evt.getNewValue();
                    serverNetworkControllerMapper.deleteGame(gameName);
                    deleteGame(gameName);
                });
            }
            case "START_GAME" -> {
                GameControllerView gameControllerView = (GameControllerView) evt.getNewValue();
                String gameName = gameControllerView.gameView().gameName();
                this.executor.submit(() -> serverNetworkControllerMapper.broadcastMessage(gameName, new UpdateViewServerToClientMessage(gameControllerView)));
            }
            default -> throw new IllegalArgumentException("Invalid property change");
        }
    }
}
