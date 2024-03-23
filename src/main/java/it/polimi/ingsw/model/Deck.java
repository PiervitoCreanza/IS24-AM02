package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents a deck of cards in the game.
 * It contains a list of cards and provides a method to draw a card from the deck.
 * @param <T> The type of cards in the deck (GameCard or ObjectiveCard)
 */
public class Deck<T> {
    private final ArrayList<T> deck;
    private final Random random;

    /**
     * Constructor for Deck. Initializes the deck.
     */
    public Deck() {
        this.deck = new ArrayList<>();
        this.random = new Random();
    }

    /**
     * Checks if the deck is empty.
     * @return true if the deck is empty, false otherwise.
     */
    public boolean isEmpty() {
        return deck.isEmpty();
    }

    /**
     * Draws a random card from the deck. The card is removed from the deck.
     * @return The card drawn from the deck.
     */
    public T draw(){
        return deck.remove(random.nextInt(deck.size()));
    }
}
