package it.polimi.ingsw.tui.utils;

public class Colors {
    public Colors() {
    }

    public String red(String text) {
        return "\u001B[31m" + text + "\u001B[0m";
    }

    public String red(int text) {
        return red(String.valueOf(text));
    }
}
