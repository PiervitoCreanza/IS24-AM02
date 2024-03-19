package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NonExistingCornerTest {
    private NonExistingCorner nonExistingCorner;

    @BeforeEach
    public void setup() {
        nonExistingCorner = new NonExistingCorner(GameItemEnum.PLANT);
    }

    @Test
    public void getGameItemReturnsNullRegardlessOfCoveredStatus() {
        assertEquals(GameItemEnum.NONE, nonExistingCorner.getGameItem());
        nonExistingCorner.setCovered();
        assertEquals(GameItemEnum.NONE, nonExistingCorner.getGameItem());
    }

    @Test
    public void isExistingReturnsFalse() {
        assertFalse(nonExistingCorner.isExisting());
    }
}