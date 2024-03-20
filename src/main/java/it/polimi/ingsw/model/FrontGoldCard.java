package it.polimi.ingsw.model;

/**
 * Represents a specific type of Front card known as FrontGoldCard. This class extends
 * the Front class and adds functionalities related to needed items for the card.
 */
public class FrontGoldCard extends Front {

    /**
     * Store for the needed items specific to this FrontGoldCard.
     */
    private GameItemStore neededItems;

    /**
     * Constructs a FrontGoldCard object with specified corners, points, and initializes
     * the needed items store.
     *
     * @param topRight    The top right corner of the front side.
     * @param topLeft     The top left corner of the front side.
     * @param bottomLeft  The bottom left corner of the front side.
     * @param bottomRight The bottom right corner of the front side.
     * @param points      The number of points attributed to this front side.
     */
    public FrontGoldCard(Corner topRight, Corner topLeft, Corner bottomLeft, Corner bottomRight, int points) {
        super(topRight, topLeft, bottomLeft, bottomRight, points);
        // The neededItems field is initialized as null or empty, as appropriate.
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
     * Sets the needed items store for this FrontGoldCard.
     *
     * @param neededItems The GameItemStore to be set as the needed items for this card.
     */
    public void setNeededItems(GameItemStore neededItems) {
        this.neededItems = neededItems;
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
