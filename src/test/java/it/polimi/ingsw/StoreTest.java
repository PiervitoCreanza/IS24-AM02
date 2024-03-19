package it.polimi.ingsw;

import it.polimi.ingsw.model.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StoreTest {
    private Store<String> store;

    @BeforeEach
    public void setup() {
        HashMap<String, Integer> initialStore = new HashMap<>();
        initialStore.put("item1", 5);
        initialStore.put("item2", 0);
        store = new Store<>(initialStore);
    }

    @Test
    @DisplayName("Get returns correct amount")
    public void getReturnsCorrectAmount() {
        assertEquals(5, store.get("item1"));
    }

    @Test
    @DisplayName("Set updates amount correctly")
    public void setUpdatesAmountCorrectly() {
        store.set("item1", 10);
        assertEquals(10, store.get("item1"));
    }

    @Test
    @DisplayName("Increment increases amount correctly")
    public void incrementIncreasesAmountCorrectly() {
        store.increment("item1", 5);
        assertEquals(10, store.get("item1"));
    }

    @Test
    @DisplayName("Decrement decreases amount correctly")
    public void decrementDecreasesAmountCorrectly() {
        store.decrement("item1", 3);
        assertEquals(2, store.get("item1"));
    }

    @Test
    @DisplayName("GetNonEmptyKeys returns keys with non-zero amounts")
    public void getNonEmptyKeysReturnsKeysWithNonZeroAmounts() {
        assertTrue(store.getNonEmptyKeys().contains("item1"));
    }

    @Test
    @DisplayName("KeySet returns all keys")
    public void keySetReturnsAllKeys() {
        assertTrue(store.keySet().contains("item1"));
        assertTrue(store.keySet().contains("item2"));
    }
}