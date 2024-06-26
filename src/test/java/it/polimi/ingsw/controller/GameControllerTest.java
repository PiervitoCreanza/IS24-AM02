package it.polimi.ingsw.controller;


import it.polimi.ingsw.Utils;
import it.polimi.ingsw.controller.gameController.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.gameCard.SideGameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.parsing.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    GameController gameController;
    Game game;
    Parser parser;
    Utils utils = new Utils();

    @BeforeEach
    void setUp() {
        gameController = new GameController("TestGame", 2, "player1");
        game = gameController.getGame();
        parser = new Parser();
    }

    @Test
    @DisplayName("Should add player to the game when joinGame is called")
    void shouldAddPlayerWhenJoinGameIsCalled() {
        gameController.joinGame("player2");
        assertEquals("player2", game.getPlayer("player2").getPlayerName());
    }

    @Test
    @DisplayName("Should place card on player's board when placeCard is called")
    void shouldPlaceCardWhenPlaceCardIsCalled() {
        GameCard card = gameController.getGame().getCurrentPlayer().getPlayerBoard().getStarterCard();
        Coordinate coordinate = new Coordinate(0, 0);
        gameController.placeCard("player1", coordinate, card.getCardId());
        assertEquals(card, game.getPlayer("player1").getPlayerBoard().getGameCard(coordinate).get());
    }

    @Test
    @DisplayName("Should draw card from field and add to player's hand when drawCardFromField is called")
    void shouldDrawCardFromFieldWhenDrawCardFromFieldIsCalled() {
        GameCard card = Mockito.mock(GameCard.class);
        utils.assertIllegalArgument("This card is not present on the field", () -> gameController.drawCardFromField("player1", card.getCardId()));
        GameCard fieldResourceCard = game.getGlobalBoard().getFieldResourceCards().getFirst();
        gameController.drawCardFromField("player1", fieldResourceCard.getCardId());
        assertTrue(game.getPlayer("player1").getPlayerHand().getCards().contains(fieldResourceCard));
    }

    @Test
    @DisplayName("Should draw card from resource deck and add to player's hand when drawCardFromResourceDeck is called")
    void shouldDrawCardFromResourceDeckWhenDrawCardFromResourceDeckIsCalled() {
        assertEquals(0, game.getPlayer("player1").getPlayerHand().getCards().size());
        gameController.drawCardFromResourceDeck("player1");
        assertEquals(1, game.getPlayer("player1").getPlayerHand().getCards().size());
    }

    @Test
    @DisplayName("Should draw card from gold deck and add to player's hand when drawCardFromGoldDeck is called")
    void shouldDrawCardFromGoldDeckWhenDrawCardFromGoldDeckIsCalled() {
        assertEquals(0, game.getPlayer("player1").getPlayerHand().getCards().size());
        gameController.drawCardFromGoldDeck("player1");
        assertEquals(1, game.getPlayer("player1").getPlayerHand().getCards().size());
    }

    @Test
    @DisplayName("Should set player's objective when setPlayerObjective is called")
    void shouldSetPlayerObjectiveWhenSetPlayerObjectiveIsCalled() {
        ObjectiveCard card = game.getPlayer("player1").getChoosableObjectives().getFirst();
        gameController.setPlayerObjective("player1", card.getCardId());
        assertEquals(card, game.getPlayer("player1").getObjectiveCard());
    }

    @Test
    @DisplayName("Should throw exception when card is not in player hand")
    void switchCardSideShouldThrowExceptionWhenCardIsNotInHand() {
        // Draw a card from the resource deck (The player has no cards in hand)
        int fakeCardId = -1;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> gameController.switchCardSide("player1", fakeCardId));
        assertEquals("Card cannot be switched. Not present in player's hand", exception.getMessage());

    }

    @Test
    @DisplayName("Should switch card side when switchCardSide is called")
    void shouldSwitchCardSideWhenSwitchCardSideIsCalled() {
        // Draw a card from the resource deck (The player has no cards in hand)
        gameController.drawCardFromResourceDeck("player1");
        GameCard card = game.getPlayer("player1").getPlayerHand().getCards().getFirst();
        SideGameCard side = card.getCurrentSide();
        gameController.switchCardSide("player1", card.getCardId());
        assertNotEquals(side, card.getCurrentSide());
        gameController.switchCardSide("player1", card.getCardId());
        assertEquals(side, card.getCurrentSide());
    }

    @Test
    @DisplayName("Should set player's color when choosePlayerColor is called")
    void shouldSetPlayerColorWhenChoosePlayerColorIsCalled() {
        gameController.choosePlayerColor("player1", PlayerColorEnum.RED);
        assertEquals(PlayerColorEnum.RED, game.getPlayer("player1").getPlayerColor());
    }
}