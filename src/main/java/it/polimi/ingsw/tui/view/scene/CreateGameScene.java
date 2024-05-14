package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.InputRequestComponent;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CreateGameScene implements Diplayable {
    private final DrawArea drawArea;
    private final TUIViewController controller;

    public CreateGameScene(TUIViewController controller) {
        this.drawArea = new TitleComponent("Create Game").getDrawArea();
        this.controller = controller;
    }

    private void redrawWithAnswer(InputRequestComponent component, String answer) {
        component.setAnswer(answer);
        drawArea.redrawArea(component);
    }

    /**
     * This method is used to display the object.
     */
    @Override
    public void display() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(drawArea);
        String gameName;
        do {
            new InputRequestComponent("Choose the name of the game:").print();
            gameName = reader.readLine();
        } while (gameName.isEmpty());

        String numberOfPlayers;
        do {
            new InputRequestComponent("Choose the number of players [1-4]:").print();
            numberOfPlayers = reader.readLine();
        } while (!numberOfPlayers.matches("[1-4]"));

        String playerName;
        do {
            new InputRequestComponent("Enter your nickname:").print();
            playerName = reader.readLine();
        } while (playerName.isEmpty());
        controller.createGame(gameName, playerName, Integer.parseInt(numberOfPlayers));
    }
}
