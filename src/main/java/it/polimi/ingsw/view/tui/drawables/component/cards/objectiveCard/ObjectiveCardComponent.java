package it.polimi.ingsw.view.tui.drawables.component.cards.objectiveCard;

import it.polimi.ingsw.model.card.objectiveCard.ItemObjectiveCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.card.objectiveCard.PositionalObjectiveCard;
import it.polimi.ingsw.view.tui.drawer.DrawArea;
import it.polimi.ingsw.view.tui.drawer.Drawable;

/**
 * This class represents a component of the Objective Card in the user interface.
 * It implements the Drawable interface, meaning it can be drawn to the console.
 * The class is responsible for managing and displaying the Objective Card.
 */
public class ObjectiveCardComponent implements Drawable {

    /**
     * The draw area of the Objective Card component.
     */
    private DrawArea drawArea;

    /**
     * Constructs a new ObjectiveCardComponent.
     * It initializes the draw area and draws the Objective Card.
     * If the Objective Card is null, an empty draw area is created.
     * If the Objective Card is a Positional Objective Card, a PositionalObjectiveCardComponent is created.
     * If the Objective Card is an Item Objective Card, an ItemObjectiveCardComponent is created.
     *
     * @param objectiveCard The Objective Card to be displayed.
     */
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
        if (objectiveCard == null) {
            drawArea = new DrawArea();
            return;
        }
        if (objectiveCard.isPositionalObjectiveCard()) {
            this.drawArea = new PositionalObjectiveCardComponent((PositionalObjectiveCard) objectiveCard, drawArea).getDrawArea();
        } else {
            this.drawArea = new ItemObjectiveCardComponent((ItemObjectiveCard) objectiveCard, drawArea).getDrawArea();
        }
    }

    /**
     * Returns a string representation of the Objective Card.
     *
     * @return a string representation of the Objective Card.
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

    /**
     * Returns the draw area of the drawable object.
     *
     * @return the draw area of the drawable object.
     */
    @Override
    public DrawArea getDrawArea() {
        return this.drawArea;
    }
}
