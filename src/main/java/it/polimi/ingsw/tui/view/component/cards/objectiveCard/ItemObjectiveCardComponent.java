package it.polimi.ingsw.tui.view.component.cards.objectiveCard;

import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.objectiveCard.ItemObjectiveCard;
import it.polimi.ingsw.tui.view.component.cards.objectiveCard.itemGroup.ItemTriangleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.drawer.Drawable;

/*
                ┌───────────────┐
                │  ┌───┐        │
                │  │ 2 │  ■     │
                │  └───┘  ■  ■  │
                │               │
                └───────────────┘
 */

/**
 * This class represents an item objective card component in the game.
 */
public class ItemObjectiveCardComponent implements Drawable {

    /**
     * The draw area of the item objective card component.
     */
    private final DrawArea drawArea;

    /**
     * Constructor for the ItemObjectiveCardComponent class.
     *
     * @param objectiveCard the objective card to be drawn.
     * @param drawArea      the draw area to be used.
     */
    public ItemObjectiveCardComponent(ItemObjectiveCard objectiveCard, DrawArea drawArea) {
        this.drawArea = drawArea;
        // There is only one item in the multiplier
        GameItemEnum item = objectiveCard.getMultiplier().getNonEmptyKeys().getFirst();

        int x = 15, y = 2;
        this.drawArea.drawAt(x, y, new ItemTriangleComponent(item).getDrawArea());
        this.drawArea.drawAt(x - 3, y + 2, "-------");
        this.drawArea.setColor(item.getColor());

        this.drawArea.drawAt(6, 3, objectiveCard.getPointsWon());
    }

    /**
     * Returns the draw area of the drawable object.
     *
     * @return the draw area of the drawable object.
     */
    public DrawArea getDrawArea() {
        return drawArea;
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
}
