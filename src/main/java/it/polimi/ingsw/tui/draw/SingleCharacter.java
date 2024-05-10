package it.polimi.ingsw.tui.draw;


import it.polimi.ingsw.tui.utils.ColorsEnum;

public class SingleCharacter {
    private char character;
    private ColorsEnum color;

    public SingleCharacter(char character) {
        this.character = character;
    }

    public SingleCharacter(char character, ColorsEnum color) {
        this.character = character;
        this.color = color;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public ColorsEnum getColor() {
        return color;
    }

    public void setColor(ColorsEnum color) {
        this.color = color;
    }

    @Override
    public String toString() {
        if (color == null) {
            return String.valueOf(character);
        } else {
            return color.getCode() + character + "\u001B[0m";
        }
    }
}
