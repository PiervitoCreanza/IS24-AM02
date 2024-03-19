package it.polimi.ingsw.model;

/**
 * The {@code FrontGoldCard} class represents a specialized type of front side,
 * specifically for a gold card in the game. It extends the {@code Front} class
 * with additional functionality to manage game items needed by the gold card.
 */
public class FrontGoldCard extends Front {

    /**
     * A store of game items required by this gold card.
     */
    private GameItemStore neededItems;

    /**
     * Constructs a new {@code FrontGoldCard} with specified corners, points, and an empty
     * store for needed items.
     *
     * @param topRight    The top right corner of the front gold card.
     * @param topLeft     The top left corner of the front gold card.
     * @param bottomLeft  The bottom left corner of the front gold card.
     * @param bottomRight The bottom right corner of the front gold card.
     * @param points      The points associated with this front gold card.
     */
    public FrontGoldCard(Corner topRight, Corner topLeft, Corner bottomLeft, Corner bottomRight, int points) {
        super(topRight, topLeft, bottomLeft, bottomRight, points);
        // The neededItems field is initialized as null or empty, as appropriate.
    }

    /**
     * Returns the game item store that contains the items needed by this gold card.
     *
     * @return The game item store required by this gold card.
     */
    public GameItemStore getNeededItems() {
        return neededItems;
    }

    /**
     * Sets the game item store that contains the items needed by this gold card.
     *
     * @param neededItems The game item store to set for this gold card.
     */
    public void setNeededItems(GameItemStore neededItems) {
        this.neededItems = neededItems;
    }

    //TODO: Implement getNeededItemStore() method with @Override annotation.
}
