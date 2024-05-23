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
 * The DrawArea object where the scene will be drawn.
 * It implements the Scene and UserInputScene interfaces.
 */
public class GamePausedScene implements Scene, PropertyChangeListener {

    /**
     * The DrawArea object where the scene will be drawn.
     */
    private final DrawArea drawArea;

    /**
     * The controller for this scene.
     */
    private final TUIViewController controller;

    /**
     * The menu handler for handling user input.
     */
    private final MenuHandler menuHandler;

    /**
     * The logger.
     */
    private final Logger logger = LogManager.getLogger(GetGamesScene.class);

    /**
     * Constructs a new GamePausedScene.
     * It initializes the draw area and draws the title "Game Paused" at the top of the scene.
     *
     * @param controller the controller for this scene
     */
    public GamePausedScene(TUIViewController controller) {
        this.controller = controller;
        this.menuHandler = new MenuHandler(this, new MenuItem("q", "quit", new EmptyCommand()));
        this.drawArea = new DrawArea();
        this.drawArea.drawAt(0, 0, new TitleComponent("Game Paused").getDrawArea());
    }

    /**
     * This method is used to display the scene.
     */
    @Override
    public void display() {
        this.drawArea.println();
        menuHandler.print();
    }

    /**
     * @param input the user input
     */
    @Override
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
        if (changedProperty.equals("q")) {
            controller.sendDisconnect();
            controller.closeConnection();
        } else {
            logger.error("Invalid property change event");
        }
    }
}
