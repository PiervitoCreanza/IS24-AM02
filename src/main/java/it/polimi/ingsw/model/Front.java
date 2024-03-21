package it.polimi.ingsw.model;

/**
 * Represents the front side of a game card. This class extends the Side class and
 * incorporates functionalities for managing points and aggregating game items from corners.
 */
public class Front extends Side {

    /**
     * The number of points associated with this front side of the card.
     */
    protected int points;

    /**
     * Constructs a Front object with specified corners and points.
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
     * Overrides the getGameItemStore method from the Side class.
     * This implementation aggregates game items from all corners of this front side into a GameItemStore.
     *
     * @return A GameItemStore containing game items from all corners of this front side.
     */
    @Override
    public GameItemStore getGameItemStore() {
        return getCornersItems();
    }

    /**
     * Calculates and returns the points for this front side of the card.
     * This method simply returns the static points value set for this front side.
     *
     * @param cardPosition The position of the card on the player's board.
     * @param playerBoard  The player's board.
     * @return The points attributed to this front side of the card.
     */
    @Override
    public int getPoints(Coordinate cardPosition, PlayerBoard playerBoard) {
        return points;
    }

}
