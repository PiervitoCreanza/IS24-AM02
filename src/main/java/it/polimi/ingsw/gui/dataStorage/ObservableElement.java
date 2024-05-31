package it.polimi.ingsw.gui.dataStorage;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ObservableElement<T> implements ObservableDataStorage {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private T element;

    /**
     * Adds a property change listener to this data storage.
     *
     * @param listener the listener to be added
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Removes a property change listener from this data storage.
     *
     * @param listener the listener to be removed
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * Loads data into this data storage.
     *
     * @param data the data to be loaded
     */
    public void loadData(T data) {
        T oldElement = element;
        element = data;
        pcs.firePropertyChange("update", oldElement, element);

    }
}
