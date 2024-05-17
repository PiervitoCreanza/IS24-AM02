package it.polimi.ingsw.tui.view.component.cards.objectiveCard.itemGroup;

import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.drawer.Drawable;

/**
 * This class represents an item triangle component in the game.
 * It is used to represent the triangle of items in the objective cards.
 * It implements the Drawable interface, meaning it can be drawn on the screen.
 */
public class ItemTriangleComponent implements Drawable {
    /**
     * The draw area of the item triangle component.
     */
    private final DrawArea drawArea;

    /**
     * Constructor for the ItemTriangleComponent class.
     *
     * @param item1 the first item of the triangle.
     * @param item2 the second item of the triangle.
     * @param item3 the third item of the triangle.
     */
    public ItemTriangleComponent(GameItemEnum item1, GameItemEnum item2, GameItemEnum item3) {
        this.drawArea = new DrawArea();
        this.drawArea.drawAt(0, 0, item1.getSymbol(), item1.getColor());
        this.drawArea.drawAt(-1, 1, item2.getSymbol(), item2.getColor());
        this.drawArea.drawAt(1, 1, item3.getSymbol(), item3.getColor());
    }

    /**
     * Constructor for the ItemTriangleComponent class.
     *
     * @param item the item of the triangle.
     */
    public ItemTriangleComponent(GameItemEnum item) {
        this.drawArea = new DrawArea();
        this.drawArea.drawAt(0, 0, item.getSymbol(), item.getColor());
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
