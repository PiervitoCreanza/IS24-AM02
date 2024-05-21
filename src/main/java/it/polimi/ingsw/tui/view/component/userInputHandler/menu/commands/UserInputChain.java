package it.polimi.ingsw.tui.view.component.userInputHandler.menu.commands;

import it.polimi.ingsw.tui.view.component.userInputHandler.UserInputHandler;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class UserInputChain implements MenuCommand {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final UserInputHandler[] handlers;
    private int currentHandlerIndex;

    public UserInputChain(UserInputHandler... handlers) {
        this.handlers = handlers;
        this.currentHandlerIndex = 0;
    }

    public UserInputChain(PropertyChangeListener listener, UserInputHandler... handlers) {
        this.handlers = handlers;
        this.currentHandlerIndex = 0;
        this.support.addPropertyChangeListener(listener);
    }

    private UserInputHandler getCurrentHandler() {
        if (currentHandlerIndex >= handlers.length)
            // Clear the current handler index
            this.currentHandlerIndex = 0;
        return handlers[currentHandlerIndex];
    }

    public void handleInput(String input) {

        // If the input is "q", notify the listener
        if ("q".equalsIgnoreCase(input)) {
            support.firePropertyChange("q", null, null);
            return;
        }

        if (getCurrentHandler().validate(input)) {
            // If the current handler has been set move to the next handler
            currentHandlerIndex++;
        }

        // If the current handler is the last one, notify the listener
        if (currentHandlerIndex >= handlers.length) {
            support.firePropertyChange("input", null, getInputs());
            return;
        }

        // If the current handler is not the last one, print the next handler prompt
        getCurrentHandler().print();
    }

    public void print() {
        getCurrentHandler().print();
    }

    public boolean isNotWaitingInput() {
        return currentHandlerIndex >= handlers.length;
    }

    @Override
    public ArrayList<String> getInputs() {
        ArrayList<String> inputs = new ArrayList<>();
        for (UserInputHandler handler : handlers) {
            inputs.add(handler.getInput());
        }
        return inputs;
    }
}
