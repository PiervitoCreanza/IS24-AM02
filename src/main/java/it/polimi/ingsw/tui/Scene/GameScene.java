package it.polimi.ingsw.tui.Scene;

import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.network.virtualView.GameView;
import it.polimi.ingsw.tui.draw.DrawArea;
import it.polimi.ingsw.tui.draw.PlayerBoardComponent;
import it.polimi.ingsw.tui.draw.PlayerInventoryComponent;

public class GameScene {
    private final DrawArea drawArea;

    public GameScene(GameControllerView gameControllerView, String clientPlayerName) {
        drawArea = new DrawArea();
        GameView gameView = gameControllerView.gameView();
        String currentPlayer = gameView.currentPlayer();
        // Draw the current player board
        drawArea.drawAt(0, 0, new PlayerBoardComponent(gameControllerView.getCurrentPlayerView().playerBoardView().playerBoard()).getDrawArea());
        // Draw the player inventory
        drawArea.drawAt(0, drawArea.getHeight(), new PlayerInventoryComponent(gameView.globalBoardView().globalObjectives(), gameControllerView.getPlayerViewByName(clientPlayerName).objectiveCard(), gameControllerView.getPlayerViewByName(clientPlayerName).playerHandView().hand()).getDrawArea());
        if (currentPlayer.equals(clientPlayerName)) {
            drawArea.drawAt(0, drawArea.getHeight(), "It's your turn!");
        } else {
            drawArea.drawAt(0, drawArea.getHeight(), "It's " + currentPlayer + "'s turn!");
        }
    }
}
