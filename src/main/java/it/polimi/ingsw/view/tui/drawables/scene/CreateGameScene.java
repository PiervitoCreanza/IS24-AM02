package it.polimi.ingsw.view.tui.drawables.scene;

import it.polimi.ingsw.view.tui.controller.TUIViewController;
import it.polimi.ingsw.view.tui.drawables.component.TitleComponent;
import it.polimi.ingsw.view.tui.drawables.component.userInputHandler.UserInputHandler;
import it.polimi.ingsw.view.tui.drawables.component.userInputHandler.menu.commands.UserInputChain;
import it.polimi.ingsw.view.tui.drawer.DrawArea;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * The CreateGameScene class represents the scene where the player can create a new game.
 * It implements the Scene and UserInputScene interfaces.
 */
public class CreateGameScene implements Scene, PropertyChangeListener {

    /**
     * The DrawArea object where the scene will be drawn.
     */
    private final DrawArea drawArea;

    /**
     * The controller that manages the user interface and the game logic.
     */
    private final TUIViewController controller;

    /**
     * The UserInputChain object that handles the user input.
     */
    private final UserInputChain userInputChain;

    /**
     * Constructs a new CreateGameScene.
     * It initializes the DrawArea and the UserInputChain with the user input handlers.
     *
     * @param controller the controller for this scene
     */
    public CreateGameScene(TUIViewController controller) {
        this.drawArea = new TitleComponent("Create Game").getDrawArea();
        this.controller = controller;

        this.userInputChain = new UserInputChain(this,
                new UserInputHandler("Choose the name of the game:", input -> !input.isEmpty()),
                new UserInputHandler("Choose the number of players [2-4]:", input -> input.matches("[2-4]")),
                new UserInputHandler("Enter your nickname:", input -> !input.isEmpty())
        );
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
        userInputChain.print();
    }

    /**
     * Handles the user input thanks to the UserInputChain.
     *
     * @param input the user input
     */
    public void handleUserInput(String input) {
        userInputChain.handleInput(input);
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String changedProperty = evt.getPropertyName();
        switch (changedProperty) {
            case "q" -> controller.selectScene(ScenesEnum.MAIN_MENU);
            case "input" -> {
                @SuppressWarnings("unchecked")
                ArrayList<String> inputs = (ArrayList<String>) evt.getNewValue();
                controller.createGame(inputs.get(0), inputs.get(2), Integer.parseInt(inputs.get(1)));
            }
            default -> handleUserInput((String) evt.getNewValue());
        }
    }
}