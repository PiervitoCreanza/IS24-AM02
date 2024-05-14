package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.InputRequestComponent;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JoinGameScene implements Diplayable {
    private final DrawArea drawArea;

    private final TUIViewController controller;

    public JoinGameScene(TUIViewController controller) {
        drawArea = new TitleComponent("Join Game").getDrawArea();
        this.controller = controller;
    }

    /**
     * This method is used to display the object.
     */
    @Override
    public void display() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(drawArea);
        String GameId;
        do {
            new InputRequestComponent("Enter game name:").print();
            GameId = reader.readLine();
            if ("q".equals(GameId)) {
                controller.selectScene(ScenesEnum.MAIN_MENU);
                return;
            }
        } while (!GameId.matches("\\d+"));

        String playerName;
        do {
            new InputRequestComponent("Enter your nickname:").print();
            playerName = reader.readLine();
            if ("q".equals(playerName)) {
                controller.selectScene(ScenesEnum.MAIN_MENU);
                return;
            }
        } while (playerName.isEmpty());

        controller.joinGame(Integer.parseInt(GameId), playerName);
    }
}