package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

/**
 * The DrawArea object where the scene will be drawn.
 */
public class GamePausedScene implements Scene {
    /**
     * The DrawArea object where the scene will be drawn.
     */
    DrawArea drawArea;

    /**
     * Constructs a new GamePausedScene.
     * It initializes the draw area and draws the title "Game Paused" at the top of the scene.
     */
    public GamePausedScene() {
        this.drawArea = new DrawArea();
        this.drawArea.drawAt(0, 0, new TitleComponent("Game Paused").getDrawArea());
    }

    /**
     * This method is used to display the object.
     */
    @Override
    public void display() {
        this.drawArea.println();
    }

    /**
     * @param input
     */
    @Override
    public void handleUserInput(String input) {

    }
}
