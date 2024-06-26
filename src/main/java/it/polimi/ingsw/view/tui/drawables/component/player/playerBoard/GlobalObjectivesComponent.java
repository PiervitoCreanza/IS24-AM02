package it.polimi.ingsw.view.tui.drawables.component.player.playerBoard;

import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.view.tui.drawables.component.cards.objectiveCard.ObjectiveCardComponent;
import it.polimi.ingsw.view.tui.drawer.DrawArea;
import it.polimi.ingsw.view.tui.drawer.Drawable;

import java.util.ArrayList;

/**
 * This class represents the global objectives component in the game.
 * It implements the Drawable interface, meaning it can be drawn on the screen.
 */
public class GlobalObjectivesComponent implements Drawable {

    /**
     * The draw area of the global objectives component.
     */
    private final DrawArea drawArea;


    /**
     * Constructor for the GlobalObjectivesComponent class.
     * It initializes the drawArea and populates it with global objectives.
     *
     * @param globalObjectives A list of ObjectiveCard objects representing the global objectives.
     * @param spacing          The spacing between the global objectives.
     */
    public GlobalObjectivesComponent(ArrayList<ObjectiveCard> globalObjectives, int spacing) {
        drawArea = new DrawArea();
        drawArea.drawAt(0, 0, new ObjectiveCardComponent(globalObjectives.getFirst()).getDrawArea());
        drawArea.drawAt(drawArea.getWidth() + spacing, 0, new ObjectiveCardComponent(globalObjectives.getLast()).getDrawArea());
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
     * Returns the string representation of the drawable object.
     *
     * @return the string representation of the drawable object.
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
