package it.polimi.ingsw.model;

/**
 * Abstract Class for define Corner of a GameCard Side
 */

public class Corner {
    private boolean isCovered;
    private final GameItemEnum gameItem;

    public Corner(boolean isCovered, GameItemEnum gameItem) {
        this.isCovered = isCovered;
        this.gameItem = gameItem;
    }

    /**
     * This method returns the game item present in the corner.
     * If the corner is covered, it returns NULL.
     *
     * @return the game item present in the corner or NULL
     */
    public GameItemEnum getGameItem() {
        return this.isCovered ? GameItemEnum.NONE : this.gameItem;
    }

    /**
     * This method sets the corner as covered.
     */
    public GameItemEnum setCovered() {
        this.isCovered = true;
        return this.gameItem;
    }
}