package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.component.PlayerComponent;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.util.ArrayList;

/**
 * This class represents the scene displayed while waiting for players to join the game.
 * It displays a list of players who have already joined, each with a unique color.
 */
public class WaitForPlayersScene implements Scene {
    /**
     * The DrawArea object where the scene will be drawn.
     */
    private final DrawArea drawArea;

    /**
     * Constructs a new WaitForPlayersScene with the specified list of player names and spacing.
     *
     * @param playersName The names of the players who have already joined
     * @param spacing     The spacing between player names in the display
     */
    public WaitForPlayersScene(ArrayList<String> playersName, int spacing) {
        drawArea = new DrawArea();

        // Array of colors used to display player names
        ColorsEnum[] colors = {ColorsEnum.RED, ColorsEnum.GREEN, ColorsEnum.YELLOW, ColorsEnum.BLUE, ColorsEnum.CYAN, ColorsEnum.BRIGHT_RED, ColorsEnum.BRIGHT_GREEN, ColorsEnum.BRIGHT_YELLOW, ColorsEnum.BRIGHT_BLUE, ColorsEnum.BRIGHT_CYAN};
        int colorIndex = 0;
        DrawArea playersDrawArea = new DrawArea();
        for (String playerName : playersName) {
            playersDrawArea.drawAt((playersDrawArea.getWidth() == 0) ? 2 : playersDrawArea.getWidth() + spacing, 0, String.valueOf(new PlayerComponent(playerName)), colors[colorIndex]);
            colorIndex++;
        }
        DrawArea titleDrawArea = new TitleComponent("Waiting for players", playersDrawArea.getWidth()).getDrawArea();
        drawArea.drawAt(0, 0, titleDrawArea);
        drawArea.drawAt(0, drawArea.getHeight(), playersDrawArea);
    }

    /**
     * This method is used to display the object.
     */
    @Override
    public void display() {
        drawArea.println();
    }

    /**
     * Handles user input. This method is currently empty because there is no user input to handle in this scene.
     *
     * @param input The user's input
     */
    public void handleUserInput(String input) {
    }

    /**
     * This method is used to get the draw area of the object.
     *
     * @return the draw area of the object.
     */
    public DrawArea getDrawArea() {
        return drawArea;
    }
}
