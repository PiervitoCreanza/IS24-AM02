package it.polimi.ingsw.model;
import it.polimi.ingsw.model.ObjectiveCard.PositionalData;
import it.polimi.ingsw.model.ObjectiveCard.PositionalObjectiveCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        assertThrows(IllegalArgumentException.class, () -> new PositionalObjectiveCard(-2,positionalData));
        assertThrows(IllegalArgumentException.class, () -> new PositionalObjectiveCard(10, null));
    }

    /**
     * This test checks if the getPoints() method of the PositionalObjectiveCard class returns the correct points when the pattern is an L shape.
     */
    @Test
    @DisplayName("Testing L Shape PositionalObjectiveCard")
    public void TryGetPointsWithLShape() {
        ArrayList<PositionalData> positionalData = new ArrayList<>();
        positionalData.add(new PositionalData(new Point(0,0), CardColor.RED));
        positionalData.add(new PositionalData(new Point(0,2), CardColor.RED));
        positionalData.add(new PositionalData(new Point(1,3), CardColor.GREEN));
        PositionalObjectiveCard positionalObjectiveCard = new PositionalObjectiveCard(3, positionalData);
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
        when(gameCard1.getCardColor()).thenReturn(CardColor.NEUTRAL);
        when(gameCard2.getCardColor()).thenReturn(CardColor.RED);
        when(gameCard3.getCardColor()).thenReturn(CardColor.RED);
        when(gameCard4.getCardColor()).thenReturn(CardColor.RED);
        when(gameCard5.getCardColor()).thenReturn(CardColor.GREEN);
        when(gameCard6.getCardColor()).thenReturn(CardColor.BLUE);
        when(gameCard7.getCardColor()).thenReturn(CardColor.PURPLE);
        when(gameCard8.getCardColor()).thenReturn(CardColor.RED);
        when(gameCard9.getCardColor()).thenReturn(CardColor.RED);
        when(gameCard10.getCardColor()).thenReturn(CardColor.RED);
        when(gameCard11.getCardColor()).thenReturn(CardColor.GREEN);
        when(gameCard12.getCardColor()).thenReturn(CardColor.GREEN);
        when(gameCard13.getCardColor()).thenReturn(CardColor.BLUE);
        when(playerBoard.getGameCards()).thenReturn(gameCards);
        when(playerBoard.getGameCardPosition(gameCard1)).thenReturn(Optional.of(new Point(0,0)));
        when(playerBoard.getGameCardPosition(gameCard2)).thenReturn(Optional.of(new Point(-1,1)));
        when(playerBoard.getGameCardPosition(gameCard3)).thenReturn(Optional.of(new Point(-1,3)));
        when(playerBoard.getGameCardPosition(gameCard4)).thenReturn(Optional.of(new Point(-1,5)));
        when(playerBoard.getGameCardPosition(gameCard5)).thenReturn(Optional.of(new Point(0,2)));
        when(playerBoard.getGameCardPosition(gameCard6)).thenReturn(Optional.of(new Point(0,4)));
        when(playerBoard.getGameCardPosition(gameCard7)).thenReturn(Optional.of(new Point(1,1)));
        when(playerBoard.getGameCardPosition(gameCard8)).thenReturn(Optional.of(new Point(1,3)));
        when(playerBoard.getGameCardPosition(gameCard9)).thenReturn(Optional.of(new Point(1,5)));
        when(playerBoard.getGameCardPosition(gameCard10)).thenReturn(Optional.of(new Point(1,7)));
        when(playerBoard.getGameCardPosition(gameCard11)).thenReturn(Optional.of(new Point(2,2)));
        when(playerBoard.getGameCardPosition(gameCard12)).thenReturn(Optional.of(new Point(2,4)));
        when(playerBoard.getGameCardPosition(gameCard13)).thenReturn(Optional.of(new Point(2,6)));
        assertEquals(6, positionalObjectiveCard.getPoints(playerBoard));
    }

    /**
     * This test checks if the getPoints() method of the PositionalObjectiveCard class returns the correct points when the pattern is stairs shape.
     */
    @Test
    @DisplayName("Testing Stairs Shape PositionalObjectiveCard")
    public void TryGetPointsWithStairsShape() {
        ArrayList<PositionalData> positionalData = new ArrayList<>();
        positionalData.add(new PositionalData(new Point(0,0), CardColor.GREEN));
        positionalData.add(new PositionalData(new Point(1,1), CardColor.GREEN));
        positionalData.add(new PositionalData(new Point(2,2), CardColor.GREEN));
        PositionalObjectiveCard positionalObjectiveCard = new PositionalObjectiveCard(2, positionalData);
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
        when(gameCard1.getCardColor()).thenReturn(CardColor.NEUTRAL);
        when(gameCard2.getCardColor()).thenReturn(CardColor.RED);
        when(gameCard3.getCardColor()).thenReturn(CardColor.RED);
        when(gameCard4.getCardColor()).thenReturn(CardColor.GREEN);
        when(gameCard5.getCardColor()).thenReturn(CardColor.GREEN);
        when(gameCard6.getCardColor()).thenReturn(CardColor.GREEN);
        when(gameCard7.getCardColor()).thenReturn(CardColor.GREEN);
        when(gameCard8.getCardColor()).thenReturn(CardColor.PURPLE);
        when(gameCard9.getCardColor()).thenReturn(CardColor.GREEN);
        when(gameCard10.getCardColor()).thenReturn(CardColor.GREEN);
        when(gameCard11.getCardColor()).thenReturn(CardColor.RED);
        when(gameCard12.getCardColor()).thenReturn(CardColor.GREEN);
        when(gameCard13.getCardColor()).thenReturn(CardColor.GREEN);
        when(gameCard14.getCardColor()).thenReturn(CardColor.BLUE);
        when(playerBoard.getGameCards()).thenReturn(gameCards);
        when(playerBoard.getGameCardPosition(gameCard1)).thenReturn(Optional.of(new Point(0,0)));
        when(playerBoard.getGameCardPosition(gameCard2)).thenReturn(Optional.of(new Point(-1,1)));
        when(playerBoard.getGameCardPosition(gameCard3)).thenReturn(Optional.of(new Point(-1,3)));
        when(playerBoard.getGameCardPosition(gameCard4)).thenReturn(Optional.of(new Point(-1,5)));
        when(playerBoard.getGameCardPosition(gameCard5)).thenReturn(Optional.of(new Point(0,2)));
        when(playerBoard.getGameCardPosition(gameCard6)).thenReturn(Optional.of(new Point(0,4)));
        when(playerBoard.getGameCardPosition(gameCard7)).thenReturn(Optional.of(new Point(0,6)));
        when(playerBoard.getGameCardPosition(gameCard8)).thenReturn(Optional.of(new Point(1,1)));
        when(playerBoard.getGameCardPosition(gameCard9)).thenReturn(Optional.of(new Point(1,3)));
        when(playerBoard.getGameCardPosition(gameCard10)).thenReturn(Optional.of(new Point(1,5)));
        when(playerBoard.getGameCardPosition(gameCard11)).thenReturn(Optional.of(new Point(1,7)));
        when(playerBoard.getGameCardPosition(gameCard12)).thenReturn(Optional.of(new Point(2,2)));
        when(playerBoard.getGameCardPosition(gameCard13)).thenReturn(Optional.of(new Point(2,4)));
        when(playerBoard.getGameCardPosition(gameCard14)).thenReturn(Optional.of(new Point(2,6)));
        assertEquals(4, positionalObjectiveCard.getPoints(playerBoard));
    }
}