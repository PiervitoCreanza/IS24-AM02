package it.polimi.ingsw.view.tui.drawables.scene;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.view.tui.controller.TUIViewController;
import it.polimi.ingsw.view.tui.drawables.component.GameInfoComponent;
import it.polimi.ingsw.view.tui.drawables.component.TitleComponent;
import it.polimi.ingsw.view.tui.drawables.component.userInputHandler.UserInputHandler;
import it.polimi.ingsw.view.tui.drawables.component.userInputHandler.menu.MenuHandler;
import it.polimi.ingsw.view.tui.drawables.component.userInputHandler.menu.MenuItem;
import it.polimi.ingsw.view.tui.drawables.component.userInputHandler.menu.commands.EmptyCommand;
import it.polimi.ingsw.view.tui.drawables.component.userInputHandler.menu.commands.UserInputChain;
import it.polimi.ingsw.view.tui.drawer.DrawArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * This class represents the scene for getting games.
 * It implements the Scene and UserInputScene interfaces.
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

    /**
     * The menu handler for handling user input.
     */
    private final MenuHandler menuHandler;

    /**
     * The logger.
     */
    private final Logger logger = LogManager.getLogger(GetGamesScene.class);

    /**
     * Constructs a new GetGamesScene.
     * It initializes the draw area with the title and the list of games.
     * It also initializes the menu handler with the available commands.
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
                new MenuItem("j", "join a game",
                        new UserInputChain(
                                new UserInputHandler("Enter game ID:", input -> input.matches("\\d+")),
                                new UserInputHandler("Enter your nickname:", input -> !input.isEmpty())
                        )
                ),
                new MenuItem("c", "create a new game", new EmptyCommand()),
                new MenuItem("q", "quit the menu", new EmptyCommand())
        );
        this.controller = controller;
    }

    /**
     * This method is used to display the scene.
     * It prints the draw area and the menuHandler.
     * The menuHandler will prompt the user to choose an action.
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
        @SuppressWarnings("unchecked")
        ArrayList<String> inputs = (ArrayList<String>) evt.getNewValue();
        switch (changedProperty) {
            case "q" -> controller.selectScene(ScenesEnum.MAIN_MENU);
            case "l" -> controller.getGames();
            case "j" -> controller.joinGame(Integer.parseInt(inputs.get(0)), inputs.get(1));
            case "c" -> controller.selectScene(ScenesEnum.CREATE_GAME);
            default -> logger.error("Invalid property change event");
        }
    }
}