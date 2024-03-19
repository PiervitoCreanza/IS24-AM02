package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class Store<T> {
    protected final HashMap<T, Integer> store;

    public Store(HashMap<T, Integer> store) {
        this.store = store;
    }

    /**
     * Gets the amount of a specific object in the store.
     *
     * @param t the object to get the amount of
     * @return the amount of the object in the store
     */
    public Integer get(T t) {
        return store.get(t);
    }

    /**
     * Sets the amount of a specific object in the store.
     *
     * @param t      the object to set the amount of
     * @param amount the amount to set
     */
    public void set(T t, Integer amount) {
        store.put(t, amount);
    }

    /**
     * Increment the amount of a specific object in the store.
     *
     * @param t      the object to increase the amount of
     * @param amount the amount to add
     */
    public void increment(T t, Integer amount) {
        store.put(t, store.get(t) + amount);
    }

    /**
     * Decrement the amount of a specific object in the store.
     *
     * @param t      the object to decrease the amount of
     * @param amount the amount to remove
     */
    public void decrement(T t, Integer amount) {
        store.put(t, store.get(t) - amount);
    }

    /**
     * Gets the keys of non-empty amounts in the store.
     *
     * @return the keys of the store
     */
    public ArrayList<T> getNonEmptyKeys() {
        return store.keySet().stream().filter(k -> store.get(k) > 0).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets all keys in the store.
     *
     * @return all keys in the store
     */
    public Set<T> keySet() {
        return store.keySet();
    }
}