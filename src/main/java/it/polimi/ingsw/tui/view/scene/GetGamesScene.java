package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.component.GameInfoComponent;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.util.ArrayList;

/**
 * This class represents the scene for getting games.
 * It implements the Displayable interface, meaning it can be displayed in the UI.
 */
public class GetGamesScene implements Displayable {
    private final DrawArea drawArea;
    private final TUIViewController controller;

    /**
     * Constructs a new GetGamesScene.
     *
     * @param controller the controller for this scene
     * @param games      the list of games to be displayed
     */
    public GetGamesScene(TUIViewController controller, ArrayList<GameRecord> games) {
        this.drawArea = new TitleComponent("Game List").getDrawArea();
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
     * It reads the user input and performs the corresponding action.
     */
    @Override
    public void display() {
        drawArea.println();
    }

    /**
     * Returns the draw area of the get games scene.
     *
     * @return the draw area of the get games scene
     */
    public DrawArea getDrawArea() {
        return drawArea;
    }

    public void handleUserInput(String input) {
        switch (input) {
            case "l", "L" -> controller.getGames();
            case "j", "J" -> controller.selectScene(ScenesEnum.JOIN_GAME);
            case "c", "C" -> controller.selectScene(ScenesEnum.CREATE_GAME);
            case "q", "Q" -> controller.selectScene(ScenesEnum.MAIN_MENU);
            default -> System.out.println("Invalid input");
        }
    }
}