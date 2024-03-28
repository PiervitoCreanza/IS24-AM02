package it.polimi.ingsw.model.card.gameCard;

import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.model.card.corner.CornerPosition;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        gameCard = new GameCard(side1, side2, CardColorEnum.RED);
        playerBoard = mock(PlayerBoard.class);
    }

    @Test
    public void switchSideShouldChangeCurrentSide() {
        gameCard.switchSide();
        assertEquals(side2, gameCard.getCurrentSide());
    }

    @Test
    public void getCornerShouldReturnCornerFromCurrentSide() {
        Corner corner = mock(Corner.class);
        when(side1.getCorner(CornerPosition.TOP_LEFT)).thenReturn(Optional.of(corner));

        Optional<Corner> result = gameCard.getCorner(CornerPosition.TOP_LEFT);

        assertTrue(result.isPresent());
        assertEquals(corner, result.get());
    }

    @Test
    public void setCornerCoveredShouldCoverCornerOnCurrentSide() {
        when(side1.setCornerCovered(CornerPosition.TOP_LEFT)).thenReturn(GameItemEnum.NONE);

        GameItemEnum result = gameCard.setCornerCovered(CornerPosition.TOP_LEFT);

        assertEquals(GameItemEnum.NONE, result);
    }

    @Test
    public void getPointsShouldReturnPointsFromCurrentSide() {
        Coordinate coordinate = new Coordinate(0, 0);
        when(side1.getPoints(coordinate, playerBoard)).thenReturn(5);

        int result = gameCard.getPoints(coordinate, playerBoard);

        assertEquals(5, result);
    }

    @Test
    public void getNeededItemStoreShouldReturnStoreFromCurrentSide() {
        GameItemStore store = mock(GameItemStore.class);
        when(side1.getNeededItemStore()).thenReturn(store);

        GameItemStore result = gameCard.getNeededItemStore();

        assertEquals(store, result);
    }

    @Test
    public void cardColorShouldMatchConstructorInput() {
        assertEquals(CardColorEnum.RED, gameCard.getCardColor());
    }

    @Test
    public void cardColorShouldNotChangeAfterSwitchingSides() {
        gameCard.switchSide();
        assertEquals(CardColorEnum.RED, gameCard.getCardColor());
    }
}