package it.polimi.ingsw.tui.view.component.leaderBoard;

import it.polimi.ingsw.network.virtualView.PlayerView;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.drawer.Drawable;

import java.util.List;

public class LeaderBoardComponent implements Drawable {

    private final DrawArea drawArea;

    public LeaderBoardComponent(List<PlayerView> playerViews, String clientPlayerName) {
        drawArea = new DrawArea("""
                ┌─────────────────────────┐
                │  Leaderboard:           │
                │                         │
                │                         │
                │                         │
                │                         │
                └─────────────────────────┘
                """);
        int x = 4;
        int y = 2;
        for (PlayerView playerView : playerViews) {
            DrawArea singlePlayerDrawArea = new DrawArea();
            singlePlayerDrawArea.drawAt(0, 0, playerView.playerName().equals(clientPlayerName) ? "You" : playerView.playerName());
            singlePlayerDrawArea.drawAt(15, 0, "=");
            singlePlayerDrawArea.drawCenteredX(17, 19, 0, new DrawArea(String.valueOf(playerView.playerPos())));
            if (playerView.playerName().equals(clientPlayerName))
                singlePlayerDrawArea.setColor(ColorsEnum.BLUE);
            if (!playerView.isConnected())
                singlePlayerDrawArea.setColor(ColorsEnum.RED);
            drawArea.drawAt(x, y++, singlePlayerDrawArea);
        }
    }

    @Override
    public int getHeight() {
        return drawArea.getHeight();
    }

    @Override
    public int getWidth() {
        return drawArea.getWidth();
    }

    @Override
    public String toString() {
        return drawArea.toString();
    }

    @Override
    public DrawArea getDrawArea() {
        return drawArea;
    }
}
