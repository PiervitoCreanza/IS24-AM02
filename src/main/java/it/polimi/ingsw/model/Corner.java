package it.polimi.ingsw.model;

import java.util.Optional;

/**
 * Abstract Class for define Corner of a GameCard Side
 */

public class Corner {
    protected boolean isCovered;
    protected final GameItemEnum gameItem;
    public Corner(boolean isCovered, GameItemEnum gameItem) {
        this.isCovered = isCovered;
        this.gameItem = gameItem;
    }

    /**
     * This method returns the game item present in the corner.
     * If the corner is covered, it returns NULL.
     * @return the game item present in the corner or NULL
     */
    public GameItemEnum getGameItem() {
        return isCovered ? GameItemEnum.NONE : gameItem;
    }

    /**
     * This method sets the corner as covered.
     */
    public void setCovered() {
        isCovered = true;
    }

    /**
     * This method checks if the corner exists (and get override by NonExistingCorner)
     * @return true
     */
    public boolean isExisting(){return true;}

}