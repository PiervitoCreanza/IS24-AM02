package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Executable;
import java.util.Optional;

@DisplayName("Player Board Test")
public class PlayerBoardTest {
    private PlayerBoard playerBoard;
    private GameCard gameCard;
    private GameCard starterCard;

    private GameCard resourceCard;

    private GameCard positionalGoldCard;
    private GameItemEnum gameItem;

    /**
     * Helper methods
     */
    private void setStarterCard() {
        playerBoard.setGameCard(new Coordinate(0, 0), starterCard);
    }

    private GameCard createCardWithCornerItem(GameItemEnum gameItem) {
        return new GameCard(new Front(new Corner(false, gameItem), new Corner(false, gameItem), new Corner(false, gameItem), new Corner(false, gameItem), 0), new Back(new GameItemStore(), new Corner(false, gameItem), new Corner(false, gameItem), new Corner(false, gameItem), new Corner(false, gameItem)), CardColor.GREEN);
    }

    private GameCard createCardWithoutCorner() {
        return new GameCard(new Front(null, null, null, null, 0), new Back(new GameItemStore(), null, null, null, null), CardColor.GREEN);
    }

    private void assertIllegalArgument(String message, org.junit.jupiter.api.function.Executable executable) {
        Exception exception = assertThrows(IllegalArgumentException.class, executable);
        assertEquals(message, exception.getMessage());
    }

    private GameCard createPositionalGoldCard(GameItemEnum gameItem, int points) {
        return new GameCard(new FrontPositionalGoldCard(new Corner(false, gameItem), new Corner(false, gameItem), new Corner(false, gameItem), new Corner(false, gameItem), points, new GameItemStore()), new Back(new GameItemStore(), new Corner(false, gameItem), new Corner(false, gameItem), new Corner(false, gameItem), new Corner(false, gameItem)), CardColor.GREEN);
    }

    @BeforeEach
    public void setup() {

        gameCard = mock(GameCard.class);

        //Create starterCard
        starterCard = createCardWithCornerItem(GameItemEnum.PLANT);

        // Create resourceCard
        resourceCard = createCardWithCornerItem(GameItemEnum.FUNGI);

        // Create positionalGoldCard
        positionalGoldCard = createPositionalGoldCard(GameItemEnum.FUNGI, 2);

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
        setStarterCard();
        assertEquals(starterCard, playerBoard.getGameCard(new Coordinate(0, 0)).get());
    }

    @Test
    @DisplayName("Get game card position returns empty optional when card not present")
    public void getGameCardPositionReturnsEmptyOptionalWhenCardNotPresent() {
        assertTrue(playerBoard.getGameCardPosition(starterCard).isEmpty());
    }

    @Test
    @DisplayName("Get game card position returns coordinate when card present")
    public void getGameCardPositionReturnsPointWhenCardPresent() {
        setStarterCard();
        assertEquals(new Coordinate(0, 0), playerBoard.getGameCardPosition(starterCard).get());
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
        playerBoard.setGameCard(coordinate, starterCard);
        assertTrue(playerBoard.getGameCards().contains(starterCard));
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
        setStarterCard();
        assertEquals(playerBoard.getGameCard(new Coordinate(0, 0)).get(), starterCard);
    }

    @Test
    @DisplayName("SetGameCard throws exception Position already occupied")
    public void setGameCardTest2() {
        setStarterCard();
        Exception exception = assertThrows(IllegalArgumentException.class, this::setStarterCard);
        assertEquals("Position already occupied", exception.getMessage());
    }

    @Test
    @DisplayName("SetGameCard throws exception Position not adjacent to any other card")
    public void setGameCardTest3() {
        Coordinate coordinate = new Coordinate(1, 1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> playerBoard.setGameCard(coordinate, resourceCard));
        assertEquals("Position not adjacent to any other card", exception.getMessage());
    }

    @Test
    @DisplayName("SetGameCard throws exception Position not compatible with adjacent cards")
    public void setGameCardTest4() {
        // Create a card with no corners
        starterCard = createCardWithoutCorner();
        // Place the starter card to avoid the exception in the previous test
        setStarterCard();

        Coordinate coordinate = new Coordinate(1, 1);
        assertIllegalArgument("Position not compatible with adjacent cards", () -> playerBoard.setGameCard(coordinate, resourceCard));
    }

    @Test
    @DisplayName("SetGameCard throws exception when not enough resources")
    public void setGameCardTest5() {
        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.INSECT, 2);
        when(gameCard.getNeededItemStore()).thenReturn(gameItemStore);
        Coordinate coordinate = new Coordinate(0, 0);

        assertIllegalArgument("Not enough resources", () -> playerBoard.setGameCard(coordinate, gameCard));
    }

    @Test
    @DisplayName("SetGameCard works correctly")
    public void setGameCardCorrectlyPlacesCardWhenPointFreeAdjacentCardPresentAndMatching() {
        setStarterCard();
        // Check that the starter card is placed correctly
        assertEquals(4, playerBoard.getGameItemAmount(GameItemEnum.PLANT));
        assertEquals(0, playerBoard.getGameItemAmount(GameItemEnum.ANIMAL));
        assertEquals(0, playerBoard.getGameItemAmount(GameItemEnum.FUNGI));
        assertEquals(0, playerBoard.getGameItemAmount(GameItemEnum.INSECT));
        assertEquals(0, playerBoard.getGameItemAmount(GameItemEnum.QUILL));
        assertEquals(0, playerBoard.getGameItemAmount(GameItemEnum.INKWELL));
        assertEquals(0, playerBoard.getGameItemAmount(GameItemEnum.MANUSCRIPT));

        // Place a new card
        Coordinate coordinate = new Coordinate(1, 1);
        int points = playerBoard.setGameCard(coordinate, positionalGoldCard);

        // Check that the new card is placed correctly
        assertEquals(positionalGoldCard, playerBoard.getGameCard(coordinate).get());

        // Check that the corners are covered correctly
        assertEquals(GameItemEnum.NONE, starterCard.getCorner(CornerPosition.TOP_RIGHT).get().getGameItem());
        assertEquals(GameItemEnum.PLANT, starterCard.getCorner(CornerPosition.TOP_LEFT).get().getGameItem());
        assertEquals(GameItemEnum.PLANT, starterCard.getCorner(CornerPosition.BOTTOM_LEFT).get().getGameItem());
        assertEquals(GameItemEnum.PLANT, starterCard.getCorner(CornerPosition.BOTTOM_RIGHT).get().getGameItem());

        // Check that the resources are updated correctly
        GameItemStore expectedGameItemStore = new GameItemStore();
        assertEquals(3, playerBoard.getGameItemAmount(GameItemEnum.PLANT));
        assertEquals(0, playerBoard.getGameItemAmount(GameItemEnum.ANIMAL));
        assertEquals(4, playerBoard.getGameItemAmount(GameItemEnum.FUNGI));
        assertEquals(0, playerBoard.getGameItemAmount(GameItemEnum.INSECT));
        assertEquals(0, playerBoard.getGameItemAmount(GameItemEnum.QUILL));
        assertEquals(0, playerBoard.getGameItemAmount(GameItemEnum.INKWELL));
        assertEquals(0, playerBoard.getGameItemAmount(GameItemEnum.MANUSCRIPT));

        // Check that the points are calculated correctly
        assertEquals(positionalGoldCard.getPoints(coordinate, playerBoard), points);
    }
}