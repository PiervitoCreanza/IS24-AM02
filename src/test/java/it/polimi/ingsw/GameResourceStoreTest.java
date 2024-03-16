package it.polimi.ingsw;

import it.polimi.ingsw.model.GameResource;
import it.polimi.ingsw.model.GameResourceStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class GameResourceStoreTest {
    private GameResourceStore gameResourceStore;

    @BeforeEach
    public void setup() {
        gameResourceStore = new GameResourceStore();
    }

    @Test
    @DisplayName("GameResourceStore initialization with empty map throws exception")
    public void gameResourceStoreInitializationWithEmptyMapThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new GameResourceStore(new HashMap<>()));
    }

    @Test
    @DisplayName("GameResourceStore initialization with partial map throws exception")
    public void gameResourceStoreInitializationWithPartialMapThrowsException() {
        HashMap<GameResource, Integer> partialMap = new HashMap<>();
        partialMap.put(GameResource.values()[0], 0);
        assertThrows(IllegalArgumentException.class, () -> new GameResourceStore(partialMap));
    }

    @Test
    @DisplayName("GameResourceStore initialization with complete map does not throw exception")
    public void gameResourceStoreInitializationWithCompleteMapDoesNotThrowException() {
        HashMap<GameResource, Integer> completeMap = new HashMap<>();
        for (GameResource gameResource : GameResource.values()) {
            completeMap.put(gameResource, 0);
        }
        assertDoesNotThrow(() -> new GameResourceStore(completeMap));
    }

    @Test
    @DisplayName("GameResourceStore initialization sets all values to zero")
    public void gameResourceStoreInitializationSetsAllValuesToZero() {
        for (GameResource gameResource : GameResource.values()) {
            assertEquals(0, gameResourceStore.get(gameResource));
        }
    }
}