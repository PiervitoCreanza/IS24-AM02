package it.polimi.ingsw.tui.draw.cards.objectiveCard;

import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.card.objectiveCard.PositionalData;
import it.polimi.ingsw.model.card.objectiveCard.PositionalObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.tui.draw.DrawArea;
import it.polimi.ingsw.tui.draw.Drawable;
import it.polimi.ingsw.tui.utils.ColorsEnum;
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
public class ObjectiveCardComponent implements Drawable {
    private final DrawArea drawArea;

    public ObjectiveCardComponent(PositionalObjectiveCard objectiveCard) {
        drawArea = new DrawArea(
                """
                        ┌───────────────┐
                        │| ┌───┐        │
                        │| │   │        │
                        │  └───┘        │
                        │   ---         │
                        └───────────────┘
                        """
        );
        drawArea.setColor(ColorsEnum.CYAN);
        drawArea.drawAt(5, 2, objectiveCard.getPointsWon(), ColorsEnum.RED);
        ArrayList<PositionalData> pd = objectiveCard.getPositionalData();

        int positionalAnchorX = 10;
        int positionalAnchorY = 1;
        ArrayList<Pair<Coordinate, CardColorEnum>> pair = new ArrayList<>();
        for (PositionalData p : pd) {
            pair.add(new Pair<>(p.coordinate(), p.cardColorEnum()));
        }
        pair = drawArea.convertCoordinates(pair);
        pair.sort(Comparator.comparingInt(p -> p.key().y));
        int extraSpaceY = 0;
        for (Pair<Coordinate, CardColorEnum> p : pair) {
            System.out.println(p.key().x + " " + p.key().y + " " + p.value());
            // Remove empty rows
            if (pair.stream().noneMatch(tp -> p.key().y == tp.key().y + 1) && p.key().y != 0) {
                extraSpaceY += 1;
            }
            drawArea.drawAt(positionalAnchorX + p.key().x * 2, positionalAnchorY + p.key().y - extraSpaceY, "■", p.value().getColor());

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
