package it.polimi.ingsw;

import it.polimi.ingsw.model.GameObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.model.Store;
import it.polimi.ingsw.model.GameObjectStore;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class StoreTest {
    private GameObjectStore store;
    private final GameObject item1 = GameObject.values()[0];
    private final GameObject item2 = GameObject.values()[1];
    private final GameObject item3 = GameObject.values()[2];

    @BeforeEach
    public void setup() {
        HashMap<GameObject, Integer> initialStore = new HashMap<>();

        initialStore.put(item1, 5);
        initialStore.put(item2, 0);
        initialStore.put(item3, 0);

        store = new GameObjectStore(initialStore);
    }

    @Test
    @DisplayName("Get returns correct amount for existing item")
    public void getReturnsCorrectAmountForExistingItem() {
        assertEquals(5, store.get(item1));
    }

    @Test
    @DisplayName("Set updates amount for existing item")
    public void setUpdatesAmountForExistingItem() {
        store.set(item1, 10);
        assertEquals(10, store.get(item1));
    }

    @Test
    @DisplayName("Increment increases amount for existing item")
    public void addIncreasesAmountForExistingItem() {
        store.increment(item1, 5);
        assertEquals(10, store.get(item1));
    }

    @Test
    @DisplayName("Decrement decreases amount for existing item")
    public void removeDecreasesAmountForExistingItem() {
        store.decrement(item1, 3);
        assertEquals(2, store.get(item1));
    }

    @Test
    @DisplayName("GetNonEmptyKeys returns keys for items with non-zero amount")
    public void getNonEmptyKeysReturnsKeysForItemsWithNonZeroAmount() {
        assertTrue(store.getNonEmptyKeys().contains(item1));
        assertFalse(store.getNonEmptyKeys().contains(item2));
        assertFalse(store.getNonEmptyKeys().contains(item3));
    }

    @Test
    @DisplayName("KeySet returns all keys")
    public void keySetReturnsAllKeys() {
        assertTrue(store.keySet().contains(item1));
        assertTrue(store.keySet().contains(item2));
        assertTrue(store.keySet().contains(item3));
    }
}