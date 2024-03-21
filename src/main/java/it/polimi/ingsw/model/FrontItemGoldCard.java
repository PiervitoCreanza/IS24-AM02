package it.polimi.ingsw.model;

import java.awt.*;

/**
 * Represents a variant of FrontGoldCard known as FrontItemGoldCard. This class extends
 * FrontGoldCard and specializes in calculating points based on the quantity of a specific
 * game item on the player's board.
 */
public class FrontItemGoldCard extends FrontGoldCard {

    /**
     * The game item that acts as a multiplier for calculating points.
     */
    private final GameItemEnum multiplier;

    /**
     * Constructs a FrontItemGoldCard object with specified corners, points, and a game item multiplier.
     *
     * @param topRight    The top right corner of the front side.
     * @param topLeft     The top left corner of the front side.
     * @param bottomLeft  The bottom left corner of the front side.
     * @param bottomRight The bottom right corner of the front side.
     * @param points      The number of points attributed to this front side.
     * @param multiplier  The game item that will be used as a multiplier for point calculation.
     */

    public FrontItemGoldCard(Corner topRight, Corner topLeft, Corner bottomLeft, Corner bottomRight, int points, GameItemStore neededItems, GameItemEnum multiplier) {
        super(topRight, topLeft, bottomLeft, bottomRight, points, neededItems);
        this.multiplier = multiplier;
    }

    /**
     * Calculates and returns the points for this FrontItemGoldCard based on the quantity
     * of a specific game item on the player's board.
     *
     * @param cardPosition The position of the card on the player's board.
     * @param playerBoard  The player's board.
     * @return The calculated points based on the quantity of the specified game item on the player's board.
     */
    @Override
    public int getPoints(Point cardPosition, PlayerBoard playerBoard){
        // The points are multiplied by the amount of the specified game item on the player's board.
        return playerBoard.getGameItemAmount(multiplier) * points;
    }
}
