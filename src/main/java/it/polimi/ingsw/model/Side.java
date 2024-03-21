package it.polimi.ingsw.model;

import java.awt.*;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents a generic side of a game card. This abstract class provides foundational
 * functionality and structure for different types of card sides, including managing corners.
 */
abstract public class Side {
    private final Optional<Corner> topRight;
    private final Optional<Corner> topLeft;
    private final Optional<Corner> bottomLeft;
    private final Optional<Corner> bottomRight;

    /**
     * Constructs a Side with the specified corners.
     *
     * @param topRight    The top right corner of the side.
     * @param topLeft     The top left corner of the side.
     * @param bottomLeft  The bottom left corner of the side.
     * @param bottomRight The bottom right corner of the side.
     */
    public Side(Corner topRight, Corner topLeft, Corner bottomLeft, Corner bottomRight) {
        this.topRight = Optional.ofNullable(topRight);
        this.topLeft = Optional.ofNullable(topLeft);
        this.bottomLeft = Optional.ofNullable(bottomLeft);
        this.bottomRight = Optional.ofNullable(bottomRight);
    }

    /**
     * Retrieves the corner of the side based on the specified position.
     *
     * @param position The position of the corner to be retrieved.
     * @return An Optional containing the corner if it exists, or empty otherwise.
     */
    public Optional<Corner> getCorner(CornerPosition position) {
        return switch (position) {
            case TOP_RIGHT -> topRight;
            case TOP_LEFT -> topLeft;
            case BOTTOM_LEFT -> bottomLeft;
            case BOTTOM_RIGHT -> bottomRight;
        };
    }

    /**
     * Sets the specified corner of the side as covered and returns the corresponding game item.
     * If the corner is not present, returns GameItemEnum.NONE.
     *
     * @param position The position of the corner to cover.
     * @return The game item enum corresponding to the covered corner, or GameItemEnum.NONE if the corner doesn't exist.
     */
    public GameItemEnum setCornerCovered(CornerPosition position) {
        Optional<Corner> corner = this.getCorner(position);
        return corner.map(Corner::setCovered).orElse(GameItemEnum.NONE);
    }

    /**
     * Abstract method to be implemented by subclasses to return the game item store of the side.
     * @return The game item store of the side.
     */
    public abstract GameItemStore getGameItemStore();

    /**
     * Returns the points for this side of the card based on its position and the player's board.
     * This method returns zero by default and can be overridden by subclasses for specific implementations.
     *
     * @param cardPosition The position of the card on the player's board.
     * @param playerBoard  The player's board.
     * @return The calculated points for the card, which is zero by default.
     */
    public int getPoints(Point cardPosition, PlayerBoard playerBoard) {
        return 0;
    }

    /**
     * Returns a default GameItemStore for the side.
     * This method can be overridden by subclasses to provide specific implementations.
     * @return A default GameItemStore with all values set to zero.
     */
    public GameItemStore getNeededItemStore() {
        return new GameItemStore();
    }

    /**
     * Aggregates and returns game items from all corners of the side.
     * This helper method is used to gather items from the corners for forming a GameItemStore.
     *
     * @return A GameItemStore with items combined from all corners.
     */
    protected GameItemStore getCornersItems() {
        GameItemStore gameItemStore = new GameItemStore();
        Stream.of(getCorner(CornerPosition.TOP_RIGHT), getCorner(CornerPosition.TOP_LEFT),
                        getCorner(CornerPosition.BOTTOM_RIGHT), getCorner(CornerPosition.BOTTOM_LEFT))
                .map(corner -> corner.orElse(new Corner(false, GameItemEnum.NONE)).getGameItem())
                .forEach(gameItem -> gameItemStore.increment(gameItem, 1));
        return gameItemStore;
    }
}
