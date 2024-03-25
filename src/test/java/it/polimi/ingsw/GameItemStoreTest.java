package it.polimi.ingsw;

import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameItemStoreTest {
    private GameItemStore gameItemStore;

    @BeforeEach
    public void setup() {
        gameItemStore = new GameItemStore();
    }

    @Test
    @DisplayName("All game items are initialized to zero")
    public void allGameItemsAreInitializedToZero() {
        for (GameItemEnum gameItem : GameItemEnum.values()) {
            assertEquals(0, gameItemStore.get(gameItem));
        }
    }

    @Test
    @DisplayName("Constructor throws exception when map size does not match game item count")
    public void constructorThrowsExceptionWhenMapSizeDoesNotMatchGameItemCount() {
        HashMap<GameItemEnum, Integer> gameItemMap = new HashMap<>();
        gameItemMap.put(GameItemEnum.NONE, 1);
        assertThrows(IllegalArgumentException.class, () -> new GameItemStore(gameItemMap));
    }

    @Test
    @DisplayName("Constructor correctly initializes store with provided map")
    public void constructorCorrectlyInitializesStoreWithProvidedMap() {
        HashMap<GameItemEnum, Integer> gameItemMap = new HashMap<>();
        for (GameItemEnum gameItem : GameItemEnum.values()) {
            gameItemMap.put(gameItem, 1);
        }
        GameItemStore gameItemStore = new GameItemStore(gameItemMap);
        for (GameItemEnum gameItem : GameItemEnum.values()) {
            assertEquals(1, gameItemStore.get(gameItem));
        }
    }
}