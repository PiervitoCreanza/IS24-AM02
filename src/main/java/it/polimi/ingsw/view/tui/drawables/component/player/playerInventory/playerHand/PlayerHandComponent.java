package it.polimi.ingsw.view.tui.drawables.component.player.playerInventory.playerHand;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.view.tui.drawables.component.cards.gameCard.GameCardComponent;
import it.polimi.ingsw.view.tui.drawer.DrawArea;
import it.polimi.ingsw.view.tui.drawer.Drawable;

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
     * The count of the player's hand.
     */
    private int count = 1;

    /**
     * Constructor for the PlayerHandComponent class.
     * It initializes the drawArea and populates it with game cards.
     *
     * @param hand A list of GameCard objects representing the player's hand.
     * @param spacing The spacing between the cards.
     */
    public PlayerHandComponent(ArrayList<GameCard> hand, int spacing) {
        drawArea = new DrawArea();

        int nCards = hand.size();
        int hole = 3 - nCards;

        hand.forEach(card -> drawArea.drawAt((drawArea.getWidth() == 0) ? 0 : drawArea.getWidth() + spacing, 0, new GameCardComponent(card, count++).getDrawArea()));
        for (int i = 0; i < hole; i++) {
            drawArea.drawAt((drawArea.getWidth() == 0) ? 0 : drawArea.getWidth() + spacing, 0, new GameCardComponent(count++).getDrawArea());
        }
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
