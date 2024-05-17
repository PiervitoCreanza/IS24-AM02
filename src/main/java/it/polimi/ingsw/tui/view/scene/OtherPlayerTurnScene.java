package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.tui.view.component.player.playerBoard.PlayerBoardComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.util.HashMap;

/**
 * This class represents the scene for when it is another player's turn.
 * It implements the Scene interface, meaning it can be displayed in the UI.
 */
public class OtherPlayerTurnScene implements Scene {
    /**
     * The DrawArea object where the scene will be drawn.
     */
    private final DrawArea drawArea;

    /**
     * Constructs a new OtherPlayerTurnScene.
     *
     * @param playerName  the name of the player whose turn it is
     * @param playerColor the color of the player whose turn it is
     * @param playerBoard the board of the player whose turn it is
     */
    public OtherPlayerTurnScene(String playerName, PlayerColorEnum playerColor, HashMap<Coordinate, GameCard> playerBoard) {
        this.drawArea = new DrawArea();
        this.drawArea.drawAt(0, 0, "It's");
        this.drawArea.drawAt(this.drawArea.getWidth() + 1, 0, playerName, playerColor.getColor());
        this.drawArea.drawAt(this.drawArea.getWidth(), 0, "'s turn!");
        this.drawArea.drawAt(this.drawArea.getWidth(), this.drawArea.getHeight() + 1, new PlayerBoardComponent(playerBoard));
    }

    /**
     * This method is used to display the object.
     */
    @Override
    public void display() {
        this.drawArea.println();
    }

    /**
     * @param input
     */
    @Override
    public void handleUserInput(String input) {

    }
}
