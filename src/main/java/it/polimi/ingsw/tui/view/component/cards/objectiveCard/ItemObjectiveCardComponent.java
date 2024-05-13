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
public class ItemObjectiveCardComponent implements Drawable {
    private final DrawArea drawArea;

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

    public DrawArea getDrawArea() {
        return drawArea;
    }

    @Override
    public String toString() {
        return drawArea.toString();
    }

    @Override
    public int getHeight() {
        return drawArea.getHeight();
    }

    @Override
    public int getWidth() {
        return drawArea.getWidth();
    }
}
