package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.component.GameInfoComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GetGamesScene implements Diplayable {
    private final DrawArea drawArea;
    private final TUIViewController controller;

    public GetGamesScene(ArrayList<GameRecord> games, TUIViewController controller) {
        this.drawArea = new DrawArea("""
                -- Games list --
                """);

        int i = 0;
        for (GameRecord game : games) {
            drawArea.drawAt(0, drawArea.getHeight(), new GameInfoComponent(game, i++).getDrawArea());
        }
        drawArea.drawAt(0, drawArea.getHeight(), "-".repeat(drawArea.getWidth()));
        drawArea.drawAt(0, drawArea.getHeight(), """
                Press <l> to refresh the list.
                Press <j> to join a game.
                Press <c> to create a new game.
                Press <q> to quit the menu.
                """, ColorsEnum.YELLOW);
        this.controller = controller;
    }

    /**
     * Displays the game scene on the console.
     */
    @Override
    public void display() throws IOException {
        System.out.println(drawArea);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();
        switch (input) {
            case "l", "L" -> {
                controller.getGames();
            }
            case "j", "J" -> {
                controller.selectScene(ScenesEnum.JOIN_GAME);
            }
            case "c", "C" -> {
                controller.selectScene(ScenesEnum.CREATE_GAME);
            }
            case "q", "Q" -> {
                controller.selectScene(ScenesEnum.MAIN_MENU);
            }
            default -> System.out.println("Invalid input");
        }
    }
}
