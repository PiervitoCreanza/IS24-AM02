package it.polimi.ingsw.tui.draw;

/**
 * Drawable is an interface that represents a drawable object.
 * A drawable object is an object that can be converted to string and printed to terminal.
 */
public interface Drawable {

    /**
     * Returns the height of the drawable object.
     *
     * @return the height of the drawable object.
     */
    int getHeight();

    /**
     * Returns the width of the drawable object.
     *
     * @return the width of the drawable object.
     */
    int getWidth();

    /**
     * Returns a string representation of the drawable object.
     *
     * @return a string representation of the drawable object.
     */
    @Override
    String toString();

    /**
     * Returns the draw area of the drawable object.
     *
     * @return the draw area of the drawable object.
     */
    DrawArea getDrawArea();
}