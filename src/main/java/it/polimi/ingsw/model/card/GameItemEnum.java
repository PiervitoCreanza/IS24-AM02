package it.polimi.ingsw.model.card;

import it.polimi.ingsw.view.tui.utils.ColorsEnum;

import java.util.stream.Stream;

/**
 * GameItemEnum is an enumeration that represents the different types of game items that can be found in the game.
 */
public enum GameItemEnum {
    /**
     * PLANT is a game resource.
     */
    PLANT("P", ColorsEnum.GREEN),
    /**
     * ANIMAL is a game resource.
     */
    ANIMAL("A", ColorsEnum.CYAN),
    /**
     * STONE is a game resource.
     */
    FUNGI("F", ColorsEnum.RED),
    /**
     * INSECT is a game object.
     */
    INSECT("I", ColorsEnum.PURPLE),
    /**
     * QUILL is a game object.
     */
    QUILL("Q", ColorsEnum.YELLOW),
    /**
     * INKWELL is a game object.
     */
    INKWELL("I", ColorsEnum.YELLOW),
    /**
     * MANUSCRIPT is a game object.
     */
    MANUSCRIPT("M", ColorsEnum.YELLOW),
    /**
     * NONE is a special enum that represents the absence of a game item.
     */
    NONE(" ", ColorsEnum.RESET);

    /**
     * The symbol that represents the game item.
     */
    private final String symbol;

    /**
     * The color of the game item.
     */
    private final ColorsEnum color;

    /**
     * Constructor that initializes the GameItemEnum with a symbol and a color.
     *
     * @param symbol The symbol that represents the game item.
     * @param color  The color of the game item.
     */
    GameItemEnum(String symbol, ColorsEnum color) {
        this.symbol = symbol;
        this.color = color;
    }

    /**
     * Returns a stream of all the GameItemEnum values.
     *
     * @return a stream of all the GameItemEnum values
     */
    public static Stream<GameItemEnum> stream() {
        return Stream.of(GameItemEnum.values());
    }

    /**
     * Returns the symbol of the game item.
     *
     * @return The symbol of the game item.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns the color of the game item.
     *
     * @return The color of the game item.
     */
    public ColorsEnum getColor() {
        return color;
    }
}
