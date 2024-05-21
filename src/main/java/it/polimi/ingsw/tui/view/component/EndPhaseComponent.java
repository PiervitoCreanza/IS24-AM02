package it.polimi.ingsw.tui.view.component;

import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.drawer.Drawable;

public class EndPhaseComponent implements Drawable {
    private final DrawArea drawArea;

    public EndPhaseComponent(boolean isLastRound, int remainingRoundsToEndGame) {
        this.drawArea = new DrawArea();
        if (isLastRound) {
            if (remainingRoundsToEndGame == 1) {
                this.drawArea.drawNewLine("""
                        This is your second last round of the game.
                        """, ColorsEnum.YELLOW, 0);
            } else {
                this.drawArea.drawNewLine("""
                        This is your last round of the game.
                        """, ColorsEnum.RED, 0);
            }
        }
    }

    public EndPhaseComponent(String playerName, PlayerColorEnum playerColor, boolean isLastRound, int remainingRoundsToEndGame) {
        this.drawArea = new DrawArea();
        if (isLastRound) {
            if (remainingRoundsToEndGame == 1) {
                this.drawArea.drawAt(0, 0, "It's the second last round of");
                this.drawArea.drawAt(this.drawArea.getWidth() + 1, 0, playerName, playerColor.getColor());
            } else {
                this.drawArea.drawAt(0, 0, "It's the last round of");
                this.drawArea.drawAt(this.drawArea.getWidth() + 1, 0, playerName, playerColor.getColor());
            }
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
