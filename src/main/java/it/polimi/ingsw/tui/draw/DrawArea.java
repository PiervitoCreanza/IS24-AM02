package it.polimi.ingsw.tui.draw;

import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class DrawArea implements Drawable {
    protected HashMap<Coordinate, SingleCharacter> drawArea = new HashMap<>();


    public DrawArea(String drawArea) {
        drawAt(0, 0, drawArea);
    }

    public DrawArea(DrawArea other) {
        this.drawArea = new HashMap<>(other.drawArea);
    }

    protected void drawAt(int x, int y, DrawArea drawArea) {
        drawArea.getCoords().stream().filter(c -> drawArea.getCharAt(c.x, c.y) != ' ')
                .forEach(c -> this.drawArea.put(new Coordinate(x + c.x, y + c.y), drawArea.getAt(c.x, c.y)));
    }

    protected void drawAt(int x, int y, String string) {
        String[] lines = string.split("\n");
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                char c = lines[i].charAt(j);
                if (c != ' ')
                    drawArea.put(new Coordinate(x + j, y + i), new SingleCharacter(lines[i].charAt(j)));
            }
        }
    }

    protected void drawAt(int x, int y, String string, ColorsEnum color) {
        String[] lines = string.split("\n");
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                char c = lines[i].charAt(j);
                if (c != ' ')
                    drawArea.put(new Coordinate(x + j, y + i), new SingleCharacter(lines[i].charAt(j), color));
            }
        }

    }

    protected void drawAt(int x, int y, int string) {
        drawAt(x, y, String.valueOf(string));
    }

    protected void drawAt(int x, int y, int string, ColorsEnum color) {
        drawAt(x, y, String.valueOf(string), color);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= getHeight(); i++) {
            for (int j = 0; j <= getWidth(); j++) {
                SingleCharacter c = drawArea.get(new Coordinate(j, i));
                sb.append(c != null ? c.toString() : ' ');
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public SingleCharacter getAt(int x, int y) {
        SingleCharacter c = drawArea.get(new Coordinate(x, y));
        return c != null ? c : new SingleCharacter(' ');
    }

    public char getCharAt(int x, int y) {
        SingleCharacter c = drawArea.get(new Coordinate(x, y));
        return c != null ? c.getCharacter() : ' ';
    }

    public Set<Coordinate> getCoords() {
        return drawArea.keySet();
    }

    public <U> ArrayList<Pair<Coordinate, U>> convertCoordinates(ArrayList<Pair<Coordinate, U>> pair) {
        int maxY = (int) pair.stream().mapToDouble(p -> p.x().getY()).max().orElse(0);
        pair.forEach(p -> p.key().y = maxY - p.key().y);
        return pair;
    }
    
    public void setColor(ColorsEnum color) {
        getCoords().forEach(c -> drawArea.get(c).setColor(color));
    }

    @Override
    public int getHeight() {
        return (int) drawArea.keySet().stream().mapToDouble(Coordinate::getY).max().orElse(0);
    }

    @Override
    public int getWidth() {
        return (int) drawArea.keySet().stream().mapToDouble(Coordinate::getX).max().orElse(0);
    }

}
