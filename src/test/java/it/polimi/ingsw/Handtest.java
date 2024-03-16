package it.polimi.ingsw;

import it.polimi.ingsw.model.Hand;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class HandTest {

    // Define a placeholder class to simulate GameCard
    private static class GameCard {
        // Define any necessary attributes and methods for testing purposes
    }

    @Test
    public void testAddCard() {
        Hand hand = new Hand();
        GameCard card = new GameCard();
        hand.addCard(card);
        ArrayList<GameCard> cards = hand.getCards();
        assertEquals(1, cards.size());
        assertTrue(cards.contains(card));
    }

    @Test
    public void testRemoveCard() {
        Hand hand = new Hand();
        GameCard card = new GameCard(); // Placeholder object
        hand.addCard(card);
        hand.removeCard(card);
        ArrayList<GameCard> cards = hand.getCards();
        assertEquals(0, cards.size());
        assertFalse(cards.contains(card));
    }

    @Test
    public void testHandFullException() {
        Hand hand = new Hand();
        GameCard card1 = new GameCard(); // Placeholder object
        GameCard card2 = new GameCard(); // Placeholder object
        GameCard card3 = new GameCard(); // Placeholder object
        hand.addCard(card1);
        hand.addCard(card2);
        hand.addCard(card3);
        assertThrows(IllegalArgumentException.class, () -> hand.addCard(new GameCard()));
    }
}


