package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.component.userInputHandler.UserInputHandler;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.commands.UserInputChain;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * The JoinGameScene class represents the scene where a player can join a game.
 * It implements the Scene and UserInputScene interfaces.
 */
public class JoinGameScene implements Scene, PropertyChangeListener {

    /**
     * The DrawArea object where the scene will be drawn.
     */
    private final DrawArea drawArea;

    /**
     * The controller that manages the user interface and the game logic.
     */
    private final TUIViewController controller;

    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(JoinGameScene.class);

    /**
     * The input handler for this scene.
     */
    private final UserInputChain inputHandler;

    /**
     * Constructs a new JoinGameScene.
     * It initializes the draw area and the controller.
     * It also initializes the input handler with the required input fields.
     *
     * @param controller the controller for this scene
     */
    public JoinGameScene(TUIViewController controller) {
        this.drawArea = new TitleComponent("Join Game").getDrawArea();
        this.controller = controller;
        this.inputHandler = new UserInputChain(this,
                new UserInputHandler("Enter game ID:", input -> input.matches("\\d+")),
                new UserInputHandler("Enter your nickname:", input -> !input.isEmpty())
        );
    }

    /**
     * This method is used to display the scene to the user.
     * It prints the draw area and the input handler.
     */
    @Override
    public void display() {
        drawArea.println();
        inputHandler.print();
    }

    /**
     * This method is used to handle user input.
     * It performs different actions based on the input and the current status of the scene.
     *
     * @param input the user's input
     */
    public void handleUserInput(String input) {
        inputHandler.handleInput(input);
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
        @SuppressWarnings("unchecked")
        ArrayList<String> inputs = (ArrayList<String>) evt.getNewValue();
        switch (changedProperty) {
            case "q" -> controller.selectScene(ScenesEnum.MAIN_MENU);
            case "input" -> controller.joinGame(Integer.parseInt(inputs.get(0)), inputs.get(1));
            default -> logger.error("Invalid property change event");
        }
    }
}