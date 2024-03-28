package it.polimi.ingsw.model.card.corner;

import it.polimi.ingsw.model.card.GameItemEnum;
import org.junit.jupiter.api.Assertions;
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
    public void testIsCovered() {
        // Create a Corner instance with isCovered set to false
        Corner corner = new Corner(GameItemEnum.NONE);
        Assertions.assertFalse(corner.isCovered());

        // Cover the corner
        corner.setCovered();
        Assertions.assertTrue(corner.isCovered());
    }
}

