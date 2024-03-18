package it.polimi.ingsw.model;

import java.util.HashMap;

public class GameItemStore extends Store<GameItemEnum> {

    /**
     * Default constructor for GameItemStore.
     * Initializes a new HashMap with the size equal to the number of different GameItems.
     * Each GameItem in the store is initially set to 0.
     */
    public GameItemStore() {
        super(new HashMap<GameItemEnum, Integer>(GameItemEnum.values().length));
        GameItemEnum.stream().forEach(gameItem -> this.store.put(gameItem, 0));
    }

    /**
     * Constructs a new GameItemStore with the given map of game items.
     *
     * @param gameItemStore the map of game items to initialize the store with
     * @throws IllegalArgumentException if the size of the provided map does not match the number of different GameItems
     */
    public GameItemStore(HashMap<GameItemEnum, Integer> gameItemStore) {
        super(gameItemStore);
        if (gameItemStore.size() != GameItemEnum.values().length) {
            throw new IllegalArgumentException("Missing game items");
        }
    }
}
