package it.polimi.ingsw.model;

import java.util.HashMap;

abstract class Store<T> {
    protected final HashMap<T, Integer> store;

    public Store(HashMap<T, Integer> store) {
        this.store = store;
    }

    /**
     * This method is used to get the amount of a specific object in the store.
     *
     * @param T t This is the object to get the amount of.
     * @return Integer This returns the amount of the object in the store.
     */
    public Integer get(T t) {
        return store.get(t);
    }

    /**
     * This method is used to set the amount of a specific object in the store.
     *
     * @param T       t This is the object to set the amount of.
     * @param Integer amount This is the amount to set.
     */
    public void set(T t, Integer amount) {
        store.put(t, amount);
    }
}
