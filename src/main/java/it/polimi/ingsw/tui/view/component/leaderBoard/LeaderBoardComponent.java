package it.polimi.ingsw.tui.view.component.leaderBoard;

import it.polimi.ingsw.network.virtualView.PlayerView;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.drawer.Drawable;

import java.util.List;

/**
 * This class represents a leaderboard component in the game.
 * It implements the Drawable interface, meaning it can be drawn on the screen.
 */
public class LeaderBoardComponent implements Drawable {

    /**
     * The draw area of the leaderboard component.
     */
    private final DrawArea drawArea;

    /**
     * Constructor for the LeaderBoardComponent class.
     * It initializes the drawArea and populates it with player information.
     *
     * @param playerViews      A list of PlayerView objects representing the players.
     * @param clientPlayerName The name of the client player.
     */
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
            String playerName = playerView.playerName();
            if (playerName.equals(clientPlayerName))
                playerName = "You";
            if (playerName.length() > 13)
                playerName = playerName.substring(0, 10) + "...";
            singlePlayerDrawArea.drawAt(0, 0, playerName);
            singlePlayerDrawArea.drawAt(15, 0, "=");
            singlePlayerDrawArea.drawCenteredX(17, 20, 0, new DrawArea(String.valueOf(playerView.playerPos())));
            if (playerName.equals("You"))
                singlePlayerDrawArea.setColor(ColorsEnum.BLUE);
            if (!playerView.isConnected())
                singlePlayerDrawArea.setColor(ColorsEnum.RED);
            drawArea.drawAt(x, y++, singlePlayerDrawArea);
        }
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
        return drawArea.getWidth();
    }

    /**
     * Returns a string representation of the drawable object.
     *
     * @return a string representation of the drawable object.
     */
    @Override
    public String toString() {
        return drawArea.toString();
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
}
