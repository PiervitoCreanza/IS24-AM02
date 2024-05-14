package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.network.virtualView.GameView;
import it.polimi.ingsw.network.virtualView.PlayerView;
import it.polimi.ingsw.tui.view.component.playerBoard.PlayerBoardComponent;
import it.polimi.ingsw.tui.view.component.playerInventory.PlayerInventoryComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

/**
 * This class represents the game scene in the terminal user interface.
 * It implements the Diplayable interface, meaning it can be displayed on the screen.
 */
public class GameScene implements Diplayable {
    private final DrawArea drawArea = new DrawArea();

    /**
     * Constructs a new GameScene.
     *
     * @param gameControllerView The view of the game controller.
     * @param clientPlayerName   The name of the player logged in the client.
     */
    public GameScene(GameControllerView gameControllerView, String clientPlayerName) {
        GameView gameView = gameControllerView.gameView();
        String currentPlayer = gameView.currentPlayer();
        PlayerView clientView = gameView.getViewByPlayer(clientPlayerName);

        // Draw the current player board. This is the player that is currently playing.
        drawArea.drawAt(0, 0, new PlayerBoardComponent(gameControllerView.getCurrentPlayerView().playerBoardView().playerBoard()).getDrawArea());

        // Draw the player inventory. This is the player that is logged in the client.
        drawArea.drawAt(0, drawArea.getHeight(), new PlayerInventoryComponent(
                gameView.globalBoardView().globalObjectives(),
                clientView.objectiveCard(),
                clientView.playerHandView().hand()).getDrawArea()
        );

        // Draw the current player's name.
        if (currentPlayer.equals(clientPlayerName)) {
            drawArea.drawAt(0, drawArea.getHeight(), "It's your turn!", clientView.color().getColor());
        } else {
            drawArea.drawAt(0, drawArea.getHeight(), "It's " + currentPlayer + "'s turn!", gameControllerView.getCurrentPlayerView().color().getColor());
        }
    }

    /**
     * Displays the game scene on the console.
     */
    @Override
    public void display() {
        System.out.println(drawArea);
    }
}