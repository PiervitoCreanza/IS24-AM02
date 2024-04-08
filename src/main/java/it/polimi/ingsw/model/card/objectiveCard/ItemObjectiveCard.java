package it.polimi.ingsw.model.card.objectiveCard;

import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import it.polimi.ingsw.model.player.PlayerBoard;

import java.util.Objects;
import java.util.ArrayList;

/**
 * This class extends the ObjectiveCard class and represents an objective card that rewards points based on the items a player has.
 * The points are calculated by multiplying the minimum number of items a player has by the points won.
 */
public class ItemObjectiveCard extends ObjectiveCard {
    /**
     * The GameItemStore object that represents the items that are considered for calculating the points.
     */
    private final GameItemStore multiplier;

    /**
     * Constructs a new ItemObjectiveCard object with the specified number of points and the items that are considered for calculating the points.
     *
     * @param objectiveCardId The unique identifier of the objective card.
     * @param pointsWon The number of points the player can win by fulfilling the objective of this card.
     * @param multiplier The GameItemStore object that represents the items that are considered for calculating the points.
     * @throws NullPointerException if multiplier is null.
     */
    public ItemObjectiveCard(int objectiveCardId, int pointsWon, GameItemStore multiplier) {
        super(objectiveCardId, pointsWon);
        Objects.requireNonNull(multiplier, "multiplier cannot be null");
        this.multiplier = multiplier;
    }

    /**
     * This method calculates and returns the points won by the player.
     *
     * @param playerBoard The player's board.
     * @return The points won by the player.
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {
        ArrayList<GameItemEnum> itemObjects = multiplier.getNonEmptyKeys();
        return itemObjects.stream()
                .map(x -> playerBoard.getGameItemAmount(x) / multiplier.get(x))
                .min(Integer::compareTo).orElse(0) * this.pointsWon;
    }
}