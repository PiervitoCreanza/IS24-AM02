package it.polimi.ingsw.tui.draw;

import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.card.objectiveCard.PositionalData;
import it.polimi.ingsw.model.card.objectiveCard.PositionalObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.tui.utils.Colors;
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
    private static final Colors colors = new Colors();
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
        drawArea.drawAt(5, 2, objectiveCard.getPointsWon());
        ArrayList<PositionalData> pd = objectiveCard.getPositionalData();

        ArrayList<Pair<Coordinate, CardColorEnum>> pair = new ArrayList<>();
        for (PositionalData p : pd) {
            pair.add(new Pair<>(p.coordinate(), p.cardColorEnum()));
        }
        pair = drawArea.convertCoordinates(pair);
        pair.forEach((p) -> {
            drawArea.drawAt(12 + p.key().x, 1 + p.key().y, "■", p.value().getColor());
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
