package it.polimi.ingsw.utils;

import java.beans.PropertyChangeListener;

/**
 * This interface defines the methods that a class should implement
 * if it needs to notify other objects of changes in its properties.
 * It is designed to work with the Java Beans PropertyChangeListener.
 */
public interface PropertyChangeNotifier {

    /**
     * Adds a PropertyChangeListener to the listener list.
     * The listener will be notified of property changes.
     *
     * @param listener The PropertyChangeListener to be added
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Removes a PropertyChangeListener from the listener list.
     * The listener will no longer be notified of property changes.
     *
     * @param listener The PropertyChangeListener to be removed
     */
    void removePropertyChangeListener(PropertyChangeListener listener);
}
