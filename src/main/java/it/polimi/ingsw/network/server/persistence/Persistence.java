package it.polimi.ingsw.network.server.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.network.virtualView.PlayerView;
import it.polimi.ingsw.parsing.adapters.ObjectiveCardAdapter;
import it.polimi.ingsw.parsing.adapters.SerializableBooleanPropertyAdapter;
import it.polimi.ingsw.parsing.adapters.SideGameCardAdapter;

import java.io.FileReader;
import java.util.HashMap;

public class Persistence {

    private final MainController mainController;

    private final ServerNetworkControllerMapper serverNetworkControllerMapper;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(SerializableBooleanProperty.class, new SerializableBooleanPropertyAdapter())
            .registerTypeAdapter(ServerToClientMessage.class, new ServerToClientMessageAdapter()) // Registering a type adapter for ServerToClientMessage class
            .registerTypeAdapter(SideGameCard.class, new SideGameCardAdapter()) // Registering a type adapter for SideGameCard class
            .registerTypeAdapter(ObjectiveCard.class, new ObjectiveCardAdapter()) // Registering a type adapter for ObjectiveCard class
            .create();

    public Persistence(MainController mainController, ServerNetworkControllerMapper serverNetworkControllerMapper) {
        this.mainController = mainController;
        this.serverNetworkControllerMapper = serverNetworkControllerMapper;
    }

    public void load(String filename) {
        try {
            // Parse the JSON file for restoring th game status
            FileReader reader = new FileReader("src/main/java/it/polimi/ingsw/network/server/persistence/" + filename + ".json");
            GameControllerView gameControllerView = gson.fromJson(reader, ServerToClientMessage.class).getView();
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
            // Set the game status
            Game game = mainController.getGameController(gameName).getGame();
            game.setCurrentPlayer(gameControllerView.getCurrentPlayerView().playerName());
            GameControllerMiddleware gameControllerMiddleware = mainController.getGameController(gameName);
            gameControllerMiddleware.setGameStatus(gameControllerView.gameStatus());
            gameControllerMiddleware.setRemainingRoundsToEndGame(gameControllerView.remainingRoundsToEndGame());
            gameControllerMiddleware.setLastRound(gameControllerView.isLastRound());
            GlobalBoard globalBoard = game.getGlobalBoard();
            globalBoard.setGlobalObjectives(gameControllerView.gameView().globalBoardView().globalObjectives());
            globalBoard.getFieldResourceCards().forEach(card -> globalBoard.getResourceDeck().addCard(card));
            globalBoard.getFieldGoldCards().forEach(card -> globalBoard.getGoldDeck().addCard(card));
            globalBoard.getFieldResourceCards().clear();
            globalBoard.getFieldGoldCards().clear();
            gameControllerView.gameView().globalBoardView().fieldResourceCards().forEach(card -> globalBoard.getFieldResourceCards().add(card));
            gameControllerView.gameView().globalBoardView().fieldGoldCards().forEach(card -> globalBoard.getFieldGoldCards().add(card));
            for (Player player : game.getPlayers()) {
                PlayerView playerView = gameControllerView.getPlayerViewByName(player.getPlayerName());
                PlayerHand playerHand = player.getPlayerHand();
                playerView.playerHandView().hand().forEach(card -> {
                    playerHand.addCard(card);
                    globalBoard.getGoldDeck().removeCard(card.getCardId());
                    globalBoard.getResourceDeck().removeCard(card.getCardId());
                });
                player.setChoosableObjectives(playerView.choosableObjectives());
                player.forceSetPlayerObjective(playerView.objectiveCard());
                player.advancePlayerPos(playerView.playerPos());
                player.setConnected(false);
                player.setPlayerColor(playerView.color());
                PlayerBoard playerBoard = player.getPlayerBoard();
                playerBoard.setStarterCard(playerView.starterCard());
                playerBoard.setPlayerBoard(playerView.playerBoardView().playerBoard());
                playerBoard.setGameItems(playerView.playerBoardView().gameItemStore());
                playerView.playerBoardView().playerBoard().values().forEach(card -> {
                    globalBoard.getGoldDeck().removeCard(card.getCardId());
                    globalBoard.getResourceDeck().removeCard(card.getCardId());
                });
                HashMap<Coordinate, GameCard> savedPlayerboard = playerView.playerBoardView().playerBoard();
                savedPlayerboard.keySet().forEach(coordinate -> playerBoard.getGameCard(coordinate).get().setPlacementIndex(savedPlayerboard.get(coordinate).getPlacementIndex()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Parsing failed");
        }
    }
}
