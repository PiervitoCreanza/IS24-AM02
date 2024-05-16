package it.polimi.ingsw.tui.view.scene;


import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

public class MainMenuScene implements Displayable {
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
    public void display() {
        this.drawArea.println();
    }

    public void handleUserInput(String input) {
        switch (input) {
            case "l", "L" -> this.controller.getGames();
            case "c", "C" -> this.controller.selectScene(ScenesEnum.CREATE_GAME);
            case "q", "Q" -> {
                System.out.println("Exiting...");
                System.exit(0);
            }
            default -> System.out.println("Invalid input");
        }
    }


}
