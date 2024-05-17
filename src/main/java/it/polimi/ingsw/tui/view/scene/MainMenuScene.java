package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

/**
 * The MainMenuScene class represents the main menu scene of the game.
 * It implements the Scene interface.
 */
public class MainMenuScene implements Scene {
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