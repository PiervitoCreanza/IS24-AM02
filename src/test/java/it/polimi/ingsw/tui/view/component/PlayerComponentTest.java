package it.polimi.ingsw.tui.view.component;

import org.junit.jupiter.api.Test;

public class PlayerComponentTest {
    @Test
    public void testGetHeight() {
        PlayerComponent playerComponent = new PlayerComponent("Player");
        playerComponent.getDrawArea().println();
    }
}
