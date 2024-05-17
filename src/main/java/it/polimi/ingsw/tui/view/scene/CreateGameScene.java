package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.util.ArrayList;

/**
 * The CreateGameScene class represents the scene where the player can create a new game.
 * It implements the Scene and UserInputScene interfaces.
 */
public class CreateGameScene implements Scene {
    /**
     * The DrawArea object where the scene will be drawn.
     */
    private final DrawArea drawArea;
    /**
     * The controller that manages the user interface and the game logic.
     */
    private final TUIViewController controller;

    /**
     * The list of handlers for the user input. Each handler corresponds to a different input field.
     */
    private final ArrayList<UserInputHandler> handlers = new ArrayList<>();

    /**
     * The status of the scene.
     */
    private int status;

    /**
     * Constructs a new CreateGameScene.
     *
     * @param controller the controller for this scene
     */
    public CreateGameScene(TUIViewController controller) {
        this.drawArea = new TitleComponent("Create Game").getDrawArea();
        this.controller = controller;
    }

    /**
     * This method is used to display the scene to the user.
     * It prompts the user to enter the name of the game, the number of players, and their nickname.
     * If the user enters valid input, it calls the controller's createGame method to create the game.
     * If the user enters invalid input or quits, it returns to the main menu.
     */
    @Override
    public void display() {
        drawArea.println();
        handlers.add(new UserInputHandler("Choose the name of the game:", input -> !input.isEmpty()));
        handlers.add(new UserInputHandler("Choose the number of players [2-4]:", input -> input.matches("[2-4]")));
        handlers.add(new UserInputHandler("Enter your nickname:", input -> !input.isEmpty()));
        handlers.get(status).print();
    }

    /**
     * Handles the user input.
     *
     * @param input the user input
     */
    public void handleUserInput(String input) {
        if (input.equals("q")) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }
        if (handlers.get(status).validate(input)) {
            status++;
            if (status >= handlers.size()) {
                controller.createGame(handlers.get(0).getInput(), handlers.get(2).getInput(), Integer.parseInt(handlers.get(1).getInput()));
                return;
            }

        }
        handlers.get(status).print();
    }
}