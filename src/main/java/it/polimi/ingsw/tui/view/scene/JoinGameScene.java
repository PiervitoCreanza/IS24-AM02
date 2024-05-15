package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JoinGameScene implements Displayable {
    private final DrawArea drawArea;

    private final TUIViewController controller;

    public JoinGameScene(TUIViewController controller) {
        this.drawArea = new TitleComponent("Join Game").getDrawArea();
        this.controller = controller;
    }

    /**
     * This method is used to display the object.
     */
    @Override
    public void display() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        drawArea.println();

        String GameId = UserInputScene.getAndValidateInput("Enter game ID:", input -> input.matches("\\d+"), reader);
        if (GameId == null) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }

        String playerName = UserInputScene.getAndValidateInput("Enter your nickname:", input -> !input.isEmpty(), reader);
        if (playerName == null) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }

        controller.joinGame(Integer.parseInt(GameId), playerName);
    }
}