package it.polimi.ingsw.model.card.objectiveCard;

import it.polimi.ingsw.model.player.PlayerBoard;

import java.util.Objects;

/**
 * This abstract class represents an objective card in the game.
 * Each objective card allows the player to earn points based on certain criteria.
 * The specific criteria are defined in the subclasses of this class.
 */
abstract public class ObjectiveCard {

    /**
     * The unique identifier of this objective card.
     */
    private final int cardId;

    /**
     * The number of points the player can win by fulfilling the objective of this card.
     */
    protected final int pointsWon;

    /**
     * Constructs a new ObjectiveCard object with the specified unique identifier and number of points.
     *
     * @param cardId    The unique identifier of the objective card.
     * @param pointsWon The number of points the player can win by fulfilling the objective of this card.
     * @throws IllegalArgumentException if pointsWon is less than or equal to 0.
     */
    public ObjectiveCard(int cardId, int pointsWon) {
        if (pointsWon <= 0) throw new IllegalArgumentException("pointsWon must be positive");
        this.cardId = cardId;
        this.pointsWon = pointsWon;
    }

    /**
     * Returns the unique identifier of this objective card.
     *
     * @return The unique identifier of this objective card.
     */
    public int getCardId() {
        return this.cardId;
    }

    /**
     * Returns the number of points the player can win by fulfilling the objective of this card.
     * The specific criteria for winning points are defined in the subclasses of this class.
     *
     * @param playerBoard The player's board.
     * @return The number of points the player can win by fulfilling the objective of this card.
     */
    abstract public int getPoints(PlayerBoard playerBoard);

    /**
     * Checks if the given object is equal to this ObjectiveCard.
     * Two ObjectiveCards are equal if they have the same cardId and pointsWon.
     *
     * @param o The object to compare this ObjectiveCard against.
     * @return true if the given object represents an ObjectiveCard equivalent to this ObjectiveCard, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObjectiveCard that)) return false;
        return this.cardId == that.cardId && this.pointsWon == that.pointsWon;
    }
}