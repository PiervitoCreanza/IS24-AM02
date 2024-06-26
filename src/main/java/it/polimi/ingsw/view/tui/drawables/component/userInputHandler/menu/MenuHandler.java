package it.polimi.ingsw.view.tui.drawables.component.userInputHandler.menu;

import it.polimi.ingsw.view.tui.drawer.DrawArea;
import it.polimi.ingsw.view.tui.utils.Utils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;

/**
 * This class is responsible for handling the menu items.
 * It prints the menu items and executes the selected item.
 */
public class MenuHandler {

    /**
     * This is the map that contains the items that the menu contains.
     * The key is the option that the user must input to select the item.
     */
    private final HashMap<String, MenuItem> itemsMap;

    /**
     * This is the items that the menu contains.
     */
    private final MenuItem[] items;

    /**
     * This is the selected item that the user has selected.
     */
    private MenuItem selectedMenuItem;

    /**
     * This property change support is used to notify the listener that the selected item has finished executing.
     */
    private final PropertyChangeSupport listeners;

    /**
     * This constructor is used to create a new MenuHandler.
     *
     * @param listener The listener that listens for the selected item.
     * @param itemsMap The items that the menu contains.
     */
    public MenuHandler(PropertyChangeListener listener, MenuItem... itemsMap) {
        this.listeners = new PropertyChangeSupport(this);
        this.listeners.addPropertyChangeListener(listener);

        this.itemsMap = new HashMap<>();

        // Add all items to the map. The key is the option that the user must input to select the item.
        for (MenuItem item : itemsMap) {
            this.itemsMap.put(item.getOpt(), item);
        }

        // Save the items in an array. This is required to preserve the order.
        this.items = itemsMap;
    }

    /**
     * This method is used to print the menu items.
     */
    public void print() {
        // We use the array as a source to preserve the order.
        for (MenuItem item : items) {
            System.out.println("Press <" + item.getOpt() + "> to " + item.getLabel());
        }

        // If the print has been called, the selected item must be reset.
        selectedMenuItem = null;
    }

    /**
     * This method is used to get the draw area of the menu.
     * It is used to draw the menu in the TUI.
     *
     * @return The draw area of the menu.
     */
    public DrawArea getDrawArea() {
        DrawArea drawArea = new DrawArea();
        // We use the array as a source to preserve the order.
        for (MenuItem item : items) {
            drawArea.drawNewLine("Press <" + item.getOpt() + "> to " + item.getLabel(), 0);
        }
        // If the print has been called, the selected item must be reset.
        selectedMenuItem = null;
        return drawArea;
    }

    /**
     * This method is called when the user inputs a string.
     * It handles the input and executes the selected item.
     *
     * @param input The input that the user has provided.
     */
    public void handleInput(String input) {
        boolean isFirstExecution = false;
        // If there is no selected item, execute the item that corresponds to the input.
        if (selectedMenuItem == null) {

            if ("q".equalsIgnoreCase(input)) {
                listeners.firePropertyChange("q", null, null);
                return;
            }
            selectedMenuItem = itemsMap.get(input.toLowerCase());

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

                // Print the menu again.
                print();
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
            listeners.firePropertyChange(selectedMenuItem.getOpt(), null, selectedMenuItem.getInputs());
            // Reset the selected item.
            selectedMenuItem = null;
        }
    }
}
