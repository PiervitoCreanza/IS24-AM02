package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

public class WaitForPlayersScene implements Displayable {
    DrawArea drawArea;

    public WaitForPlayersScene(GameControllerView gameControllerView) {
        drawArea = new DrawArea("""
                Waiting for players to join...
                """);
        DrawArea line = new DrawArea("Players in the game:");
        line.setColor(ColorsEnum.YELLOW);
        int playersNumber = gameControllerView.gameView().playerViews().size();
        line.drawAt(line.getWidth(), 0, playersNumber);
        drawArea.drawAt(0, drawArea.getHeight(), line);

    }

    /**
     * This method is used to display the object.
     */
    @Override
    public void display() {
        System.out.println(drawArea);
    }
}
