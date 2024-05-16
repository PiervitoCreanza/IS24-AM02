package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The CreateGameScene class represents the scene where the player can create a new game.
 * It implements the Displayable and UserInputScene interfaces.
 */
public class CreateGameScene implements Displayable, UserInputScene {
    /**
     * The DrawArea object where the scene will be drawn.
     */
    private final DrawArea drawArea;
    /**
     * The controller that manages the user interface and the game logic.
     */
    private final TUIViewController controller;

    /**
     * Constructs a new CreateGameScene.
     *
     * @param controller the controller for this scene
     */
    public CreateGameScene(TUIViewController controller) {
        this.drawArea = new TitleComponent("Create Game").getDrawArea();
        this.controller = controller;
    }

    /**
     * This method is used to display the scene to the user.
     * It prompts the user to enter the name of the game, the number of players, and their nickname.
     * If the user enters valid input, it calls the controller's createGame method to create the game.
     * If the user enters invalid input or quits, it returns to the main menu.
     */
    @Override
    public void display() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(drawArea);
        String gameName = UserInputScene.getAndValidateInput("Choose the name of the game:", input -> !input.isEmpty(), reader);
        if (gameName == null) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }

        String numberOfPlayers = UserInputScene.getAndValidateInput("Choose the number of players [2-4]:", input -> input.matches("[2-4]"), reader);
        if (numberOfPlayers == null) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }

        String playerName = UserInputScene.getAndValidateInput("Enter your nickname:", input -> !input.isEmpty(), reader);
        if (playerName == null) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }
        controller.createGame(gameName, playerName, Integer.parseInt(numberOfPlayers));
    }
}