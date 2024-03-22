package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

@DisplayName("Player Board Test")
public class PlayerBoardTest {
    private PlayerBoard playerBoard;
    private GameCard gameCard;
    private GameItemEnum gameItem;

    @BeforeEach
    public void setup() {

        gameCard = mock(GameCard.class);
        playerBoard = new PlayerBoard(gameCard);
        when(gameCard.getNeededItemStore()).thenReturn(new GameItemStore());
        gameItem = GameItemEnum.values()[0];
    }

    @Test
    @DisplayName("Get game card returns empty optional when no card at coordinate")
    public void getGameCardReturnsEmptyOptionalWhenNoCardAtPoint() {
        Coordinate coordinate = new Coordinate(0, 0);
        assertTrue(playerBoard.getGameCard(coordinate).isEmpty());
    }

    @Test
    @DisplayName("Get game card returns card when present at coordinate")
    public void getGameCardReturnsCardWhenPresentAtPoint() {
        Coordinate coordinate = new Coordinate(0, 0);
        playerBoard.setGameCard(coordinate, gameCard);
        assertEquals(gameCard, playerBoard.getGameCard(coordinate).get());
    }

    @Test
    @DisplayName("Get game card position returns empty optional when card not present")
    public void getGameCardPositionReturnsEmptyOptionalWhenCardNotPresent() {
        assertTrue(playerBoard.getGameCardPosition(gameCard).isEmpty());
    }

    @Test
    @DisplayName("Get game card position returns coordinate when card present")
    public void getGameCardPositionReturnsPointWhenCardPresent() {
        Coordinate coordinate = new Coordinate(0, 0);
        playerBoard.setGameCard(coordinate, gameCard);
        assertEquals(coordinate, playerBoard.getGameCardPosition(gameCard).get());
    }

    @Test
    @DisplayName("Get game cards returns empty list when no cards present")
    public void getGameCardsReturnsEmptyListWhenNoCardsPresent() {
        assertTrue(playerBoard.getGameCards().isEmpty());
    }

    @Test
    @DisplayName("Get game cards returns list of cards when cards present")
    public void getGameCardsReturnsListOfCardsWhenCardsPresent() {
        Coordinate coordinate = new Coordinate(0, 0);
        playerBoard.setGameCard(coordinate, gameCard);
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
        Coordinate coordinate = new Coordinate(0, 0);
        playerBoard.setGameCard(coordinate, gameCard);
        assertEquals(playerBoard.getGameCard(coordinate).get(), gameCard);
    }

    @Test
    @DisplayName("SetGameCard throws exception when coordinate already occupied")
    public void setGameCardTest2() {
        Coordinate coordinate = new Coordinate(0, 0);
        playerBoard.setGameCard(coordinate, gameCard);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> playerBoard.setGameCard(coordinate, gameCard));
        assertEquals("Position already occupied", exception.getMessage());
    }

    @Test
    @DisplayName("SetGameCard throws exception when no adjacent card present")
    public void setGameCardTest3() {
        Coordinate coordinate = new Coordinate(1, 1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> playerBoard.setGameCard(coordinate, gameCard));
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

        Coordinate coordinate = new Coordinate(1, 1);
        // Place the starter card to avoid the exception of the previous test
        playerBoard.setGameCard(new Coordinate(0, 0), gameCard);
        assertThrows(IllegalArgumentException.class, () -> playerBoard.setGameCard(coordinate, gameCard));
    }

    @Test
    @DisplayName("SetGameCard throws exception when not enough resources")
    public void setGameCardTest5() {
        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(gameItem, 1);
        when(gameCard.getNeededItemStore()).thenReturn(gameItemStore);
        Coordinate coordinate = new Coordinate(0, 0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> playerBoard.setGameCard(coordinate, gameCard));
        assertEquals("Not enough resources", exception.getMessage());
    }

    @Test
    @DisplayName("SetGameCard works correctly")
    public void setGameCardCorrectlyPlacesCardWhenPointFreeAdjacentCardPresentAndMatching() {
        Corner nonEmptyCorner = mock(Corner.class);
        GameItemStore gameItemStore = new GameItemStore();
        when(gameCard.getCorner(any(CornerPosition.class))).thenReturn(Optional.of(nonEmptyCorner));
        when(gameCard.setCornerCovered(any(CornerPosition.class))).thenReturn(GameItemEnum.PLANT);
        Coordinate coordinate = new Coordinate(1, 1);
        playerBoard.setGameCard(new Coordinate(0, 0), gameCard);
        playerBoard.setGameCard(coordinate, gameCard);
        assertEquals(gameCard, playerBoard.getGameCard(coordinate).get());
        // TODO: FINISH THE TEST
    }

    @Test
    @DisplayName("SetGameCard places card when coordinate free")
    public void setGameCardPlacesCardWhenPointFree() {
        Coordinate coordinate = new Coordinate(0, 0);
        playerBoard.setGameCard(coordinate, gameCard);
        assertEquals(gameCard, playerBoard.getGameCard(coordinate).get());
    }
}