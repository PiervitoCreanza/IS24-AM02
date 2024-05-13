package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.tui.Scene.GetGamesScene;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

class GetGamesSceneTest {
    @Test
    void testToString() {
        HashSet<GameRecord> games = new HashSet<>();
        games.add(new GameRecord("Game1", 3, 4));
        games.add(new GameRecord("VeryUltra LimitlessLongGameName", 1, 2));
        GetGamesScene getGamesScene = new GetGamesScene(games);
        System.out.println(getGamesScene);
    }

}