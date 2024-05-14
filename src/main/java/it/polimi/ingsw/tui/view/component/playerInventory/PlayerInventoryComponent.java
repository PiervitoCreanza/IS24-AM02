package it.polimi.ingsw.tui.view.component.playerInventory;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.tui.view.component.cards.objectiveCard.ObjectiveCardComponent;
import it.polimi.ingsw.tui.view.component.playerBoard.GlobalObjectivesComponent;
import it.polimi.ingsw.tui.view.component.playerInventory.playerHand.PlayerHandComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.drawer.Drawable;

import java.util.ArrayList;

public class PlayerInventoryComponent implements Drawable {
    private final DrawArea drawArea;

    public PlayerInventoryComponent(ArrayList<ObjectiveCard> globalObjectives, ObjectiveCard playerObjective, ArrayList<GameCard> hand) {
        drawArea = new DrawArea();
        int spacing = 5;
        drawArea.drawAt(2, 0, "Global Objectives:");
        drawArea.drawAt(2, 1, new GlobalObjectivesComponent(globalObjectives, spacing).getDrawArea());

        if (playerObjective != null) {
            int width = drawArea.getWidth();
            drawArea.drawAt(width + spacing, 0, "Your Objective:");
            drawArea.drawAt(width + spacing, 1, new ObjectiveCardComponent(playerObjective).getDrawArea());
        }
        if (hand.size() < 3) {
            return;
        }
        int height = drawArea.getHeight();
        drawArea.drawAt(2, height + 4, "Your Hand:");
        drawArea.drawAt(2, height + 5, new PlayerHandComponent(hand).getDrawArea());
        String divider = "═".repeat(drawArea.getWidth() - 1);
        drawArea.drawAt(2, height + 2, divider);
        drawArea.drawAt(2, drawArea.getHeight() + 2, divider);

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
