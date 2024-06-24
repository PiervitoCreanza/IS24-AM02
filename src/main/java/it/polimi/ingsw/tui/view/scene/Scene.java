package it.polimi.ingsw.tui.view.scene;

/**
 * This is an interface for Scenes that can be displayed.
 */
public interface Scene {
    /**
     * This method is used to display the object.
     */
    void display();

    /**
     * This method is used to handle user input in a Scene.
     *
     * @param input The user input.
     */
    void handleUserInput(String input);

}