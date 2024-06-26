package it.polimi.ingsw.view.tui.drawables.component.userInputHandler.menu.commands;

import java.util.ArrayList;

/**
 * This is the MenuCommand interface.
 * It is part of the user input handling system in the TUI view.
 * Any class that implements this interface can be executed as a command in a menu.
 */
public interface MenuCommand {

    /**
     * Executes the command.
     * This method is called when the command is selected in a menu.
     * The specific behavior of the command is determined by the implementing class.
     *
     * @param input The input to the command
     */
    void handleInput(String input);

    /**
     * Checks if the command is not waiting for input.
     * This method should return true if the command does not require any additional input to be executed.
     * If the command requires additional input, this method should return false.
     *
     * @return true if the command is not waiting for input, false otherwise.
     */
    boolean isNotWaitingInput();

    /**
     * Retrieves the inputs for the command.
     * This method should return the list of inputs that the command has collected.
     * If the command does not require any inputs, this method should return an empty list.
     *
     * @return the list of inputs collected by the command.
     */
    ArrayList<String> getInputs();
}