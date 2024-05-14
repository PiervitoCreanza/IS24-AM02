package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CreateGameScene implements Diplayable, UserInputScene {
    private final DrawArea drawArea;
    private final TUIViewController controller;

    public CreateGameScene(TUIViewController controller) {
        this.drawArea = new TitleComponent("Create Game").getDrawArea();
        this.controller = controller;
    }

    /**
     * This method is used to display the object.
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

        String numberOfPlayers = UserInputScene.getAndValidateInput("Choose the number of players [1-4]:", input -> input.matches("[1-4]"), reader);
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
