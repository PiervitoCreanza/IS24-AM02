package it.polimi.ingsw.tui.view.component.userInputHandler.menu;

import it.polimi.ingsw.tui.utils.Utils;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;

public class MenuHandler {
    private final HashMap<String, MenuItem> items;

    private MenuItem selectedMenuItem;

    private final PropertyChangeSupport support;

    public MenuHandler(PropertyChangeListener listener, MenuItem... items) {
        this.support = new PropertyChangeSupport(this);
        this.support.addPropertyChangeListener(listener);

        this.items = new HashMap<>();

        // Add all items to the map. The key is the option that the user must input to select the item.
        for (MenuItem item : items) {
            this.items.put(item.getOpt(), item);
        }
    }

    public void print() {
        for (MenuItem item : items.values()) {
            System.out.println("Press <" + item.getOpt() + "> to " + item.getLabel());
        }
    }

    public DrawArea getDrawArea() {
        DrawArea drawArea = new DrawArea();
        for (MenuItem item : items.values()) {
            drawArea.drawNewLine("Press <" + item.getOpt() + "> to " + item.getLabel(), 0);
        }
        return drawArea;
    }

    public void handleInput(String input) {
        boolean isFirstExecution = false;
        // If there is no selected item, execute the item that corresponds to the input.
        if (selectedMenuItem == null) {

            if ("q".equalsIgnoreCase(input)) {
                support.firePropertyChange("q", null, null);
                return;
            }
            selectedMenuItem = items.get(input);

            // If the input is not valid, print the menu again and return.
            if (selectedMenuItem == null) {
                System.out.println(Utils.ANSI_RED + "Invalid input" + Utils.ANSI_RESET);
                print();
                return;
            }

            isFirstExecution = true;
        } else {
            // if there is a selected item and the input is "q", reset the selected item.
            if ("q".equalsIgnoreCase(input)) {
                // Reset the selected item.
                selectedMenuItem = null;
                return;
            }
        }

        // Execute the selected item.
        if (isFirstExecution) {
            // If this is the first execution, the input is the option that the user selected.
            // As a result, the input is null for the menuItem.
            selectedMenuItem.handleInput(null);
        } else {
            selectedMenuItem.handleInput(input);
        }

        // If the selected item has finished executing.
        if (selectedMenuItem.isNotWaitingInput()) {
            // Notify the scene that the selected item has finished executing.
            support.firePropertyChange(selectedMenuItem.getOpt(), null, selectedMenuItem.getInputs());
            // Reset the selected item.
            selectedMenuItem = null;
        }
    }
}
