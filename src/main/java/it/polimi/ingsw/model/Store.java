package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class that represents a store, which contains objects and their amounts.
 *
 * @param <T> the type of the objects in the store
 */
public class Store<T> {
    /**
     * The store that contains the objects and their amounts.
     */
    protected final HashMap<T, Integer> store;

    /**
     * Constructor for Store. Initializes the store with the specified HashMap.
     *
     * @param store the HashMap to initialize the store with
     */
    public Store(HashMap<T, Integer> store) {
        this.store = Objects.requireNonNull(store, "store cannot be null");
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
     * Adds the amounts of the objects in another store to this store.
     *
     * @param other the store to add
     */
    public void addStore(Store<T> other) {
        Objects.requireNonNull(other, "store cannot be null");
        store.keySet().forEach(key -> increment(key, other.get(key)));
    }

    /**
     * Subtracts the amounts of the objects in another store from this store.
     *
     * @param other the store to subtract
     */
    public void subtractStore(Store<T> other) {
        Objects.requireNonNull(other, "store cannot be null");
        store.keySet().forEach(key -> decrement(key, other.get(key)));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Store<?> store1 = (Store<?>) o;

        return store.equals(store1.store);
    }

    @Override
    public int hashCode() {
        return store.hashCode();
    }
}