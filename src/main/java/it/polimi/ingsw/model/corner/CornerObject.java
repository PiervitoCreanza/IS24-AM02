package it.polimi.ingsw.model.corner;

import it.polimi.ingsw.model.GameObject;

import java.util.Optional;

/**
 * Class for the Corner that contains object
 */

public class CornerObject extends Corner{
    GameObject gameObject;

    public CornerObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    /**
     * This method returns the game object in the corner.
     * If the corner is covered, it returns an empty Optional.
     * Otherwise, it returns an Optional containing the game object.
     * @return Optional containing the game object otherwise an empty Optional.
     */

    @Override
    public Optional<GameObject> getGameObject() {
        return this.isCovered ? Optional.empty() : Optional.ofNullable(this.gameObject);
    }
}
