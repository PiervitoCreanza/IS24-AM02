package it.polimi.ingsw.model.card.gameCard.front;

import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.model.card.gameCard.SideGameCard;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.utils.store.GameItemStore;

/**
 * Represents the front side of a game card. This class extends the SideGameCard class and
 * incorporates functionalities for managing points and aggregating game items from corners.
 */
public class FrontGameCard extends SideGameCard {

    /**
     * The number of points associated with this front side of the card.
     */
    protected int points;

    /**
     * Constructs a FrontGameCard object with specified corners and points.
     *
     * @param topRight    The top right corner of the front side.
     * @param topLeft     The top left corner of the front side.
     * @param bottomLeft  The bottom left corner of the front side.
     * @param bottomRight The bottom right corner of the front side.
     * @param points      The number of points attributed to this front side.
     */
    public FrontGameCard(Corner topRight, Corner topLeft, Corner bottomLeft, Corner bottomRight, int points) {
        super(topRight, topLeft, bottomLeft, bottomRight);
        this.points = points;
    }

    /**
     * Overrides the getGameItemStore method from the SideGameCard class.
     * This implementation aggregates game items from all corners of this front side into a GameItemStore.
     *
     * @return A GameItemStore containing game items from all corners of this front side.
     */
    @Override
    public GameItemStore getGameItemStore() {
        return getCornersItems();
    }

    /**
     * Returns the points of this front side of the game card. It's not overridden by subclasses.
     *
     * @return The points of this front side of the game card.
     */
    @Override
    public int getPoints() {
        return points;
    }

    /**
     * Calculates and returns the points for this front side of the card.
     * This method simply returns the static points value set for this front side.
     *
     * @param cardPosition The position of the card on the player's board.
     * @param playerBoard  The player's board.
     * @return The points attributed to this front side of the card.
     */
    @Override
    public int calculatePoints(Coordinate cardPosition, PlayerBoard playerBoard) {
        return points;
    }

    /**
     * Overrides the equals method for the GameCard class.
     * First checks if the cards are equals using the super class equals() method then checks if points are equals.
     *
     * @param o the object to be compared with the current object
     * @return true if the specified object is equal to the current object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FrontGameCard that)) return false;
        if (!super.equals(o)) return false;
        return this.points == that.points;
    }
}
