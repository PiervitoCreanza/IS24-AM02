package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import jdk.jshell.spi.ExecutionControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        gameCard = new GameCard();
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
        // Not implemented yet
        assert false;
    }

    @Test
    @DisplayName("Set game card places card when point free")
    public void setGameCardPlacesCardWhenPointFree() {
        Point point = new Point(0, 0);
        playerBoard.setGameCard(point, gameCard);
        assertEquals(gameCard, playerBoard.getGameCard(point).get());
    }
}