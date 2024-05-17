package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

public class GamePausedScene implements Displayable {
    DrawArea drawArea;

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
