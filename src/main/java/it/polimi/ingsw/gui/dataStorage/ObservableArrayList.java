package it.polimi.ingsw.gui.dataStorage;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class ObservableArrayList<T> extends ArrayList<T> {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    @Override
    public boolean add(T t) {
        boolean result = super.add(t);
        pcs.firePropertyChange("set", size() - 1, t);
        return result;
    }

    @Override
    public boolean remove(Object o) {
        int index = super.indexOf(o);
        boolean result = super.remove(o);
        if (result)
            pcs.firePropertyChange("remove", index, o);
        return result;
    }

    @Override
    public T set(int index, T element) {
        if (element.equals(super.get(index))) {
            return null;
        }
        T result = super.set(index, element);
        pcs.firePropertyChange("set", index, element);
        return result;
    }

    public void loadData(ArrayList<T> data) {
        int i = 0;
        for (T t : data) {
            if (i < size()) {
                set(i, t);
            } else {
                add(t);
            }
            i++;
        }
        // Remove the remaining elements
        if (i < size()) {
            int decrescentIndex = size() - 1;
            while (i < size()) {
                remove(super.get(decrescentIndex));
                decrescentIndex--;
            }
        }

    }

    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
