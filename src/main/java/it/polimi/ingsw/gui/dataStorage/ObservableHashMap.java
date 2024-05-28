package it.polimi.ingsw.gui.dataStorage;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;

public class ObservableHashMap<K, V> extends HashMap<K, V> {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    @Override
    public V put(K key, V value) {
        V oldValue = super.put(key, value);

        // If the value was changed, fire a property change event.
        if (oldValue == null || !oldValue.equals(value)) {
            pcs.firePropertyChange("put", null, key);
        }

        return oldValue;
    }

    public V putWithoutNotify(K key, V value) {
        V oldValue = super.put(key, value);
        return oldValue;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
