package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.component.PlayerComponent;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class WaitForPlayersScene implements Displayable {
    DrawArea drawArea;

    public WaitForPlayersScene(ArrayList<String> playersName, int spacing) {
        drawArea = new DrawArea();

        ColorsEnum[] colors = ColorsEnum.values();
        ColorsEnum[] colorsToRemove = {ColorsEnum.BLACK, ColorsEnum.WHITE, ColorsEnum.RESET, ColorsEnum.BRIGHT_BLACK, ColorsEnum.BRIGHT_WHITE};
        colors = Arrays.stream(colors).filter(color -> !Arrays.asList(colorsToRemove).contains(color)).toArray(ColorsEnum[]::new);
        Random random = new Random();

        DrawArea playersDrawArea = new DrawArea();
        for (String playerName : playersName) {
            int colorIndex = random.nextInt(colors.length);
            playersDrawArea.drawAt((playersDrawArea.getWidth() == 0) ? 2 : playersDrawArea.getWidth() + spacing, 0, String.valueOf(new PlayerComponent(playerName)), colors[colorIndex]);
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
