package it.polimi.ingsw.tui.draw.cards.objectiveCard;

import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.card.objectiveCard.PositionalData;
import it.polimi.ingsw.model.card.objectiveCard.PositionalObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.tui.draw.DrawArea;
import it.polimi.ingsw.tui.draw.Drawable;
import it.polimi.ingsw.tui.utils.Pair;

import java.util.ArrayList;
import java.util.Comparator;

/*
                ┌───────────────┐
                │  ┌───┐        │
                │  │ 2 │  ■     │
                │  └───┘  ■  ■  │
                │               │
                └───────────────┘
 */
public class PositionalObjectiveCardComponent implements Drawable {
    private final DrawArea drawArea;

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
