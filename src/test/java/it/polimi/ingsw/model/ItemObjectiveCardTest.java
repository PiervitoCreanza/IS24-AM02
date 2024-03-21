package it.polimi.ingsw.model;

import it.polimi.ingsw.model.ObjectiveCard.ItemObjectiveCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This class contains unit tests for the ItemObjectiveCard class.
 */
public class ItemObjectiveCardTest {
    /**
     * This test checks if the ItemObjectiveCard constructor throws an IllegalArgumentException when a negative point value or a null GameItemStore is passed.
     */
    @Test
    @DisplayName("Test ItemObjectiveCardTest constructor")
    public void TryExceptionInConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new ItemObjectiveCard(-2, mock(GameItemStore.class)));
        assertThrows(IllegalArgumentException.class, () -> new ItemObjectiveCard(10, null));
    }

    /**
     * This test checks if the getPoints() method of the ItemObjectiveCard class returns the correct points when multiple is based on Resources.
     */
    @Test
    @DisplayName("Test ItemObjectiveCardTest getPoints with Resources")
    public void TryGetPointsWithResources() {
        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.increment(GameItemEnum.FUNGI, 3);
        ItemObjectiveCard itemObjectiveCard = new ItemObjectiveCard(2, gameItemStore);
        PlayerBoard playerBoard = mock(PlayerBoard.class);
        when(playerBoard.getGameItemAmount(GameItemEnum.FUNGI)).thenReturn(7);
        when(playerBoard.getGameItemAmount(GameItemEnum.ANIMAL)).thenReturn(34);
        assertEquals(4, itemObjectiveCard.getPoints(playerBoard));
    }

    /**
     * This test checks if the getPoints() method of the ItemObjectiveCard class returns the correct points when multiple is based on one Object.
     */
    @Test
    @DisplayName("Test ItemObjectiveCardTest getPoints with Object")
    public void TryGetPointsWithObject() {
        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.increment(GameItemEnum.INKWELL, 2);
        ItemObjectiveCard itemObjectiveCard = new ItemObjectiveCard(2, gameItemStore);
        PlayerBoard playerBoard = mock(PlayerBoard.class);
        when(playerBoard.getGameItemAmount(GameItemEnum.INKWELL)).thenReturn(7);
        when(playerBoard.getGameItemAmount(GameItemEnum.ANIMAL)).thenReturn(34);
        assertEquals(6, itemObjectiveCard.getPoints(playerBoard));
    }

    /**
     * This test checks if the getPoints() method of the ItemObjectiveCard class returns the correct points when multiple is based on all Object.
     */
    @Test
    @DisplayName("Test ItemObjectiveCardTest getPoints with AllObject")
    public void TryGetPointsWithAllObject() {
        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.increment(GameItemEnum.INKWELL, 1);
        gameItemStore.increment(GameItemEnum.MANUSCRIPT, 1);
        gameItemStore.increment(GameItemEnum.QUILL, 1);
        ItemObjectiveCard itemObjectiveCard = new ItemObjectiveCard(3, gameItemStore);
        PlayerBoard playerBoard = mock(PlayerBoard.class);
        when(playerBoard.getGameItemAmount(GameItemEnum.INKWELL)).thenReturn(4);
        when(playerBoard.getGameItemAmount(GameItemEnum.MANUSCRIPT)).thenReturn(3);
        when(playerBoard.getGameItemAmount(GameItemEnum.QUILL)).thenReturn(6);
        when(playerBoard.getGameItemAmount(GameItemEnum.ANIMAL)).thenReturn(34);
        assertEquals(9, itemObjectiveCard.getPoints(playerBoard));
    }
}
