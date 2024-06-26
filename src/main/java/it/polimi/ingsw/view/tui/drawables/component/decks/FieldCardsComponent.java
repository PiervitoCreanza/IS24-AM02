package it.polimi.ingsw.view.tui.drawables.component.decks;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.view.tui.drawables.component.cards.gameCard.GameCardComponent;
import it.polimi.ingsw.view.tui.drawer.DrawArea;
import it.polimi.ingsw.view.tui.drawer.Drawable;

import java.util.ArrayList;

/**
 * This class represents the field component of the player's inventory.
 * It implements the Drawable interface, meaning it can be drawn in the UI.
 */
public class FieldCardsComponent implements Drawable {

    /**
     * The draw area of the field component.
     */
    private final DrawArea drawArea;

    /**
     * The count of the cards in the field.
     */
    private int count = 1;

    /**
     * The spacing between cards.
     */
    private final int spacing;

    /**
     * Constructs a new FieldCardsComponent.
     *
     * @param fieldResourceCards the resource cards in the field
     * @param fieldGoldCards     the gold cards in the field
     * @param spacing            the spacing between the resource cards and gold cards
     */
    public FieldCardsComponent(ArrayList<GameCard> fieldResourceCards, ArrayList<GameCard> fieldGoldCards, int spacing) {
        drawArea = new DrawArea();
        this.spacing = spacing;
        drawArea.drawAt(2, 0, "Resources card:");
        drawArea.drawAt(2, 1, twoCardsComponent(fieldResourceCards));

        int height = drawArea.getHeight();
        drawArea.drawAt(2, height, "Gold card:");
        drawArea.drawAt(2, height + 1, twoCardsComponent(fieldGoldCards));
    }

    /**
     * Creates a DrawArea for the given cards.
     *
     * @param cards the cards to create a DrawArea for
     * @return the created DrawArea
     */
    private DrawArea twoCardsComponent(ArrayList<GameCard> cards) {
        DrawArea cardsArea = new DrawArea();
        if (cards.size() == 2)
            cards.forEach(card -> cardsArea.drawAt((cardsArea.getWidth() == 0) ? 0 : cardsArea.getWidth() + spacing, 0, new GameCardComponent(card, count++).getDrawArea()));
        else if (cards.size() == 1) {
            cardsArea.drawAt(0, 0, new GameCardComponent(cards.getFirst(), count++).getDrawArea());
            cardsArea.drawAt(cardsArea.getWidth() + spacing, 0, new GameCardComponent(count++).getDrawArea());
        } else {
            cardsArea.drawAt(0, 0, new GameCardComponent(count++).getDrawArea());
            cardsArea.drawAt(cardsArea.getWidth() + spacing, 0, new GameCardComponent(count++).getDrawArea());
        }
        return cardsArea;
    }

    /**
     * Returns the height of the draw area.
     *
     * @return the height of the draw area
     */
    @Override
    public int getHeight() {
        return drawArea.getHeight();
    }

    /**
     * Returns the width of the draw area.
     *
     * @return the width of the draw area
     */
    @Override
    public int getWidth() {
        return drawArea.getWidth();
    }

    /**
     * Returns the draw area of the field component.
     *
     * @return the draw area of the field component
     */
    @Override
    public DrawArea getDrawArea() {
        return drawArea;
    }

    /**
     * Returns a string representation of the field component.
     *
     * @return a string representation of the field component
     */
    @Override
    public String toString() {
        return drawArea.toString();
    }
}