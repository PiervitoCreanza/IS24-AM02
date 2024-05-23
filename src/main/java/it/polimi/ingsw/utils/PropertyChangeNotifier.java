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

    /**
     * Notifies all listeners about the change of a property.
     * The PropertyChangeListeners firePropertyChange methods will be called.
     *
     * @param propertyName The name of the property that was changed
     * @param message      The new value of the property
     */
    void notify(String propertyName, Object message);

    /**
     * Notifies all listeners about the change of a property.
     * The PropertyChangeListeners firePropertyChange methods will be called.
     *
     * @param propertyName The name of the property that was changed
     * @param oldMessage   The old value of the property
     * @param message      The new value of the property
     */
    void notify(String propertyName, Object oldMessage, Object message);
}
