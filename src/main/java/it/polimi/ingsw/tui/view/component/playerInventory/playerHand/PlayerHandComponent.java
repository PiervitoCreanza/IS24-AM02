package it.polimi.ingsw.tui.view.component.playerInventory.playerHand;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.tui.view.component.cards.gameCard.GameCardComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.drawer.Drawable;

import java.util.ArrayList;

/**
 * This class represents a player's hand component in the game.
 * It implements the Drawable interface, meaning it can be drawn on the screen.
 */
public class PlayerHandComponent implements Drawable {

    /**
     * The draw area of the player's hand component.
     */
    private final DrawArea drawArea;

    /**
     * Constructor for the PlayerHandComponent class.
     * It initializes the drawArea and populates it with game cards.
     *
     * @param hand A list of GameCard objects representing the player's hand.
     */
    public PlayerHandComponent(ArrayList<GameCard> hand, int spacing) {
        drawArea = new DrawArea();
        if (hand.size() < 3)
            return;

        drawArea.drawAt(0, 0, new GameCardComponent(hand.getFirst(), 1).getDrawArea());
        drawArea.drawAt(drawArea.getWidth() + spacing, 0, new GameCardComponent(hand.get(1), 2).getDrawArea());
        drawArea.drawAt(drawArea.getWidth() + spacing, 0, new GameCardComponent(hand.get(2), 3).getDrawArea());
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
