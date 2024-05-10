package it.polimi.ingsw.tui.utils;

public record Pair<T, U>(T x, U y) {
    public T key() {
        return x;
    }

    public U value() {
        return y;
    }
}