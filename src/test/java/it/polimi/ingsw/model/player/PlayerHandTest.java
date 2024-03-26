package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.PlayerHand;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class PlayerHandTest {

    private PlayerHand playerHand;
    private GameCard card;

    @BeforeEach
    public void setup() {
        card = mock(GameCard.class);
        playerHand = new PlayerHand();
    }

    @Test
    public void testAddCard() {
        playerHand.addCard(card);
        ArrayList<GameCard> cards = playerHand.getCards();
        assertEquals(1, cards.size());
        assertTrue(cards.contains(card));
    }

    @Test
    public void testRemoveCard() {
        playerHand.addCard(card);
        playerHand.removeCard(card);
        ArrayList<GameCard> cards = playerHand.getCards();
        assertEquals(0, cards.size());
        assertFalse(cards.contains(card));
    }

    @Test
    public void testHandFullException() {
        GameCard card1 = mock(GameCard.class);
        GameCard card2 = mock(GameCard.class);
        GameCard card3 = mock(GameCard.class);
        playerHand.addCard(card1);
        playerHand.addCard(card2);
        playerHand.addCard(card3);
        assertThrows(IllegalArgumentException.class, () -> playerHand.addCard(card));
    }
}


