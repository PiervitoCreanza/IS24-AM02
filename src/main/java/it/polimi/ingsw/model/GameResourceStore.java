package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GameResourceStore extends Store<GameResource> {

    public GameResourceStore() {
        super(new HashMap<GameResource, Integer>(GameResource.values().length));
        GameResource.stream().forEach(gameResource -> this.store.put(gameResource, 0));
    }

    public GameResourceStore(HashMap<GameResource, Integer> gameResourceStore) {
        super(gameResourceStore);
        if (gameResourceStore.size() != GameResource.values().length) {
            throw new IllegalArgumentException("Missing game resources");
        }
    }
}
