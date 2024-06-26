package it.polimi.ingsw.view.tui.drawables.component.decks;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.view.tui.drawables.component.cards.gameCard.GameCardComponent;
import it.polimi.ingsw.view.tui.drawer.DrawArea;
import it.polimi.ingsw.view.tui.drawer.Drawable;
import it.polimi.ingsw.view.tui.utils.ColorsEnum;

/**
 * This class represents the component of the decks in the game.
 * It implements the Drawable interface, meaning it can be drawn on the screen.
 */
public class DecksComponent implements Drawable {

    /**
     * The draw area of the decks component.
     */
    private final DrawArea drawArea;

    /**
     * Constructs a new DecksComponent with the given parameters.
     *
     * @param firstResourceCard The first card of the resource deck.
     * @param firstGoldCard     The first card of the gold deck.
     * @param spacing           The spacing between the decks.
     */
    public DecksComponent(GameCard firstResourceCard, GameCard firstGoldCard, int spacing) {
        drawArea = new DrawArea();
        drawArea.drawAt(2, 0, "Resource Deck:");
        drawArea.drawAt(2, 1, deckComponent(firstResourceCard, 5));
        int width = drawArea.getWidth();
        drawArea.drawAt(width + spacing, 0, "Gold Deck:");
        drawArea.drawAt(width + spacing, 1, deckComponent(firstGoldCard, 6));
    }

    /**
     * Creates a DrawArea for a deck with the given card.
     *
     * @param card The card to be drawn in the deck.
     * @param number The number of the deck.
     * @return The DrawArea representing the deck.
     */
    private DrawArea deckComponent(GameCard card, int number) {
        DrawArea deck = new DrawArea("""
                                
                ┌                       
                │                       
                │                       
                │                       
                │                       
                │                       
                └─────────────────────┘
                """);
        deck.setColor(ColorsEnum.BRIGHT_BLACK);
        if (card != null)
            card.switchSide();
        deck.drawAt(1, 0, card == null ? new GameCardComponent().getDrawArea() : new GameCardComponent(card).getDrawArea());
        deck.drawCenteredX(deck.getHeight(), String.valueOf(number));
        return deck;
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
     * Returns a string representation of the drawable object.
     *
     * @return a string representation of the drawable object.
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
