package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * This class represents a deck of cards in the game.
 * It contains a list of cards and provides a method to draw a card from the deck.
 *
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
     * Constructor for Deck. Initializes the deck with the specified cards.
     *
     * @param cards The list of cards to initialize the deck with. This cannot be null.
     * @throws NullPointerException if the cards list is null.
     */
    public Deck(ArrayList<T> cards) {
        this.deck = new ArrayList<>(Objects.requireNonNull(cards));
        // Shuffle the deck.
        Collections.shuffle(deck);
    }

    /**
     * Checks if the deck is empty.
     *
     * @return true if the deck is empty, false otherwise.
     */
    public boolean isEmpty() {
        return deck.isEmpty();
    }

    /**
     * Draws a random card from the deck. The card is removed from the deck.
     * If the deck is empty, it throws a RuntimeException.
     *
     * @return The card drawn from the deck.
     * @throws RuntimeException if the deck is empty.
     */
    public T draw() {
        if (deck.isEmpty())
            throw new RuntimeException("The deck is empty");
        return deck.removeFirst();
    }

    /**
     * Returns the list of cards in the deck.
     *
     * @return The list of cards in the deck.
     */
    public ArrayList<T> getCards() {
        return deck;
    }

    /**
     * Returns the first card in the deck.
     *
     * @return The first card in the deck.
     */
    public T getFirst() {
        return deck.getFirst();
    }

    /**
     * Adds a new card to the deck.
     * This method first checks if the new card is null, throwing a NullPointerException if it is.
     * Then it checks if the new card already exists in the deck by comparing it to each card in the deck.
     * If the new card already exists in the deck, it throws an IllegalArgumentException.
     * If the new card does not exist in the deck, it is added to the deck.
     *
     * @param newCard The card to add to the deck. This cannot be null.
     * @throws NullPointerException     if the new card is null.
     * @throws IllegalArgumentException if the new card already exists in the deck.
     */
    public void addCard(T newCard) {
        Objects.requireNonNull(newCard, "Can't add a NULL card to deck");
        if (deck.stream().anyMatch(c -> c.equals(newCard))) {
            throw new IllegalArgumentException("Can't add a duplicate card to deck");
        }
        deck.add(newCard);
    }
}
