package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class GetGamesSceneTest {
    @Test
    void testToString() {
        ArrayList<GameRecord> games = new ArrayList<>();
        games.add(new GameRecord("Game1", 3, 4));
        games.add(new GameRecord("VeryUltra LimitlessLongGameName", 1, 2));
        GetGamesScene getGamesScene = new GetGamesScene(games, null);
        System.out.println(getGamesScene);
    }

}