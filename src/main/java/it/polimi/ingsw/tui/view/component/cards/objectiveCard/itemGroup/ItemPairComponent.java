package it.polimi.ingsw.tui.view.component.cards.objectiveCard.itemGroup;

import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.drawer.Drawable;

/**
 * This class represents an item pair component in the game.
 * It is used to represent the pair of items in the objective cards.
 * It implements the Drawable interface, meaning it can be drawn on the screen.
 */
public class ItemPairComponent implements Drawable {

    /**
     * The draw area of the item triangle component.
     */
    private final DrawArea drawArea;

    /**
     * Constructor for the ItemPairComponent class.
     *
     * @param item1 the first item of the pair.
     * @param item2 the second item of the pair.
     */
    public ItemPairComponent(GameItemEnum item1, GameItemEnum item2) {
        this.drawArea = new DrawArea();
        this.drawArea.drawAt(-1, 1, item1.getSymbol(), item1.getColor());
        this.drawArea.drawAt(1, 1, item2.getSymbol(), item2.getColor());
    }

    /**
     * Constructor for the ItemPairComponent class.
     *
     * @param item the items of the pair.
     */
    public ItemPairComponent(GameItemEnum item) {
        this.drawArea = new DrawArea();
        this.drawArea.drawAt(-1, 1, item.getSymbol(), item.getColor());
        this.drawArea.drawAt(1, 1, item.getSymbol(), item.getColor());
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
     * Returns the draw area of the drawable object.
     *
     * @return the draw area of the drawable object.
     */
    @Override
    public DrawArea getDrawArea() {
        return drawArea;
    }
}
