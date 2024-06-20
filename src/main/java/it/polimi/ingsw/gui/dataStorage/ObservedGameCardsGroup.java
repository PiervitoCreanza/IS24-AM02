package it.polimi.ingsw.gui.dataStorage;

import javafx.scene.Node;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Set;

/**
 * This class represents a group of observed game cards.
 * It maintains a mapping between card names and their corresponding observed game card objects.
 * It also provides methods to add, retrieve, and clear cards.
 */
public class ObservedGameCardsGroup {
    /**
     * A map to store observed game cards with their names as keys.
     */
    private final HashMap<String, ObservedGameCard> cardCells;

    /**
     * The root node of the JavaFX scene graph.
     */
    private final Node root;

    /**
     * Constructs a new ObservedGameCardsGroup with the specified root node.
     *
     * @param root the root node of the JavaFX scene graph
     */
    public ObservedGameCardsGroup(Node root) {
        this.root = root;
        this.cardCells = new HashMap<>();
    }

    /**
     * Adds a new card to the group.
     *
     * @param cardName the name of the card
     * @param card     the observed game card to be added
     */
    public void addCard(String cardName, ObservedGameCard card) {
        cardCells.put(cardName, card);
    }

    /**
     * Adds a new card to the group by creating a new ObservedGameCard from an ImageView.
     * The ImageView is looked up in the scene graph by its name.
     *
     * @param boundImageViewName the name of the ImageView in the scene graph
     */
    public void addCard(String boundImageViewName) {
        cardCells.put(boundImageViewName, new ObservedGameCard((ImageView) root.lookup("#" + boundImageViewName)));
    }

    /**
     * Retrieves an observed game card by its name.
     *
     * @param cardName the name of the card
     * @return the observed game card with the specified name, or null if no such card exists
     */
    public ObservedGameCard getCard(String cardName) {
        return cardCells.get(cardName);
    }

    /**
     * Retrieves all observed game cards in the group.
     *
     * @return a set of all observed game cards in the group
     */
    public Set<ObservedGameCard> getCards() {
        return Set.copyOf(cardCells.values());
    }

    /**
     * Retrieves the names of all observed game cards in the group.
     *
     * @return a set of the names of all observed game cards in the group
     */
    public Set<String> getCardNames() {
        return Set.copyOf(cardCells.keySet());
    }

    /**
     * Removes all observed game cards from the group.
     */
    public void clear() {
        cardCells.clear();
    }
}