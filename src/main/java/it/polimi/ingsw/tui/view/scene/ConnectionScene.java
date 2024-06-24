package it.polimi.ingsw.tui.view.scene;


import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.utils.ColorsEnum;
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
 * The ConnectionScene class represents the scene where the player is notified of a connection error.
 * It implements the Scene and PropertyChangeListener interfaces.
 */
public class ConnectionScene implements Scene, PropertyChangeListener {

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
    private final Logger logger = LogManager.getLogger(GamePausedScene.class);

    /**
     * Constructs a new GamePausedScene.
     * It initializes the draw area and draws the title "Game Paused" at the top of the scene.
     *
     * @param controller the controller for this scene
     * @param message    the message to be displayed
     */
    public ConnectionScene(TUIViewController controller, String message, Boolean canRetry) {
        this.controller = controller;
        if(canRetry){
            this.menuHandler = new MenuHandler(this,
                    new MenuItem("r", "retry", new EmptyCommand()),
                    new MenuItem("q", "quit", new EmptyCommand()));
        } else {
            this.menuHandler = new MenuHandler(this,
                    new MenuItem("q", "quit", new EmptyCommand()));
        }
        this.drawArea = new DrawArea();
        this.drawArea.drawAt(0, 0, new TitleComponent("Connection Info", message.length()).getDrawArea());
        this.drawArea.drawNewLine(message, ColorsEnum.RED, 0);
    }

    /**
     * This method is used to display the object.
     */
    @Override
    public void display() {
        this.drawArea.println();
        menuHandler.print();
    }

    /**
     * This method is used to handle user input in a Scene.
     *
     * @param input The user input.
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
        switch (changedProperty) {
            case "r" -> controller.connect();
            case "q" -> System.exit(0);
            default -> logger.error("Invalid property change event");
        }
    }
}
