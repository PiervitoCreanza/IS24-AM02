package it.polimi.ingsw.model;

/**
 * The {@code FrontPositionalGoldCard} class represents a specific type of gold card in the game.
 * This class extends the {@code FrontGoldCard} class, inheriting its features and functionality.
 * It may include additional attributes or methods specific to positional aspects of a gold card in future development.
 */
public class FrontPositionalGoldCard extends FrontGoldCard {

    /**
     * Constructs a new {@code FrontPositionalGoldCard} with specified corners and points.
     * Inherits the behavior of {@code FrontGoldCard} including the management of needed game items.
     *
     * @param topRight    The top right corner of the front positional gold card.
     * @param topLeft     The top left corner of the front positional gold card.
     * @param bottomLeft  The bottom left corner of the front positional gold card.
     * @param bottomRight The bottom right corner of the front positional gold card.
     * @param points      The points associated with this front positional gold card.
     */
    public FrontPositionalGoldCard(Corner topRight, Corner topLeft, Corner bottomLeft, Corner bottomRight, int points) {
        super(topRight, topLeft, bottomLeft, bottomRight, points);
    }

    //TODO: Implement getPoints(PlayerBoard) method with @Override annotation, which is inherited from the superclass.
}
