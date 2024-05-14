package it.polimi.ingsw.tui.view.component;

import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.drawer.Drawable;

/**
 * This class is a component that represents a title.
 */
public class TitleComponent implements Drawable {
    /**
     * The draw area of the drawable object.
     */
    private final DrawArea drawArea;

    public TitleComponent(String title) {
        drawArea = new DrawArea("""
                ┌───────────────────────────────────────────────────────┐
                │                                                       │
                └───────────────────────────────────────────────────────┘
                """);
        drawArea.drawCenteredX(1, title);
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
