package it.polimi.ingsw.model;

import java.awt.Point;
import java.util.stream.Stream;

/**
 * Represents the front side of a game card, extending the Side class.
 * This class is equipped to manage points and game items related to the front side of a card.
 */
public class Front extends Side {

    /**
     * The number of points associated with this front side.
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
     * Sets the points for this front side of the card.
     *
     * @param points The new points value to be set.
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Gets the game item store for this front side of the card.
     * The method creates a GameItemStore and populates it based on the game items in each corner of the front side.
     *
     * @return A GameItemStore containing game items from all corners of the front side.
     */
    @Override
    public GameItemStore getGameItemStore() {
        GameItemStore gameItemStore = new GameItemStore();
        Stream.of(getCorner(CornerPosition.TOP_RIGHT), getCorner(CornerPosition.TOP_LEFT),
                        getCorner(CornerPosition.BOTTOM_RIGHT), getCorner(CornerPosition.BOTTOM_LEFT))
                .map(corner -> corner.orElse(new Corner(false, GameItemEnum.NONE)).getGameItem())
                .forEach(gameItem -> gameItemStore.increment(gameItem, 1));
        return gameItemStore;
    }

    /**
     * Calculates and returns the points for this front side of the card.
     * This implementation simply returns the static points value.
     *
     * @param cardPosition The position of the card on the player's board.
     * @param playerBoard  The player's board.
     * @return The points attributed to this front side of the card.
     */
    @Override
    public int getPoints(Point cardPosition, PlayerBoard playerBoard) {
        return points;
    }
}
