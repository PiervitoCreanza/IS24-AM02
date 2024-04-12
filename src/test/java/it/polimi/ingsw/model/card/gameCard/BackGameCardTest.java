package it.polimi.ingsw.model.card.gameCard;

import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * This class contains unit tests for the BackGameCard class.
 */

public class BackGameCardTest {
    /**
     * This test checks if the BackGameCard constructor throws a NullPointerException when a null GameItemStore is passed.
     */
    @Test
    @DisplayName("Test BackGameCard constructor")
    public void DontAcceptGameItemStoreNull() {
        assertThrows(NullPointerException.class, () -> new BackGameCard(mock(Corner.class), mock(Corner.class), mock(Corner.class), mock(Corner.class), null));
    }

    /**
     * This test checks if the getGameItemStore() method of the BackGameCard class returns the correct GameItemStore when the corners have NONE game item.
     */
    @Test
    @DisplayName("Test getGameItemStore method when corners have NONE game item")
    public void getGameItemStoreWhenCornersHaveNONE() {
        GameItemStore resources = new GameItemStore();
        resources.set(GameItemEnum.PLANT, 1);
        resources.set(GameItemEnum.ANIMAL, 1);
        resources.set(GameItemEnum.FUNGI, 1);
        resources.set(GameItemEnum.NONE, 4);
        Corner cornerNone = new Corner(GameItemEnum.NONE);
        BackGameCard backGameCard = new BackGameCard(cornerNone, cornerNone, cornerNone, cornerNone, resources);
        assertEquals(resources, backGameCard.getGameItemStore());
    }

    /**
     * This test checks if the getGameItemStore() method of the BackGameCard class returns the correct GameItemStore when the corners have some game item.
     */
    @Test
    @DisplayName("Test getGameItemStore method when corners have some game item")
    public void getGameItemStoreWhenCornersHaveSomeGameItem() {
        GameItemStore resources = new GameItemStore();
        resources.set(GameItemEnum.FUNGI, 1);
        Corner topRight = new Corner(GameItemEnum.NONE);
        Corner topLeft = new Corner(GameItemEnum.ANIMAL);
        Corner bottomLeft = new Corner(GameItemEnum.NONE);
        Corner bottomRight = new Corner(GameItemEnum.FUNGI);
        BackGameCard backGameCard = new BackGameCard(topRight, topLeft, bottomLeft, bottomRight, resources);
        GameItemStore expected = new GameItemStore();
        expected.set(GameItemEnum.ANIMAL, 1);
        expected.set(GameItemEnum.FUNGI, 2);
        assertEquals(expected, backGameCard.getGameItemStore());
    }

    @Test
    @DisplayName("Equals method returns true when comparing identical BackGameCards")
    public void equalsReturnsTrueForIdenticalCards() {
        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.FUNGI, 3);
        Corner corner = new Corner(GameItemEnum.PLANT);
        BackGameCard backGameCard1 = new BackGameCard(corner, corner, corner, corner, gameItemStore);
        BackGameCard backGameCard2 = new BackGameCard(corner, corner, corner, corner, gameItemStore);
        assertTrue(backGameCard1.equals(backGameCard2));
    }

    @Test
    @DisplayName("Equals method returns false when comparing different BackGameCards")
    public void equalsReturnsFalseForDifferentCards() {
        GameItemStore gameItemStore1 = new GameItemStore();
        gameItemStore1.set(GameItemEnum.FUNGI, 3);
        Corner corner = new Corner(GameItemEnum.PLANT);
        BackGameCard backGameCard1 = new BackGameCard(corner, corner, corner, corner, gameItemStore1);

        GameItemStore gameItemStore2 = new GameItemStore();
        gameItemStore2.set(GameItemEnum.ANIMAL, 2);
        BackGameCard backGameCard2 = new BackGameCard(corner, corner, corner, corner, gameItemStore2);

        assertFalse(backGameCard1.equals(backGameCard2));
    }
}