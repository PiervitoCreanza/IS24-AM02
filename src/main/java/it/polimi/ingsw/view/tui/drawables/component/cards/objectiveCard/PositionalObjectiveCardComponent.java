package it.polimi.ingsw.view.tui.drawables.component.cards.objectiveCard;

import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.card.objectiveCard.PositionalData;
import it.polimi.ingsw.model.card.objectiveCard.PositionalObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.view.tui.drawer.DrawArea;
import it.polimi.ingsw.view.tui.drawer.Drawable;
import it.polimi.ingsw.view.tui.utils.Pair;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class represents a component of the Positional Objective Card in the user interface.
 * It implements the Drawable interface, meaning it can be drawn to the console.
 * The class is responsible for managing and displaying the Positional Objective Card.
 */
public class PositionalObjectiveCardComponent implements Drawable {

    // The area where the Positional Objective Card is drawn
    /**
     * The draw area of the Positional Objective Card component.
     */
    private final DrawArea drawArea;

    /**
     * Constructs a new PositionalObjectiveCardComponent.
     * It initializes the draw area and draws the Positional Objective Card at the specified coordinates.
     *
     * @param objectiveCard The Positional Objective Card to be displayed.
     * @param drawArea      The area where the Positional Objective Card is drawn.
     */
    public PositionalObjectiveCardComponent(PositionalObjectiveCard objectiveCard, DrawArea drawArea) {
        this.drawArea = drawArea;
        ArrayList<PositionalData> pd = objectiveCard.getPositionalData();

        ArrayList<Pair<Coordinate, CardColorEnum>> pair = new ArrayList<>();
        for (PositionalData p : pd) {
            pair.add(new Pair<>(p.coordinate(), p.cardColorEnum()));
        }
        // Convert from positive to negative y-axis
        pair = drawArea.convertCoordinates(pair);

        // Sort the pair by y-axis
        pair.sort(Comparator.comparingInt(p -> p.key().y));
        // Set anchor points.
        int x = 12, y = 2;
        // If there are only 2 column we add 2 spaces to center it.
        if (pair.stream().noneMatch(p -> p.key().x == 2)) {
            x += 2;
        }

        int extraSpaceY = 0;
        for (Pair<Coordinate, CardColorEnum> p : pair) {
            // Remove empty rows
            if (pair.stream().noneMatch(tp -> p.key().y == tp.key().y + 1) && p.key().y != 0) {
                extraSpaceY += 1;
            }
            drawArea.drawAt(x + p.key().x * 3, y + p.key().y - extraSpaceY, "██", p.value().getColor());
        }

        this.drawArea.drawAt(6, 3, objectiveCard.getPointsWon());
    }

    /**
     * Returns the draw area of the Positional Objective Card.
     *
     * @return the draw area of the Positional Objective Card.
     */
    public DrawArea getDrawArea() {
        return drawArea;
    }

    /**
     * Returns a string representation of the Positional Objective Card.
     *
     * @return a string representation of the Positional Objective Card.
     */
    @Override
    public String toString() {
        return drawArea.toString();
    }

    /**
     * Returns the height of the Positional Objective Card.
     *
     * @return the height of the Positional Objective Card.
     */
    @Override
    public int getHeight() {
        return drawArea.getHeight();
    }

    /**
     * Returns the width of the Positional Objective Card.
     *
     * @return the width of the Positional Objective Card.
     */
    @Override
    public int getWidth() {
        return drawArea.getWidth();
    }
}