package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CornerTest {
    private Corner corner;

    @BeforeEach
    public void setup() {
        corner = new Corner(false, GameItemEnum.PLANT);
    }

    @Test
    public void getGameItemReturnsCorrectItemWhenNotCovered() {
        assertEquals(GameItemEnum.PLANT, corner.getGameItem());
    }

    @Test
    public void getGameItemReturnsNullWhenCovered() {
        corner.setCovered();
        assertEquals(GameItemEnum.NONE, corner.getGameItem());
    }

    @Test
    public void isExistingReturnsTrue() {
        assertTrue(corner.isExisting());
    }
}