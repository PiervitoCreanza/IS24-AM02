package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {
    private Game testGame;
    private GlobalBoard mockGlobalBoard;
    private Player testPlayer1;
    private Player testPlayer2;

    /**
     * Helper methods
     */

    //Method used to setup players before calculating the winner
    private void winnersSetUp(){
        testGame.addPlayer("Player2");

        testPlayer1 = testGame.getPlayer("Player1");
        testPlayer2 = testGame.getPlayer("Player2");

        ObjectiveCard mockPlayerObjective1 = Mockito.mock(ObjectiveCard.class);
        ObjectiveCard mockPlayerObjective2 = Mockito.mock(ObjectiveCard.class);

        //We need to remove one of the current ChoosableObjectives and add the mock one, otherwise the set method will fail
        testPlayer1.getChoosableObjectives().removeFirst();
        testPlayer2.getChoosableObjectives().removeFirst();
        testPlayer1.getChoosableObjectives().add(mockPlayerObjective1);
        testPlayer2.getChoosableObjectives().add(mockPlayerObjective2);
        testPlayer1.setPlayerObjective(mockPlayerObjective1);
        testPlayer2.setPlayerObjective(mockPlayerObjective2);

        //We also initialize the globalObjectives, both with same mock and both "not completed" for the players
        ObjectiveCard mockGlobalObjective = Mockito.mock(ObjectiveCard.class);
        when(mockGlobalBoard.getGlobalObjectives()).thenReturn(new ArrayList<>(List.of(mockGlobalObjective, mockGlobalObjective)));
        when(mockGlobalObjective.getPoints(testPlayer1.getPlayerBoard())).thenReturn(0);
        when(mockGlobalObjective.getPoints(testPlayer2.getPlayerBoard())).thenReturn(0);
    }

    //Method used to check if the calculated winners are equals to the expected ones
    private boolean checkWinners(ArrayList<Player> expectedWinners){
        ArrayList<Player> calculatedWinners = testGame.getWinners();
        return expectedWinners.containsAll(calculatedWinners) && calculatedWinners.containsAll(expectedWinners);
    }

    @BeforeEach
    void setUp() {
        @SuppressWarnings("unchecked")
        Deck<ObjectiveCard> mockObjectiveCardDeck = Mockito.mock(Deck.class);
        @SuppressWarnings("unchecked")
        Deck<GameCard> mockGameCardDeck = Mockito.mock(Deck.class);
        mockGlobalBoard = Mockito.mock(GlobalBoard.class);
        when(mockGlobalBoard.getObjectiveDeck()).thenReturn(mockObjectiveCardDeck);
        when(mockGlobalBoard.getStarterDeck()).thenReturn(mockGameCardDeck);
        when(mockObjectiveCardDeck.draw()).thenReturn(Mockito.mock(ObjectiveCard.class));
        when(mockGameCardDeck.draw()).thenReturn(Mockito.mock(GameCard.class));
        testGame = new Game("TestGame", 2, "Player1", mockGlobalBoard);
    }

    @Test
    @DisplayName("Game constructor throws exception when GameName is NULL")
    void nullGameNameShouldThrowException(){
        Exception exception = assertThrows(NullPointerException.class, () -> new Game(null, 2, "Player1", mockGlobalBoard));
        assertEquals("The game name can't be NULL", exception.getMessage());
    }

    @Test
    @DisplayName("Game constructor throws exception when nPlayers is not between 2-4")
    void wrongNumberOfPlayersShouldThrowException(){
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> new Game("WrongGame", 1, "Player1", mockGlobalBoard));
        assertEquals("Players must be between 2-4", exception1.getMessage());
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> new Game("WrongGame", 5, "Player1", mockGlobalBoard));
        assertEquals("Players must be between 2-4", exception2.getMessage());
    }

    @Test
    @DisplayName("Game constructor adds only one player and set it as current player")
    void gameConstructorShouldAddOnlyOnePlayerAndSetAsCurrentPlayer(){
        assertEquals(1, testGame.getPlayers().size());
        assertEquals("Player1", testGame.getCurrentPlayer().getPlayerName());
    }

    @Test
    @DisplayName("getPlayer throws exception when NULL player name is used as argument")
    void nullPlayerNameInGetPlayerShouldThrowException(){
        Exception exception = assertThrows(NullPointerException.class,() -> testGame.getPlayer(null));
        assertEquals("The player name can't be NULL", exception.getMessage());
    }

    @Test
    @DisplayName("getPlayer throws exception if the player isn't found")
    void getPlayerShouldThrowExceptionIfPlayerIsNotFound(){
        Exception exception = assertThrows(IllegalArgumentException.class,() -> testGame.getPlayer("Player2"));
        assertEquals("Player with name \"Player2\" doesn't exists", exception.getMessage());
    }

    @Test
    @DisplayName("getPlayer returns the correct player")
    void getPlayerShouldReturnTheCorrectPlayer(){
        assertEquals("Player1", testGame.getPlayer("Player1").getPlayerName());
    }

    @Test
    @DisplayName("addPlayer throws exception when the maximum number of players has been reached")
    void addPlayerShouldThrowExceptionIfPlayersQuotaIsExceeded(){
        testGame.addPlayer("Player2");
        Exception exception = assertThrows(RuntimeException.class,() -> testGame.addPlayer("Player3"));
        assertEquals("Maximum number of players already reached", exception.getMessage());
    }

    @Test
    @DisplayName("addPlayer throws exception when NULL player name is used as argument")
    void nullPlayerNameInAddPlayerShouldThrowException(){
        Exception exception = assertThrows(NullPointerException.class,() -> testGame.addPlayer(null));
        assertEquals("The player name can't be NULL", exception.getMessage());
    }

    @Test
    @DisplayName("addPlayer throws exception if a player with the same name already exists")
    void addPlayerShouldThrowExceptionIfPlayerWithTheSameNameAlreadyExists(){
        Exception exception = assertThrows(IllegalArgumentException.class,() -> testGame.addPlayer("Player1"));
        assertEquals("A player with the same name, already exists", exception.getMessage());
    }

    @Test
    @DisplayName("addPlayer adds a player with the correct attributes")
    void addPlayerShouldAddPlayerWithTheCorrectAttributes(){
        testGame.addPlayer("Player2");
        assertEquals(2, testGame.getPlayers().size());
        assertEquals("Player2", testGame.getPlayers().get(1).getPlayerName());
        assertEquals(2, testGame.getPlayers().get(1).getChoosableObjectives().size());
    }

    @Test
    @DisplayName("setNextPlayer changes currentPlayer")
    void setNextPlayerShouldChangeCurrentPlayer() {
        testGame.addPlayer("Player2");
        Player firstPlayer = testGame.getCurrentPlayer();
        testGame.setNextPlayer();
        Player secondPlayer = testGame.getCurrentPlayer();
        assertNotEquals(firstPlayer, secondPlayer);
        testGame.setNextPlayer();
        assertEquals(firstPlayer, testGame.getCurrentPlayer());
    }

    @Test
    @DisplayName("isStarted returns true when enough players have joined the game")
    void isStartedShouldReturnTrueWhenEnoughPlayers() {
        testGame.addPlayer("Player2");
        assertTrue(testGame.isStarted());
    }

    @Test
    @DisplayName("isStarted returns false when not enough players have joined the game")
    void isStartedShouldReturnFalseWhenNotEnoughPlayers() {
        assertFalse(testGame.isStarted());
    }

    @Test
    @DisplayName("isOver returns true when a player reaches 20 points")
    void isOverShouldReturnTrueWhenPlayerScoreIs20() {
        testGame.getCurrentPlayer().advancePlayerPos(20);
        assertTrue(testGame.isOver());
    }

    @Test
    @DisplayName("isOver returns false if nobody reached 20 points and decks are full")
    void isOverShouldReturnFalseWhenPlayerScoreIsLessThan20AndDecksAreNotEmpty() {
        when(mockGlobalBoard.isGoldDeckEmpty()).thenReturn(false);
        when(mockGlobalBoard.isResourceDeckEmpty()).thenReturn(false);
        assertFalse(testGame.isOver());
    }

    @Test
    @DisplayName("isOver returns true when both gold and resource decks are empty")
    void isOverShouldReturnTrueWhenBothDecksAreEmpty() {
        when(mockGlobalBoard.isGoldDeckEmpty()).thenReturn(true);
        when(mockGlobalBoard.isResourceDeckEmpty()).thenReturn(true);
        assertTrue(testGame.isOver());
    }

    @Test
    @DisplayName("isOver returns false when only one deck is empty")
    void isOverShouldReturnFalseWhenOnlyOneDeckIsEmpty() {
        when(mockGlobalBoard.isGoldDeckEmpty()).thenReturn(true);
        when(mockGlobalBoard.isResourceDeckEmpty()).thenReturn(false);
        assertFalse(testGame.isOver());

        when(mockGlobalBoard.isGoldDeckEmpty()).thenReturn(false);
        when(mockGlobalBoard.isResourceDeckEmpty()).thenReturn(true);
        assertFalse(testGame.isOver());
    }

    @Test
    @DisplayName("calculateWinners returns only a single player as winner, he has the highest score")
    void calculateWinnersOneWinnerHighestPoints() {
        winnersSetUp();
        //Both players have a playerPos = 0
        //Player 1 completes an objective and earns 1 point
        when(testPlayer1.getObjectiveCard().getPoints(testPlayer1.getPlayerBoard())).thenReturn(1);
        when(testPlayer2.getObjectiveCard().getPoints(testPlayer2.getPlayerBoard())).thenReturn(0);
        testGame.calculateWinners();
        //player1pos = 1 > player2pos = 0
        assertTrue(checkWinners(new ArrayList<>(List.of(testPlayer1))));
    }

    @Test
    @DisplayName("calculateWinners returns only a single player as winner, he has completed the most objectives")
    void calculateWinnersOneWinnerHighestObjectives() {
        winnersSetUp();
        testPlayer2.advancePlayerPos(1);
        //player1pos = 0, player2pos = 1
        //Player 1 completes an objective and earns 1 point
        when(testPlayer1.getObjectiveCard().getPoints(testPlayer1.getPlayerBoard())).thenReturn(1);
        when(testPlayer2.getObjectiveCard().getPoints(testPlayer2.getPlayerBoard())).thenReturn(0);
        testGame.calculateWinners();
        //(player1pos = 1) == (player2pos = 1) but (player1objectives = 1) > (player2objectives = 0)
        assertTrue(checkWinners(new ArrayList<>(List.of(testPlayer1))));
    }

    @Test
    @DisplayName("calculateWinners returns all the winners, there's a tie")
    void calculateWinnersTie() {
        winnersSetUp();
        //player1pos = 0, player2pos = 0
        //Both complete an objective and earn 1 point
        when(testPlayer1.getObjectiveCard().getPoints(testPlayer1.getPlayerBoard())).thenReturn(1);
        when(testPlayer2.getObjectiveCard().getPoints(testPlayer2.getPlayerBoard())).thenReturn(1);
        testGame.calculateWinners();
        //(player1pos = 1) == (player2pos = 1) and (player1objectives = 1) == (player2objectives = 1). It's a tie!
        assertTrue(checkWinners(new ArrayList<>(List.of(testPlayer1, testPlayer2))));
    }
}