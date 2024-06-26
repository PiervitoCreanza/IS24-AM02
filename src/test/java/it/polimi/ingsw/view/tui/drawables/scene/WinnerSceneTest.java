package it.polimi.ingsw.view.tui.drawables.scene;

import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.network.virtualView.PlayerView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class WinnerSceneTest {
    ArrayList<String> winners = new ArrayList<>();
    List<PlayerView> players = new ArrayList<>();
    WinnerScene winnerScene;

    @BeforeEach
    void setUp() {
        winners.add("winner1");
        players.add(new PlayerView("player1", PlayerColorEnum.RED, 23, null, null, true, null, null, null));
        players.add(new PlayerView("player2", PlayerColorEnum.BLUE, 20, null, null, true, null, null, null));
        players.add(new PlayerView("winner1", PlayerColorEnum.GREEN, 26, null, null, true, null, null, null));
        players.add(new PlayerView("winner2", PlayerColorEnum.YELLOW, 26, null, null, true, null, null, null));
        winnerScene = new WinnerScene(null, winners, players, 1);
    }

    @Test
    @DisplayName("Test Display of Final Scene")
    void testConstructor() {
        winnerScene.getDrawArea().println();
    }
}
