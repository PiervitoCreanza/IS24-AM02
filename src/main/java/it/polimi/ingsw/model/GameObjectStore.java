package it.polimi.ingsw.model;

import java.util.HashMap;

public class GameObjectStore extends Store<GameObject> {
    public GameObjectStore() {
        super(new HashMap<GameObject, Integer>(GameObject.values().length));
        GameObject.stream().forEach(gameObject -> this.store.put(gameObject, 0));
    }

    public GameObjectStore(HashMap<GameObject, Integer> gameObjectStore) {
        super(gameObjectStore);
        if (gameObjectStore.size() != GameObject.values().length) {
            throw new IllegalArgumentException("Missing game objects");
        }
    }
}
