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
}