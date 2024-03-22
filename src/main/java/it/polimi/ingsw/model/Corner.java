package it.polimi.ingsw.model;

/**
 * This class represents a corner of a game card in the game.
 * Each corner can be covered or uncovered and can contain a game item.
 */
public class Corner {
    /**
     * A boolean indicating whether the corner is covered or not.
     */
    private boolean isCovered;

    /**
     * The game item present in the corner.
     */
    private final GameItemEnum gameItem;

    /**
     * Constructs a new Corner object with the specified covered status and game item.
     * @param isCovered A boolean indicating whether the corner is covered or not.
     * @param gameItem The game item present in the corner.
     */
    public Corner(boolean isCovered, GameItemEnum gameItem) {
        this.isCovered = isCovered;
        this.gameItem = gameItem;
    }

    /**
     * Returns the game item present in the corner.
     * If the corner is covered, it returns NONE.
     * @return the game item present in the corner or NONE if the corner is covered.
     */
    public GameItemEnum getGameItem() {
        return this.isCovered ? GameItemEnum.NONE : this.gameItem;
    }

    /**
     * Sets the corner as covered and returns the game item that was present in the corner.
     * Because when we set a corner as covered we lose the information about the game item that was present in the corner,
     * @return The game item that was present in the corner.
     */
    public GameItemEnum setCovered() {
        this.isCovered = true;
        return this.gameItem;
    }
}