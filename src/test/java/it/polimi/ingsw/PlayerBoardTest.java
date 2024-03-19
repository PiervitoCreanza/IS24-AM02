package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import jdk.jshell.spi.ExecutionControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Point;
import java.util.HashMap;

@DisplayName("Player Board Test")
public class PlayerBoardTest {
    private PlayerBoard playerBoard;
    private GameCard gameCard;
    private GameItemEnum gameItem;

    @BeforeEach
    public void setup() {
        playerBoard = new PlayerBoard();
        gameCard = mock(GameCard.class);
        when(gameCard.getNeededItemStore()).thenReturn(new GameItemStore());
        gameItem = GameItemEnum.values()[0];
    }

    @Test
    @DisplayName("Get game card returns empty optional when no card at point")
    public void getGameCardReturnsEmptyOptionalWhenNoCardAtPoint() {
        Point point = new Point(0, 0);
        assertTrue(playerBoard.getGameCard(point).isEmpty());
    }

    @Test
    @DisplayName("Get game card returns card when present at point")
    public void getGameCardReturnsCardWhenPresentAtPoint() {
        Point point = new Point(0, 0);
        playerBoard.setGameCard(point, gameCard);
        assertEquals(gameCard, playerBoard.getGameCard(point).get());
    }

    @Test
    @DisplayName("Get game card position returns empty optional when card not present")
    public void getGameCardPositionReturnsEmptyOptionalWhenCardNotPresent() {
        assertTrue(playerBoard.getGameCardPosition(gameCard).isEmpty());
    }

    @Test
    @DisplayName("Get game card position returns point when card present")
    public void getGameCardPositionReturnsPointWhenCardPresent() {
        Point point = new Point(0, 0);
        playerBoard.setGameCard(point, gameCard);
        assertEquals(point, playerBoard.getGameCardPosition(gameCard).get());
    }

    @Test
    @DisplayName("Get game cards returns empty list when no cards present")
    public void getGameCardsReturnsEmptyListWhenNoCardsPresent() {
        assertTrue(playerBoard.getGameCards().isEmpty());
    }

    @Test
    @DisplayName("Get game cards returns list of cards when cards present")
    public void getGameCardsReturnsListOfCardsWhenCardsPresent() {
        Point point = new Point(0, 0);
        playerBoard.setGameCard(point, gameCard);
        assertTrue(playerBoard.getGameCards().contains(gameCard));
    }

    @Test
    @DisplayName("Get game item amount returns zero when item not present")
    public void getGameResourceAmountReturnsZeroWhenResourceNotPresent() {
        assertEquals(0, playerBoard.getGameItemAmount(gameItem));
    }

    @Test
    @DisplayName("Set game card throws exception when point occupied")
    public void setGameCardThrowsExceptionWhenPointOccupied() {
        Point point = new Point(0, 0);
        playerBoard.setGameCard(point, gameCard);
        assertThrows(IllegalArgumentException.class, () -> playerBoard.setGameCard(point, gameCard));
    }

    @Test
    @DisplayName("Set game card throws exception when point free and no adjacent card present")
    public void setGameCardThrowsExceptionWhenPointFreeAndNoAdjacentCardPresent() {
        Point point = new Point(1, 1);
        assertThrows(IllegalArgumentException.class, () -> playerBoard.setGameCard(point, gameCard));
    }

    @Test
    @DisplayName("Set game card throws exception when point free, adjacent card present but not matching")
    public void setGameCardThrowsExceptionWhenPointFreeAdjacentCardPresentButNotMatching() {
        Corner emptyCorner = mock(Corner.class);
        when(gameCard.getCorner(CornerPosition.BOTTOM_LEFT)).thenReturn(emptyCorner);
        when(emptyCorner.isExisting()).thenReturn(false);
        Point point = new Point(1, 1);
        playerBoard.setGameCard(new Point(0, 0), gameCard);
        assertThrows(IllegalArgumentException.class, () -> playerBoard.setGameCard(point, gameCard));
    }

    @Test
    @DisplayName("Set game card correctly places card when point free, adjacent card present and matching")
    public void setGameCardCorrectlyPlacesCardWhenPointFreeAdjacentCardPresentAndMatching() {
        Corner emptyCorner = mock(Corner.class);
        when(gameCard.getCorner(CornerPosition.BOTTOM_LEFT)).thenReturn(emptyCorner);

        when(emptyCorner.isExisting()).thenReturn(true);
        Point point = new Point(1, 1);
        playerBoard.setGameCard(new Point(0, 0), gameCard);
        playerBoard.setGameCard(point, gameCard);
        assertEquals(gameCard, playerBoard.getGameCard(point).get());
    }

    @Test
    @DisplayName("Set game card places card when point free")
    public void setGameCardPlacesCardWhenPointFree() {
        Point point = new Point(0, 0);
        playerBoard.setGameCard(point, gameCard);
        assertEquals(gameCard, playerBoard.getGameCard(point).get());
    }
}