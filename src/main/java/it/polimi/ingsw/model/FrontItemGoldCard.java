package it.polimi.ingsw.model;

/**
 * The {@code FrontItemGoldCard} class represents a gold card with a specific item multiplier feature.
 * This class extends {@code FrontGoldCard}, adding a property to manage a game item
 * that acts as a multiplier. This unique feature is intended to modify the points calculation or
 * other game mechanics.
 */
public class FrontItemGoldCard extends FrontGoldCard {
    /**
     * A game item that acts as a multiplier for this gold card.
     */
    private GameItemEnum multiplier;

    /**
     * Constructs a new {@code FrontItemGoldCard} with specified corners, points, and a game item multiplier.
     * Inherits the behavior of {@code FrontGoldCard} including the management of needed game items.
     *
     * @param topRight    The top right corner of the front item gold card.
     * @param topLeft     The top left corner of the front item gold card.
     * @param bottomLeft  The bottom left corner of the front item gold card.
     * @param bottomRight The bottom right corner of the front item gold card.
     * @param points      The points associated with this front item gold card.
     * @param multiplier  The game item that acts as a multiplier for this gold card.
     */
    public FrontItemGoldCard(Corner topRight, Corner topLeft, Corner bottomLeft, Corner bottomRight, int points, GameItemEnum multiplier) {
        super(topRight, topLeft, bottomLeft, bottomRight, points);
        this.multiplier = multiplier;
    }

    //TODO: Implement getPoints(PlayerBoard) method with @Override annotation, which is inherited from the superclass.
}
