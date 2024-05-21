package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.component.PlayerComponent;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
     * @param playerNames The names of the players who have already joined
     * @param spacing     The spacing between player names in the display
     */
    public WaitForPlayersScene(ArrayList<String> playerNames, int spacing) {
        drawArea = new DrawArea();

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
