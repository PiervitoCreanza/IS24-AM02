package it.polimi.ingsw.tui.view.drawer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DrawAreaTest {

    @Test
    void toStringTest() {
        DrawArea drawArea = new DrawArea("""
                This
                test
                """);
        assertEquals("""
                This
                test
                """, drawArea.toString());
    }

    void overlapTest() {
        DrawArea drawArea = new DrawArea("""
                Ciao
                """);

    }

    @Test
    void getAtTest() {
        DrawArea drawArea = new DrawArea("""
                This is a
                test
                """);
        assertEquals('T', drawArea.getCharAt(0, 0));
        assertEquals('h', drawArea.getCharAt(1, 0));
        assertEquals('i', drawArea.getCharAt(2, 0));
        assertEquals('s', drawArea.getCharAt(3, 0));
        assertEquals(' ', drawArea.getCharAt(4, 0));
        assertEquals('i', drawArea.getCharAt(5, 0));
        assertEquals('s', drawArea.getCharAt(6, 0));
        assertEquals(' ', drawArea.getCharAt(7, 0));
        assertEquals('a', drawArea.getCharAt(8, 0));
        assertEquals('t', drawArea.getCharAt(0, 1));
        assertEquals('e', drawArea.getCharAt(1, 1));
        assertEquals('s', drawArea.getCharAt(2, 1));
        assertEquals('t', drawArea.getCharAt(3, 1));
        assertEquals(' ', drawArea.getCharAt(4, 1));
    }

}