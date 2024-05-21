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
 */
public class GamePausedScene implements Scene, PropertyChangeListener {
    /**
     * The DrawArea object where the scene will be drawn.
     */
    DrawArea drawArea;

    private final TUIViewController controller;

    private final MenuHandler menuHandler;

    private final Logger logger = LogManager.getLogger(GetGamesScene.class);

    /**
     * Constructs a new GamePausedScene.
     * It initializes the draw area and draws the title "Game Paused" at the top of the scene.
     */
    public GamePausedScene(TUIViewController controller) {
        this.controller = controller;
        this.menuHandler = new MenuHandler(this, new MenuItem("q", "quit the menu", new EmptyCommand()));
        this.drawArea = new DrawArea();
        this.drawArea.drawAt(0, 0, new TitleComponent("Game Paused").getDrawArea());
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
     * @param input the user input
     */
    @Override
    public void handleUserInput(String input) {
        menuHandler.handleInput(input);
    }

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
