package it.polimi.ingsw.tui.view.component;

import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.drawer.Drawable;

public class InputRequestComponent implements Drawable {

    private final DrawArea drawArea;

    public InputRequestComponent(String question) {
        drawArea = new DrawArea();
        drawArea.drawAt(6, 0, question, ColorsEnum.CYAN);
    }

    public InputRequestComponent showError(String error) {
        DrawArea errorArea = new DrawArea();
        errorArea.drawCenteredX(0, error);
        errorArea.setColor(ColorsEnum.RED);
        drawArea.drawAt(0, 0, errorArea);
        return this;
    }

    public InputRequestComponent hideError() {
        drawArea.clearLine(0);
        return this;
    }

    public void print() {
        drawArea.print();
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
