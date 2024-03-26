package it.polimi.ingsw.model.utils.store;

import it.polimi.ingsw.model.utils.store.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class StoreTest {
    private Store<String> store;
    private final int item1Amount = 5;
    private final int item2Amount = 0;

    @BeforeEach
    public void setup() {
        HashMap<String, Integer> initialStore = new HashMap<>();
        initialStore.put("item1", item1Amount);
        initialStore.put("item2", item2Amount);
        store = new Store<>(initialStore);
    }

    @Test
    @DisplayName("Get returns correct amount")
    public void getReturnsCorrectAmount() {
        assertEquals(item1Amount, store.get("item1"));
        assertEquals(item2Amount, store.get("item2"));
    }

    @Test
    @DisplayName("Set updates amount correctly")
    public void setUpdatesAmountCorrectly() {
        int newAmount = 10;
        store.set("item1", newAmount);
        assertEquals(newAmount, store.get("item1"));
    }

    @Test
    @DisplayName("Increment increases amount correctly")
    public void incrementIncreasesAmountCorrectly() {
        int amount = 5;
        store.increment("item1", amount);
        assertEquals(item1Amount + amount, store.get("item1"));
    }

    @Test
    @DisplayName("Decrement decreases amount correctly")
    public void decrementDecreasesAmountCorrectly() {
        int amount = 3;
        store.decrement("item1", amount);
        assertEquals(item1Amount - amount, store.get("item1"));
    }

    @Test
    @DisplayName("GetNonEmptyKeys returns keys with non-zero amounts")
    public void getNonEmptyKeysReturnsKeysWithNonZeroAmounts() {
        assertTrue(store.getNonEmptyKeys().contains("item1"));
        assertFalse(store.getNonEmptyKeys().contains("item2"));
    }

    @Test
    @DisplayName("KeySet returns all keys")
    public void keySetReturnsAllKeys() {
        assertTrue(store.keySet().contains("item1"));
        assertTrue(store.keySet().contains("item2"));
    }

    @Test
    @DisplayName("addStore adds correctly")
    public void addStoreAddsCorrectly() {
        HashMap<String, Integer> otherStore = new HashMap<>();
        int otherItem1Amount = 3;
        int otherItem2Amount = 0;
        otherStore.put("item1", otherItem1Amount);
        otherStore.put("item2", otherItem2Amount);
        store.addStore(new Store<>(otherStore));
        assertEquals(item1Amount + otherItem1Amount, store.get("item1"));
        assertEquals(item2Amount + otherItem2Amount, store.get("item2"));
    }

    @Test
    @DisplayName("subtractStore subtracts correctly")
    public void subtractStoreSubtractsCorrectly() {
        HashMap<String, Integer> otherStore = new HashMap<>();
        int otherItem1Amount = 3;
        int otherItem2Amount = 0;
        otherStore.put("item1", otherItem1Amount);
        otherStore.put("item2", otherItem2Amount);
        store.subtractStore(new Store<>(otherStore));
        assertEquals(item1Amount - otherItem1Amount, store.get("item1"));
        assertEquals(item2Amount - otherItem2Amount, store.get("item2"));
    }
}