package it.polimi.ingsw.model.card.gameCard;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.model.card.corner.CornerPosition;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import javafx.beans.property.BooleanProperty;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a game card in the game.
 * Each game card has two sides, a current side and another side, along with a specific color.
 */
public class GameCard implements Card, Serializable {

    /**
     * The unique identifier for the game card.
     */
    private final int cardId;

    /**
     * The side of the card that is currently facing up.
     */
    private final SerializableBooleanProperty isFlipped = new SerializableBooleanProperty(false);

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
     * @param cardId              The unique identifier for the game card.
     * @param currentSideGameCard The current side of the card.
     * @param otherSideGameCard   The other side of the card.
     * @param cardColorEnum       The color of the card.
     * @throws NullPointerException if any of the parameters are null.
     */
    public GameCard(int cardId, SideGameCard currentSideGameCard, SideGameCard otherSideGameCard, CardColorEnum cardColorEnum) {
        this.cardId = cardId;
        this.currentSideGameCard = Objects.requireNonNull(currentSideGameCard, "currentSideGameCard cannot be null");
        this.otherSideGameCard = Objects.requireNonNull(otherSideGameCard, "otherSideGameCard cannot be null");
        this.cardColorEnum = Objects.requireNonNull(cardColorEnum, "cardColorEnum cannot be null");
    }

    /**
     * Returns the unique identifier for the game card.
     *
     * @return The unique identifier for the game card.
     */
    public int getCardId() {
        return cardId;
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
        isFlipped.set(!isFlipped.get());
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
     * Gets the back game item store of the current side of the card.
     *
     * @return The back game item store of the current side.
     */
    public GameItemStore getBackItemStore() {
        return currentSideGameCard.getBackItemStore();
    }

    /**
     * Returns the points of the current side of the game card.
     *
     * @return The points of the current side of the game card.
     */
    public int getPoints() {
        return currentSideGameCard.getPoints();
    }

    /**
     * Calculates and returns the points for the card based on its position and the player's board.
     *
     * @param cardPosition The position of the card on the player's board.
     * @param playerBoard  The player's board.
     * @return The calculated points for the card.
     */
    public int calculatePoints(Coordinate cardPosition, PlayerBoard playerBoard) {
        return currentSideGameCard.calculatePoints(cardPosition, playerBoard);
    }

    /**
     * Gets the needed item store for the current side of the card.
     *
     * @return The needed item store for the current side.
     */
    public GameItemStore getNeededItemStore() {
        return currentSideGameCard.getNeededItemStore();
    }

    /**
     * Gets the multiplier for the current side of the card.
     *
     * @return The multiplier for the current side.
     */
    public GameItemEnum getMultiplier() {
        return currentSideGameCard.getMultiplier();
    }

    /**
     * Checks if the current side of the card is gold positional.
     *
     * @return true if the current side of the card is gold positional, false otherwise.
     */
    public boolean isGoldPositional() {
        return currentSideGameCard.isGoldPositional();
    }

    /**
     * Returns a boolean indicating if the card is flipped.
     *
     * @return true if the card is flipped, false otherwise.
     */
    public boolean isFlipped() {
        return isFlipped.get();
    }

    /**
     * Sets the current side of the card.
     *
     * @param flipped The flipped status to set.
     */
    public void setFlipped(boolean flipped) {
        if (flipped == isFlipped.get()) {
            return;
        }
        switchSide();
    }

    /**
     * Gets the boolean property indicating if the card is flipped.
     *
     * @return The boolean property indicating if the card is flipped.
     */
    public BooleanProperty getIsFlippedProperty() {
        return isFlipped;
    }

    /**
     * Overrides the equals method for the GameCard class.
     * Checks if all attributes are equals and then checks if the sides are equals.
     *
     * @param o the object to be compared with the current object
     * @return true if the specified object is equal to the current object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameCard that)) return false;
        return this.cardId == that.cardId && Objects.equals(this.currentSideGameCard, that.currentSideGameCard) && Objects.equals(this.otherSideGameCard, that.otherSideGameCard) && this.cardColorEnum == that.cardColorEnum;
    }
}
