package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainControllerTest {

    private MainController mainController;

    @BeforeEach
    void setUp() {
        mainController = new MainController();
    }

    @Test
    @DisplayName("Test for createGame() and getGames()")
    void createGame() {
        Game game = mainController.createGame("gameName", "playerName", 2);
        assertTrue(mainController.getGames().contains(game));
        assertEquals(1, mainController.getGames().size());
        assertThrows(IllegalArgumentException.class, () -> mainController.createGame("gameName", "playerName", 2));
    }

    @Test
    @DisplayName("Test for deleteGame()")
    void deleteGame() {
        Game game = mainController.createGame("gameName", "playerName", 2);
        assertTrue(mainController.getGames().contains(game));
        assertEquals(1, mainController.getGames().size());

        mainController.deleteGame("gameName");
        assertEquals(0, mainController.getGames().size());
        assertThrows(IllegalArgumentException.class, () -> mainController.deleteGame("gameName"));
    }

    @Test
    @DisplayName("Test for deleteGame() with player creator check")
    void deleteGameWithPlayerNameCHeck() {
        Game game = mainController.createGame("gameName", "playerName", 2);
        mainController.joinGame("gameName", "player2");
        assertTrue(mainController.getGames().contains(game));
        assertEquals(1, mainController.getGames().size());

        String errorMessage = assertThrows(IllegalArgumentException.class, () -> mainController.deleteGame("gameName", "player2")).getMessage();
        assertEquals("Only the creator of the game can delete it", errorMessage);
        mainController.deleteGame("gameName", "playerName");
        assertEquals(0, mainController.getGames().size());
        assertThrows(IllegalArgumentException.class, () -> mainController.deleteGame("gameName", ""));
    }

    @Test
    @DisplayName("Test for joinGame()")
    void joinGame() {
        Game game = mainController.createGame("gameName", "playerName", 2);
        mainController.joinGame("gameName", "playerName2");
        assertEquals(2, game.getPlayers().size());
        assertThrows(IllegalStateException.class, () -> mainController.joinGame("gameName", "playerName3"));
        assertThrows(IllegalArgumentException.class, () -> mainController.joinGame("gameName1", "playerName3"));
    }
}