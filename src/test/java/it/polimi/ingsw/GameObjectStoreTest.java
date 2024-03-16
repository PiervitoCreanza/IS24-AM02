package it.polimi.ingsw;

import it.polimi.ingsw.model.GameObject;
import it.polimi.ingsw.model.GameObjectStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class GameObjectStoreTest {
    private GameObjectStore gameObjectStore;

    @BeforeEach
    public void setup() {
        gameObjectStore = new GameObjectStore();
    }

    @Test
    @DisplayName("GameObjectStore initialization with empty map throws exception")
    public void gameObjectStoreInitializationWithEmptyMapThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new GameObjectStore(new HashMap<>()));
    }

    @Test
    @DisplayName("GameObjectStore initialization with partial map throws exception")
    public void gameObjectStoreInitializationWithPartialMapThrowsException() {
        HashMap<GameObject, Integer> partialMap = new HashMap<>();
        partialMap.put(GameObject.values()[0], 0);
        assertThrows(IllegalArgumentException.class, () -> new GameObjectStore(partialMap));
    }

    @Test
    @DisplayName("GameObjectStore initialization with complete map does not throw exception")
    public void gameObjectStoreInitializationWithCompleteMapDoesNotThrowException() {
        HashMap<GameObject, Integer> completeMap = new HashMap<>();
        for (GameObject gameObject : GameObject.values()) {
            completeMap.put(gameObject, 0);
        }
        assertDoesNotThrow(() -> new GameObjectStore(completeMap));
    }

    @Test
    @DisplayName("GameObjectStore initialization sets all values to zero")
    public void gameObjectStoreInitializationSetsAllValuesToZero() {
        for (GameObject gameObject : GameObject.values()) {
            assertEquals(0, gameObjectStore.get(gameObject));
        }
    }
}