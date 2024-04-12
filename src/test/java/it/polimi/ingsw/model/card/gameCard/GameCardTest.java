package it.polimi.ingsw.model.card.gameCard;

import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.model.card.corner.CornerPosition;
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

public class GameCardTest {

    private GameCard gameCard;
    private SideGameCard side1;
    private SideGameCard side2;
    private PlayerBoard playerBoard;

    @BeforeEach
    public void setup() {
        side1 = mock(SideGameCard.class);
        side2 = mock(SideGameCard.class);
        gameCard = new GameCard(1, side1, side2, CardColorEnum.RED);
        playerBoard = mock(PlayerBoard.class);
    }

    @Test
    @DisplayName("switchSide() test for GameCard class")
    public void switchSideShouldChangeCurrentSide() {
        gameCard.switchSide();
        assertEquals(side2, gameCard.getCurrentSide());
    }

    @Test
    @DisplayName("getCorner() test for GameCard class")
    public void getCornerShouldReturnCornerFromCurrentSide() {
        Corner corner = mock(Corner.class);
        when(side1.getCorner(CornerPosition.TOP_LEFT)).thenReturn(Optional.of(corner));

        Optional<Corner> result = gameCard.getCorner(CornerPosition.TOP_LEFT);

        assertTrue(result.isPresent());
        assertEquals(corner, result.get());
    }

    @Test
    @DisplayName("setCornerCovered() test for GameCard class")
    public void setCornerCoveredShouldCoverCornerOnCurrentSide() {
        when(side1.setCornerCovered(CornerPosition.TOP_LEFT)).thenReturn(GameItemEnum.NONE);

        GameItemEnum result = gameCard.setCornerCovered(CornerPosition.TOP_LEFT);

        assertEquals(GameItemEnum.NONE, result);
    }

    @Test
    @DisplayName("getPoints() test for GameCard class")
    public void getPointsShouldReturnPointsFromCurrentSide() {
        //not really useful because this method is defined in this class just to allow the Override in the child classes
        Coordinate coordinate = new Coordinate(0, 0);
        when(side1.getPoints(coordinate, playerBoard)).thenReturn(5);

        int result = gameCard.getPoints(coordinate, playerBoard);

        assertEquals(5, result);
    }

    @Test
    @DisplayName("getNeededItemStore() test for GameCard class")
    public void getNeededItemStoreShouldReturnStoreFromCurrentSide() {
        GameItemStore store = mock(GameItemStore.class);
        when(side1.getNeededItemStore()).thenReturn(store);

        GameItemStore result = gameCard.getNeededItemStore();

        assertEquals(store, result);
    }

    @Test
    @DisplayName("Card Color should match constructor input test for GameCard class")
    public void cardColorShouldMatchConstructorInput() {
        assertEquals(CardColorEnum.RED, gameCard.getCardColor());
    }

    @Test
    @DisplayName("Card Color should not change after switching Sides test for GameCard class")
    public void cardColorShouldNotChangeAfterSwitchingSides() {
        gameCard.switchSide();
        assertEquals(CardColorEnum.RED, gameCard.getCardColor());
        gameCard.switchSide();
        assertEquals(CardColorEnum.RED, gameCard.getCardColor());
    }


    @Test
    @DisplayName("getGameItemStore() should return the game item store of the current side")
    public void getGameItemStoreShouldReturnCurrentSideStore() {
        GameItemStore store = mock(GameItemStore.class);
        when(gameCard.getGameItemStore()).thenReturn(store);

        GameItemStore result = gameCard.getGameItemStore();

        assertEquals(store, result);
    }

    @Test
    @DisplayName("getGameItemStore() should return null if the current side has no store")
    public void getGameItemStoreShouldReturnNullIfNoStore() {
        when(gameCard.getGameItemStore()).thenReturn(null);

        GameItemStore result = gameCard.getGameItemStore();

        assertNull(result);
    }

    @Test
    @DisplayName("Equals method returns true when comparing identical GameCards")
    public void equalsReturnsTrueForIdenticalCards() {
        SideGameCard side1 = mock(SideGameCard.class);
        SideGameCard side2 = mock(SideGameCard.class);
        GameCard gameCard1 = new GameCard(1, side1, side2, CardColorEnum.RED);
        GameCard gameCard2 = new GameCard(1, side1, side2, CardColorEnum.RED);
        assertTrue(gameCard1.equals(gameCard2));
    }

    @Test
    @DisplayName("Equals method returns false when comparing different GameCards")
    public void equalsReturnsFalseForDifferentCards() {
        SideGameCard side1 = mock(SideGameCard.class);
        SideGameCard side2 = mock(SideGameCard.class);
        SideGameCard side3 = mock(SideGameCard.class);
        GameCard gameCard1 = new GameCard(1, side1, side2, CardColorEnum.RED);
        GameCard gameCard2 = new GameCard(2, side1, side3, CardColorEnum.GREEN);
        assertFalse(gameCard1.equals(gameCard2));
    }
}