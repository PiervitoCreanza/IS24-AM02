package it.polimi.ingsw.tui.view.scene;


import it.polimi.ingsw.tui.view.drawer.DrawArea;

public class MainMenuScene {
    private final DrawArea drawArea;

    public MainMenuScene() {
        drawArea = new DrawArea("""
                -- Main Menu --
                Press <l> to list available games.
                Press <j> to join a game.
                Press <c> to create a new game.
                Press <q> to quit.
                """);
    }

    @Override
    public String toString() {
        return drawArea.toString();
    }
}
