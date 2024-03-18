package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents the global board of the game.
 * It contains decks of different types of cards and the cards present on the field.
 * It also contains the two global objectives.
 */
public class GlobalBoard {
    private final Deck<GameCard> goldDeck;
    private final Deck<GameCard> resourceDeck;
    private final Deck<ObjectiveCard> objectiveDeck;
    private final Deck<GameCard> starterDeck;
    private final ArrayList<ObjectiveCard> globalObjectives;
    private final ArrayList<GameCard> fieldGoldCards;
    private final ArrayList<GameCard> fieldResourceCards;

    /**
     * Constructor for GlobalBoard. Initializes the decks and draws cards for the field and for the objectives.
     * @param goldDeck The deck of gold cards.
     * @param resourceDeck The deck of resource cards.
     * @param objectiveDeck The deck of objective cards.
     * @param starterDeck The deck of starter cards.
     */
    public GlobalBoard(ArrayList<GameCard> goldDeck, ArrayList<GameCard> resourceDeck, ArrayList<ObjectiveCard> objectiveDeck, ArrayList<GameCard> starterDeck) {
        this.goldDeck = new Deck<>(goldDeck);
        this.resourceDeck = new Deck<>(resourceDeck);
        this.objectiveDeck = new Deck<>(objectiveDeck);
        this.starterDeck = new Deck<>(starterDeck);
        this.globalObjectives = new ArrayList<>(List.of(this.objectiveDeck.draw(), this.objectiveDeck.draw()));
        this.fieldGoldCards = new ArrayList<>(List.of(this.goldDeck.draw(), this.goldDeck.draw()));
        this.fieldResourceCards = new ArrayList<>(List.of(this.resourceDeck.draw(), this.resourceDeck.draw()));
    }

    /**
     * Returns the deck of gold cards.
     * @return The deck of gold cards.
     */
    public Deck<GameCard> getGoldDeck() {
        return goldDeck;
    }

    /**
     * Returns the deck of resource cards.
     * @return The deck of resource cards.
     */
    public Deck<GameCard> getResourceDeck() {
        return resourceDeck;
    }

    /**
     * Returns the deck of objective cards.
     * @return The deck of objective cards.
     */
    public Deck<ObjectiveCard> getObjectiveDeck() {
        return objectiveDeck;
    }

    /**
     * Returns the deck of starter cards.
     * @return The deck of starter cards.
     */
    public Deck<GameCard> getStarterDeck() {
        return starterDeck;
    }

    /**
     * Returns the two global objectives.
     * @return An ArrayList of ObjectiveCard containing the two objectives.
     */
    public ArrayList<ObjectiveCard> getGlobalObjectives() {
        return globalObjectives;
    }

    /**
     * Returns the gold cards present on the field.
     * @return An ArrayList of GameCard containing the two gold cards present on the field.
     */
    public ArrayList<GameCard> getFieldGoldCards() {
        return fieldGoldCards;
    }

    /**
     * Returns the resource cards present on the field.
     * @return An ArrayList of GameCard containing the two resource cards present on the field.
     */
    public ArrayList<GameCard> getFieldResourceCards() {
        return fieldResourceCards;
    }

    /**
     * Draws a card from the field. If the card is in the field of gold cards, it is removed and a new card is drawn from the gold deck.
     * If the card is in the field of resource cards, it is removed and a new card is drawn from the resource deck.
     * @param card The card to draw from the field.
     * @throws IllegalArgumentException if the card is not present on the field.
     */
    public void drawCardFromField(GameCard card){
        if (fieldGoldCards.contains(card)) {
            fieldGoldCards.remove(card);
            fieldGoldCards.add(goldDeck.draw());
        } else if (fieldResourceCards.contains(card)) {
            fieldResourceCards.remove(card);
            fieldResourceCards.add(resourceDeck.draw());
        } else {
            throw new IllegalArgumentException("This card is not present on the field");
        }
    }
}
