package it.polimi.ingsw.view.tui.drawables.component;

import it.polimi.ingsw.view.tui.drawer.DrawArea;
import it.polimi.ingsw.view.tui.drawer.Drawable;
import it.polimi.ingsw.view.tui.utils.ColorsEnum;

/**
 * This class is a component that represents a player.
 */
public class PlayerComponent implements Drawable {

    /**
     * The draw area of the component.
     */
    private final DrawArea drawArea;

    /**
     * Constructor for the PlayerComponent class.
     *
     * @param playerName the name of the player.
     */
    public PlayerComponent(String playerName) {
        drawArea = new DrawArea("""
                ┌──────────────────────────────┐
                │                              │
                │                              │
                │                              │
                └──────────────────────────────┘
                """);
        if (playerName.length() > 26) {
            playerName = playerName.substring(0, 23) + "...";
        }
        drawArea.drawCenteredX(2, playerName);
    }

    /**
     * Constructor for the PlayerComponent class.
     *
     * @param playerName the name of the player.
     * @param color      the color of the player.
     */
    public PlayerComponent(String playerName, ColorsEnum color) {
        this(playerName);
        drawArea.setColor(color);
    }

    /**
     * Constructor for the PlayerComponent class.
     *
     * @param playerName the name of the player.
     * @param point      the point of the player.
     */
    public PlayerComponent(String playerName, int point) {
        this(playerName);
        drawArea.drawCenteredX(3, String.valueOf(point));

    }

    /**
     * Returns the height of the drawable object.
     *
     * @return the height of the drawable object.
     */
    @Override
    public int getHeight() {
        return drawArea.getHeight();
    }

    /**
     * Returns the width of the drawable object.
     *
     * @return the width of the drawable object.
     */
    @Override
    public int getWidth() {
        return drawArea.getHeight();
    }

    /**
     * Returns the draw area of the drawable object.
     *
     * @return the draw area of the drawable object.
     */
    @Override
    public DrawArea getDrawArea() {
        return drawArea;
    }

    /**
     * Returns the string representation of the drawable object.
     *
     * @return the string representation of the drawable object.
     */
    @Override
    public String toString() {
        return drawArea.toString();
    }
}
