package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.component.cards.objectiveCard.ObjectiveCardComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.util.ArrayList;

public class InitSetObjectiveCardScene implements Displayable {
    private final DrawArea drawArea;
    private final TUIViewController controller;
    private final ArrayList<ObjectiveCard> objectiveCards;
    private UserInputHandler handler;

    public InitSetObjectiveCardScene(TUIViewController controller, ArrayList<ObjectiveCard> objectiveCards) {
        this.objectiveCards = objectiveCards;
        this.drawArea = new TitleComponent("Choose your objective card: ").getDrawArea();

        DrawArea objectiveArea = new DrawArea();
        int width = 0;
        // Use this only for take the width of the objective card, it's not hardcoded :)
        ObjectiveCardComponent tempObjective = new ObjectiveCardComponent(objectiveCards.getFirst());
        int widthCounter = tempObjective.getWidth() / 2;
        int count = 1;
        for (ObjectiveCard objectiveCard : objectiveCards) {
            objectiveArea.drawAt(widthCounter, 0, count);
            widthCounter += tempObjective.getWidth() + 1;
            count++;
            objectiveArea.drawAt(width, 1, new ObjectiveCardComponent(objectiveCard));
            width += tempObjective.getWidth() + 1;
        }
        drawArea.drawCenteredX(drawArea.getHeight(), objectiveArea);

        this.controller = controller;
    }

    @Override
    public void display() {
        drawArea.println();
        handler = new UserInputHandler("Choose your Objective card [1/2]:", input -> input.matches("[1-2]"));
        handler.print();
    }

    public void handleUserInput(String input) {
        if (input.equals("q")) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }
        if (handler.validate(input)) {
            controller.setPlayerObjective(objectiveCards.get(Integer.parseInt(handler.getInput()) - 1).getCardId());
            return;
        }
        handler.print();
    }
}
