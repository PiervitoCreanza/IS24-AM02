package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * The Hand class represents a player's hand in the game.
 * It contains a list of GameCard objects, with methods to add and remove cards.
 * A player's hand can hold up to 3 cards.
 */
public class Hand {
    /**
     * The list of GameCard objects representing the hand of a player.
     * This list is initialized with a capacity of 3, as a player can hold up to 3 cards in their hand.
     */
    private final ArrayList<GameCard> hand;

    public Hand() {
        hand = new ArrayList<GameCard>(3);
    }

    /**
     * This method is used to get the cards in the hand.
     *
     * @return ArrayList<GameCard> This returns the list of cards in the hand.
     */
    public ArrayList<GameCard> getCards() {
        return hand;
    }

    /**
     * This method is used to add a card to the hand.
     *
     * @param card This is the card to be added to the hand.
     * @throws IllegalArgumentException This is thrown if the hand is full.
     */
    public void addCard(GameCard card) {
        if (hand.size() == 3) {
            throw new IllegalArgumentException("Hand is full");
        }
        hand.add(card);
    }

    /**
     * This method is used to remove a card from the hand.
     *
     * @param card This is the card to be removed from the hand.
     */
    public void removeCard(GameCard card) {
        hand.remove(card);
    }
}
