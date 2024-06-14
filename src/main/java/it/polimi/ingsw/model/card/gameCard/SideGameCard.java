package it.polimi.ingsw.model.card.gameCard;

import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.model.card.corner.CornerPosition;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.utils.store.GameItemStore;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents a side of a game card.
 * Each side has four corners, each of which can hold a game item.
 */
public abstract class SideGameCard {
    /**
     * The top right corner of the game card.
     */
    private final Corner topRight;
    /**
     * The top left corner of the game card.
     */
    private final Corner topLeft;
    /**
     * The bottom left corner of the game card.
     */
    private final Corner bottomLeft;
    /**
     * The bottom right corner of the game card.
     */
    private final Corner bottomRight;

    /**
     * Constructs a SideGameCard with the specified corners.
     * <p>
     * Note: SideGameCard is an abstract class and cannot be instantiated directly.
     * However, any class that extends SideGameCard can be instantiated and will benefit from polymorphism.
     * <p>
     * Note: A corner can be non-existent and therefore null. This is different from a corner that exists but has no game item,
     * which is represented by a Corner with GameItemEnum.NONE.
     *
     * @param topRight    the top right corner
     * @param topLeft     the top left corner
     * @param bottomLeft  the bottom left corner
     * @param bottomRight the bottom right corner
     */
    public SideGameCard(Corner topRight, Corner topLeft, Corner bottomLeft, Corner bottomRight) {
        this.topRight = topRight;
        this.topLeft = topLeft;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
    }

    /**
     * Returns the corner at the specified position.
     * <p>
     * Note: If the corner is non-existent (i.e., null), this method will return an Optional.isEmpty().
     * This is different from a corner that exists but has no game item, which is represented by a Corner with GameItemEnum.NONE.
     * In this case, the method will return an Optional containing the corner that has GameItemEnum.NONE in it.
     * If the corner exists and has a game item, the method will return an Optional containing the corner.
     *
     * @param position the position of the corner
     * @return an Optional containing the corner if it exists, otherwise an empty Optional
     */
    public Optional<Corner> getCorner(CornerPosition position) {
        return switch (position) {
            case TOP_RIGHT -> Optional.ofNullable(topRight);
            case TOP_LEFT -> Optional.ofNullable(topLeft);
            case BOTTOM_LEFT -> Optional.ofNullable(bottomLeft);
            case BOTTOM_RIGHT -> Optional.ofNullable(bottomRight);
        };
    }

    /**
     * Sets the corner at the specified position as covered.
     *
     * @param position the position of the corner
     * @return the game item of the corner if it exists, otherwise GameItemEnum.NONE
     */
    public GameItemEnum setCornerCovered(CornerPosition position) {
        Optional<Corner> corner = this.getCorner(position);
        return corner.map(Corner::setCovered).orElse(GameItemEnum.NONE);
    }

    /**
     * Returns the game item store of the side.
     *
     * @return the game item store
     */
    public abstract GameItemStore getGameItemStore();

    /**
     * Returns the points of the side at the specified card position.
     *
     * @param cardPosition the position of the card
     * @param playerBoard  the player board
     * @return the points
     */
    public int getPoints(Coordinate cardPosition, PlayerBoard playerBoard) {
        return 0;
    }

    /**
     * Returns the game item store needed by the side.
     *
     * @return the needed game item store
     */
    public GameItemStore getNeededItemStore() {
        return new GameItemStore();
    }

    /**
     * Returns the game items of the corners.
     *
     * @return the game item store of the corners
     */
    protected GameItemStore getCornersItems() {
        GameItemStore gameItemStore = new GameItemStore();
        Stream.of(topRight, topLeft, bottomRight, bottomLeft)
                .filter(Objects::nonNull)
                .map(Corner::getGameItem)
                .forEach(gameItem -> gameItemStore.increment(gameItem, 1));
        return gameItemStore;
    }

    /**
     * Overrides the equals method for the SideGameCard class.
     * Checks if all corners are equals.
     *
     * @param o the object to be compared with the current object
     * @return true if the specified object is equal to the current object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SideGameCard that)) return false;
        return Objects.equals(this.topRight, that.topRight) && Objects.equals(this.topLeft, that.topLeft) && Objects.equals(this.bottomLeft, that.bottomLeft) && Objects.equals(this.bottomRight, that.bottomRight);
    }
}