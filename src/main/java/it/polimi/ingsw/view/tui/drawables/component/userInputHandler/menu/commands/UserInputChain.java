package it.polimi.ingsw.view.tui.drawables.component.userInputHandler.menu.commands;

import it.polimi.ingsw.view.tui.drawables.component.userInputHandler.UserInputHandler;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * This class represents a chain of UserInputHandler objects.
 * It implements the MenuCommand interface, meaning it can be executed as a command in a menu.
 */
public class UserInputChain implements MenuCommand {

    /**
     * The PropertyChangeSupport object used to notify listeners of changes in the UserInputChain.
     */
    private final PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    /**
     * The array of UserInputHandler objects in the chain.
     */
    private final UserInputHandler[] handlers;

    /**
     * The index of the current handler in the chain.
     */
    private int currentHandlerIndex;

    /**
     * Constructor for the UserInputChain class.
     * It initializes the chain with the given UserInputHandler objects.
     *
     * @param handlers The UserInputHandler objects in the chain.
     */
    public UserInputChain(UserInputHandler... handlers) {
        this.handlers = handlers;
        this.currentHandlerIndex = 0;
    }

    /**
     * Constructor for the UserInputChain class.
     * It initializes the chain with the given UserInputHandler objects and adds the given listener.
     *
     * @param listener The PropertyChangeListener to be added to the chain.
     * @param handlers The UserInputHandler objects in the chain.
     */
    public UserInputChain(PropertyChangeListener listener, UserInputHandler... handlers) {
        this.handlers = handlers;
        this.currentHandlerIndex = 0;
        this.listeners.addPropertyChangeListener(listener);
    }

    /**
     * Retrieves the current UserInputHandler in the chain.
     *
     * @return the current UserInputHandler in the chain.
     */
    private UserInputHandler getCurrentHandler() {
        if (currentHandlerIndex >= handlers.length)
            // Clear the current handler index
            this.currentHandlerIndex = 0;
        return handlers[currentHandlerIndex];
    }

    /**
     * Handles the input for the chain.
     * This method validates the input and moves to the next handler if the input is valid.
     * If the input is "q", it notifies the listener.
     * If the current handler is the last one, it notifies the listener with the inputs.
     *
     * @param input The input to be handled.
     */
    @Override
    public void handleInput(String input) {

        // If the input is "q", notify the listener
        if ("q".equalsIgnoreCase(input)) {
            listeners.firePropertyChange("q", null, null);
            return;
        }

        if (getCurrentHandler().validate(input)) {
            // If the current handler has been set move to the next handler
            currentHandlerIndex++;
        }

        // If the current handler is the last one, notify the listener
        if (currentHandlerIndex >= handlers.length) {
            listeners.firePropertyChange("input", null, getInputs());
            return;
        }

        // If the current handler is not the last one, print the next handler prompt
        getCurrentHandler().print();
    }

    /**
     * Prints the prompt of the current UserInputHandler in the chain.
     */
    public void print() {
        getCurrentHandler().print();
    }

    /**
     * Checks if the chain is not waiting for input.
     *
     * @return true if the chain is not waiting for input, false otherwise.
     */
    public boolean isNotWaitingInput() {
        return currentHandlerIndex >= handlers.length;
    }

    /**
     * Retrieves the inputs required by the chain.
     *
     * @return the inputs required by the chain.
     */
    @Override
    public ArrayList<String> getInputs() {
        ArrayList<String> inputs = new ArrayList<>();
        for (UserInputHandler handler : handlers) {
            inputs.add(handler.getInput());
        }
        return inputs;
    }
}
