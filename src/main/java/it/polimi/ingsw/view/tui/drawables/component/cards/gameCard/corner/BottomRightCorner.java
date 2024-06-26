package it.polimi.ingsw.view.tui.drawables.component.cards.gameCard.corner;

import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.view.tui.drawer.DrawArea;
import it.polimi.ingsw.view.tui.drawer.Drawable;
import it.polimi.ingsw.view.tui.utils.ColorsEnum;

/**
 * This class represents the bottom right corner of a game card.
 * It implements the Drawable interface, meaning it can be drawn on a DrawArea.
 */
public class BottomRightCorner implements Drawable {

    /**
     * The draw area of the bottom right corner.
     */
    private final DrawArea drawArea;

    /**
     * Constructor for a BottomRightCorner.
     * It initializes the drawArea based on the provided corner and color.
     *
     * @param bottomRightCorner The corner to be drawn.
     * @param color             The color to be used for drawing.
     */
    public BottomRightCorner(Corner bottomRightCorner, ColorsEnum color) {
        if (bottomRightCorner == null) {
            drawArea = new DrawArea("""
                        │
                        │
                    ────┘
                    """);
            drawArea.setColor(color);
        } else if (bottomRightCorner.isCovered()) {
            drawArea = new DrawArea("");
        } else {
            drawArea = new DrawArea("""
                    ┌───┤
                    │   │
                    ┴───┘
                    """);
            drawArea.setColor(color);
            drawArea.drawAt(2, 1, bottomRightCorner.getGameItem().getSymbol(), bottomRightCorner.getGameItem().getColor());
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
