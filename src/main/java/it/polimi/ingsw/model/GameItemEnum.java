package it.polimi.ingsw.model;

import java.util.stream.Stream;

public enum GameItemEnum {
    PLANT,
    ANIMAL,
    FUNGI,
    INSECT,
    QUILL,
    INKWELL,
    MANUSCRIPT,
    NONE;

    public static Stream<GameItemEnum> stream() {
        return Stream.of(GameItemEnum.values());
    }
}
