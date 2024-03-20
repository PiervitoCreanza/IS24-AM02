package it.polimi.ingsw.model;

import java.awt.Point;

/**
 * Represents the front side of a game card. This class is an extension of the Side class,
 * specialized with additional functionalities such as tracking points.
 */
public class Front extends Side {

    /**
     * The number of points associated with this front side.
     */
    protected int points;

    /**
     * Constructs a Front object with specified corners and points.
     * Initializes the corners and sets the points for this side.
     *
     * @param topRight    The top right corner of the front side.
     * @param topLeft     The top left corner of the front side.
     * @param bottomLeft  The bottom left corner of the front side.
     * @param bottomRight The bottom right corner of the front side.
     * @param points      The number of points attributed to this front side.
     */
    public Front(Corner topRight, Corner topLeft, Corner bottomLeft, Corner bottomRight, int points) {
        super(topRight, topLeft, bottomLeft, bottomRight);
        this.points = points;
    }

    /**
     * Sets the points for this front side of the card.
     *
     * @param points The new points value to be set.
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Gets the game item store for this front side of the card.
     * The method currently calls the superclass' getGameItemStore method.
     * Further specific implementations for the front side may be added here.
     *
     * @return A GameItemStore specific to this front side.
     */
    @Override
    public GameItemStore getGameItemStore() {
        GameItemStore gameItemStore = new GameItemStore();
        return super.getGameItemStore();
    }

    /**
     * Calculates and returns the points for this front side of the card.
     * The method signature includes the playerBoard parameter to maintain consistency
     * with the overriding method in the superclass, avoiding the use of method overloading.
     *
     * @param cardPosition The position of the card on the player's board.
     * @param playerBoard  The player's board. This parameter is included to maintain method signature consistency but is not used in the current implementation.
     * @return The points associated with this front side of the card.
     */
    @Override
    public int getPoints(Point cardPosition, PlayerBoard playerBoard) {
        return points;
    }

}
