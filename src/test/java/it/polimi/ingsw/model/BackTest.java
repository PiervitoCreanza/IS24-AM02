package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BackTest {
    private Back back;
    private GameItemStore gameItemStore;

    @BeforeEach
    public void setup() {
        gameItemStore = mock(GameItemStore.class);
        back = new Back(gameItemStore);
    }

    @Test
    public void getGameItemStoreReturnsCorrectStore() {
        assertEquals(gameItemStore, back.getGameItemStore());
    }
}