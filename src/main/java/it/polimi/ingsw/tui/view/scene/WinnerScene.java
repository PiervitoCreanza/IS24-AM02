package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.network.virtualView.PlayerView;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.component.PlayerComponent;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.util.HashMap;
import java.util.List;

public class WinnerScene implements Scene {
    /**
     * The DrawArea object where the scene will be drawn.
     */
    private final DrawArea drawArea;

    /**
     * The controller that manages the user interface and the game logic.
     */
    private final TUIViewController controller;

    /**
     * Constructs a new MainMenuScene.
     *
     * @param controller the controller for this scene
     */
    public WinnerScene(TUIViewController controller, List<String> winners, List<PlayerView> players, int spacing) {
        this.controller = controller;
        this.drawArea = new DrawArea();
        DrawArea titleDrawArea = new DrawArea();
        DrawArea winnersDrawArea = new DrawArea();
        DrawArea losersDrawArea = new DrawArea();

        HashMap<PlayerColorEnum, ColorsEnum> colors = new HashMap<>();
        colors.put(PlayerColorEnum.RED, ColorsEnum.BRIGHT_RED);
        colors.put(PlayerColorEnum.BLUE, ColorsEnum.BRIGHT_BLUE);
        colors.put(PlayerColorEnum.GREEN, ColorsEnum.BRIGHT_GREEN);
        colors.put(PlayerColorEnum.YELLOW, ColorsEnum.BRIGHT_YELLOW);


        for (PlayerView player : players) {
            if (winners.contains(player.playerName())) {
                winnersDrawArea.drawAt((winnersDrawArea.getWidth() == 0) ? 0 : winnersDrawArea.getWidth() + spacing, 0, String.valueOf(new PlayerComponent(player.playerName(), player.playerPos())), colors.get(player.color()));
            } else {
                losersDrawArea.drawAt((losersDrawArea.getWidth() == 0) ? 0 : losersDrawArea.getWidth() + spacing, 0, String.valueOf(new PlayerComponent(player.playerName(), player.playerPos())), colors.get(player.color()));
            }
        }

        int widthMax = Math.max(winnersDrawArea.getWidth(), losersDrawArea.getWidth());
        titleDrawArea.drawAt(0, 0, new TitleComponent("Game Over", widthMax));

        this.drawArea.drawAt(0, 0, titleDrawArea);
        this.drawArea.drawAt(0, drawArea.getHeight(), (winners.size() == 1) ? "The winner is:" : "The winners are:", ColorsEnum.BRIGHT_GREEN);
        this.drawArea.drawCenteredX(drawArea.getHeight(), winnersDrawArea);
        this.drawArea.drawAt(0, drawArea.getHeight() + 1, (players.size() - winners.size() == 1) ? "Other player:" : "Other players:");
        this.drawArea.drawCenteredX(drawArea.getHeight(), losersDrawArea);
        this.drawArea.drawNewLine("""
                Press <q> to quit.
                """, 1);
    }

    /**
     * This method is used to display the scene to the user.
     */
    @Override
    public void display() {
        this.drawArea.println();
    }

    /**
     * This method is used to handle user input.
     *
     * @param input the user's input
     */
    public void handleUserInput(String input) {
        if (input.equalsIgnoreCase("q")) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
        } else {
            System.out.println("Invalid input");
        }
    }

    public DrawArea getDrawArea() {
        return drawArea;
    }
}
