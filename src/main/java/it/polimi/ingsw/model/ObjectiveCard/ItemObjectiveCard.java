package it.polimi.ingsw.model.ObjectiveCard;

import it.polimi.ingsw.model.GameItemEnum;
import it.polimi.ingsw.model.GameItemStore;
import it.polimi.ingsw.model.PlayerBoard;
import java.util.Objects;
import java.util.ArrayList;

/**
 * This class extends the ObjectiveCard class and represents an objective card that rewards points based on the items a player has.
 * The points are calculated by multiplying the minimum number of items a player has by the points won.
 */

public class ItemObjectiveCard extends ObjectiveCard {
    GameItemStore multiplier;

    public ItemObjectiveCard(int pointsWon, GameItemStore multiplier) {
        super(pointsWon);
        Objects.requireNonNull(multiplier, "multiplier cannot be null");
        this.multiplier = multiplier;
    }

    /**
     * This method calculates and returns the points won by the player.
     *
     * @param playerBoard of the player to calculate the points
     * @return points obtained by this card
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {
        ArrayList<GameItemEnum> itemObjects = multiplier.getNonEmptyKeys();
        return itemObjects.stream()
                .map(x -> playerBoard.getGameItemAmount(x) / multiplier.get(x))
                .min(Integer::compareTo).orElse(0) * this.pointsWon;
    }
}
