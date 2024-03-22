package it.polimi.ingsw.model;

import java.util.Objects;

/**
 * Abstract Class for the BackSide of the GameCard
 */

public class Back extends Side {
    protected final GameItemStore resources;

    public Back(GameItemStore resources, Corner topRight, Corner topLeft, Corner bottomLeft, Corner bottomRight) {
        super(topRight, topLeft, bottomLeft, bottomRight);
        Objects.requireNonNull(resources, "resources cannot be null");
        this.resources = resources;
    }

    /**
     * This method return a GameItemStore representing the items on the corners and in the center of the card.
     * @return all the items of the card
     */
    public GameItemStore getGameItemStore() {
        GameItemStore gameItemStore = getCornersItems();
        resources.getNonEmptyKeys().forEach(key ->
                // Increment the corresponding value in the gameItemStore object by the value associated with the key in the resources object
                gameItemStore.increment(key, resources.get(key))
        );
        return gameItemStore;
    }
}
