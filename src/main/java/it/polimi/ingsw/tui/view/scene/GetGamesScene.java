package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.component.GameInfoComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.util.ArrayList;

public class GetGamesScene implements Diplayable {
    private final DrawArea drawArea;

    public GetGamesScene(ArrayList<GameRecord> games) {
        this.drawArea = new DrawArea("""
                -- Games list --
                """);

        int i = 0;
        for (GameRecord game : games) {
            drawArea.drawAt(0, drawArea.getHeight(), new GameInfoComponent(game, i++).getDrawArea());
        }
        drawArea.drawAt(0, drawArea.getHeight(), "-".repeat(drawArea.getWidth()));
        drawArea.drawAt(0, drawArea.getHeight(), """
                Press <r> to refresh the list.
                Press <j> to join a game.
                Press <c> to create a new game.
                Press <q> to quit.
                """, ColorsEnum.YELLOW);

    }

    /**
     * Displays the game scene on the console.
     */
    @Override
    public void display() {
        System.out.println(drawArea);
    }
}
