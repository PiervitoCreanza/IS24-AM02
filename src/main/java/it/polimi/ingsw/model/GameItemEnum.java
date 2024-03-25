package it.polimi.ingsw.model;

import java.util.stream.Stream;

/**
 * GameItemEnum is an enumeration that represents the different types of game items that can be found in the game.
 */
public enum GameItemEnum {
    /**
     * PLANT is a game resource.
     */
    PLANT,
    /**
     * ANIMAL is a game resource.
     */
    ANIMAL,
    /**
     * STONE is a game resource.
     */
    FUNGI,
    /**
     * INSECT is a game object.
     */
    INSECT,
    /**
     * QUILL is a game object.
     */
    QUILL,
    /**
     * INKWELL is a game object.
     */
    INKWELL,
    /**
     * MANUSCRIPT is a game object.
     */
    MANUSCRIPT,
    /**
     * NONE is a special enum that represents the absence of a game item.
     */
    NONE;

    /**
     * Returns a stream of all the GameItemEnum values.
     *
     * @return a stream of all the GameItemEnum values
     */
    public static Stream<GameItemEnum> stream() {
        return Stream.of(GameItemEnum.values());
    }
}
