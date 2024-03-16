package it.polimi.ingsw.model.corner;

import it.polimi.ingsw.model.GameObject;

/**
 * Class for the Corner that contains object
 */

public class CornerObject extends Corner{
    GameObject gameObject;

    public CornerObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    @Override
    public GameObject getGameObject() {
        return gameObject;
    }
}
