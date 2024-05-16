package it.polimi.ingsw.tui.view.scene;

/**
 * This is an interface for objects that can be displayed.
 * It contains a single method, display, which is used to display the object.
 */
public interface Displayable {
    /**
     * This method is used to display the object.
     */
    void display();

    void handleUserInput(String input);

}