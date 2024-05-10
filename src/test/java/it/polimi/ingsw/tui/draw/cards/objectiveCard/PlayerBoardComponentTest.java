package it.polimi.ingsw.tui.draw.cards.objectiveCard;

import it.polimi.ingsw.network.virtualView.PlayerView;
import org.junit.jupiter.api.Test;

class PlayerBoardComponentTest {

    @Test
    void testToString() {
        PlayerBoardComponent playerBoardComponent = new PlayerBoardComponent(new PlayerView(null, 1, null, null, true, null, null, null));
        System.out.println(playerBoardComponent);
    }
}