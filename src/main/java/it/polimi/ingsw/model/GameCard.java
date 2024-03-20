package it.polimi.ingsw.model;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a game card with two sides and a specific color.
 * Each side can hold different game items and may have different game mechanics.
 */
public class GameCard {
    /**
     * The current active side of the card.
     */
    private Side currentSide;

    /**
     * The alternate side of the card.
     */
    private Side otherSide;

    /**
     * The color of the card.
     */
    private final CardColor cardColor;

    /**
     * Constructs a new GameCard with specified sides and color.
     *
     * @param currentSide The initial active side of the card.
     * @param otherSide   The alternate side of the card.
     * @param cardColor   The color of the card.
     * @throws NullPointerException if any argument is null.
     */
    public GameCard(Side currentSide, Side otherSide, CardColor cardColor) {
        this.currentSide = Objects.requireNonNull(currentSide, "currentSide cannot be null");
        this.otherSide = Objects.requireNonNull(otherSide, "otherSide cannot be null");
        this.cardColor = Objects.requireNonNull(cardColor, "cardColor cannot be null");
    }

    /**
     * Gets the current active side of the card.
     *
     * @return The current active side.
     */
    public Side getCurrentSide() {
        return currentSide;
    }

    /**
     * Switches the active side of the card with the alternate side.
     */
    public void switchSide() {
        Side tempSide = currentSide;
        currentSide = otherSide;
        otherSide = tempSide;
    }

    /**
     * Gets the color of the card.
     *
     * @return The color of the card.
     */
    public CardColor getCardColor() {
        return cardColor;
    }

    /**
     * Gets the corner item of the current side at a given position.
     *
     * @param position The position of the corner to retrieve.
     * @return An Optional containing the corner item, if present.
     */
    public Optional<Corner> getCorner(CornerPosition position) {
        return currentSide.getCorner(position);
    }

    /**
     * Sets a corner of the current side to be covered and returns the corresponding game item.
     *
     * @param position The position of the corner to cover.
     * @return The game item enum value associated with the covered corner.
     */
    public GameItemEnum setCornerCovered(CornerPosition position) {
        return currentSide.setCornerCovered(position);
    }

    /**
     * Retrieves the game item store from the current side of the card.
     * This method masks the underlying implementation.
     *
     * @return The game item store of the current side.
     */
    public GameItemStore getGameItemStore() {
        return currentSide.getGameItemStore();
    }

    /**
     * Calculates and returns the points for a given player board.
     * This method masks the underlying implementation.
     *
     * @param playerBoard The player board to calculate points for.
     * @return The number of points.
     */
    public int getPoints(PlayerBoard playerBoard) {
        return currentSide.getPoints(playerBoard);
    }

    /**
     * Retrieves the needed item store from the current side of the card.
     * This method masks the underlying implementation.
     *
     * @return The needed item store of the current side.
     */
    public GameItemStore getNeededItemStore() {
        return currentSide.getNeededItemStore();
    }
}
