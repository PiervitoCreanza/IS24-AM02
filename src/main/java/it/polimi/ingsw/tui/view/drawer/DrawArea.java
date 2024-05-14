package it.polimi.ingsw.tui.view.drawer;

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
    private HashMap<Coordinate, MagicChar> drawArea = new HashMap<>();

    /**
     * The width of the drawable area.
     */
    private int width = 0;

    /**
     * The height of the drawable area.
     */
    private int height = 0;

    /**
     * Default constructor that initializes the drawable area with an empty string.
     */
    public DrawArea() {
        drawArea = new HashMap<>();
    }

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
        this.width = other.getWidth();
        this.height = other.getHeight();
    }

    /**
     * Updates the width and height of the drawable area based on the given coordinate.
     * The width and height are updated only if the x or y coordinate of the given point
     * is greater than the current width or height respectively.
     * The width and height are 1-indexed, hence 1 is added to the coordinate value.
     *
     * @param c The coordinate based on which the width and height are to be updated.
     */
    private void updateWidthHeight(Coordinate c) {
        // If the x-coordinate of the point is greater than the current width,
        // update the width to be one more than the x-coordinate.
        if (c.x >= width) width = c.x + 1;

        // If the y-coordinate of the point is greater than the current height,
        // update the height to be one more than the y-coordinate.
        if (c.y >= height) height = c.y + 1;
    }

    /**
     * Draws the specified drawable area at the specified coordinates.
     *
     * @param x             The x-coordinate at which to draw the drawable area.
     * @param y             The y-coordinate at which to draw the drawable area.
     * @param otherDrawArea The drawable area to draw.
     */
    public void drawAt(int x, int y, DrawArea otherDrawArea) {
        otherDrawArea.getCoords().stream()
                .filter(c -> otherDrawArea.getCharAt(c.x, c.y) != ' ')
                .forEach(c -> {
                    // Create a new coordinate with the x and y coordinates of the new character to put.
                    Coordinate newCharCoordinate = new Coordinate(x + c.x, y + c.y);

                    // Put the new character in the drawable area.
                    this.drawArea.put(newCharCoordinate, otherDrawArea.getAt(c.x, c.y));

                    // Update width and height of the draw area.
                    updateWidthHeight(newCharCoordinate);
                });
    }

    /**
     * Draws the specified string at the specified coordinates with the specified color.
     *
     * @param x      The x-coordinate at which to draw the string.
     * @param y      The y-coordinate at which to draw the string.
     * @param string The string to draw.
     * @param color  The color to use when drawing the string.
     */
    public void drawAt(int x, int y, String string, ColorsEnum color) {
        String[] lines = string.split("\n");
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                char charToAdd = lines[i].charAt(j);
                if (charToAdd != ' ') {
                    // Create a new coordinate with the x and y coordinates of the new character to put.
                    Coordinate newCharCoordinate = new Coordinate(x + j, y + i);

                    // Put the new character in the drawable area.
                    drawArea.put(newCharCoordinate, new MagicChar(charToAdd, color));

                    // Update width and height of the draw area.
                    updateWidthHeight(newCharCoordinate);
                }
            }
        }
    }

    /**
     * Draws the specified string at the specified coordinates.
     *
     * @param x      The x-coordinate at which to draw the string.
     * @param y      The y-coordinate at which to draw the string.
     * @param string The string to draw.
     */
    public void drawAt(int x, int y, String string) {
        drawAt(x, y, string, null);
    }

    /**
     * Draws the specified integer at the specified coordinates.
     *
     * @param x      The x-coordinate at which to draw the integer.
     * @param y      The y-coordinate at which to draw the integer.
     * @param string The integer to draw.
     */
    public void drawAt(int x, int y, int string) {
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
    public void drawAt(int x, int y, int string, ColorsEnum color) {
        drawAt(x, y, String.valueOf(string), color);
    }

    public void drawNewLine(String string, ColorsEnum color) {
        drawAt(0, height, string, color);
    }

    public void drawNewLine(String string) {
        drawNewLine(string, null);
    }

    /**
     * Returns a string representation of the drawable area.
     *
     * @return A string representation of the drawable area.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
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
     * Converts a positive y-coordinate system to a negative one. And shifts the x-coordinates to the right to put it in the 1st quadrant.
     * This is useful when drawing on a terminal, where the origin is in the top-left corner.
     *
     * @param pair The pairs whose y-coordinates to convert.
     * @param <U>  The type of the values in the pairs.
     * @return The pairs with their y-coordinates converted.
     */
    public <U> ArrayList<Pair<Coordinate, U>> convertCoordinates(ArrayList<Pair<Coordinate, U>> pair) {
        int maxY = pair.stream().mapToInt(p -> p.x().y).max().orElse(0);
        int minX = pair.stream().mapToInt(p -> p.x().x).min().orElse(0);
        pair.forEach(p -> {
            if (minX < 0) {
                p.key().x += Math.abs(minX);
            }
            p.key().y = maxY - p.key().y;
        });
        return pair;
    }

    /**
     * Converts a positive y-coordinate system to a negative one. And shifts the x-coordinates to the right to put it in the 1st quadrant.
     * This is useful when drawing on a terminal, where the origin is in the top-left corner.
     *
     * @param map The map whose y-coordinates to convert.
     * @param <U> The type of the values in the map.
     * @return The map with their y-coordinates converted.
     */
    public <U> HashMap<Coordinate, U> convertCoordinates(HashMap<Coordinate, U> map) {
        int maxY = map.keySet().stream().mapToInt(p -> p.y).max().orElse(0);
        int minX = map.keySet().stream().mapToInt(p -> p.x).min().orElse(0);
        map.keySet().forEach(p -> {
            if (minX < 0) {
                p.x += Math.abs(minX);
            }
            p.y = maxY - p.y;
        });
        return map;
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
     * Draws the specified drawable area centered on the x-axis between the specified starting and ending x-coordinates.
     *
     * @param startingX The starting x-coordinate.
     * @param endingX   The ending x-coordinate.
     * @param y         The y-coordinate at which to draw the drawable area.
     * @param drawArea  The drawable area to draw.
     */
    public void drawCenteredX(int startingX, int endingX, int y, DrawArea drawArea) {
        int diff = (endingX - startingX - drawArea.getWidth()) / 2;
        drawAt(startingX + diff, y, drawArea);
    }

    /**
     * Returns the height of the drawable area.
     *
     * @return The height of the drawable area.
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Returns the width of the drawable area.
     *
     * @return The width of the drawable area.
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * Returns the draw area of the drawable object.
     *
     * @return the draw area of the drawable object.
     */
    @Override
    public DrawArea getDrawArea() {
        return this;
    }
}