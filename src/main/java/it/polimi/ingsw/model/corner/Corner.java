package it.polimi.ingsw.model.corner;

import it.polimi.ingsw.model.GameObject;
import it.polimi.ingsw.model.GameResource;

/**
 * Abstract Class for define Corner of a GameCard Side
 */

abstract public class Corner {
    protected boolean isCovered;
    public Corner() {
        this.isCovered = false;
    }
    public GameObject getGameObject(){
        return null;
    }
    public GameResource getGameResource(){
        return null;
    }
    public void setCovered(boolean covered) {
        isCovered = covered;
    }
}
