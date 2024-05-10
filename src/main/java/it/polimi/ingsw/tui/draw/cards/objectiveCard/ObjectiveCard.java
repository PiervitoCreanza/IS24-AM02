package it.polimi.ingsw.tui.draw.cards.objectiveCard;

import it.polimi.ingsw.tui.draw.DrawArea;

public class ObjectiveCard {
    private final DrawArea drawArea;

    public ObjectiveCard(ObjectiveCard objectiveCard) {
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
    }

}
