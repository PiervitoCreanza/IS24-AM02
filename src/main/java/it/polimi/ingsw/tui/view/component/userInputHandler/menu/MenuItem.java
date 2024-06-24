package it.polimi.ingsw.tui.view.component.userInputHandler.menu;

import it.polimi.ingsw.tui.view.component.userInputHandler.menu.commands.MenuCommand;

import java.util.ArrayList;

/**
 * This class represents a single item in a menu.
 * It contains the option that the user must input to select the item, the label that will be displayed to the user,
 * and the action that will be executed when the user selects the item.
 */
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

    /**
     * Creates a new MenuItem with the provided option, label, and action.
     *
     * @param opt    The option that the user must input to select the item.
     * @param label  The label that will be displayed to the user.
     * @param action The action that will be executed when the user selects the item.
     */
    public MenuItem(String opt, String label, MenuCommand action) {
        this.opt = opt;
        this.label = label;
        this.action = action;
    }

    /**
     * Retrieves the label of the item.
     *
     * @return the label of the item.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Retrieves the option of the item.
     *
     * @return the option of the item.
     */
    public String getOpt() {
        return opt;
    }

    /**
     * Handles the input for the item.
     * This method delegates the handling of the input to the action.
     *
     * @param input The input to be handled.
     */
    public void handleInput(String input) {
        action.handleInput(input);
    }

    /**
     * Checks if the item is not waiting for input.
     * This method delegates the check to the action.
     *
     * @return true if the item is not waiting for input, false otherwise.
     */
    public boolean isNotWaitingInput() {
        return action.isNotWaitingInput();
    }

    /**
     * Retrieves the inputs required by the item.
     * This method delegates the retrieval to the action.
     *
     * @return the inputs required by the item.
     */
    public ArrayList<String> getInputs() {
        return action.getInputs();
    }
}
