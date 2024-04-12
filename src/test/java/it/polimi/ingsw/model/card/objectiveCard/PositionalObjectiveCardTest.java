package it.polimi.ingsw.model.card.objectiveCard;

import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This class contains unit tests for the PositionalObjectiveCard class.
 */

public class PositionalObjectiveCardTest {
    /**
     * This test checks if the PositionalObjectiveCard constructor throws an IllegalArgumentException when a negative point value or a null ArrayList of PositionalData is passed.
     */
    @Test
    @DisplayName("Test PositionalObjectiveCardTest constructor")
    public void TryExceptionInConstructor() {
        ArrayList<PositionalData> positionalData = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> new PositionalObjectiveCard(1, -2, positionalData));
        assertThrows(NullPointerException.class, () -> new PositionalObjectiveCard(1, 10, null));
    }

    /**
     * This test checks if the getPoints() method of the PositionalObjectiveCard class returns the correct points when the pattern is an L shape.
     */
    @Test
    @DisplayName("Testing L Shape PositionalObjectiveCard")
    public void TryGetPointsWithLShape() {
        ArrayList<PositionalData> positionalData = new ArrayList<>();
        positionalData.add(new PositionalData(new Coordinate(0, 0), CardColorEnum.RED));
        positionalData.add(new PositionalData(new Coordinate(0, -2), CardColorEnum.RED));
        positionalData.add(new PositionalData(new Coordinate(1, -3), CardColorEnum.GREEN));
        PositionalObjectiveCard positionalObjectiveCard = new PositionalObjectiveCard(1, 3, positionalData);
        PlayerBoard playerBoard = mock(PlayerBoard.class);
        ArrayList<GameCard> gameCards = new ArrayList<>();
        GameCard gameCard1 = mock(GameCard.class);
        GameCard gameCard2 = mock(GameCard.class);
        GameCard gameCard3 = mock(GameCard.class);
        GameCard gameCard4 = mock(GameCard.class);
        GameCard gameCard5 = mock(GameCard.class);
        GameCard gameCard6 = mock(GameCard.class);
        GameCard gameCard7 = mock(GameCard.class);
        GameCard gameCard8 = mock(GameCard.class);
        GameCard gameCard9 = mock(GameCard.class);
        GameCard gameCard10 = mock(GameCard.class);
        GameCard gameCard11 = mock(GameCard.class);
        GameCard gameCard12 = mock(GameCard.class);
        GameCard gameCard13 = mock(GameCard.class);
        gameCards.add(gameCard1);
        gameCards.add(gameCard2);
        gameCards.add(gameCard3);
        gameCards.add(gameCard4);
        gameCards.add(gameCard5);
        gameCards.add(gameCard6);
        gameCards.add(gameCard7);
        gameCards.add(gameCard8);
        gameCards.add(gameCard9);
        gameCards.add(gameCard10);
        gameCards.add(gameCard11);
        gameCards.add(gameCard12);
        gameCards.add(gameCard13);
        when(gameCard1.getCardColor()).thenReturn(CardColorEnum.NONE);
        when(gameCard2.getCardColor()).thenReturn(CardColorEnum.RED);
        when(gameCard3.getCardColor()).thenReturn(CardColorEnum.RED);
        when(gameCard4.getCardColor()).thenReturn(CardColorEnum.RED);
        when(gameCard5.getCardColor()).thenReturn(CardColorEnum.GREEN);
        when(gameCard6.getCardColor()).thenReturn(CardColorEnum.BLUE);
        when(gameCard7.getCardColor()).thenReturn(CardColorEnum.PURPLE);
        when(gameCard8.getCardColor()).thenReturn(CardColorEnum.RED);
        when(gameCard9.getCardColor()).thenReturn(CardColorEnum.RED);
        when(gameCard10.getCardColor()).thenReturn(CardColorEnum.RED);
        when(gameCard11.getCardColor()).thenReturn(CardColorEnum.GREEN);
        when(gameCard12.getCardColor()).thenReturn(CardColorEnum.GREEN);
        when(gameCard13.getCardColor()).thenReturn(CardColorEnum.BLUE);
        when(playerBoard.getGameCards()).thenReturn(gameCards);
        when(playerBoard.getGameCardPosition(gameCard1)).thenReturn(Optional.of(new Coordinate(0, 0)));
        when(playerBoard.getGameCardPosition(gameCard2)).thenReturn(Optional.of(new Coordinate(-1, 1)));
        when(playerBoard.getGameCardPosition(gameCard3)).thenReturn(Optional.of(new Coordinate(-1, 3)));
        when(playerBoard.getGameCardPosition(gameCard4)).thenReturn(Optional.of(new Coordinate(-1, 5)));
        when(playerBoard.getGameCardPosition(gameCard5)).thenReturn(Optional.of(new Coordinate(0, 2)));
        when(playerBoard.getGameCardPosition(gameCard6)).thenReturn(Optional.of(new Coordinate(0, 4)));
        when(playerBoard.getGameCardPosition(gameCard7)).thenReturn(Optional.of(new Coordinate(1, 1)));
        when(playerBoard.getGameCardPosition(gameCard8)).thenReturn(Optional.of(new Coordinate(1, 3)));
        when(playerBoard.getGameCardPosition(gameCard9)).thenReturn(Optional.of(new Coordinate(1, 5)));
        when(playerBoard.getGameCardPosition(gameCard10)).thenReturn(Optional.of(new Coordinate(1, 7)));
        when(playerBoard.getGameCardPosition(gameCard11)).thenReturn(Optional.of(new Coordinate(2, 2)));
        when(playerBoard.getGameCardPosition(gameCard12)).thenReturn(Optional.of(new Coordinate(2, 4)));
        when(playerBoard.getGameCardPosition(gameCard13)).thenReturn(Optional.of(new Coordinate(2, 6)));
        when(playerBoard.getGameCard(new Coordinate(0, 0))).thenReturn(Optional.of(gameCard1));
        when(playerBoard.getGameCard(new Coordinate(-1, 1))).thenReturn(Optional.of(gameCard2));
        when(playerBoard.getGameCard(new Coordinate(-1, 3))).thenReturn(Optional.of(gameCard3));
        when(playerBoard.getGameCard(new Coordinate(-1, 5))).thenReturn(Optional.of(gameCard4));
        when(playerBoard.getGameCard(new Coordinate(0, 2))).thenReturn(Optional.of(gameCard5));
        when(playerBoard.getGameCard(new Coordinate(0, 4))).thenReturn(Optional.of(gameCard6));
        when(playerBoard.getGameCard(new Coordinate(1, 1))).thenReturn(Optional.of(gameCard7));
        when(playerBoard.getGameCard(new Coordinate(1, 3))).thenReturn(Optional.of(gameCard8));
        when(playerBoard.getGameCard(new Coordinate(1, 5))).thenReturn(Optional.of(gameCard9));
        when(playerBoard.getGameCard(new Coordinate(1, 7))).thenReturn(Optional.of(gameCard10));
        when(playerBoard.getGameCard(new Coordinate(2, 2))).thenReturn(Optional.of(gameCard11));
        when(playerBoard.getGameCard(new Coordinate(2, 4))).thenReturn(Optional.of(gameCard12));
        when(playerBoard.getGameCard(new Coordinate(2, 6))).thenReturn(Optional.of(gameCard13));
        assertEquals(6, positionalObjectiveCard.getPoints(playerBoard));
    }

    /**
     * This test checks if the getPoints() method of the PositionalObjectiveCard class returns the correct points when the pattern is stairs shape.
     */
    @Test
    @DisplayName("Testing Stairs Shape PositionalObjectiveCard")
    public void TryGetPointsWithStairsShape() {
        ArrayList<PositionalData> positionalData = new ArrayList<>();
        positionalData.add(new PositionalData(new Coordinate(0, 0), CardColorEnum.GREEN));
        positionalData.add(new PositionalData(new Coordinate(1, -1), CardColorEnum.GREEN));
        positionalData.add(new PositionalData(new Coordinate(2, -2), CardColorEnum.GREEN));
        PositionalObjectiveCard positionalObjectiveCard = new PositionalObjectiveCard(1, 2, positionalData);
        PlayerBoard playerBoard = mock(PlayerBoard.class);
        ArrayList<GameCard> gameCards = new ArrayList<>();
        GameCard gameCard1 = mock(GameCard.class);
        GameCard gameCard2 = mock(GameCard.class);
        GameCard gameCard3 = mock(GameCard.class);
        GameCard gameCard4 = mock(GameCard.class);
        GameCard gameCard5 = mock(GameCard.class);
        GameCard gameCard6 = mock(GameCard.class);
        GameCard gameCard7 = mock(GameCard.class);
        GameCard gameCard8 = mock(GameCard.class);
        GameCard gameCard9 = mock(GameCard.class);
        GameCard gameCard10 = mock(GameCard.class);
        GameCard gameCard11 = mock(GameCard.class);
        GameCard gameCard12 = mock(GameCard.class);
        GameCard gameCard13 = mock(GameCard.class);
        GameCard gameCard14 = mock(GameCard.class);
        gameCards.add(gameCard1);
        gameCards.add(gameCard2);
        gameCards.add(gameCard3);
        gameCards.add(gameCard4);
        gameCards.add(gameCard5);
        gameCards.add(gameCard6);
        gameCards.add(gameCard7);
        gameCards.add(gameCard8);
        gameCards.add(gameCard9);
        gameCards.add(gameCard10);
        gameCards.add(gameCard11);
        gameCards.add(gameCard12);
        gameCards.add(gameCard13);
        gameCards.add(gameCard14);
        when(gameCard1.getCardColor()).thenReturn(CardColorEnum.NONE);
        when(gameCard2.getCardColor()).thenReturn(CardColorEnum.RED);
        when(gameCard3.getCardColor()).thenReturn(CardColorEnum.RED);
        when(gameCard4.getCardColor()).thenReturn(CardColorEnum.GREEN);
        when(gameCard5.getCardColor()).thenReturn(CardColorEnum.GREEN);
        when(gameCard6.getCardColor()).thenReturn(CardColorEnum.GREEN);
        when(gameCard7.getCardColor()).thenReturn(CardColorEnum.GREEN);
        when(gameCard8.getCardColor()).thenReturn(CardColorEnum.PURPLE);
        when(gameCard9.getCardColor()).thenReturn(CardColorEnum.GREEN);
        when(gameCard10.getCardColor()).thenReturn(CardColorEnum.GREEN);
        when(gameCard11.getCardColor()).thenReturn(CardColorEnum.RED);
        when(gameCard12.getCardColor()).thenReturn(CardColorEnum.GREEN);
        when(gameCard13.getCardColor()).thenReturn(CardColorEnum.GREEN);
        when(gameCard14.getCardColor()).thenReturn(CardColorEnum.BLUE);
        when(playerBoard.getGameCards()).thenReturn(gameCards);
        when(playerBoard.getGameCardPosition(gameCard1)).thenReturn(Optional.of(new Coordinate(0, 0)));
        when(playerBoard.getGameCardPosition(gameCard2)).thenReturn(Optional.of(new Coordinate(-1, 1)));
        when(playerBoard.getGameCardPosition(gameCard3)).thenReturn(Optional.of(new Coordinate(-1, 3)));
        when(playerBoard.getGameCardPosition(gameCard4)).thenReturn(Optional.of(new Coordinate(-1, 5)));
        when(playerBoard.getGameCardPosition(gameCard5)).thenReturn(Optional.of(new Coordinate(0, 2)));
        when(playerBoard.getGameCardPosition(gameCard6)).thenReturn(Optional.of(new Coordinate(0, 4)));
        when(playerBoard.getGameCardPosition(gameCard7)).thenReturn(Optional.of(new Coordinate(0, 6)));
        when(playerBoard.getGameCardPosition(gameCard8)).thenReturn(Optional.of(new Coordinate(1, 1)));
        when(playerBoard.getGameCardPosition(gameCard9)).thenReturn(Optional.of(new Coordinate(1, 3)));
        when(playerBoard.getGameCardPosition(gameCard10)).thenReturn(Optional.of(new Coordinate(1, 5)));
        when(playerBoard.getGameCardPosition(gameCard11)).thenReturn(Optional.of(new Coordinate(1, 7)));
        when(playerBoard.getGameCardPosition(gameCard12)).thenReturn(Optional.of(new Coordinate(2, 2)));
        when(playerBoard.getGameCardPosition(gameCard13)).thenReturn(Optional.of(new Coordinate(2, 4)));
        when(playerBoard.getGameCardPosition(gameCard14)).thenReturn(Optional.of(new Coordinate(2, 6)));
        when(playerBoard.getGameCard(new Coordinate(0, 0))).thenReturn(Optional.of(gameCard1));
        when(playerBoard.getGameCard(new Coordinate(-1, 1))).thenReturn(Optional.of(gameCard2));
        when(playerBoard.getGameCard(new Coordinate(-1, 3))).thenReturn(Optional.of(gameCard3));
        when(playerBoard.getGameCard(new Coordinate(-1, 5))).thenReturn(Optional.of(gameCard4));
        when(playerBoard.getGameCard(new Coordinate(0, 2))).thenReturn(Optional.of(gameCard5));
        when(playerBoard.getGameCard(new Coordinate(0, 4))).thenReturn(Optional.of(gameCard6));
        when(playerBoard.getGameCard(new Coordinate(0, 6))).thenReturn(Optional.of(gameCard7));
        when(playerBoard.getGameCard(new Coordinate(1, 1))).thenReturn(Optional.of(gameCard8));
        when(playerBoard.getGameCard(new Coordinate(1, 3))).thenReturn(Optional.of(gameCard9));
        when(playerBoard.getGameCard(new Coordinate(1, 5))).thenReturn(Optional.of(gameCard10));
        when(playerBoard.getGameCard(new Coordinate(1, 7))).thenReturn(Optional.of(gameCard11));
        when(playerBoard.getGameCard(new Coordinate(2, 2))).thenReturn(Optional.of(gameCard12));
        when(playerBoard.getGameCard(new Coordinate(2, 4))).thenReturn(Optional.of(gameCard13));
        when(playerBoard.getGameCard(new Coordinate(2, 6))).thenReturn(Optional.of(gameCard14));
        assertEquals(4, positionalObjectiveCard.getPoints(playerBoard));
    }

    @Test
    @DisplayName("Equals method returns true when comparing identical ItemObjectiveCards")
    public void equalsReturnsTrueForIdenticalCards() {
        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.FUNGI, 3);
        ItemObjectiveCard itemObjectiveCard1 = new ItemObjectiveCard(1, 2, gameItemStore);
        ItemObjectiveCard itemObjectiveCard2 = new ItemObjectiveCard(1, 2, gameItemStore);
        assertTrue(itemObjectiveCard1.equals(itemObjectiveCard2));
    }

    @Test
    @DisplayName("Equals method returns false when comparing different ItemObjectiveCards")
    public void equalsReturnsFalseForDifferentCards() {
        GameItemStore gameItemStore1 = new GameItemStore();
        gameItemStore1.set(GameItemEnum.FUNGI, 3);
        ItemObjectiveCard itemObjectiveCard1 = new ItemObjectiveCard(1, 2, gameItemStore1);

        GameItemStore gameItemStore2 = new GameItemStore();
        gameItemStore2.set(GameItemEnum.ANIMAL, 2);
        ItemObjectiveCard itemObjectiveCard2 = new ItemObjectiveCard(1, 2, gameItemStore2);

        assertFalse(itemObjectiveCard1.equals(itemObjectiveCard2));
    }
}
