package it.polimi.ingsw.tui.view.component.cards.gameCard.corner;

import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.drawer.Drawable;

/**
 * This class represents the bottom left corner of a game card.
 * It implements the Drawable interface, meaning it can be drawn on a DrawArea.
 */
public class BottomLeftCorner implements Drawable {

    /**
     * The draw area of the bottom left corner.
     */
    private final DrawArea drawArea;

    /**
     * Constructor for a BottomLeftCorner.
     * It initializes the drawArea based on the provided corner and color.
     *
     * @param bottomLeftCorner The corner to be drawn.
     * @param color            The color to be used for drawing.
     */
    public BottomLeftCorner(Corner bottomLeftCorner, ColorsEnum color) {
        if (bottomLeftCorner == null) {
            drawArea = new DrawArea("""
                    │
                    │
                    └────
                    """);
            drawArea.setColor(color);
        } else if (bottomLeftCorner.isCovered()) {
            drawArea = new DrawArea("");
        } else {
            drawArea = new DrawArea("""
                    ├───┐
                    │   │
                    └───┴
                    """);
            drawArea.setColor(color);
            drawArea.drawAt(2, 1, bottomLeftCorner.getGameItem().getSymbol(), bottomLeftCorner.getGameItem().getColor());
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
     * Returns the draw area of the drawable object.
     *
     * @return the draw area of the drawable object.
     */
    @Override
    public DrawArea getDrawArea() {
        return drawArea;
    }
}
