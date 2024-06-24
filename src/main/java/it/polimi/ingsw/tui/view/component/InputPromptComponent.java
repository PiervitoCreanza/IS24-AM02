package it.polimi.ingsw.tui.view.component;

import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.drawer.Drawable;

/**
 * This class represents an input prompt component in the user interface.
 * It implements the Drawable interface, meaning it can be drawn to the console.
 */
public class InputPromptComponent implements Drawable {

    /**
     * The draw area of the input prompt component.
     */
    private final DrawArea drawArea;

    /**
     * Constructs a new InputPromptComponent.
     * It initializes the draw area and draws the question at the specified coordinates with the specified color.
     *
     * @param question The question to be displayed in the input prompt.
     */
    public InputPromptComponent(String question) {
        drawArea = new DrawArea();
        drawArea.drawAt(0, 0, question, ColorsEnum.CYAN);
    }

    /**
     * Prints the input prompt to the console.
     */
    public void print() {
        drawArea.print();
    }

    /**
     * Prints the input prompt to the console and moves the cursor to the next line.
     */
    public void println() {
        drawArea.println();
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
