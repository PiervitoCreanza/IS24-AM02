package it.polimi.ingsw.model.card.gameCard.front.goldCard;

import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public void shouldReturnNeededItemsWhenGetNeededItemsIsCalled() {
        GameItemStore actualNeededItems = frontGoldGameCard.getNeededItems();
        assertEquals(neededItems, actualNeededItems, "The returned needed items should be equal to the needed items set in the setup");
    }

    @Test
    public void shouldReturnNeededItemsWhenGetNeededItemStoreIsCalled() {
        GameItemStore actualNeededItems = frontGoldGameCard.getNeededItemStore();
        assertEquals(neededItems, actualNeededItems, "The returned needed items should be equal to the needed items set in the setup");
    }
}