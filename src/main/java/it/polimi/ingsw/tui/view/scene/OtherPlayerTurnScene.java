package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.tui.view.component.playerBoard.PlayerBoardComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.util.HashMap;

public class OtherPlayerTurnScene implements Displayable {
    private final DrawArea drawArea;

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
