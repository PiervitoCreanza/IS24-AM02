package it.polimi.ingsw.view.tui.drawables.component.userInputHandler.menu.commands;

import java.util.ArrayList;

/**
 * This class represents an EmptyCommand in the menu.
 * It implements the MenuCommand interface, but does not perform any action when executed.
 */
public class EmptyCommand implements MenuCommand {

    /**
     * Handles the input for the command.
     * As this is an EmptyCommand, this method does not perform any action.
     *
     * @param input The input to be handled.
     */
    @Override
    public void handleInput(String input) {
    }

    /**
     * Checks if the command is not waiting for input.
     * This method returns true as the EmptyCommand does not require any additional input to be executed.
     *
     * @return true as the command is not waiting for input.
     */
    @Override
    public boolean isNotWaitingInput() {
        return true;
    }

    /**
     * Retrieves the inputs for the command.
     * As this is an EmptyCommand, this method returns an empty list as it does not require any inputs.
     *
     * @return an empty list as the command does not require any inputs.
     */
    @Override
    public ArrayList<String> getInputs() {
        return new ArrayList<>();
    }
}