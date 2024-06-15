package it.polimi.ingsw.gui.dataStorage;

import java.util.LinkedList;

public class ObservableQueue<E> extends LinkedList<E> {
    private QueueChangeListener<E> listener;

    public ObservableQueue() {
    }

    @Override
    public boolean add(E e) {
        boolean added = super.add(e);
        if (added && listener != null) {
            listener.elementAdded(e);
        }
        return added;
    }

    public void setListener(QueueChangeListener<E> listener) {
        this.listener = listener;
    }

    public interface QueueChangeListener<E> {
        void elementAdded(E element);
    }
}