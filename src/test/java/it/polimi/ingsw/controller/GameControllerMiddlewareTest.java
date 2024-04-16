package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GameControllerMiddlewareTest {

    GameControllerMiddleware gameControllerMiddleware;
    Game game;
    GameController gameController;
    int currentPlayerIndex = 0;

    @BeforeEach
    void setUp() {
        GameController gameController = Mockito.mock(GameController.class);
        game = Mockito.mock(Game.class);
        gameControllerMiddleware = new GameControllerMiddleware(gameController, game);
        when(game.getCurrentPlayer()).thenAnswer(invocation -> {
            Player player = Mockito.mock(Player.class);
            when(player.getPlayerName()).thenReturn("player" + currentPlayerIndex);
            return player;
        });
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                currentPlayerIndex = (currentPlayerIndex + 1) % 2;
                return null; // Void methods should return null
            }
        }).when(game).setNextPlayer();
        when(game.getCurrentPlayerIndex()).thenAnswer(invocation -> currentPlayerIndex);
        Player player0 = Mockito.mock(Player.class);
        when(player0.getPlayerName()).thenReturn("player0");
        Player player1 = Mockito.mock(Player.class);
        when(player1.getPlayerName()).thenReturn("player1");
        ArrayList<Player> players = new ArrayList<>();
        players.add(player0);
        players.add(player1);
        when(game.getPlayers()).thenAnswer(invocation -> players);
    }

    @Test
    @DisplayName("Test if the first player can join the game")
    void joinGame() {
        gameControllerMiddleware.joinGame("playerName");
        assertEquals(GameStatusEnum.WAIT_FOR_PLAYERS, gameControllerMiddleware.getGameStatus());
    }

    @Test
    @DisplayName("Test if a player cannot join when the game is full")
    void joinGame2() {
        when(game.isStarted()).thenReturn(true);
        gameControllerMiddleware.joinGame("playerName");
        assertThrows(IllegalStateException.class, () -> gameControllerMiddleware.joinGame("playerName"));
    }

    @Test
    @DisplayName("Test if an error is thrown when the game status is neither PLACE_CARD nor INIT_PLACE_STARTER_CARD")
    void placeCard() {
        gameControllerMiddleware.setGameStatus(GameStatusEnum.WAIT_FOR_PLAYERS);
        assertThrows(IllegalStateException.class, () -> gameControllerMiddleware.placeCard("player0", null, null));
    }

    @Test
    @DisplayName("Test if a card can be placed when gameStatus is PLACE_CARD")
    void placeCard1() {
        gameControllerMiddleware.setGameStatus(GameStatusEnum.PLACE_CARD);
        gameControllerMiddleware.placeCard("player0", null, null);
        assertEquals(GameStatusEnum.DRAW_CARD, gameControllerMiddleware.getGameStatus());
    }

    @Test
    @DisplayName("Test if a player can draw a card from the field when the game status is DRAW_CARD")
    void drawCardFromField() {
        gameControllerMiddleware.setGameStatus(GameStatusEnum.DRAW_CARD);
        gameControllerMiddleware.drawCardFromField("player0", null);
        assertEquals(GameStatusEnum.PLACE_CARD, gameControllerMiddleware.getGameStatus());
    }

    @Test
    @DisplayName("Test if a player cannot draw a card when the game status is not DRAW_CARD or it is not their turn")
    void drawCardFromField2() {
        gameControllerMiddleware.setGameStatus(GameStatusEnum.PLACE_CARD);
        assertThrows(IllegalStateException.class, () -> gameControllerMiddleware.drawCardFromField("player0", null));
        gameControllerMiddleware.setGameStatus(GameStatusEnum.DRAW_CARD);
        assertThrows(IllegalStateException.class, () -> gameControllerMiddleware.drawCardFromField("player1", null));
    }

    @Test
    @DisplayName("Test if the game status changes to INIT_CHOOSE_OBJECTIVE_CARD after placing the starter card\"")
    void drawCardFromDeck() {
        gameControllerMiddleware.setGameStatus(GameStatusEnum.INIT_PLACE_STARTER_CARD);
        gameControllerMiddleware.placeCard("player0", null, null);
        assertEquals(GameStatusEnum.INIT_CHOOSE_OBJECTIVE_CARD, gameControllerMiddleware.getGameStatus());
    }

    @Test
    @DisplayName("Test if card side can be switched only when game status is PLACE_CARD")
    void switchCardSide() {
        Player player = Mockito.mock(Player.class);
        when(player.getPlayerName()).thenReturn("player0");
        when(game.getCurrentPlayer()).thenReturn(player);
        gameControllerMiddleware.setGameStatus(GameStatusEnum.PLACE_CARD);
        gameControllerMiddleware.switchCardSide("player0", null);
        gameControllerMiddleware.setGameStatus(GameStatusEnum.DRAW_CARD);
        assertThrows(IllegalStateException.class, () -> gameControllerMiddleware.switchCardSide("player0", null));
    }

    @Test
    @DisplayName("Test if a player's objective can be set only when game status is INIT_CHOOSE_OBJECTIVE_CARD")
    void setPlayerObjective() {
        gameControllerMiddleware.setGameStatus(GameStatusEnum.INIT_CHOOSE_OBJECTIVE_CARD);
        gameControllerMiddleware.setPlayerObjective("player0", null);
        gameControllerMiddleware.setGameStatus(GameStatusEnum.DRAW_CARD);
        assertThrows(IllegalStateException.class, () -> gameControllerMiddleware.setPlayerObjective("player0", null));
    }

    @Test
    @DisplayName("Test if the game status changes to PLACE_CARD when the player is the last one")
    void handleDrawFinish() {
        when(game.getCurrentPlayerIndex()).thenReturn(0);
        gameControllerMiddleware.setGameStatus(GameStatusEnum.INIT_CHOOSE_OBJECTIVE_CARD);
        gameControllerMiddleware.setPlayerObjective("player0", null);
        assertEquals(GameStatusEnum.PLACE_CARD, gameControllerMiddleware.getGameStatus());
    }

    @Test
    @DisplayName("Test the general game flow")
    void generalGameFlow() {
        // Player 0 joins
        gameControllerMiddleware.joinGame("player0");
        assertEquals(GameStatusEnum.WAIT_FOR_PLAYERS, gameControllerMiddleware.getGameStatus());

        // We mock the game to started
        when(game.isStarted()).thenReturn(true);

        // Player 1 joins
        gameControllerMiddleware.joinGame("player1");
        assertEquals(GameStatusEnum.INIT_PLACE_STARTER_CARD, gameControllerMiddleware.getGameStatus());

        // -- Player 0 can't draw cards yet
        assertThrows(IllegalStateException.class, () -> gameControllerMiddleware.drawCardFromResourceDeck("player0"));

        // Player 0 can switch the starter card side
        gameControllerMiddleware.switchCardSide("player0", null);

        // Player 0 places his starter card
        gameControllerMiddleware.placeCard("player0", null, null);
        // Player 1 can't place his starter card yet
        assertThrows(IllegalStateException.class, () -> gameControllerMiddleware.placeCard("player1", null, null));

        // Player 0 places his starter card
        gameControllerMiddleware.setPlayerObjective("player0", null);

        // Player 0 can't place his objective card anymore
        assertThrows(IllegalStateException.class, () -> gameControllerMiddleware.placeCard("player0", null, null));

        // Player 0 can't choose his objective card anymore
        assertThrows(IllegalStateException.class, () -> gameControllerMiddleware.setPlayerObjective("player0", null));

        assertEquals(GameStatusEnum.INIT_PLACE_STARTER_CARD, gameControllerMiddleware.getGameStatus());
        // -- Player 1 can't draw cards yet
        assertThrows(IllegalStateException.class, () -> gameControllerMiddleware.drawCardFromGoldDeck("player1"));

        // Player 1 places his starter card
        gameControllerMiddleware.placeCard("player1", null, null);
        // Player 0 can't place his starter card
        assertThrows(IllegalStateException.class, () -> gameControllerMiddleware.placeCard("player0", null, null));

        // Player 1 chooses his objective card
        gameControllerMiddleware.setPlayerObjective("player1", null);

        // Init phase is over
        assertEquals(GameStatusEnum.PLACE_CARD, gameControllerMiddleware.getGameStatus());

        // Player 0 plays his first real turn
        gameControllerMiddleware.placeCard("player0", null, null);
        assertEquals(GameStatusEnum.DRAW_CARD, gameControllerMiddleware.getGameStatus());
        // Player 1 can draw a card
        gameControllerMiddleware.drawCardFromField("player0", null);

        // Player 2 places a card
        gameControllerMiddleware.placeCard("player1", null, null);
        assertEquals(GameStatusEnum.DRAW_CARD, gameControllerMiddleware.getGameStatus());

        // We mock the game to be in the last round
        when(game.isLastRound()).thenReturn(true);
        // Player 2 can draw a card
        gameControllerMiddleware.drawCardFromField("player1", null);
        // It is the last round phase. We have to play
        // Player 0 can place a card
        gameControllerMiddleware.placeCard("player0", null, null);
        // Player 0 can draw a card
        gameControllerMiddleware.drawCardFromField("player0", null);
        // Player 1 can place a card
        gameControllerMiddleware.placeCard("player1", null, null);
        // Player 1 can place a card
        gameControllerMiddleware.drawCardFromField("player1", null);
        // Game is over
        assertEquals(GameStatusEnum.GAME_OVER, gameControllerMiddleware.getGameStatus());
    }
    // TODO: Add test for game end with 3 or 4 players.
}