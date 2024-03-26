package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * This class contains unit tests for the Back class.
 */

public class BackTest {
    /**
     * This test checks if the Back constructor throws a NullPointerException when a null GameItemStore is passed.
     */
    @Test
    @DisplayName("Test Back constructor")
    public void DontAcceptGameItemStoreNull() {
        assertThrows(NullPointerException.class, () -> new Back(null, mock(Corner.class), mock(Corner.class), mock(Corner.class), mock(Corner.class)));
    }

    /**
     * This test checks if the getGameItemStore() method of the Back class returns the correct GameItemStore when the corners have NONE game item.
     */
    @Test
    @DisplayName("Test getGameItemStore method when corners have NONE game item")
    public void getGameItemStoreWhenCornersHaveNONE() {
        GameItemStore resources = new GameItemStore();
        resources.set(GameItemEnum.PLANT, 1);
        resources.set(GameItemEnum.ANIMAL, 1);
        resources.set(GameItemEnum.FUNGI, 1);
        resources.set(GameItemEnum.NONE, 4);
        Corner cornerNone = new Corner(false, GameItemEnum.NONE);
        Back back = new Back(resources, cornerNone, cornerNone, cornerNone, cornerNone);
        assertEquals(resources, back.getGameItemStore());
    }

    /**
     * This test checks if the getGameItemStore() method of the Back class returns the correct GameItemStore when the corners have some game item.
     */
    @Test
    @DisplayName("Test getGameItemStore method when corners have some game item")
    public void getGameItemStoreWhenCornersHaveSomeGameItem() {
        GameItemStore resources = new GameItemStore();
        resources.set(GameItemEnum.FUNGI, 1);
        Corner topRight = new Corner(false, GameItemEnum.NONE);
        Corner topLeft = new Corner(false, GameItemEnum.ANIMAL);
        Corner bottomLeft = new Corner(false, GameItemEnum.NONE);
        Corner bottomRight = new Corner(false, GameItemEnum.FUNGI);
        Back back = new Back(resources, topRight, topLeft, bottomLeft, bottomRight);
        GameItemStore expected = new GameItemStore();
        expected.set(GameItemEnum.ANIMAL, 1);
        expected.set(GameItemEnum.FUNGI, 2);
        expected.set(GameItemEnum.NONE, 2);
        assertEquals(expected, back.getGameItemStore());
    }
}