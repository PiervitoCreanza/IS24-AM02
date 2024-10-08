package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.network.virtualView.PlayerHandView;
import it.polimi.ingsw.network.virtualView.VirtualViewable;

import java.util.ArrayList;

/**
 * The PlayerHand class represents a player's hand in the game.
 * It contains a list of GameCard objects, with methods to add and remove cards.
 * A player's hand can hold up to 3 cards.
 */
public class PlayerHand implements VirtualViewable<PlayerHandView> {
    /**
     * The list of GameCard objects representing the hand of a player.
     * This list is initialized with a capacity of 3, as a player can hold up to 3 cards in their hand.
     */
    private final ArrayList<GameCard> hand;

    /**
     * Constructor for PlayerHand. Initializes an empty list of GameCard objects.
     */
    public PlayerHand() {
        hand = new ArrayList<>(3);
    }

    /**
     * This method is used to get the cards in the hand.
     *
     * @return ArrayList of GameCard. This returns the list of cards in the hand.
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
            throw new IllegalArgumentException("PlayerHand is full");
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

    /**
     * This method is used to check if the hand contains a specific card.
     *
     * @param cardId This is the card to check for in the hand.
     * @return boolean This returns true if the hand contains the card, false otherwise.
     */
    public boolean containsCard(int cardId) {
        return hand.stream().anyMatch(c -> c.getCardId() == cardId);
    }

    /**
     * This method is used to get a card from the hand by its ID.
     *
     * @param cardId This is the ID of the card to get from the hand.
     * @return GameCard This returns the card with the specified ID, or null if the card is not in the hand.
     */
    public GameCard getById(int cardId) {
        return hand.stream().filter(c -> c.getCardId() == cardId).findFirst().orElse(null);
    }

    /**
     * This method is used to get the virtual view of the player's hand.
     *
     * @return PlayerHandView This returns the virtual view of the player's hand.
     */
    @Override
    public PlayerHandView getVirtualView() {
        return new PlayerHandView(hand);
    }
}
