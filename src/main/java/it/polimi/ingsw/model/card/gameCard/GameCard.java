package it.polimi.ingsw.model.card.gameCard;

import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.model.card.corner.CornerPosition;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.utils.store.GameItemStore;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a game card in the game.
 * Each game card has two sides, a current side and another side, along with a specific color.
 */
public class GameCard {

    /**
     * The unique identifier for the game card.
     */
    private final int gameCardId;

    /**
     * The side of the card that is currently facing up.
     */
    private SideGameCard currentSideGameCard;

    /**
     * The side of the card that is currently facing down.
     */
    private SideGameCard otherSideGameCard;

    /**
     * The color of the card.
     */
    private final CardColorEnum cardColorEnum;

    /**
     * Constructs a GameCard with specified current and other side, and card color.
     * <p>
     * Note: The parameters currentSideGameCard and otherSideGameCard should be instances of classes that extend SideGameCard.
     * These classes can be instantiated and will benefit from polymorphism.
     * <p>
     * Note: A corner in a SideGameCard can be non-existent and therefore null. This is different from a corner that exists but has no game item,
     * which is represented by a Corner with GameItemEnum.NONE.
     *
     * @param gameCardId          The unique identifier for the game card.
     * @param currentSideGameCard The current side of the card.
     * @param otherSideGameCard   The other side of the card.
     * @param cardColorEnum       The color of the card.
     * @throws NullPointerException if any of the parameters are null.
     */
    public GameCard(int gameCardId, SideGameCard currentSideGameCard, SideGameCard otherSideGameCard, CardColorEnum cardColorEnum) {
        this.gameCardId = gameCardId;
        this.currentSideGameCard = Objects.requireNonNull(currentSideGameCard, "currentSideGameCard cannot be null");
        this.otherSideGameCard = Objects.requireNonNull(otherSideGameCard, "otherSideGameCard cannot be null");
        this.cardColorEnum = Objects.requireNonNull(cardColorEnum, "cardColorEnum cannot be null");
    }

    /**
     * Returns the unique identifier for the game card.
     *
     * @return The unique identifier for the game card.
     */
    public int getGameCardId() {
        return gameCardId;
    }

    /**
     * Gets the current side of the card.
     *
     * @return The current side of the card.
     */
    public SideGameCard getCurrentSide() {
        return currentSideGameCard;
    }

    /**
     * Switches the current side of the card with the other side.
     */
    public void switchSide() {
        SideGameCard tempSideGameCard = currentSideGameCard;
        currentSideGameCard = otherSideGameCard;
        otherSideGameCard = tempSideGameCard;
    }

    /**
     * Gets the color of the card.
     *
     * @return The color of the card.
     */
    public CardColorEnum getCardColor() {
        return cardColorEnum;
    }

    /**
     * Gets the corner of the current side of the card based on the specified position.
     *
     * @param position The position of the corner.
     * @return An Optional containing the corner if it exists, or empty otherwise.
     */
    public Optional<Corner> getCorner(CornerPosition position) {
        return currentSideGameCard.getCorner(position);
    }

    /**
     * Sets the corner of the current side of the card as covered based on the specified position.
     *
     * @param position The position of the corner to cover.
     * @return The game item enum corresponding to the covered corner.
     */
    public GameItemEnum setCornerCovered(CornerPosition position) {
        return currentSideGameCard.setCornerCovered(position);
    }

    /**
     * Gets the game item store of the current side of the card.
     *
     * @return The game item store of the current side.
     */
    public GameItemStore getGameItemStore() {
        return currentSideGameCard.getGameItemStore();
    }

    /**
     * Calculates and returns the points for the card based on its position and the player's board.
     *
     * @param cardPosition The position of the card on the player's board.
     * @param playerBoard  The player's board.
     * @return The calculated points for the card.
     */
    public int getPoints(Coordinate cardPosition, PlayerBoard playerBoard) {
        return currentSideGameCard.getPoints(cardPosition, playerBoard);
    }

    /**
     * Gets the needed item store for the current side of the card.
     *
     * @return The needed item store for the current side.
     */
    public GameItemStore getNeededItemStore() {
        return currentSideGameCard.getNeededItemStore();
    }
}
