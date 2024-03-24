package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents a deck of cards in the game.
 * It contains a list of cards and provides a method to draw a card from the deck.
 * @param <T> The type of cards in the deck (GameCard or ObjectiveCard)
 */
public class Deck<T> {

    /**
     * Represents the deck of cards in the game.
     * This is an ArrayList that contains all the cards in the deck.
     * The type of cards in the deck is determined by the generic type parameter T.
     */
    private final ArrayList<T> deck;

    /**
     * Used for generating random numbers.
     * This is used when drawing a card from the deck to ensure the card drawn is random.
     */
    private final Random random;

    /**
     * Constructor for Deck. Initializes the deck.
     */
    public Deck() {     //TODO
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
