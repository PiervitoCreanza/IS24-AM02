package it.polimi.ingsw.tui.view.component.userInputHandler.menu;

import it.polimi.ingsw.tui.view.component.userInputHandler.menu.commands.MenuCommand;

import java.util.ArrayList;

public class MenuItem {
    /**
     * The option that the user must input to select this item.
     */
    private final String opt;

    /**
     * The label that will be displayed to the user.
     */
    private final String label;

    /**
     * The action that will be executed when the user selects this item.
     */
    private final MenuCommand action;

    public MenuItem(String opt, String label, MenuCommand action) {
        this.opt = opt;
        this.label = label;
        this.action = action;
    }

    public String getLabel() {
        return label;
    }

    public String getOpt() {
        return opt;
    }

    public void handleInput(String input) {
        action.handleInput(input);
    }

    public boolean isNotWaitingInput() {
        return action.isNotWaitingInput();
    }

    public ArrayList<String> getInputs() {
        return action.getInputs();
    }
}
