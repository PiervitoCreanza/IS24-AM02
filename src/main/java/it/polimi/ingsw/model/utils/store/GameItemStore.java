package it.polimi.ingsw.model.utils.store;

import it.polimi.ingsw.model.card.GameItemEnum;

import java.util.HashMap;

/**
 * GameItemStore class is a subclass of Store that represents the store of game items.
 * It is used to keep track of the amount of each game item in the game.
 * The store is initialized with all the game items set to 0.
 */
public class GameItemStore extends Store<GameItemEnum> {

    /**
     * Default constructor for GameItemStore.
     * Initializes a new HashMap with the size equal to the number of different GameItems.
     * Each GameItem in the store is initially set to 0.
     */
    public GameItemStore() {
        super(new HashMap<>(GameItemEnum.values().length));
        // We assign the amount 0 to each game item in order to have the store initialized with all the game items set to 0.
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
