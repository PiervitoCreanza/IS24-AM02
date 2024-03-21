package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import jdk.jshell.spi.ExecutionControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Point;
import java.util.HashMap;
import java.util.Optional;

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

    /**
     * SetGameCard tests
     */
    @Test
    @DisplayName("SetGameCard places the starter card")
    public void setGameCardTest1() {
        Point point = new Point(0, 0);
        playerBoard.setGameCard(point, gameCard);
        assertEquals(playerBoard.getGameCard(point).get(), gameCard);
    }

    @Test
    @DisplayName("SetGameCard throws exception when point already occupied")
    public void setGameCardTest2() {
        Point point = new Point(0, 0);
        playerBoard.setGameCard(point, gameCard);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> playerBoard.setGameCard(point, gameCard));
        assertEquals("Position already occupied", exception.getMessage());
    }

    @Test
    @DisplayName("SetGameCard throws exception when no adjacent card present")
    public void setGameCardTest3() {
        Point point = new Point(1, 1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> playerBoard.setGameCard(point, gameCard));
        assertEquals("Position not adjacent to any other card", exception.getMessage());
    }

    @Test
    @DisplayName("SetGameCard throws exception when adjacent card is missing intersecting corner")
    public void setGameCardTest4() {
        // Mock a Corner card with empty corners
        Optional<Corner> emptyCorner = Optional.empty();
        when(gameCard.getCorner(CornerPosition.BOTTOM_LEFT)).thenReturn(emptyCorner);
        when(gameCard.getCorner(CornerPosition.BOTTOM_RIGHT)).thenReturn(emptyCorner);
        when(gameCard.getCorner(CornerPosition.TOP_LEFT)).thenReturn(emptyCorner);
        when(gameCard.getCorner(CornerPosition.TOP_RIGHT)).thenReturn(emptyCorner);

        Point point = new Point(1, 1);
        // Place the starter card to avoid the exception of the previous test
        playerBoard.setGameCard(new Point(0, 0), gameCard);
        assertThrows(IllegalArgumentException.class, () -> playerBoard.setGameCard(point, gameCard));
    }

    @Test
    @DisplayName("SetGameCard throws exception when not enough resources")
    public void setGameCardTest5() {
        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(gameItem, 1);
        when(gameCard.getNeededItemStore()).thenReturn(gameItemStore);
        Point point = new Point(0, 0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> playerBoard.setGameCard(point, gameCard));
        assertEquals("Not enough resources", exception.getMessage());
    }

    @Test
    @DisplayName("SetGameCard works correctly")
    public void setGameCardCorrectlyPlacesCardWhenPointFreeAdjacentCardPresentAndMatching() {
        Corner nonEmptyCorner = mock(Corner.class);
        GameItemStore gameItemStore = new GameItemStore();
        when(gameCard.getCorner(any(CornerPosition.class))).thenReturn(nonEmptyCorner);
        when(nonEmptyCorner.isExisting()).thenReturn(true);
        when(gameCard.setCornerCovered(any(CornerPosition.class))).thenReturn(GameItemEnum.PLANT);
        Point point = new Point(1, 1);
        playerBoard.setGameCard(new Point(0, 0), gameCard);
        playerBoard.setGameCard(point, gameCard);
        assertEquals(gameCard, playerBoard.getGameCard(point).get());
        // TODO: FINISH THE TEST
    }

    @Test
    @DisplayName("SetGameCard places card when point free")
    public void setGameCardPlacesCardWhenPointFree() {
        Point point = new Point(0, 0);
        playerBoard.setGameCard(point, gameCard);
        assertEquals(gameCard, playerBoard.getGameCard(point).get());
    }
}