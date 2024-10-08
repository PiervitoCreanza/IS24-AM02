package it.polimi.ingsw.view.tui.drawables.scene;

import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.view.tui.controller.TUIViewController;
import it.polimi.ingsw.view.tui.drawables.component.PlayerComponent;
import it.polimi.ingsw.view.tui.drawables.component.TitleComponent;
import it.polimi.ingsw.view.tui.drawables.component.userInputHandler.menu.MenuHandler;
import it.polimi.ingsw.view.tui.drawables.component.userInputHandler.menu.MenuItem;
import it.polimi.ingsw.view.tui.drawables.component.userInputHandler.menu.commands.EmptyCommand;
import it.polimi.ingsw.view.tui.drawer.DrawArea;
import it.polimi.ingsw.view.tui.utils.ColorsEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * This class represents the scene displayed while waiting for players to join the game.
 * It implements the Scene and UserInputScene interfaces.
 */
public class WaitForPlayersScene implements Scene, PropertyChangeListener {

    /**
     * The DrawArea object where the scene will be drawn.
     */
    private final DrawArea drawArea;

    /**
     * The controller that manages the user interface and the game logic.
     */
    private final TUIViewController controller;

    /**
     * The menu handler for the scene.
     */
    private final MenuHandler menuHandler;

    /**
     * The logger.
     */
    private final Logger logger = LogManager.getLogger(GetGamesScene.class);

    /**
     * Constructs a new WaitForPlayersScene with the specified list of player names and spacing.
     * The scene will display the player names in the order they are given.
     *
     * @param controller  The controller that manages the user interface and the game logic
     * @param playerNames The names of the players who have already joined
     * @param spacing     The spacing between player names in the display
     */
    public WaitForPlayersScene(TUIViewController controller, ArrayList<String> playerNames, int spacing) {
        this.controller = controller;
        this.drawArea = new DrawArea();
        this.menuHandler = new MenuHandler(this,
                new MenuItem("q", "quit", new EmptyCommand()),
                new MenuItem("c", "chat", new EmptyCommand()));

        // Get the list of player colors
        ArrayList<ColorsEnum> colors = PlayerColorEnum.stream().map(PlayerColorEnum::getColor).collect(Collectors.toCollection(ArrayList::new));

        DrawArea playersDrawArea = new DrawArea();
        int playerIndex = 0;
        for (String playerName : playerNames) {
            int spacingX = spacing;

            // If the first player is being drawn, use a different spacing value
            if (playerIndex == 0) {
                spacingX = 2;
            }

            playersDrawArea.drawAt(playersDrawArea.getWidth() + spacingX, 0, new PlayerComponent(playerName, colors.get(playerIndex)));
            playerIndex++;
        }
        DrawArea titleDrawArea = new TitleComponent("Waiting for players", playersDrawArea.getWidth()).getDrawArea();
        drawArea.drawAt(0, 0, titleDrawArea);
        drawArea.drawAt(0, drawArea.getHeight(), playersDrawArea);
    }

    /**
     * This method is used to display the object.
     * It prints the draw area and the menu handler.
     */
    @Override
    public void display() {
        drawArea.println();
        menuHandler.print();
    }

    /**
     * Handles user input. This method is currently empty because there is no user input to handle in this scene.
     *
     * @param input The user's input
     */
    public void handleUserInput(String input) {
        menuHandler.handleInput(input);
    }

    /**
     * This method is used to get the draw area of the object.
     *
     * @return the draw area of the object.
     */
    public DrawArea getDrawArea() {
        return drawArea;
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
            controller.closeConnection();
        } else if (changedProperty.equals("c")) {
            controller.showChat();
        } else {
            logger.error("Invalid property change event");
        }
    }
}
