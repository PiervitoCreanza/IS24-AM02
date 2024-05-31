package it.polimi.ingsw.gui.dataStorage;

import java.beans.PropertyChangeListener;

/**
 * This interface represents an observable data storage.
 * It provides methods to add and remove property change listeners,
 * and to load data into the storage.
 */
public interface ObservableDataStorage {

    /**
     * Adds a property change listener to this data storage.
     *
     * @param listener the listener to be added
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Removes a property change listener from this data storage.
     *
     * @param listener the listener to be removed
     */
    void removePropertyChangeListener(PropertyChangeListener listener);
}