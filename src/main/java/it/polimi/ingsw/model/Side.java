package it.polimi.ingsw.model;

import java.awt.*;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents a side of a game card. It is an abstract class and can be extended to represent
 * different types of card sides.
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
     * Gets the corner of the side based on the specified position.
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
     *
     * @param position The position of the corner to cover.
     * @return The game item enum corresponding to the covered corner, or GameItemEnum.NONE if the corner doesn't exist.
     */
    public GameItemEnum setCornerCovered(CornerPosition position) {
        Optional<Corner> corner = this.getCorner(position);
        return corner.map(Corner::setCovered).orElse(GameItemEnum.NONE);
    }

    /**
     * Returns all the items contained in the corners of the side.
     *
     * @return A GameItemStore containing all game items from the corners.
     */
    public GameItemStore getGameItemStore() {
        GameItemStore gameItemStore = new GameItemStore();
        Stream.of(topRight, topLeft, bottomRight, bottomLeft)
                .map(corner -> corner.orElse(new Corner(false, GameItemEnum.NONE)).getGameItem())
                .forEach(gameItem -> gameItemStore.increment(gameItem, 1));

        return gameItemStore;
    }

    /**
     * Returns the points for this side of the card based on its position and the player's board.
     * This method returns zero by default and is intended to be overridden in subclasses for special cases.
     *
     * @param cardPosition The position of the card on the player's board.
     * @param playerBoard  The player's board.
     * @return The calculated points for the card, which is zero by default.
     */
    public int getPoints(Point cardPosition, PlayerBoard playerBoard) {
        return 0;
    }

    /**
     * Returns the needed item store for this side of the card.
     * By default, a card does not have any NeededItemStore. This method is intended to be overridden in subclasses.
     *
     * @return A default GameItemStore with all values set to zero.
     */
    public GameItemStore getNeededItemStore() {
        return new GameItemStore();
    }
}
