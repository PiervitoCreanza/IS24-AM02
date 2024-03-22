package it.polimi.ingsw.model;

/**
 * Represents a FrontGoldCard, a specialized version of the Front card that includes
 * a store for needed items. This class extends Front and adds functionality to manage
 * needed items for the card.
 */
public class FrontGoldCard extends Front {

    /**
     * Store for the needed items specific to this FrontGoldCard.
     */
    private final GameItemStore neededItems;

    /**
     * Constructs a FrontGoldCard with specified corners, points, and needed items.
     *
     * @param topRight    The top right corner of the front side.
     * @param topLeft     The top left corner of the front side.
     * @param bottomLeft  The bottom left corner of the front side.
     * @param bottomRight The bottom right corner of the front side.
     * @param points      The number of points attributed to this front side.
     * @param neededItems The store of needed items for this card.
     */
    public FrontGoldCard(Corner topRight, Corner topLeft, Corner bottomLeft, Corner bottomRight, int points, GameItemStore neededItems) {
        super(topRight, topLeft, bottomLeft, bottomRight, points);
        this.neededItems = neededItems;
    }

    /**
     * Gets the needed items store for this FrontGoldCard.
     *
     * @return The GameItemStore representing the needed items for this card.
     */
    public GameItemStore getNeededItems() {
        return neededItems;
    }

    /**
     * Overrides the getNeededItemStore method from the Front class.
     * Returns the needed items specific to this FrontGoldCard.
     *
     * @return The GameItemStore representing the needed items for this FrontGoldCard.
     */
    @Override
    public GameItemStore getNeededItemStore() {
        return neededItems;
    }
}
