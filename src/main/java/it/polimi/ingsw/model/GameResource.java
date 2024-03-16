package it.polimi.ingsw.model;

import java.util.stream.Stream;

/**
 * Type of the Resource that can be used
 */
public enum GameResource {
    PLANT,
    ANIMAL,
    FUNGI,
    INSECT;

    public static Stream<GameResource> stream() {
        return Stream.of(GameResource.values());
    }
}
