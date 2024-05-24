package it.polimi.ingsw.network.server.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.controller.gameController.GameControllerMiddleware;
import it.polimi.ingsw.data.ObjectiveCardAdapter;
import it.polimi.ingsw.data.SideGameCardAdapter;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GlobalBoard;
import it.polimi.ingsw.model.card.gameCard.SideGameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.player.PlayerHand;
import it.polimi.ingsw.network.client.message.adapter.ServerToClientMessageAdapter;
import it.polimi.ingsw.network.server.ServerNetworkControllerMapper;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.network.virtualView.PlayerView;

import java.io.FileReader;
import java.util.HashMap;

public class Persistence {

    private final MainController mainController;

    private final ServerNetworkControllerMapper serverNetworkControllerMapper;

    private final Gson gson = new GsonBuilder()
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
            serverNetworkControllerMapper.getGameConnectionMapper().put("Room1", new HashMap<>());
            GameControllerView gameControllerView = gson.fromJson(reader, ServerToClientMessage.class).getView();
            // Create the game
            boolean first = true;
            // Add all the players to the game
            for (PlayerView playerView : gameControllerView.gameView().playerViews()) {
                if (first) {
                    mainController.createGame("Room1", playerView.playerName(), gameControllerView.gameView().playerViews().size());
                    first = false;
                } else {
                    mainController.joinGame("Room1", playerView.playerName());
                }
            }
            // Set the game status
            Game game = mainController.getGameController("Room1").getGame();
            game.setCurrentPlayer(gameControllerView.getCurrentPlayerView().playerName());
            GameControllerMiddleware gameControllerMiddleware = mainController.getGameController("Room1");
            gameControllerMiddleware.setGameStatus(gameControllerView.gameStatus());
            gameControllerMiddleware.setRemainingRoundsToEndGame(gameControllerView.remainingRoundsToEndGame());
            gameControllerMiddleware.setLastRound(gameControllerView.isLastRound());
            GlobalBoard globalBoard = game.getGlobalBoard();
            globalBoard.setGlobalObjectives(gameControllerView.gameView().globalBoardView().globalObjectives());
            globalBoard.getFieldResourceCards().clear();
            globalBoard.getFieldGoldCards().clear();
            for (int i = 0; i < 2; i++) {
                globalBoard.getFieldGoldCards().add(globalBoard.getGoldDeck().draw());
                globalBoard.getFieldResourceCards().add(globalBoard.getResourceDeck().draw());
            }

            for (Player player : game.getPlayers()) {
                PlayerView playerView = gameControllerView.getPlayerViewByName(player.getPlayerName());
                PlayerHand playerHand = player.getPlayerHand();
                playerView.playerHandView().hand().forEach(card -> {
                    playerHand.addCard(card);
                    globalBoard.getGoldDeck().removeCard(card);
                    globalBoard.getResourceDeck().removeCard(card);
                });
                player.forceSetPlayerObjective(playerView.objectiveCard());
                player.advancePlayerPos(playerView.playerPos());
                player.setConnected(false);
                PlayerBoard playerBoard = player.getPlayerBoard();
                playerBoard.setPlayerBoard(playerView.playerBoardView().playerBoard());
                playerBoard.setGameItems(playerView.playerBoardView().gameItemStore());
                playerView.playerBoardView().playerBoard().values().forEach(card -> {
                    globalBoard.getGoldDeck().removeCard(card);
                    globalBoard.getResourceDeck().removeCard(card);
                });
            }
        } catch (Exception e) {
            throw new RuntimeException("Parsing failed");
        }
    }
}
