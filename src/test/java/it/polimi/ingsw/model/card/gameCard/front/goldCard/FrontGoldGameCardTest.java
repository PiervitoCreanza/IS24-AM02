package it.polimi.ingsw.model.card.gameCard.front.goldCard;

import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FrontGoldGameCardTest {

    private FrontGoldGameCard frontGoldGameCard;
    private GameItemStore neededItems;

    @BeforeEach
    public void setUp() {
        Corner topRight = new Corner(GameItemEnum.PLANT);
        Corner topLeft = new Corner(GameItemEnum.ANIMAL);
        Corner bottomLeft = new Corner(GameItemEnum.FUNGI);
        Corner bottomRight = new Corner(GameItemEnum.NONE);
        int points = 2;
        neededItems = new GameItemStore();
        neededItems.set(GameItemEnum.ANIMAL, 2);
        neededItems.set(GameItemEnum.FUNGI, 1);
        frontGoldGameCard = new FrontGoldGameCard(topRight, topLeft, bottomLeft, bottomRight, points, neededItems);
    }

    @Test
    @DisplayName("getNeededItemStore() test for FrontGoldGameCard class")
    public void shouldReturnNeededItemsWhenGetNeededItemStoreIsCalled() {
        GameItemStore actualNeededItems = frontGoldGameCard.getNeededItemStore();
        assertEquals(neededItems, actualNeededItems, "The returned needed items should be equal to the needed items set in the setup");
    }


    @Test
    @DisplayName("Equals should return true when comparing cards with same attributes")
    void equalsShouldReturnTrueWhenComparingCardsWithSameAttributes() {
        FrontGoldGameCard card1 = new FrontGoldGameCard(null, null, null, null, 0, null);
        FrontGoldGameCard card2 = new FrontGoldGameCard(null, null, null, null, 0, null);
        assertEquals(card1, card2);
    }

    @Test
    @DisplayName("Equals should return false when comparing cards with different needed items")
    void equalsShouldReturnFalseWhenComparingCardsWithDifferentNeededItems() {
        GameItemStore store1 = new GameItemStore();
        GameItemStore store2 = new GameItemStore();
        store2.set(GameItemEnum.ANIMAL, 1);
        FrontGoldGameCard card1 = new FrontGoldGameCard(null, null, null, null, 0, store1);
        FrontGoldGameCard card2 = new FrontGoldGameCard(null, null, null, null, 0, store2);
        assertNotEquals(card1, card2);
    }
}