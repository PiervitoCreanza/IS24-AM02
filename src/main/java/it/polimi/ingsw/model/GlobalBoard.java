package it.polimi.ingsw.model;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.network.virtualView.GlobalBoardView;
import it.polimi.ingsw.network.virtualView.VirtualViewable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class that represents the global board of the game.
 * It contains decks of different types of cards and the cards present on the field.
 * It also contains the two global objectives.
 */
public class GlobalBoard implements VirtualViewable<GlobalBoardView> {

    /**
     * Represents the deck of gold cards in the game.
     * This is a Deck object that contains all the gold cards in the game.
     */
    private final Deck<GameCard> goldDeck;

    /**
     * Represents the deck of resource cards in the game.
     * This is a Deck object that contains all the resource cards in the game.
     */
    private final Deck<GameCard> resourceDeck;

    /**
     * Represents the deck of objective cards in the game.
     * This is a Deck object that contains all the objective cards in the game.
     */
    private final Deck<ObjectiveCard> objectiveDeck;

    /**
     * Represents the deck of starter cards in the game.
     * This is a Deck object that contains all the starter cards in the game.
     */
    private final Deck<GameCard> starterDeck;

    /**
     * Represents the two global objectives of the game.
     * This is an ArrayList that contains the two ObjectiveCard objects that represent the global objectives of the game.
     */
    private ArrayList<ObjectiveCard> globalObjectives;

    /**
     * Represents the two gold cards present on the field.
     * This is an ArrayList that contains the two GameCard objects that represent the gold cards present on the field.
     */
    private final ArrayList<GameCard> fieldGoldCards;

    /**
     * Represents the two resource cards present on the field.
     * This is an ArrayList that contains the two GameCard objects that represent the resource cards present on the field.
     */
    private final ArrayList<GameCard> fieldResourceCards;

    /**
     * Constructor for GlobalBoard. Initializes the decks and draws cards for the field and for the objectives.
     */
    public GlobalBoard() {
        Parser parser = new Parser();
        this.goldDeck = parser.getGoldDeck();
        this.resourceDeck = parser.getResourceDeck();
        this.objectiveDeck = parser.getObjectiveDeck();
        this.starterDeck = parser.getStarterDeck();
        this.globalObjectives = new ArrayList<>(List.of(this.objectiveDeck.draw(), this.objectiveDeck.draw()));
        this.fieldGoldCards = new ArrayList<>(List.of(this.goldDeck.draw(), this.goldDeck.draw()));
        this.fieldResourceCards = new ArrayList<>(List.of(this.resourceDeck.draw(), this.resourceDeck.draw()));
    }

    /**
     * This constructor is used to create a new instance of GlobalBoard with the provided decks.
     * It initializes the decks with the provided ones and draws cards for the field and for the objectives.
     * It is only used for testing purpose.
     *
     * @param goldDeck      The deck of gold cards to be used in the game.
     * @param resourceDeck  The deck of resource cards to be used in the game.
     * @param objectiveDeck The deck of objective cards to be used in the game.
     * @param starterDeck   The deck of starter cards to be used in the game.
     */
    public GlobalBoard(Deck<GameCard> goldDeck, Deck<GameCard> resourceDeck, Deck<ObjectiveCard> objectiveDeck, Deck<GameCard> starterDeck) {
        this.goldDeck = goldDeck;
        this.resourceDeck = resourceDeck;
        this.objectiveDeck = objectiveDeck;
        this.starterDeck = starterDeck;
        this.globalObjectives = new ArrayList<>(List.of(this.objectiveDeck.draw(), this.objectiveDeck.draw()));
        this.fieldGoldCards = new ArrayList<>(List.of(this.goldDeck.draw(), this.goldDeck.draw()));
        this.fieldResourceCards = new ArrayList<>(List.of(this.resourceDeck.draw(), this.resourceDeck.draw()));
    }

