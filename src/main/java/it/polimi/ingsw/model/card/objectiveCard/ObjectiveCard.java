package it.polimi.ingsw.model.card.objectiveCard;

import it.polimi.ingsw.model.player.PlayerBoard;

/**
 * This abstract class represents an objective card in the game.
 * Each objective card allows the player to earn points based on certain criteria.
 * The specific criteria are defined in the subclasses of this class.
 */
abstract public class ObjectiveCard {

    /**
     * The unique identifier of this objective card.
     */
    protected final int objectiveCardId;

    /**
     * The number of points the player can win by fulfilling the objective of this card.
     */
    protected final int pointsWon;

    /**
     * Constructs a new ObjectiveCard object with the specified unique identifier and number of points.
     *
     * @param objectiveCardId The unique identifier of the objective card.
     * @param pointsWon The number of points the player can win by fulfilling the objective of this card.
     * @throws IllegalArgumentException if pointsWon is less than or equal to 0.
     */
    public ObjectiveCard(int objectiveCardId, int pointsWon) {
        if (pointsWon <= 0) throw new IllegalArgumentException("pointsWon must be positive");
        this.objectiveCardId = objectiveCardId;
        this.pointsWon = pointsWon;
    }

    /**
     * Returns the unique identifier of this objective card.
     *
     * @return The unique identifier of this objective card.
     */
    public int getObjectiveCardId() {
        return this.objectiveCardId;
    }

    /**
     * Returns the number of points the player can win by fulfilling the objective of this card.
     * The specific criteria for winning points are defined in the subclasses of this class.
     *
     * @param playerBoard The player's board.
     * @return The number of points the player can win by fulfilling the objective of this card.
     */
    abstract public int getPoints(PlayerBoard playerBoard);
}