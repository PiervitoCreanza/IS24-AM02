package it.polimi.ingsw.tui.Scene;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.tui.draw.DrawArea;
import it.polimi.ingsw.tui.draw.GameInfoComponent;
import it.polimi.ingsw.tui.utils.ColorsEnum;

import java.util.HashSet;

public class GetGamesScene {
    private final DrawArea drawArea;

    public GetGamesScene(HashSet<GameRecord> games) {
        drawArea = new DrawArea("""
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

    @Override
    public String toString() {
        return drawArea.toString();
    }
}
