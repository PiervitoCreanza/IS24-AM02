package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.GameInfoComponent;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.MenuHandler;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.MenuItem;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.commands.EmptyCommand;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * This class represents the scene for getting games.
 * It implements the Scene interface, meaning it can be displayed in the UI.
 */
public class GetGamesScene implements Scene, PropertyChangeListener {

    /**
     * The DrawArea object where the scene will be drawn.
     */
    private final DrawArea drawArea;

    /**
     * The controller that manages the user interface and the game logic.
     */
    private final TUIViewController controller;

    private final MenuHandler menuHandler;

    private final Logger logger = LogManager.getLogger(GetGamesScene.class);

    /**
     * Constructs a new GetGamesScene.
     *
     * @param controller the controller for this scene
     * @param games      the list of games to be displayed
     */
    public GetGamesScene(TUIViewController controller, ArrayList<GameRecord> games) {
        this.drawArea = new TitleComponent("Game List").getDrawArea();
        int i = 0;
        for (GameRecord game : games) {
            drawArea.drawAt(0, drawArea.getHeight(), new GameInfoComponent(game, i++).getDrawArea());
        }
        drawArea.drawAt(0, drawArea.getHeight(), "-".repeat(drawArea.getWidth()));

        this.menuHandler = new MenuHandler(this,
                new MenuItem("l", "refresh the list", new EmptyCommand()),
                new MenuItem("j", "join a game", new EmptyCommand()),
                new MenuItem("c", "create a new game", new EmptyCommand()),
                new MenuItem("q", "quit the menu", new EmptyCommand())
        );
        this.controller = controller;
    }

    /**
     * Displays the game scene on the console.
     * It reads the user input and performs the corresponding action.
     */
    @Override
    public void display() {
        drawArea.println();
        menuHandler.print();
    }

    /**
     * Returns the draw area of the get games scene.
     *
     * @return the draw area of the get games scene
     */
    public DrawArea getDrawArea() {
        return drawArea;
    }

    /**
     * Handles the user input.
     *
     * @param input the user input
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
            case "q" -> controller.selectScene(ScenesEnum.MAIN_MENU);
            case "l" -> controller.getGames();
            case "j" -> controller.selectScene(ScenesEnum.JOIN_GAME);
            case "c" -> controller.selectScene(ScenesEnum.CREATE_GAME);
            default -> logger.error("Invalid property change event");
        }
    }
}