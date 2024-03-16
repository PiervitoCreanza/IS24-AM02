package it.polimi.ingsw.model.back;
import it.polimi.ingsw.model.GameResource;
import it.polimi.ingsw.model.Side;

import java.util.ArrayList;

/**
 * Abstract Class for the BackSide of the GameCard
 */

abstract public class Back extends Side {
    protected GameResourceStore resources;

    public Back(GameResourceStore resources) {
        this.resources = resources;
    }
    //L'idea è quella di ritornare tutte le risorse contenute nella carta
    public GameResourceStore getGameResources() {
        return this.resources;
    }

    /**
     * Return the points of the BackSide that is always 0
     * @return 0
     */

    public int getPoints(){
        return 0;
    }
}
