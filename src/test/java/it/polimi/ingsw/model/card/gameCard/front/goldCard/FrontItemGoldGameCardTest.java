package it.polimi.ingsw.model.card.gameCard.front.goldCard;

import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.model.card.gameCard.BackGameCard;
import it.polimi.ingsw.model.card.gameCard.front.FrontGameCard;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@DisplayName("Front Item Gold Game Card Test")
public class FrontItemGoldGameCardTest {

    private FrontItemGoldGameCard frontItemGoldGameCard;

    @BeforeEach
    public void setUp() {
        // Set up corners with different items for the game card
        Corner topRight = new Corner(GameItemEnum.PLANT);
        Corner topLeft = new Corner(GameItemEnum.ANIMAL);
        Corner bottomLeft = new Corner(GameItemEnum.FUNGI);
        Corner bottomRight = new Corner(GameItemEnum.NONE);

        // Set points and required items for the game card
        int points = 2; // Testing 1 extra point for each INKWELL card
        GameItemStore neededItems = new GameItemStore(); // 2 animals and 1 fungi are needed
        neededItems.set(GameItemEnum.ANIMAL, 2);
        neededItems.set(GameItemEnum.FUNGI, 1);
        GameItemEnum multiplier = GameItemEnum.INKWELL;

        // Initialize FrontItemGoldGameCard with the specified configuration
        frontItemGoldGameCard = new FrontItemGoldGameCard(topRight, topLeft, bottomLeft, bottomRight, points, neededItems, multiplier);
    }

    @Test
    @DisplayName("calculatePoints() test for FrontItemGoldGameCard class")
    public void testCalculatePoints() {
        // Arrange: Creating a mock player board and setting game item amounts
        PlayerBoard playerBoardMock = Mockito.mock(PlayerBoard.class);
        // Assume there are 3 INKWELL items on the player board
        when(playerBoardMock.getGameItemAmount(GameItemEnum.INKWELL)).thenReturn(3);
        // The coordinate is needed for method overload; its actual value is irrelevant in this context
        Coordinate coordinate = null;

        // Act: Calculate the points for the given game card configuration
        int points = frontItemGoldGameCard.calculatePoints(coordinate, playerBoardMock);

        // Assert: Verifying the expected points are calculated correctly
        assertEquals(6, points, "The points should be 6.");
    }

    @Test
    @DisplayName("Equals should return true when comparing cards with same attributes")
    void equalsShouldReturnTrueWhenComparingCardsWithSameAttributes() {
        FrontItemGoldGameCard card1 = new FrontItemGoldGameCard(null, null, null, null, 0, null, GameItemEnum.PLANT);
        FrontItemGoldGameCard card2 = new FrontItemGoldGameCard(null, null, null, null, 0, null, GameItemEnum.PLANT);
        assertEquals(card1, card2);
    }

    @Test
    @DisplayName("Equals should return false when comparing cards with different multipliers")
    void equalsShouldReturnFalseWhenComparingCardsWithDifferentMultipliers() {
        FrontItemGoldGameCard card1 = new FrontItemGoldGameCard(null, null, null, null, 0, null, GameItemEnum.PLANT);
        FrontItemGoldGameCard card2 = new FrontItemGoldGameCard(null, null, null, null, 0, null, GameItemEnum.ANIMAL);
        assertNotEquals(card1, card2);
    }

    @Test
    @DisplayName("getMultiplier should return the card multiplier only if it is an ItemGoldGameCard")
    void getMultiplierShouldReturnTheCardMultiplier() {
        assertEquals(GameItemEnum.INKWELL, frontItemGoldGameCard.getMultiplier());
        assertEquals(GameItemEnum.NONE, new FrontGameCard(null, null, null, null, 0).getMultiplier());
        assertEquals(GameItemEnum.NONE, new BackGameCard(null, null, null, null, new GameItemStore()).getMultiplier());
        assertEquals(GameItemEnum.NONE, new FrontPositionalGoldGameCard(null, null, null, null, 0, new GameItemStore()).getMultiplier());
        assertEquals(GameItemEnum.NONE, new FrontGoldGameCard(null, null, null, null, 0, new GameItemStore()).getMultiplier());
    }
}
