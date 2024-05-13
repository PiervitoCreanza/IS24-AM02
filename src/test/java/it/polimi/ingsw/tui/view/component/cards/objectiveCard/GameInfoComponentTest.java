package it.polimi.ingsw.tui.draw;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import org.junit.jupiter.api.Test;

class GameInfoComponentTest {
    @Test
    void testToString() {
        GameRecord game = new GameRecord("VeryUltra LimitlessLongGameName", 1, 2);
        GameInfoComponent gameInfoComponent = new GameInfoComponent(game, 0);
        System.out.println(gameInfoComponent);
    }

}