    /**
     * Returns the deck of gold cards.
     *
     * @return The deck of gold cards.
     */
    public Deck<GameCard> getGoldDeck() {
        return goldDeck;
    }

    /**
     * Returns the deck of resource cards.
     *
     * @return The deck of resource cards.
     */
    public Deck<GameCard> getResourceDeck() {
        return resourceDeck;
    }

    /**
     * Returns the deck of objective cards.
     *
     * @return The deck of objective cards.
     */
    public Deck<ObjectiveCard> getObjectiveDeck() {
        return objectiveDeck;
    }

    /**
     * Returns the deck of starter cards.
     *
     * @return The deck of starter cards.
     */
    public Deck<GameCard> getStarterDeck() {
        return starterDeck;
    }

    /**
     * Returns the two global objectives.
     *
     * @return An ArrayList of ObjectiveCard containing the two objectives.
     */
    public ArrayList<ObjectiveCard> getGlobalObjectives() {
        return globalObjectives;
    }

    /**
     * Returns the gold cards present on the field.
     *
     * @return An ArrayList of GameCard containing the two gold cards present on the field.
     */
    public ArrayList<GameCard> getFieldGoldCards() {
        return fieldGoldCards;
    }

    /**
     * Returns the resource cards present on the field.
     *
     * @return An ArrayList of GameCard containing the two resource cards present on the field.
     */
    public ArrayList<GameCard> getFieldResourceCards() {
        return fieldResourceCards;
    }

    /**
     * Checks if the gold deck is empty.
     *
     * @return true if the gold deck is empty, false otherwise.
     */
    public boolean isGoldDeckEmpty() {
        return goldDeck.isEmpty();
    }

    /**
     * Checks if the resource deck is empty.
     *
     * @return true if the resource deck is empty, false otherwise.
     */
    public boolean isResourceDeckEmpty() {
        return resourceDeck.isEmpty();
    }

    /**
     * Draws a card from the field. If the card is in the field of gold cards, it is removed and a new card is drawn from the gold deck.
     * If the card is in the field of resource cards, it is removed and a new card is drawn from the resource deck.
     *
     * @param cardId The id of card to draw from the field.
     * @return The card that has been drawn.
     * @throws IllegalArgumentException if the card is not present on the field.
     */
    public GameCard drawCardFromField(int cardId) {
        Optional<GameCard> cardToRemove = fieldGoldCards.stream().filter(c -> c.getCardId() == cardId).findFirst();
        if (cardToRemove.isPresent()) {
            fieldGoldCards.remove(cardToRemove.get());
            if (!goldDeck.isEmpty())
                fieldGoldCards.add(goldDeck.draw());
            return cardToRemove.get();
        }

        cardToRemove = fieldResourceCards.stream().filter(c -> c.getCardId() == cardId).findFirst();
        if (cardToRemove.isPresent()) {
            fieldResourceCards.remove(cardToRemove.get());
            if (!resourceDeck.isEmpty())
                fieldResourceCards.add(resourceDeck.draw());
            return cardToRemove.get();
        }

        throw new IllegalArgumentException("This card is not present on the field");
    }

    public void setGlobalObjectives(ArrayList<ObjectiveCard> globalObjectives) {
        this.globalObjectives = globalObjectives;
    }

    /**
     * Returns the virtual view of the global board.
     *
     * @return The virtual view of the global board.
     */
    @Override
    public GlobalBoardView getVirtualView() {
        return new GlobalBoardView(goldDeck.getFirst(), resourceDeck.getFirst(), fieldGoldCards, fieldResourceCards, globalObjectives);
    }

    /**
     * Checks if the field and the decks are empty.
     *
     * @return true if the field and the decks are empty, false otherwise.
     */
    public boolean areFieldAndDecksEmpty() {
        return isGoldDeckEmpty() && isResourceDeckEmpty() && fieldGoldCards.isEmpty() && fieldResourceCards.isEmpty();
    }
}
