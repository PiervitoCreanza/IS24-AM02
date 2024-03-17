package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GameResourceStore extends Store<GameResource> {

    /**
     * Default constructor for GameResourceStore.
     * Initializes a new HashMap with the size equal to the number of different GameResources.
     * Each GameResource in the store is initially set to 0.
     */
    public GameResourceStore() {
        super(new HashMap<GameResource, Integer>(GameResource.values().length));
        GameResource.stream().forEach(gameResource -> this.store.put(gameResource, 0));
    }

    /**
     * Constructs a new GameResourceStore with the given map of game resources.
     *
     * @param gameResourceStore the map of game resources to initialize the store with
     * @throws IllegalArgumentException if the size of the provided map does not match the number of different GameResources
     */
    public GameResourceStore(HashMap<GameResource, Integer> gameResourceStore) {
        super(gameResourceStore);
        if (gameResourceStore.size() != GameResource.values().length) {
            throw new IllegalArgumentException("Missing game resources");
        }
    }
}
