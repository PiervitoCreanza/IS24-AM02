package it.polimi.ingsw.tui.view.component.cards.objectiveCard;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.tui.view.component.GameInfoComponent;
import org.junit.jupiter.api.Test;

class GameInfoComponentTest {
    @Test
    void testToString() {
        GameRecord game = new GameRecord("VeryUltra LimitlessLongGameName", 1, 2);
        GameInfoComponent gameInfoComponent = new GameInfoComponent(game, 0);
        System.out.println(gameInfoComponent);
    }

}