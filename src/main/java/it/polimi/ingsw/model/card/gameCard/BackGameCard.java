package it.polimi.ingsw.model.card.gameCard;

import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.model.utils.store.GameItemStore;

import java.util.Objects;

/**
 * This class represents the back side of a game card in the game.
 * It extends the SideGameCard class and adds a GameItemStore to represent the resources on the back side of the card.
 */
public class BackGameCard extends SideGameCard {
    /**
     * The resources in the center of the back side of the card.
     */
    protected final GameItemStore resources;

    /**
     * Constructs a new BackGameCard object with the specified resources and corners.
     *
     * @param resources   The resources on the back side of the card.
     * @param topRight    The top right corner of the card.
     * @param topLeft     The top left corner of the card.
     * @param bottomLeft  The bottom left corner of the card.
     * @param bottomRight The bottom right corner of the card.
     * @throws NullPointerException if resources is null.
     */
    public BackGameCard(Corner topRight, Corner topLeft, Corner bottomLeft, Corner bottomRight, GameItemStore resources) {
        super(topRight, topLeft, bottomLeft, bottomRight);
        Objects.requireNonNull(resources, "resources cannot be null");
        this.resources = resources;
    }

    /**
     * Returns a GameItemStore representing the items on the corners and in the center of the card.
     *
     * @return all the items in the side of this card
     */
    @Override
    public GameItemStore getGameItemStore() {
        GameItemStore gameItemStore = getCornersItems();
        resources.getNonEmptyKeys().forEach(key ->
                // Increment the corresponding value in the gameItemStore object by the value associated with the key in the resources object
                gameItemStore.increment(key, resources.get(key))
        );
        return gameItemStore;
    }

    /**
     * Returns the resources on the back side of the card.
     *
     * @return the resources on the back side of the card
     */
    @Override
    public GameItemStore getBackItemStore() {
        return resources;
    }

    /**
     * Overrides the equals method for the GameCard class.
     * First checks if the cards are equals using the super class equals() method then checks if resources are equals.
     *
     * @param o the object to be compared with the current object
     * @return true if the specified object is equal to the current object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BackGameCard that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(this.resources, that.resources);
    }
}
