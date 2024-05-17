package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.component.cards.objectiveCard.ObjectiveCardComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.util.ArrayList;

/**
 * The InitSetObjectiveCardScene class represents the scene where the player sets their objective card.
 * It implements the Scene interface.
 */
public class InitSetObjectiveCardScene implements Scene {

    /**
     * The DrawArea object where the scene will be drawn.
     */
    private final DrawArea drawArea;

    /**
     * The controller that manages the user interface and the game logic.
     */
    private final TUIViewController controller;

    /**
     * The list of objective cards that the player can choose from.
     */
    private final ArrayList<ObjectiveCard> objectiveCards;

    /**
     * The UserInputHandler object that handles the user input.
     */
    private UserInputHandler handler;

    /**
     * Constructs a new InitSetObjectiveCardScene.
     * It initializes the drawArea and populates it with the available objective cards.
     *
     * @param controller     the controller for this scene
     * @param objectiveCards the list of objective cards that the player can choose from
     */
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

    /**
     * This method is used to display the scene to the user.
     */
    @Override
    public void display() {
        drawArea.println();
        handler = new UserInputHandler("Choose your Objective card [1/2]:", input -> input.matches("[1-2]"));
        handler.print();
    }

    /**
     * This method is used to handle user input.
     *
     * @param input the user's input
     */
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
