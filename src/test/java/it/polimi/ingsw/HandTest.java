package it.polimi.ingsw;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameCard;
import it.polimi.ingsw.model.Hand;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HandTest {

    private Hand hand;
    private GameCard card;

    @BeforeEach
    public void setup() {
        card = mock(GameCard.class);
        hand = new Hand();
    }

    @Test
    public void testAddCard() {
        hand.addCard(card);
        ArrayList<GameCard> cards = hand.getCards();
        assertEquals(1, cards.size());
        assertTrue(cards.contains(card));
    }

    @Test
    public void testRemoveCard() {
        hand.addCard(card);
        hand.removeCard(card);
        ArrayList<GameCard> cards = hand.getCards();
        assertEquals(0, cards.size());
        assertFalse(cards.contains(card));
    }

    @Test
    public void testHandFullException() {
        GameCard card1 = mock(GameCard.class);
        GameCard card2 = mock(GameCard.class);
        GameCard card3 = mock(GameCard.class);
        hand.addCard(card1);
        hand.addCard(card2);
        hand.addCard(card3);
        assertThrows(IllegalArgumentException.class, () -> hand.addCard(card));
    }
}


