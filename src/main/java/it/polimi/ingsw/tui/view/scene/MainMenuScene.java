package it.polimi.ingsw.tui.view.scene;


import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainMenuScene implements Diplayable {
    private final DrawArea drawArea;

    private final TUIViewController controller;

    public MainMenuScene(TUIViewController controller) {
        this.controller = controller;
        this.drawArea = new DrawArea("""
                -- Main Menu --
                Press <l> to list available games.
                Press <j> to join a game.
                Press <c> to create a new game.
                Press <q> to quit.
                """);
    }

    /**
     * Displays the game scene on the console.
     */
    @Override
    public void display() {
        System.out.println(drawArea);
    }

    private void test() throws IOException {
        drawArea.println();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("reading");
        String input = reader.readLine();
        System.out.println("ok");
        switch (input) {
            case "l", "L" -> {
                controller.getGames();
            }
            case "c", "C" -> {
                controller.selectScene(ScenesEnum.CREATE_GAME);
            }
            case "q", "Q" -> {
                System.out.println("Exiting...");
                System.exit(0);
            }
            default -> System.out.println("Invalid input");
        }
    }
}
