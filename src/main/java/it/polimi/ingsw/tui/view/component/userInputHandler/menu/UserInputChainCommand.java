package it.polimi.ingsw.tui.view.component.userInputHandler.menu;

import it.polimi.ingsw.tui.view.component.userInputHandler.UserInputHandler;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.commands.MenuCommand;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.commands.UserInputChain;

import java.util.ArrayList;

/**
 * This class represents a command that is part of a chain of commands.
 * It implements the MenuCommand interface and delegates the handling of the input to a UserInputChain.
 */
public class UserInputChainCommand implements MenuCommand {

    /**
     * The UserInputChain that will handle the input for this command.
     */
    private final UserInputChain userInputChain;

    /**
     * Creates a new UserInputChainCommand with the provided handlers.
     *
     * @param handlers The handlers that will be part of the chain.
     */
    public UserInputChainCommand(UserInputHandler... handlers) {
        this.userInputChain = new UserInputChain(handlers);
    }

    /**
     * Handles the input for the command.
     * This method delegates the handling of the input to the UserInputChain.
     *
     * @param input The input to be handled.
     */
    @Override
    public void handleInput(String input) {
        userInputChain.handleInput(input);
    }

    /**
     * Checks if the command is not waiting for input.
     * This method delegates the check to the UserInputChain.
     *
     * @return true if the command is not waiting for input, false otherwise.
     */
    @Override
    public boolean isNotWaitingInput() {
        return userInputChain.isNotWaitingInput();
    }

    /**
     * Retrieves the inputs for the command.
     * This method delegates the retrieval to the UserInputChain.
     *
     * @return the inputs required by the command.
     */
    @Override
    public ArrayList<String> getInputs() {
        return userInputChain.getInputs();
    }
}
