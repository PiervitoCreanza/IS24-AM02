package it.polimi.ingsw.model.card.gameCard.front.goldCard;

import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.model.card.gameCard.BackGameCard;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.gameCard.front.FrontGameCard;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Front Positional Gold Game Card Test")
public class FrontPositionalGoldGameCardTest {

    private FrontPositionalGoldGameCard frontPositionalGoldGameCard;

    @BeforeEach
    public void setUp() {
        // Set up the corners of the game card
        Corner topRight = new Corner(GameItemEnum.NONE);
        Corner topLeft = null; // Represents a non-existing corner
        Corner bottomLeft = new Corner(GameItemEnum.NONE);
        Corner bottomRight = new Corner(GameItemEnum.NONE);

        // Assign points and required items for the game card
        int points = 2; // Testing 1 extra point for each INKWELL card
        GameItemStore neededItems = new GameItemStore(); // Required items: 3 plants, 1 insect
        neededItems.set(GameItemEnum.PLANT, 3);
        neededItems.set(GameItemEnum.INSECT, 1);

        // Initialize the game card with specified corners and items
        // Reference for card setup: https://share.cleanshot.com/FgrdDSk0
        frontPositionalGoldGameCard = new FrontPositionalGoldGameCard(topRight, topLeft, bottomLeft, bottomRight, points, neededItems);
    }

    @Test
    @DisplayName("calculatePoints() test for FrontPositionalGoldGameCard class with all 4 cards present ")
    public void testCalculatePoints() {
        // Testing the scenario where the game card covers all four corners but is not at the origin
        // Creating a mock PlayerBoard
        PlayerBoard playerBoard = mock(PlayerBoard.class);

        // Creating mock cards for each corner
        GameCard gameCard1 = mock(GameCard.class);
        GameCard gameCard2 = mock(GameCard.class);
        GameCard gameCard3 = mock(GameCard.class);
        GameCard gameCard4 = mock(GameCard.class);

        // Simulating the scenario with mock cards at specified coordinates
        // Scenario reference: https://share.cleanshot.com/0lLTPDN0
        when(playerBoard.getGameCard(new Coordinate(0, 6))).thenReturn(Optional.of(gameCard1));
        when(playerBoard.getGameCard(new Coordinate(2, 6))).thenReturn(Optional.of(gameCard2));
        when(playerBoard.getGameCard(new Coordinate(0, 4))).thenReturn(Optional.of(gameCard3));
        when(playerBoard.getGameCard(new Coordinate(2, 4))).thenReturn(Optional.of(gameCard4));

        // Act: Calculate the points for the given coordinate
        Coordinate coordinate = new Coordinate(1, 5);
        int points = frontPositionalGoldGameCard.calculatePoints(coordinate, playerBoard);

        // Assert: Verify the expected points (8 points)
        assertEquals(8, points);
    }


    @Test
    @DisplayName("calculatePoints() test for FrontPositionalGoldGameCard class with 2 cards missing")
    public void testCalculatePointsWithoutOneCard() {
        // Testing the scenario where the game card covers 3 corners but 1 card is missing
        // Creating a mock PlayerBoard
        PlayerBoard playerBoard = mock(PlayerBoard.class);

        // Creating mock cards for each corner
        GameCard gameCard1 = mock(GameCard.class);
        GameCard gameCard2 = mock(GameCard.class);

        // Simulating the scenario with mock cards at specified coordinates
        // Scenario reference: https://share.cleanshot.com/FtGgH19F
        when(playerBoard.getGameCard(new Coordinate(0, 6))).thenReturn(Optional.of(gameCard1));
        when(playerBoard.getGameCard(new Coordinate(0, 4))).thenReturn(Optional.of(gameCard2));
        // Act: Calculate the points for the given coordinate
        Coordinate coordinate = new Coordinate(-1, 5);
        int points = frontPositionalGoldGameCard.calculatePoints(coordinate, playerBoard);

        // Assert: Verify the expected points (4 points)
        assertEquals(4, points);
    }

    @Test
    @DisplayName("isGoldPositional should return true only if it is a PositionalGoldGameCard")
    void isGoldPositionalShouldReturnTrueOnlyIfPositionalGoldGameCard() {
        assertTrue(frontPositionalGoldGameCard.isGoldPositional());
        assertFalse(new FrontGameCard(null, null, null, null, 0).isGoldPositional());
        assertFalse(new BackGameCard(null, null, null, null, new GameItemStore()).isGoldPositional());
        assertFalse(new FrontItemGoldGameCard(null, null, null, null, 0, new GameItemStore(), null).isGoldPositional());
        assertFalse(new FrontGoldGameCard(null, null, null, null, 0, new GameItemStore()).isGoldPositional());
    }
}
