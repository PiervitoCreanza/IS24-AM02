package it.polimi.ingsw.view.tui.drawables.component;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.view.tui.drawer.DrawArea;
import it.polimi.ingsw.view.tui.drawer.Drawable;
import it.polimi.ingsw.view.tui.utils.ColorsEnum;

/**
 * This class is a component that represents a game info.
 */
public class GameInfoComponent implements Drawable {

    /**
     * The draw area of the component.
     */
    private final DrawArea drawArea;

    /**
     * Constructor for the GameInfoComponent class.
     *
     * @param game the game record.
     * @param n    the number of the game.
     */
    public GameInfoComponent(GameRecord game, int n) {
        drawArea = new DrawArea("""
                ┌───────────────────────────────────────────────────────┐
                │                                                       │
                └───────────────────────────────────────────────────────┘
                """);
        DrawArea line = new DrawArea("#" + n);
        line.setColor(ColorsEnum.RED);
        int spacingX = 3;

        line.drawAt(line.getWidth() + spacingX, 0, game.isFull() ? "Full" : "Open", game.isFull() ? ColorsEnum.RED : ColorsEnum.GREEN);
        line.drawAt(line.getWidth() + spacingX, 0, "Players:", ColorsEnum.YELLOW);
        line.drawAt(line.getWidth() + 1, 0, game.joinedPlayers());
        line.drawAt(line.getWidth(), 0, "/" + game.maxAllowedPlayers());
        // Truncate game name if it's too long
        String gameName = game.gameName();
        if (gameName.length() > 20) {
            gameName = gameName.substring(0, 17) + "...";
        }
        line.drawAt(line.getWidth() + spacingX, 0, "Game: ", ColorsEnum.YELLOW);
        line.drawAt(line.getWidth() + 1, 0, gameName);
        drawArea.drawAt(2, 1, line);
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
