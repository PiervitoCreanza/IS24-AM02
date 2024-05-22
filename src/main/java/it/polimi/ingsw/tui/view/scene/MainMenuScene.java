package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.MenuHandler;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.MenuItem;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.commands.EmptyCommand;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The MainMenuScene class represents the main menu scene of the game.
 * It implements the Scene and UserInputScene interfaces.
 */
public class MainMenuScene implements Scene, PropertyChangeListener {

    /**
     * The DrawArea object where the scene will be drawn.
     */
    private final DrawArea drawArea;

    /**
     * The controller that manages the user interface and the game logic.
     */
    private final TUIViewController controller;

    /**
     * The logger of the class.
     */
    private static final Logger logger = LogManager.getLogger(MainMenuScene.class);

    /**
     * The menu handler that manages the menu items.
     */
    private final MenuHandler menuHandler;

    /**
     * Constructs a new MainMenuScene.
     * It initializes the draw area and the menu handler.
     * The menu items are "list available games", "create a new game" and "quit".
     *
     * @param controller the controller for this scene
     */
    public MainMenuScene(TUIViewController controller) {
        this.controller = controller;
        this.drawArea = new TitleComponent("Main Menu").getDrawArea();
        this.menuHandler = new MenuHandler(this,
                new MenuItem("l", "list available games", new EmptyCommand()),
                new MenuItem("c", "create a new game", new EmptyCommand()),
                new MenuItem("q", "quit", new EmptyCommand())
        );
    }

    /**
     * This method is used to display the scene to the user.
     * It prints the draw area and the menu items.
     */
    @Override
    public void display() {
        this.drawArea.println();
        menuHandler.print();
    }

    /**
     * This method is used to handle user input.
     *
     * @param input the user's input
     */
    public void handleUserInput(String input) {
        menuHandler.handleInput(input);
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
            case "q" -> {
                System.out.println("Exiting...");
                System.exit(0);
            }
            case "l" -> controller.getGames();
            case "c" -> controller.selectScene(ScenesEnum.CREATE_GAME);
            default -> logger.error("Invalid property change event");
        }
    }
}