package it.polimi.ingsw.model;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a game card in the game.
 * Each game card has two sides, a current side and another side, along with a specific color.
 */
public class GameCard {

    /**
     * The side of the card that is currently facing up.
     */
    private Side currentSide;

    /**
     * The side of the card that is currently facing down.
     */
    private Side otherSide;

    /**
     * The color of the card.
     */
    private final CardColor cardColor;

    /**
     * Constructs a GameCard with specified current and other side, and card color.
     * @param currentSide The current side of the card.
     * @param otherSide The other side of the card.
     * @param cardColor The color of the card.
     * @throws NullPointerException if any of the parameters are null.
     */
    public GameCard(Side currentSide, Side otherSide, CardColor cardColor) {
        this.currentSide = Objects.requireNonNull(currentSide, "currentSide cannot be null");
        this.otherSide = Objects.requireNonNull(otherSide, "otherSide cannot be null");
        this.cardColor = Objects.requireNonNull(cardColor, "cardColor cannot be null");
    }

    /**
     * Gets the current side of the card.
     * @return The current side of the card.
     */
    public Side getCurrentSide() {
        return currentSide;
    }

    /**
     * Switches the current side of the card with the other side.
     */
    public void switchSide() {
        Side tempSide = currentSide;
        currentSide = otherSide;
        otherSide = tempSide;
    }

    /**
     * Gets the color of the card.
     * @return The color of the card.
     */
    public CardColor getCardColor() {
        return cardColor;
    }

    /**
     * Gets the corner of the current side of the card based on the specified position.
     * @param position The position of the corner.
     * @return An Optional containing the corner if it exists, or empty otherwise.
     */
    public Optional<Corner> getCorner(CornerPosition position) {
        return currentSide.getCorner(position);
    }

    /**
     * Sets the corner of the current side of the card as covered based on the specified position.
     * @param position The position of the corner to cover.
     * @return The game item enum corresponding to the covered corner.
     */
    public GameItemEnum setCornerCovered(CornerPosition position) {
        return currentSide.setCornerCovered(position);
    }

    /**
     * Gets the game item store of the current side of the card.
     * @return The game item store of the current side.
     */
    public GameItemStore getGameItemStore() {
        return currentSide.getGameItemStore();
    }

    /**
     * Calculates and returns the points for the card based on its position and the player's board.
     * @param cardPosition The position of the card on the player's board.
     * @param playerBoard The player's board.
     * @return The calculated points for the card.
     */
    public int getPoints(Coordinate cardPosition, PlayerBoard playerBoard) {
        return currentSide.getPoints(cardPosition, playerBoard);
    }

    /**
     * Gets the needed item store for the current side of the card.
     * @return The needed item store for the current side.
     */
    public GameItemStore getNeededItemStore() {
        return currentSide.getNeededItemStore();
    }
}
