package it.polimi.ingsw.tui.draw;

import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.card.objectiveCard.PositionalData;
import it.polimi.ingsw.model.card.objectiveCard.PositionalObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.utils.Pair;

import java.util.ArrayList;

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
                        │  ┌───┐        │
                        │  │   │        │
                        │  └───┘        │
                        │               │
                        └───────────────┘
                        """
        );
        drawArea.setColor(ColorsEnum.CYAN);
        drawArea.drawAt(5, 2, objectiveCard.getPointsWon(), ColorsEnum.RED);
        ArrayList<PositionalData> pd = objectiveCard.getPositionalData();

        int positionalAnchorX = 12;
        int positionalAnchorY = 1;
        ArrayList<Pair<Coordinate, CardColorEnum>> pair = new ArrayList<>();
        for (PositionalData p : pd) {
            pair.add(new Pair<>(p.coordinate(), p.cardColorEnum()));
        }
        pair = drawArea.convertCoordinates(pair);
        ArrayList<Pair<Coordinate, CardColorEnum>> finalPair = pair;
        pair.forEach((p) -> {
            // Remove empty rows
            if (finalPair.stream().anyMatch(tp -> tp.key().x == p.key().x && tp.key().y == p.key().y + 2) && finalPair.stream().noneMatch(tp -> tp.key().y == p.key().y + 1)) {
                drawArea.drawAt(positionalAnchorX + p.key().x, positionalAnchorY + 1 + p.key().y, "■", p.value().getColor());
            } else {
                drawArea.drawAt(positionalAnchorX + p.key().x, positionalAnchorY + p.key().y, "■", p.value().getColor());
            }

        });

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
