package it.polimi.ingsw.tui.view.scene;

import java.io.IOException;

/**
 * This is an interface for objects that can be displayed.
 * It contains a single method, display, which is used to display the object.
 */
public interface Diplayable {
    /**
     * This method is used to display the object.
     */
    void display() throws IOException;

}