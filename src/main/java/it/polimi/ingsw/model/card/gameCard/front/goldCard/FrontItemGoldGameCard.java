package it.polimi.ingsw.model.card.gameCard.front.goldCard;

import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.card.corner.Corner;

import java.util.Objects;

/**
 * Represents a variant of FrontGoldGameCard known as FrontItemGoldGameCard. This class extends
 * FrontGoldGameCard and specializes in calculating points based on the quantity of a specific
 * game item on the player's board.
 */
public class FrontItemGoldGameCard extends FrontGoldGameCard {

    /**
     * The game item that acts as a multiplier for calculating points.
     */
    private final GameItemEnum multiplier;

    /**
     * Constructs a FrontItemGoldGameCard object with specified corners, points, and a game item multiplier.
     *
     * @param topRight    The top right corner of the front side.
     * @param topLeft     The top left corner of the front side.
     * @param bottomLeft  The bottom left corner of the front side.
     * @param bottomRight The bottom right corner of the front side.
     * @param points      The number of points attributed to this front side.
     * @param neededItems The game items needed for this card.
     * @param multiplier  The game item that will be used as a multiplier for point calculation.
     */
    public FrontItemGoldGameCard(Corner topRight, Corner topLeft, Corner bottomLeft, Corner bottomRight, int points, GameItemStore neededItems, GameItemEnum multiplier) {
        super(topRight, topLeft, bottomLeft, bottomRight, points, neededItems);
        this.multiplier = multiplier;
    }

    /**
     * Calculates and returns the points for this FrontItemGoldGameCard based on the quantity
     * of a specific game item on the player's board.
     *
     * @param cardPosition The position of the card on the player's board.
     * @param playerBoard  The player's board.
     * @return The calculated points based on the quantity of the specified game item on the player's board.
     */
    @Override
    public int getPoints(Coordinate cardPosition, PlayerBoard playerBoard) {
        // The points are multiplied by the amount of the specified game item on the player's board.
        return playerBoard.getGameItemAmount(multiplier) * points;
    }

    /**
     * Checks if the given object is equal to this FrontItemGoldGameCard.
     * Two FrontItemGoldGameCards are equal if they have the same cardId, pointsWon, and multiplier.
     *
     * @param o The object to compare this FrontItemGoldGameCard against.
     * @return true if the given object represents a FrontItemGoldGameCard equivalent to this FrontItemGoldGameCard, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FrontItemGoldGameCard that)) return false;
        if (!super.equals(o)) return false;
        return this.multiplier == that.multiplier;
    }
}
