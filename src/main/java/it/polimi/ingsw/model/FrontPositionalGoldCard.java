package it.polimi.ingsw.model;

import java.awt.*;

/**
 * Represents a specific type of FrontGoldCard, named FrontPositionalGoldCard.
 * This class extends FrontGoldCard and specializes in calculating points based on
 * the card's position on the board.
 */
public class FrontPositionalGoldCard extends FrontGoldCard {

    /**
     * Constructs a FrontPositionalGoldCard object with specified corners and points.
     *
     * @param topRight    The top right corner of the front side.
     * @param topLeft     The top left corner of the front side.
     * @param bottomLeft  The bottom left corner of the front side.
     * @param bottomRight The bottom right corner of the front side.
     * @param points      The number of points attributed to this front side.
     */
    public FrontPositionalGoldCard(Corner topRight, Corner topLeft, Corner bottomLeft, Corner bottomRight, int points, GameItemStore neededItems) {
        super(topRight, topLeft, bottomLeft, bottomRight, points, neededItems);
    }

    /**
     * Calculates and returns the points for this FrontPositionalGoldCard based on its
     * position on the player's board. The points are calculated by considering the
     * adjacency of other cards to each corner of this card.
     *
     * @param cardPosition The position of the card on the player's board.
     * @param playerBoard  The player's board.
     * @return The calculated points based on the card's position and its adjacent cards.
     */
    @Override
    public int getPoints(Point cardPosition, PlayerBoard playerBoard){
        int N = 0;

        // Define the four corners of the current card position
        Point[] corners = {
                new Point(cardPosition.x + 1, cardPosition.y + 1),
                new Point(cardPosition.x + 1, cardPosition.y - 1),
                new Point(cardPosition.x - 1, cardPosition.y - 1),
                new Point(cardPosition.x - 1, cardPosition.y + 1)
        };

        // Check each corner
        for (Point corner : corners) {
            // If the Optional returned by getGameCard is not empty, increment N
            if (playerBoard.getGameCard(corner).isPresent()) {
                N++;
            }
        }

        // Return the product of N and points
        return N * points;
    }
}
