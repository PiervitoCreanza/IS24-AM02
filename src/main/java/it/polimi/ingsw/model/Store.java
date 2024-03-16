package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

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

    /**
     * This method is used to add a specific amount of a specific object to the store.
     *
     * @param T       t This is the object to add the amount of.
     * @param Integer amount This is the amount to add.
     */
    public void add(T t, Integer amount) {
        store.put(t, store.get(t) + amount);
    }

    /**
     * This method is used to remove a specific amount of a specific object from the store.
     *
     * @param T       t This is the object to remove the amount of.
     * @param Integer amount This is the amount to remove.
     */
    public void remove(T t, Integer amount) {
        store.put(t, store.get(t) - amount);
    }

    /**
     * This method is used to get the keys of non-empty amounts in the store.
     *
     * @return ArrayList<T> This returns the keys of the store.
     */
    public ArrayList<T> getNonEmptyKeys() {
        return store.keySet().stream().filter(k -> store.get(k) > 0).collect(Collectors.toCollection(ArrayList::new));
    }
}
