package it.polimi.ingsw.model.card.gameCard.front.goldCard;

import it.polimi.ingsw.model.card.gameCard.front.FrontGameCard;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import it.polimi.ingsw.model.card.corner.Corner;

/**
 * Represents a FrontGoldGameCard, a specialized version of the FrontGameCard card that includes
 * a store for needed items. This class extends FrontGameCard and adds functionality to manage
 * needed items for the card.
 */
public class FrontGoldGameCard extends FrontGameCard {

    /**
     * Store for the needed items specific to this FrontGoldGameCard.
     */
    private final GameItemStore neededItems;

    /**
     * Constructs a FrontGoldGameCard with specified corners, points, and needed items.
     *
     * @param topRight    The top right corner of the front side.
     * @param topLeft     The top left corner of the front side.
     * @param bottomLeft  The bottom left corner of the front side.
     * @param bottomRight The bottom right corner of the front side.
     * @param points      The number of points attributed to this front side.
     * @param neededItems The store of needed items for this card.
     */
    public FrontGoldGameCard(Corner topRight, Corner topLeft, Corner bottomLeft, Corner bottomRight, int points, GameItemStore neededItems) {
        super(topRight, topLeft, bottomLeft, bottomRight, points);
        this.neededItems = neededItems;
    }

    /**
     * Gets the needed items store for this FrontGoldGameCard.
     *
     * @return The GameItemStore representing the needed items for this card.
     */
    public GameItemStore getNeededItems() {
        return neededItems;
    }

    /**
     * Overrides the getNeededItemStore method from the FrontGameCard class.
     * Returns the needed items specific to this FrontGoldGameCard.
     *
     * @return The GameItemStore representing the needed items for this FrontGoldGameCard.
     */
    @Override
    public GameItemStore getNeededItemStore() {
        return neededItems;
    }
}
