package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.component.playerInventory.PlayerInventoryComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DrawCardScene implements Diplayable, UserInputScene {
    private final DrawArea drawArea;
    private final TUIViewController controller;

    public DrawCardScene(TUIViewController controller, ArrayList<ObjectiveCard> globalObjectives, ObjectiveCard playerObjective, ArrayList<GameCard> hand) {
        this.controller = controller;
        this.drawArea = new TitleComponent("Draw Card").getDrawArea();
        drawArea.drawAt(0, this.drawArea.getHeight(), new PlayerInventoryComponent(globalObjectives, playerObjective, hand, 0));
        // TODO: Implement Interface
    }

    /**
     * This method is used to display the object.
     */
    @Override
    public void display() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        drawArea.println();
        // TODO: Implement
        controller.drawCardFromResourceDeck();

    }
}
