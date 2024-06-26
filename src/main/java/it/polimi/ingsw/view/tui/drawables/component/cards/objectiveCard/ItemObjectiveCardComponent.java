package it.polimi.ingsw.view.tui.drawables.component.cards.objectiveCard;

import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.objectiveCard.ItemObjectiveCard;
import it.polimi.ingsw.view.tui.drawables.component.cards.objectiveCard.itemGroup.ItemPairComponent;
import it.polimi.ingsw.view.tui.drawables.component.cards.objectiveCard.itemGroup.ItemTriangleComponent;
import it.polimi.ingsw.view.tui.drawer.DrawArea;
import it.polimi.ingsw.view.tui.drawer.Drawable;

import java.util.ArrayList;

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
        ArrayList<GameItemEnum> items = objectiveCard.getMultiplier().getNonEmptyKeys();

        int x = 15, y = 2;
        if (items.size() == 3)
            this.drawArea.drawAt(x, y, new ItemTriangleComponent(items).getDrawArea());
        else if (objectiveCard.getMultiplier().get(items.getFirst()) == 3)
            this.drawArea.drawAt(x, y, new ItemTriangleComponent(items.getFirst()).getDrawArea());
        else
            this.drawArea.drawAt(x, y, new ItemPairComponent(items.getFirst()).getDrawArea());
        this.drawArea.drawAt(x - 3, y + 2, "-------");
        this.drawArea.setColor(items.getFirst().getColor());

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
