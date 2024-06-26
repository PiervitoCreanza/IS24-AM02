package it.polimi.ingsw.view.tui.drawables.component.player.playerInventory;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.view.tui.drawables.component.cards.objectiveCard.ObjectiveCardComponent;
import it.polimi.ingsw.view.tui.drawables.component.player.playerBoard.GlobalObjectivesComponent;
import it.polimi.ingsw.view.tui.drawables.component.player.playerInventory.playerHand.PlayerHandComponent;
import it.polimi.ingsw.view.tui.drawer.DrawArea;
import it.polimi.ingsw.view.tui.drawer.Drawable;

import java.util.ArrayList;

/**
 * This class represents a player's inventory component in the game.
 * It implements the Drawable interface, meaning it can be drawn on the screen.
 */
public class PlayerInventoryComponent implements Drawable {

    /**
     * The draw area of the player's inventory component.
     */
    private final DrawArea drawArea;

    /**
     * Constructor for the PlayerInventoryComponent class.
     * It initializes the drawArea and populates it with global objectives, player objectives, and player hand.
     *
     * @param globalObjectives A list of ObjectiveCard objects representing the global objectives.
     * @param playerObjective  An ObjectiveCard object representing the player's objective.
     * @param hand             A list of GameCard objects representing the player's hand.
     * @param spacing          The spacing between the global objectives.
     */
    public PlayerInventoryComponent(ArrayList<ObjectiveCard> globalObjectives, ObjectiveCard playerObjective, ArrayList<GameCard> hand, int spacing) {
        drawArea = new DrawArea();

        DrawArea handArea = new DrawArea();
        handArea.drawAt(2, 0, "Your Hand:");
        handArea.drawAt(2, 1, new PlayerHandComponent(hand, spacing).getDrawArea());

        DrawArea ObjectivesArea = new DrawArea();
        ObjectivesArea.drawAt(0, 0, "Global Objectives:");
        ObjectivesArea.drawAt(0, 1, new GlobalObjectivesComponent(globalObjectives, spacing).getDrawArea());
        if (playerObjective != null) {
            int width = ObjectivesArea.getWidth();
            ObjectivesArea.drawAt(width + spacing, 0, "Your Objective:");
            ObjectivesArea.drawAt(width + spacing, 1, new ObjectiveCardComponent(playerObjective).getDrawArea());
        }

        drawArea.drawAt(0, 1, handArea);
        int width = drawArea.getWidth();
        drawArea.drawColumn(width + spacing, 1, drawArea.getHeight(), "║");
        drawArea.drawAt(drawArea.getWidth() + spacing, 1, ObjectivesArea);
        drawArea.drawAt(0, 0, "═".repeat(drawArea.getWidth() + 1));
        drawArea.drawNewLine("═".repeat(drawArea.getWidth() + 1), -1);
        drawArea.drawAt(width + spacing, 0, "╦");
        drawArea.drawAt(width + spacing, drawArea.getHeight() - 1, "╩");
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
