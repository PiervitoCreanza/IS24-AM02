package it.polimi.ingsw.model.player;


import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.utils.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DisplayName("PlayerTest")
public class PlayerTest {
    private Player player;
    private ObjectiveCard objectiveCard;

    @BeforeEach
    public void setup() {
        objectiveCard = mock(ObjectiveCard.class);
        GameCard starterCard = mock(GameCard.class);
        ObjectiveCard card = mock(ObjectiveCard.class);
        ArrayList<ObjectiveCard> choosableObjectives = new ArrayList<>();
        choosableObjectives.add(card);
        choosableObjectives.add(objectiveCard);
        player = new Player("TestPlayer", choosableObjectives, starterCard);
    }

    @Test
    @DisplayName("Get player position returns initial position")
    public void getPlayerPosReturnsInitialPosition() {
        assertEquals(0, player.getPlayerPos());
    }

    @Test
    @DisplayName("Advance player position increases position")
    public void advancePlayerPosIncreasesPosition() {
        player.advancePlayerPos(5);
        assertEquals(5, player.getPlayerPos());
    }

    @Test
    @DisplayName("Advance player position returns false when position < 20")
    public void advancePlayerPosReturnsFalseWhenPositionLessThanTwenty() {
        assertFalse(player.advancePlayerPos(5));
    }

    @Test
    @DisplayName("Advance player position returns true when position == 20")
    public void advancePlayerPosReturnsTrueWhenPositionEqualToTwenty() {
        assertTrue(player.advancePlayerPos(20));
    }

    @Test
    @DisplayName("Advance player position returns true when position > 20")
    public void advancePlayerPosReturnsTrueWhenPositionMoreThanTwenty() {
        assertTrue(player.advancePlayerPos(25));
    }

    @Test
    @DisplayName("Get player board returns non-null player board")
    public void getPlayerBoardReturnsNonNullPlayerBoard() {
        assertNotNull(player.getPlayerBoard());
    }

    @Test
    @DisplayName("Get player hand returns non-null hand")
    public void getPlayerHandReturnsNonNullHand() {
        assertNotNull(player.getPlayerHand());
    }

    @Test
    @DisplayName("Get objective card returns null before setting")
    public void getObjectiveCardReturnsNullBeforeSetting() {
        assertNull(player.getObjectiveCard());
    }

    @Test
    @DisplayName("Get objective card returns non-null after setting")
    public void getObjectiveCardReturnsNonNullAfterSetting() {
        player.setPlayerObjective(objectiveCard);
        assertEquals(objectiveCard, player.getObjectiveCard());
    }

    @Test
    @DisplayName("SetGameCard successfully places the card and removes it from the hand")
    public void setGameCardSuccessfullyPlacesCardAndRemovesFromHand() {
        Parser parser = new Parser();
        GameCard card = parser.getStarterDeck().draw();
        player.getPlayerHand().addCard(card);
        assertEquals(card, player.getPlayerHand().getCards().getFirst());
        player.setGameCard(new Coordinate(0, 0), card);
        assertEquals(card, player.getPlayerBoard().getGameCard(new Coordinate(0, 0)).get());
        assertFalse(player.getPlayerHand().getCards().contains(card));
    }
}