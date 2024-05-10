package it.polimi.ingsw.tui.draw.cards.objectiveCard;

import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.objectiveCard.ItemObjectiveCard;
import it.polimi.ingsw.tui.draw.DrawArea;
import it.polimi.ingsw.tui.draw.Drawable;

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

    public ItemObjectiveCardComponent(ItemObjectiveCard objectiveCard) {
        drawArea = new DrawArea(
                """
                        ┌───────────────┐
                        │  ┌───┐        │
                        │  │   │        │
                        │  └───┘        │
                        │               │
                        └───────────────┘
                        """
        );

        drawArea.setColor(objectiveCard.getMultiplier().getNonEmptyKeys().getFirst().getColor());
        drawArea.drawAt(5, 2, objectiveCard.getPointsWon());
        for (int i = 0; i < objectiveCard.getMultiplier().getNonEmptyKeys().size(); i++) {
            GameItemEnum item = objectiveCard.getMultiplier().getNonEmptyKeys().get(i);
            for (int j = 0; j < objectiveCard.getMultiplier().get(item); j++) {
                drawArea.drawAt(12, 1 + i + j, item.getSymbol(), item.getColor());
            }

        }


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
