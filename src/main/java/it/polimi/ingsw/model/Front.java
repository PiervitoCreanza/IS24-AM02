package it.polimi.ingsw.model;

/**
 * The {@code Front} class represents a front side of an item in the game.
 * It extends the {@code Side} class by adding a points attribute,
 * which represents the score associated with this front side.
 */
public class Front extends Side {

    /**
     * The points associated with this front side.
     */
    private int points;

    /**
     * Constructs a new {@code Front} with specified corners and points.
     * The corners define the boundaries of the front side, and the points
     * represent the score associated with it.
     *
     * @param topRight    The top right corner of the front side.
     * @param topLeft     The top left corner of the front side.
     * @param bottomLeft  The bottom left corner of the front side.
     * @param bottomRight The bottom right corner of the front side.
     * @param points      The points associated with this front side.
     */
    public Front(Corner topRight, Corner topLeft, Corner bottomLeft, Corner bottomRight, int points) {
        super(topRight, topLeft, bottomLeft, bottomRight);
        this.points = points;
    }

    /**
     * Returns the points associated with this front side.
     *
     * @return The points associated with this front side.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Sets the points associated with this front side.
     *
     * @param points The points to set for this front side.
     */
    public void setPoints(int points) {
        this.points = points;
    }

    //TODO: Implement getGameItemStore() and getPoints(PlayerBoard) methods with @Override annotations.
}
