package it.polimi.ingsw.tui.view.scene;


import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainMenuScene implements Diplayable {
    private final DrawArea drawArea;

    private final TUIViewController controller;

    public MainMenuScene(TUIViewController controller) {
        this.controller = controller;
        this.drawArea = new TitleComponent("Main Menu").getDrawArea();
        this.drawArea.drawNewLine("""
                Press <l> to list available games.
                Press <c> to create a new game.
                Press <q> to quit.
                """, 0);
    }

    /**
     * Displays the game scene on the console.
     */
    @Override
    public void display() throws IOException {
        this.drawArea.println();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();
        switch (input) {
            case "l", "L" -> {
                this.controller.getGames();
            }
            case "c", "C" -> {
                this.controller.selectScene(ScenesEnum.CREATE_GAME);
            }
            case "q", "Q" -> {
                System.out.println("Exiting...");
                System.exit(0);
            }
            default -> System.out.println("Invalid input");
        }
    }
}
