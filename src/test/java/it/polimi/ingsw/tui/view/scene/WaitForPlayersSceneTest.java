package it.polimi.ingsw.tui.view.scene;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class WaitForPlayersSceneTest {

    @Test
    public void testWaitForPlayersScene() {
        ArrayList<String> playersName = new ArrayList<>();
        playersName.add("player1");
        playersName.add("player2");
        playersName.add("player3");
        playersName.add("player4");

        WaitForPlayersScene waitForPlayersScene = new WaitForPlayersScene(playersName, 4);
        waitForPlayersScene.display();
    }
}
