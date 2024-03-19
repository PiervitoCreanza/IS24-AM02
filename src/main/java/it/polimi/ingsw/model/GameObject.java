package it.polimi.ingsw.model;

import java.util.stream.Stream;

/**
 * Type of the Object that can be used
 */
public enum GameObject {
    QUILL,
    INKWELL,
    MANUSCRIPT;

    public static Stream<GameObject> stream() {
        return Stream.of(GameObject.values());
    }
}
