package it.polimi.ingsw.model;

import it.polimi.ingsw.model.GameResourceStore;
import it.polimi.ingsw.model.Side;

/**
 * Abstract Class for the BackSide of the GameCard
 */

public class Back extends Side {
    protected GameItemStore resources;

    public Back(GameItemStore resources) {
        this.resources = resources;
    }
    public GameItemStore getGameItemStore() {
        return resources;
    }
    
}
