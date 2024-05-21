package it.polimi.ingsw.tui.view.component.userInputHandler.menu;

import it.polimi.ingsw.tui.view.component.userInputHandler.UserInputHandler;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.commands.MenuCommand;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.commands.UserInputChain;

import java.util.ArrayList;

public class UserInputChainCommand implements MenuCommand {
    private final UserInputChain userInputChain;

    public UserInputChainCommand(UserInputHandler... handlers) {
        this.userInputChain = new UserInputChain(handlers);
    }

    @Override
    public void handleInput(String input) {
        userInputChain.handleInput(input);
    }

    @Override
    public boolean isNotWaitingInput() {
        return userInputChain.isNotWaitingInput();
    }

    /**
     * @return
     */
    @Override
    public ArrayList<String> getInputs() {
        return userInputChain.getInputs();
    }
}
