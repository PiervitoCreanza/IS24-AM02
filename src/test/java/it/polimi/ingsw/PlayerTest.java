package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Player Test")
public class PlayerTest {
    private Player player;

    @BeforeEach
    public void setup() {
        player = new Player("TestPlayer", 0);
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
        ObjectiveCard objectiveCard = new ObjectiveCard();
        player.setPlayerObjective(objectiveCard);
        assertEquals(objectiveCard, player.getObjectiveCard());
    }
}