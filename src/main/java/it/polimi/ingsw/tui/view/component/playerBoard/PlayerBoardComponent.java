package it.polimi.ingsw.tui.view.component.playerBoard;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.tui.view.component.cards.gameCard.GameCardComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.drawer.Drawable;

import java.util.HashMap;

/**
 * This class represents a player's board component in the game.
 * It implements the Drawable interface, meaning it can be drawn on the screen.
 */
public class PlayerBoardComponent implements Drawable {

    /**
     * The draw area of the player's board component.
     */
    private final DrawArea drawArea;

    /**
     * Constructor for the PlayerBoardComponent class.
     * It initializes the drawArea and populates it with game cards.
     *
     * @param playerBoard A HashMap of Coordinate and GameCard objects representing the player's board.
     */
    public PlayerBoardComponent(HashMap<Coordinate, GameCard> playerBoard) {
        drawArea = new DrawArea();
        int correctionX = 5;
        int correctionY = 3;
        HashMap<Integer, Coordinate> realCoordinates = new HashMap<>();
        // We save the real coordinates of the card before converting them
        playerBoard.forEach((k, v) -> realCoordinates.put(v.getCardId(), new Coordinate(k.x, k.y)));
        // We convert the coordinates of the cards
        playerBoard = drawArea.convertCoordinates(playerBoard);
        playerBoard.forEach((coord, card) -> {
            GameCardComponent gameCardComponent = new GameCardComponent(card, realCoordinates.get(card.getCardId()));
            drawArea.drawAt(coord.x * (gameCardComponent.getWidth() - correctionX), coord.y * (gameCardComponent.getHeight() - correctionY), gameCardComponent.getDrawArea());
        });
    }

    /**
     * Returns the height of the drawable object.
     *
     * @return the height of the drawable object.
     */
    @Override
    public int getHeight() {
        return drawArea.getHeight();
    }

    /**
     * Returns the width of the drawable object.
     *
     * @return the width of the drawable object.
     */
    @Override
    public int getWidth() {
        return drawArea.getWidth();
    }

    /**
     * Returns the string representation of the drawable object.
     *
     * @return the string representation of the drawable object.
     */
    @Override
    public String toString() {
        return drawArea.toString();
    }

    /**
     * Returns the draw area of the drawable object.
     *
     * @return the draw area of the drawable object.
     */
    @Override
    public DrawArea getDrawArea() {
        return drawArea;
    }
}