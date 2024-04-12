package it.polimi.ingsw.model.card.corner;

import it.polimi.ingsw.model.card.GameItemEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test checks if the BackGameCard constructor throws a NullPointerException when a null GameItemStore is passed.
 * The test uses mock objects for the Corner parameters.
 */

public class CornerTest {
    private Corner corner;

    @BeforeEach
    public void setup() {
        corner = new Corner(GameItemEnum.PLANT);
    }

    /**
     * This test checks if the getGameItem() method of the Corner class returns the correct game item when the corner is not covered.
     */
    @Test
    @DisplayName("Test the return of game item when not covered")
    public void getGameItemReturnsCorrectItemWhenNotCovered() {
        assertEquals(GameItemEnum.PLANT, corner.getGameItem());
    }

    /**
     * This test checks if the getGameItem() method of the Corner class returns NONE when the corner is covered.
     */
    @Test
    @DisplayName("Test the return of game item when covered")
    public void getGameItemReturnsNoneWhenCovered() {
        corner.setCovered();
        assertEquals(GameItemEnum.NONE, corner.getGameItem());
    }

    @Test
    @DisplayName("isCovered() test for Corner class - false and true cases")
    public void testIsCovered1() {
        // Create a Corner instance with isCovered set to false
        Corner corner = new Corner(GameItemEnum.NONE);
        assertFalse(corner.isCovered());

        /* Cover the corner, check the returned ENUM value */
        assertEquals(GameItemEnum.NONE, corner.setCovered());
        /* Check if the cover has been effectively covered */
        assertTrue(corner.isCovered());
    }

    @Test
    @DisplayName("Equals should return true when comparing corners with same attributes")
    void equalsShouldReturnTrueWhenComparingCornersWithSameAttributes() {
        Corner corner1 = new Corner(true, GameItemEnum.NONE);
        Corner corner2 = new Corner(true, GameItemEnum.NONE);
        assertTrue(corner1.equals(corner2));
    }

    @Test
    @DisplayName("Equals should return false when comparing corners with different covered status")
    void equalsShouldReturnFalseWhenComparingCornersWithDifferentCoveredStatus() {
        Corner corner1 = new Corner(true, GameItemEnum.NONE);
        Corner corner2 = new Corner(false, GameItemEnum.NONE);
        assertFalse(corner1.equals(corner2));
    }

    @Test
    @DisplayName("Equals should return false when comparing corners with different game items")
    void equalsShouldReturnFalseWhenComparingCornersWithDifferentGameItems() {
        Corner corner1 = new Corner(true, GameItemEnum.NONE);
        Corner corner2 = new Corner(true, GameItemEnum.PLANT);
        assertFalse(corner1.equals(corner2));
    }
}

