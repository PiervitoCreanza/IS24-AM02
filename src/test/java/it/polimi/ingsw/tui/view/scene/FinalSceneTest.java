package it.polimi.ingsw.tui.view.scene;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class FinalSceneTest {
    ArrayList<String> winners = new ArrayList<>();
    ArrayList<String> players = new ArrayList<>();
    FinalScene finalScene;

    @BeforeEach
    void setUp() {
        winners.add("winner1");
        winners.add("winner2");
        players.add("player1");
        players.add("player2");
        players.add("winner1");
        players.add("winner2");
        finalScene = new FinalScene(null, winners, players, 1);
    }

    @Test
    void testConstructor() {
        finalScene.getDrawArea().println();
    }
}
