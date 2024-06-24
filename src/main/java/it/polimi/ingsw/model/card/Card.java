package it.polimi.ingsw.model.card;

/**
 * The Card interface represents a card in the game.
 * Each card has a unique identifier, represented by an integer.
 */
public interface Card {
    /**
     * Gets the unique identifier of the card.
     *
     * @return the card's unique identifier
     */
    int getCardId();
}