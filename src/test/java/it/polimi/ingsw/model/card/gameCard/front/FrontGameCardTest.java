package it.polimi.ingsw.model.card.gameCard.front;

import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;

@DisplayName("Front Game Card Test")
//100% line coverage was already ensured by child classes tests
public class FrontGameCardTest {

    private FrontGameCard frontGameCard;

    @BeforeEach
    public void setUp() {
        Corner topRight = new Corner(GameItemEnum.NONE);
        Corner topLeft = new Corner(GameItemEnum.NONE);
        Corner bottomLeft = new Corner(GameItemEnum.NONE);
        Corner bottomRight = new Corner(GameItemEnum.NONE);
        int points = 5;

        frontGameCard = new FrontGameCard(topRight, topLeft, bottomLeft, bottomRight, points);
    }

    @Test
    @DisplayName("calculatePoints test for FrontGameCard class")
    public void calculatePointsReturnsCorrectValue() {
        PlayerBoard playerBoard = mock(PlayerBoard.class);
        Coordinate coordinate = new Coordinate(1, 1);

        int points = frontGameCard.calculatePoints(coordinate, playerBoard);

        assertEquals(5, points);
    }

    @Test
    @DisplayName("getGameItemStore test for FrontGameCard class")
    public void getGameItemStoreTest() {
        GameItemStore gameItemStore = new GameItemStore();
        // The NONE item is a placeholder for null values, so we don't want to set it to a specific amount.
        // Allowing this would lead to inconsistencies in the equality checks.
        //We need to initialize a new appropriate card for this test, avoiding using the one in @BeforeEach
        Corner topRight = new Corner(GameItemEnum.FUNGI);
        Corner topLeft = new Corner(GameItemEnum.PLANT);
        Corner bottomLeft = new Corner(GameItemEnum.INSECT);
        Corner bottomRight = new Corner(GameItemEnum.ANIMAL);
        int points = 5;
        frontGameCard = new FrontGameCard(topRight, topLeft, bottomLeft, bottomRight, points);
        //initialize the store with the correct values relative to this card
        gameItemStore.set(GameItemEnum.FUNGI, 1);
        gameItemStore.set(GameItemEnum.PLANT, 1);
        gameItemStore.set(GameItemEnum.INSECT, 1);
        gameItemStore.set(GameItemEnum.ANIMAL, 1);
        assertEquals(gameItemStore, frontGameCard.getGameItemStore());
    }

    @Test
    @DisplayName("Equals should return true when comparing cards with same attributes")
    void equalsShouldReturnTrueWhenComparingCardsWithSameAttributes() {
        FrontGameCard card1 = new FrontGameCard(null, null, null, null, 5);
        FrontGameCard card2 = new FrontGameCard(null, null, null, null, 5);
        assertEquals(card1, card2);
    }

    @Test
    @DisplayName("Equals should return false when comparing cards with different points")
    void equalsShouldReturnFalseWhenComparingCardsWithDifferentPoints() {
        FrontGameCard card1 = new FrontGameCard(null, null, null, null, 5);
        FrontGameCard card2 = new FrontGameCard(null, null, null, null, 10);
        assertNotEquals(card1, card2);
    }

}