package it.polimi.ingsw.model;


/**
 * Abstract Class for the BackSide of the GameCard
 */

public class Back extends Side {
    protected GameItemStore resources;

    public Back(GameItemStore resources, Corner topRight, Corner topLeft, Corner bottomLeft, Corner bottomRight) {
        super(topRight, topLeft, bottomLeft, bottomRight);
        this.resources = resources;
    }

    public GameItemStore getGameItemStore() {
        return resources;
    }

}
