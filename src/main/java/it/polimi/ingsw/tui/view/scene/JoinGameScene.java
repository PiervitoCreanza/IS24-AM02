package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.util.ArrayList;

/**
 * The JoinGameScene class represents the scene where a player can join a game.
 * It implements the Displayable interface.
 */
public class JoinGameScene implements Displayable {
    /**
     * The DrawArea object where the scene will be drawn.
     */
    private final DrawArea drawArea;

    /**
     * The controller that manages the user interface and the game logic.
     */
    private final TUIViewController controller;

    /**
     * The list of handlers for user input.
     */
    private final ArrayList<UserInputHandler> handlers = new ArrayList<>();

    /**
     * The current status of the scene.
     */
    private int status = 0;

    /**
     * Constructs a new JoinGameScene.
     *
     * @param controller the controller for this scene
     */
    public JoinGameScene(TUIViewController controller) {
        this.drawArea = new TitleComponent("Join Game").getDrawArea();
        this.controller = controller;
    }

    /**
     * This method is used to display the scene to the user.
     */
    @Override
    public void display() {
        drawArea.println();
        handlers.add(new UserInputHandler("Enter game ID:", input -> input.matches("\\d+")));
        handlers.add(new UserInputHandler("Enter your nickname:", input -> !input.isEmpty()));
        handlers.get(status).print();
    }

    /**
     * This method is used to handle user input.
     * It performs different actions based on the input and the current status of the scene.
     *
     * @param input the user's input
     */
    public void handleUserInput(String input) {
        if (input.equals("q")) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }
        if (handlers.get(status).validate(input)) {
            status++;
            if (status >= handlers.size()) {
                controller.joinGame(Integer.parseInt(handlers.get(0).getInput()), handlers.get(1).getInput());
                return;
            }
            handlers.get(status).print();
        }
    }
}