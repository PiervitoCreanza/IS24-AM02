package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.model.card.corner.CornerPosition;
import it.polimi.ingsw.model.card.gameCard.BackGameCard;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.gameCard.front.FrontGameCard;
import it.polimi.ingsw.model.card.gameCard.front.goldCard.FrontPositionalGoldGameCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import it.polimi.ingsw.parsing.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        playerBoard.placeGameCard(new Coordinate(0, 0), starterCard);
    }

    private GameCard createCardWithCornerItem(GameItemEnum gameItem) {
        return new GameCard(1, new FrontGameCard(new Corner(false, gameItem), new Corner(false, gameItem), new Corner(false, gameItem), new Corner(false, gameItem), 0), new BackGameCard(new Corner(false, gameItem), new Corner(false, gameItem), new Corner(false, gameItem), new Corner(false, gameItem), new GameItemStore()), CardColorEnum.GREEN);
    }

    private GameCard createCardWithoutCorner() {
        return new GameCard(2, new FrontGameCard(null, null, null, null, 0), new BackGameCard(null, null, null, null, new GameItemStore()), CardColorEnum.GREEN);
    }

    private void assertIllegalArgument(String message, org.junit.jupiter.api.function.Executable executable) {
        Exception exception = assertThrows(IllegalArgumentException.class, executable);
        assertEquals(message, exception.getMessage());
    }

    private GameCard createPositionalGoldCard(GameItemEnum gameItem, int points) {
        return new GameCard(3, new FrontPositionalGoldGameCard(new Corner(false, gameItem), new Corner(false, gameItem), new Corner(false, gameItem), new Corner(false, gameItem), points, new GameItemStore()), new BackGameCard(new Corner(false, gameItem), new Corner(false, gameItem), new Corner(false, gameItem), new Corner(false, gameItem), new GameItemStore()), CardColorEnum.GREEN);
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
        playerBoard.placeGameCard(coordinate, starterCard);
        assertTrue(playerBoard.getGameCards().contains(starterCard));
    }

    @Test
    @DisplayName("Get game item amount returns zero when item not present")
    public void getGameResourceAmountReturnsZeroWhenResourceNotPresent() {
        assertEquals(0, playerBoard.getGameItemAmount(gameItem));
    }

    /**
     * PlaceGameCard tests
     */
    @Test
    @DisplayName("PlaceGameCard places the starter card")
    public void placeGameCardTest1() {
        setStarterCard();
        assertEquals(playerBoard.getGameCard(new Coordinate(0, 0)).get(), starterCard);
    }

    @Test
    @DisplayName("PlaceGameCard throws exception Position already occupied")
    public void placeGameCardTest2() {
        setStarterCard();
        Exception exception = assertThrows(IllegalArgumentException.class, this::setStarterCard);
        assertEquals("You're trying to place the card in an already occupied position.", exception.getMessage());
    }

    @Test
    @DisplayName("PlaceGameCard throws exception Position not adjacent to any other card")
    public void placeGameCardTest3() {
        Coordinate coordinate = new Coordinate(1, 1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> playerBoard.placeGameCard(coordinate, resourceCard));
        assertEquals("Position not adjacent to any other card", exception.getMessage());
    }

    @Test
    @DisplayName("PlaceGameCard throws exception Position not compatible with adjacent cards")
    public void placeGameCardTest4() {
        // Create a card with no corners
        starterCard = createCardWithoutCorner();
        // Place the starter card to avoid the exception in the previous test
        setStarterCard();

        Coordinate coordinate = new Coordinate(1, 1);
        assertIllegalArgument("Position not compatible with adjacent cards", () -> playerBoard.placeGameCard(coordinate, resourceCard));
    }

    @Test
    @DisplayName("PlaceGameCard throws exception when not enough resources")
    public void placeGameCardTest5() {
        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.INSECT, 2);
        when(gameCard.getNeededItemStore()).thenReturn(gameItemStore);
        Coordinate coordinate = new Coordinate(0, 0);

        assertIllegalArgument("You don't have enough game resources to place this card", () -> playerBoard.placeGameCard(coordinate, gameCard));
    }

    @Test
    @DisplayName("PlaceGameCard works correctly")
    public void placeGameCardCorrectlyPlacesCardWhenPointFreeAdjacentCardPresentAndMatching() {
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
        int points = playerBoard.placeGameCard(coordinate, positionalGoldCard);

        // Check that the new card is placed correctly
        assertEquals(positionalGoldCard, playerBoard.getGameCard(coordinate).get());

        // Check that the corners are covered correctly
        assertEquals(GameItemEnum.NONE, starterCard.getCorner(CornerPosition.TOP_RIGHT).get().getGameItem());
        assertEquals(GameItemEnum.PLANT, starterCard.getCorner(CornerPosition.TOP_LEFT).get().getGameItem());
        assertEquals(GameItemEnum.PLANT, starterCard.getCorner(CornerPosition.BOTTOM_LEFT).get().getGameItem());
        assertEquals(GameItemEnum.PLANT, starterCard.getCorner(CornerPosition.BOTTOM_RIGHT).get().getGameItem());

        // Check that the resources are updated correctly
        assertEquals(3, playerBoard.getGameItemAmount(GameItemEnum.PLANT));
        assertEquals(0, playerBoard.getGameItemAmount(GameItemEnum.ANIMAL));
        assertEquals(4, playerBoard.getGameItemAmount(GameItemEnum.FUNGI));
        assertEquals(0, playerBoard.getGameItemAmount(GameItemEnum.INSECT));
        assertEquals(0, playerBoard.getGameItemAmount(GameItemEnum.QUILL));
        assertEquals(0, playerBoard.getGameItemAmount(GameItemEnum.INKWELL));
        assertEquals(0, playerBoard.getGameItemAmount(GameItemEnum.MANUSCRIPT));

        // Check that the points are calculated correctly
        assertEquals(positionalGoldCard.calculatePoints(coordinate, playerBoard), points);
    }

    @Test
    @DisplayName("getAvailablePositions return <0,0> when no cards present")
    public void getAvailablePositionsReturns00WhenNoCardsPresent() {
        assertEquals(1, playerBoard.getAvailablePositions().size());
        assertTrue(playerBoard.getAvailablePositions().contains(new Coordinate(0, 0)));
    }

    private void placeCard(int x, int y, GameCard card, PlayerBoard playerBoard) {
        playerBoard.placeGameCard(new Coordinate(x, y), card);
    }

    private GameCard getGameCardById(int id, ArrayList<GameCard> cards) {
        return cards.stream().filter(c -> c.getCardId() == id).findFirst().get();
    }

    @Test
    @DisplayName("getAvailablePositions returns correct coordinates when cards present")
    public void getAvailablePositionsReturnsCorrectCoordinatesWhenCardsPresent() {
        Parser p = new Parser();
        GameCard starterCard = p.getStarterDeck().getCards().stream().filter(c -> c.getCardId() == 84).findFirst().get();
        PlayerBoard playerBoard = new PlayerBoard(starterCard);
        ArrayList<GameCard> cards = p.getResourceDeck().getCards();

        placeCard(0, 0, starterCard, playerBoard);
        placeCard(1, 1, getGameCardById(2, cards), playerBoard);
        placeCard(-1, -1, getGameCardById(5, cards), playerBoard);
        placeCard(-1, 1, getGameCardById(10, cards), playerBoard);

        assertEquals(6, playerBoard.getAvailablePositions().size());
        assertTrue(playerBoard.getAvailablePositions().contains(new Coordinate(0, 2)));
        assertTrue(playerBoard.getAvailablePositions().contains(new Coordinate(1, -1)));
        assertTrue(playerBoard.getAvailablePositions().contains(new Coordinate(2, 2)));
        assertTrue(playerBoard.getAvailablePositions().contains(new Coordinate(2, 0)));
        assertTrue(playerBoard.getAvailablePositions().contains(new Coordinate(0, -2)));
        assertTrue(playerBoard.getAvailablePositions().contains(new Coordinate(-2, -2)));
    }
}