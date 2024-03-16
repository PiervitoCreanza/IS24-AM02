package it.polimi.ingsw;

import it.polimi.ingsw.model.GameCard;
import it.polimi.ingsw.model.Hand;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class HandTest {

    private Hand hand;

    @BeforeEach
    public void setup() {
        hand = new Hand();
    }

    @Test
    public void testAddCard() {
        GameCard card = new GameCard();
        hand.addCard(card);
        ArrayList<GameCard> cards = hand.getCards();
        assertEquals(1, cards.size());
        assertTrue(cards.contains(card));
    }

    @Test
    public void testRemoveCard() {
        GameCard card = new GameCard();
        hand.addCard(card);
        hand.removeCard(card);
        ArrayList<GameCard> cards = hand.getCards();
        assertEquals(0, cards.size());
        assertFalse(cards.contains(card));
    }

    @Test
    public void testHandFullException() {
        GameCard card1 = new GameCard();
        GameCard card2 = new GameCard();
        GameCard card3 = new GameCard();
        hand.addCard(card1);
        hand.addCard(card2);
        hand.addCard(card3);
        assertThrows(IllegalArgumentException.class, () -> hand.addCard(new GameCard()));
    }
}


