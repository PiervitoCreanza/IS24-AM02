package it.polimi.ingsw;

import it.polimi.ingsw.model.GameCard;
import it.polimi.ingsw.model.GameObject;
import it.polimi.ingsw.model.GameResource;
import it.polimi.ingsw.model.PlayerBoard;
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
    private GameResource gameResource;
    private GameObject gameObject;

    @BeforeEach
    public void setup() {
        playerBoard = new PlayerBoard();
        gameCard = new GameCard();
        gameResource = GameResource.values()[0];
        gameObject = GameObject.values()[0];
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
    @DisplayName("Get game resource amount returns zero when resource not present")
    public void getGameResourceAmountReturnsZeroWhenResourceNotPresent() {
        assertEquals(0, playerBoard.getGameResourceAmount(gameResource));
    }

    @Test
    @DisplayName("Get game object amount returns zero when object not present")
    public void getGameObjectAmountReturnsZeroWhenObjectNotPresent() {
        assertEquals(0, playerBoard.getGameObjectAmount(gameObject));
    }

    @Test
    @DisplayName("IsPlaceable returns false when point occupied")
    public void isPlaceableReturnsFalseWhenPointOccupied() {
        Point point = new Point(0, 0);
        playerBoard.setGameCard(point, gameCard);
        assertFalse(playerBoard.isPlaceable(point, gameCard));
    }

    @Test
    @DisplayName("IsPlaceable returns false when point free and no adjacent card present")
    public void isPlaceableReturnsFalseWhenPointFreeAndNoAdjacentCardPresent() {
        Point point = new Point(1, 1);
        assertFalse(playerBoard.isPlaceable(point, gameCard));
    }

    @Test
    @DisplayName("IsPlaceable returns false when point free, adjacent card present but not matching")
    public void isPlaceableReturnsFalseWhenPointFreeAdjacentCardPresentButNotMatching() {
        // Not implemented yet
        assert false;
    }

    @Test
    @DisplayName("IsPlaceable returns true when point free and adjacent card present")
    public void isPlaceableReturnsTrueWhenPointFreeAndAdjacentCardPresent() {
        Point point1 = new Point(0, 0);
        Point point2 = new Point(1, 1);
        playerBoard.setGameCard(point1, gameCard);
        assertTrue(playerBoard.isPlaceable(point2, gameCard));
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