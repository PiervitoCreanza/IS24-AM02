package it.polimi.ingsw.model.corner;

import it.polimi.ingsw.model.GameObject;
import it.polimi.ingsw.model.GameResource;
import java.util.Optional;

/**
 * Abstract Class for define Corner of a GameCard Side
 */

abstract public class Corner {
    protected boolean isCovered;
    public Corner() {
        this.isCovered = false;
    }
    public Optional<GameObject> getGameObject(){
        return Optional.empty();
    }
    public Optional<GameResource> getGameResource(){
        return Optional.empty();
    }
    public void setCovered(boolean covered) {
        isCovered = covered;
    }
}
