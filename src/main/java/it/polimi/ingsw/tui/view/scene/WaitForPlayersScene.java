package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.component.PlayerComponent;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.util.ArrayList;

public class WaitForPlayersScene implements Displayable {
    DrawArea drawArea;

    public WaitForPlayersScene(ArrayList<String> playersName, int spacing) {
        drawArea = new DrawArea();

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
