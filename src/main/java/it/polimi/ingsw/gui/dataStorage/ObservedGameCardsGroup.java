package it.polimi.ingsw.gui.dataStorage;

import javafx.scene.Node;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Set;

/**
 * Class representing a group of observed game cards.
 * It contains the logic for managing a group of game cards and their associated names.
 * It is used to manage a set of cards in the game hand.
 */
public class ObservedGameCardsGroup {
    /**
     * Map representing the game cards.
     * It maps card names to observed game cards.
     */
    private final HashMap<String, ObservedGameCard> cardCells;

    /**
     * Node representing the root of the game cards group.
     * This node is the parent of all the game cards in the group.
     */
    private final Node root;

    /**
     * Constructor for the ObservedGameCardsGroup class.
     * It initializes the root and the card cells.
     *
     * @param root the root node of the game cards group which contains all the game cards.
     */
    public ObservedGameCardsGroup(Node root) {
        this.root = root;
        this.cardCells = new HashMap<>();
    }

    /**
     * Method to add a card to the group.
     *
     * @param cardName the name of the card
     * @param card     the observed game card to add
     */
    public void addCard(String cardName, ObservedGameCard card) {
        cardCells.put(cardName, card);
    }

    /**
     * Method to add a card to the group by its image view name.
     *
     * @param boundImageViewName the name of the image view of the card
     */
    public void addCard(String boundImageViewName) {
        cardCells.put(boundImageViewName, new ObservedGameCard((ImageView) root.lookup("#" + boundImageViewName)));
    }

    /**
     * Method to get a card from the game cards group by its name.
     *
     * @param cardName the name of the card
     * @return the observed game card with the specified name
     */
    public ObservedGameCard getCard(String cardName) {
        return cardCells.get(cardName);
    }

    /**
     * Method to get all the cards in the game cards group.
     *
     * @return a set of all the observed game cards in the group
     */
    public Set<ObservedGameCard> getCards() {
        return Set.copyOf(cardCells.values());
    }

    /**
     * Method to get all the card names in the game cards group.
     *
     * @return a set of all the card names in the group
     */
    public Set<String> getCardNames() {
        return Set.copyOf(cardCells.keySet());
    }

    /**
     * Method to clear all the cards in the game cards group.
     */
    public void clear() {
        cardCells.clear();
    }
}