package it.polimi.ingsw.tui.view.component.cards.objectiveCard;

import it.polimi.ingsw.model.card.objectiveCard.ItemObjectiveCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.card.objectiveCard.PositionalObjectiveCard;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.drawer.Drawable;

public class ObjectiveCardComponent implements Drawable {
    private DrawArea drawArea;

    public ObjectiveCardComponent(ObjectiveCard objectiveCard) {
        drawArea = new DrawArea(
                """
                        ┌─────────────────────┐
                        │    ---              │
                        │  ┌─────┐            │
                        │  │  2  │            │
                        │  └─────┘            │
                        │    ---              │
                        └─────────────────────┘
                        """
        );
        if (objectiveCard.isPositionalObjectiveCard()) {
            this.drawArea = new PositionalObjectiveCardComponent((PositionalObjectiveCard) objectiveCard, drawArea).getDrawArea();
        } else {
            this.drawArea = new ItemObjectiveCardComponent((ItemObjectiveCard) objectiveCard, drawArea).getDrawArea();
        }
    }

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

    @Override
    public DrawArea getDrawArea() {
        return this.drawArea;
    }
}
