package it.polimi.ingsw.model;


import java.util.stream.Stream;

/**
 * Abstract Class for the BackSide of the GameCard
 */

public class Back extends Side {
    protected GameItemStore resources;

    public Back(GameItemStore resources, Corner topRight, Corner topLeft, Corner bottomLeft, Corner bottomRight) {
        super(topRight, topLeft, bottomLeft, bottomRight);
        if(resources == null) throw new NullPointerException("resources cannot be null");
        this.resources = resources;
    }

    public GameItemStore getGameItemStore() {
        GameItemStore gameItemStore = super.getGameItemStore();
        resources.getNonEmptyKeys().forEach(key -> gameItemStore.increment(key, resources.get(key)));
        return gameItemStore;
    }
}
