package it.polimi.ingsw.tui.draw;

import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Class representing a drawable area.
 * It is a utility class that provides methods to draw strings and integers at specified coordinates.
 * It also provides methods to set colors and get the width and height of the drawable area.
 */
public class DrawArea implements Drawable {
    /**
     * The drawable area. It is a map that associates coordinates with single characters.
     */
    protected HashMap<Coordinate, MagicChar> drawArea = new HashMap<>();

    /**
     * Constructor that initializes the drawable area with a string.
     *
     * @param drawArea The string to initialize the drawable area with.
     */
    public DrawArea(String drawArea) {
        drawAt(0, 0, drawArea);
    }

    /**
     * Copy constructor that creates a new drawable area from an existing one.
     * It is useful to compose drawable areas.
     *
     * @param other The existing drawable area to copy from.
     */
    public DrawArea(DrawArea other) {
        this.drawArea = new HashMap<>(other.drawArea);
    }

    /**
     * Draws the specified drawable area at the specified coordinates.
     *
     * @param x        The x-coordinate at which to draw the drawable area.
     * @param y        The y-coordinate at which to draw the drawable area.
     * @param drawArea The drawable area to draw.
     */
    protected void drawAt(int x, int y, DrawArea drawArea) {
        drawArea.getCoords().stream().filter(c -> drawArea.getCharAt(c.x, c.y) != ' ')
                .forEach(c -> this.drawArea.put(new Coordinate(x + c.x, y + c.y), drawArea.getAt(c.x, c.y)));
    }

    /**
     * Draws the specified string at the specified coordinates.
     *
     * @param x      The x-coordinate at which to draw the string.
     * @param y      The y-coordinate at which to draw the string.
     * @param string The string to draw.
     */
    protected void drawAt(int x, int y, String string) {
        String[] lines = string.split("\n");
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                char c = lines[i].charAt(j);
                if (c != ' ')
                    drawArea.put(new Coordinate(x + j, y + i), new MagicChar(lines[i].charAt(j)));
            }
        }
    }

    /**
     * Draws the specified string at the specified coordinates with the specified color.
     *
     * @param x      The x-coordinate at which to draw the string.
     * @param y      The y-coordinate at which to draw the string.
     * @param string The string to draw.
     * @param color  The color to use when drawing the string.
     */
    protected void drawAt(int x, int y, String string, ColorsEnum color) {
        String[] lines = string.split("\n");
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                char c = lines[i].charAt(j);
                if (c != ' ')
                    drawArea.put(new Coordinate(x + j, y + i), new MagicChar(lines[i].charAt(j), color));
            }
        }
    }

    /**
     * Draws the specified integer at the specified coordinates.
     *
     * @param x      The x-coordinate at which to draw the integer.
     * @param y      The y-coordinate at which to draw the integer.
     * @param string The integer to draw.
     */
    protected void drawAt(int x, int y, int string) {
        drawAt(x, y, String.valueOf(string));
    }

    /**
     * Draws the specified integer at the specified coordinates with the specified color.
     *
     * @param x      The x-coordinate at which to draw the integer.
     * @param y      The y-coordinate at which to draw the integer.
     * @param string The integer to draw.
     * @param color  The color to use when drawing the integer.
     */
    protected void drawAt(int x, int y, int string, ColorsEnum color) {
        drawAt(x, y, String.valueOf(string), color);
    }

    /**
     * Returns a string representation of the drawable area.
     *
     * @return A string representation of the drawable area.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= getHeight(); i++) {
            for (int j = 0; j <= getWidth(); j++) {
                MagicChar c = drawArea.get(new Coordinate(j, i));
                sb.append(c != null ? c.toString() : ' ');
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Returns the MagicChar at the specified coordinates.
     * The MagicChar is a utility class that represents a character with a color.
     *
     * @param x The x-coordinate of the character to return.
     * @param y The y-coordinate of the character to return.
     * @return The MagicChar at the specified coordinates.
     */
    public MagicChar getAt(int x, int y) {
        MagicChar c = drawArea.get(new Coordinate(x, y));
        return c != null ? c : new MagicChar(' ');
    }

    /**
     * Returns the char at the specified coordinates.
     *
     * @param x The x-coordinate of the character to return.
     * @param y The y-coordinate of the character to return.
     * @return The char at the specified coordinates.
     */
    public char getCharAt(int x, int y) {
        MagicChar c = drawArea.get(new Coordinate(x, y));
        return c != null ? c.getCharacter() : ' ';
    }

    /**
     * Returns the set of coordinates in the drawable area.
     *
     * @return The set of coordinates in the drawable area.
     */
    public Set<Coordinate> getCoords() {
        return drawArea.keySet();
    }

    /**
     * Converts a positive y-coordinate system to a negative one.
     * This is useful when drawing on a terminal, where the origin is in the top-left corner.
     *
     * @param pair The pairs whose y-coordinates to convert.
     * @param <U>  The type of the values in the pairs.
     * @return The pairs with their y-coordinates converted.
     */
    public <U> ArrayList<Pair<Coordinate, U>> convertCoordinates(ArrayList<Pair<Coordinate, U>> pair) {
        int maxY = (int) pair.stream().mapToDouble(p -> p.x().getY()).max().orElse(0);
        pair.forEach(p -> p.key().y = maxY - p.key().y);
        return pair;
    }

    /**
     * Sets the color of all characters in the drawable area to the specified color.
     *
     * @param color The color to set.
     */
    public void setColor(ColorsEnum color) {
        getCoords().forEach(c -> drawArea.get(c).setColor(color));
    }

    /**
     * Returns the height of the drawable area.
     *
     * @return The height of the drawable area.
     */
    @Override
    public int getHeight() {
        return (int) drawArea.keySet().stream().mapToDouble(Coordinate::getY).max().orElse(0);
    }

    /**
     * Returns the width of the drawable area.
     *
     * @return The width of the drawable area.
     */
    @Override
    public int getWidth() {
        return (int) drawArea.keySet().stream().mapToDouble(Coordinate::getX).max().orElse(0);
    }
}