package it.polimi.ingsw.tui.draw;

import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.tui.draw.cards.objectiveCard.ObjectiveCardComponent;

import java.util.ArrayList;

public class GlobalObjectivesComponent implements Drawable {
    private final DrawArea drawArea;

    public GlobalObjectivesComponent(ArrayList<ObjectiveCard> globalObjectives, int spacing) {
        drawArea = new DrawArea();
        drawArea.drawAt(0, 0, new ObjectiveCardComponent(globalObjectives.getFirst()).getDrawArea());
        drawArea.drawAt(drawArea.getWidth() + spacing, 0, new ObjectiveCardComponent(globalObjectives.getLast()).getDrawArea());
    }

    @Override
    public int getHeight() {
        return drawArea.getHeight();
    }

    @Override
    public int getWidth() {
        return drawArea.getWidth();
    }

    @Override
    public String toString() {
        return drawArea.toString();
    }

    @Override
    public DrawArea getDrawArea() {
        return drawArea;
    }
}
