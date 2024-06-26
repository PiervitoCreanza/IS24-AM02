package it.polimi.ingsw.view.tui.utils;

/**
 * A simple pair of two elements.
 *
 * @param <T> the type of the first element
 * @param <U> the type of the second element
 */
public record Pair<T, U>(T x, U y) {

    /**
     * Returns the first element of the pair.
     *
     * @return the first element of the pair
     */
    public T key() {
        return x;
    }

    /**
     * Returns the second element of the pair.
     *
     * @return the second element of the pair
     */
    public U value() {
        return y;
    }
}