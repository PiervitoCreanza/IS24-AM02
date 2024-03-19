package it.polimi.ingsw.model;

import java.util.HashMap;

public class GameObjectStore extends Store<GameObject> {
    /**
     * Default constructor for GameObjectStore.
     * Initializes a new HashMap with the size equal to the number of different GameObjects.
     * Each GameObject in the store is initially set to 0.
     */
    public GameObjectStore() {
        super(new HashMap<GameObject, Integer>(GameObject.values().length));
        GameObject.stream().forEach(gameObject -> this.store.put(gameObject, 0));
    }

    /**
     * Constructs a new GameObjectStore with the given map of game objects.
     *
     * @param gameObjectStore the map of game objects to initialize the store with
     * @throws IllegalArgumentException if the size of the provided map does not match the number of different GameObjects
     */
    public GameObjectStore(HashMap<GameObject, Integer> gameObjectStore) {
        super(gameObjectStore);
        if (gameObjectStore.size() != GameObject.values().length) {
            throw new IllegalArgumentException("Missing game objects");
        }
    }
}
