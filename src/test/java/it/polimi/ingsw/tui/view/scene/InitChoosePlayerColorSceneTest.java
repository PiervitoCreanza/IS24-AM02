package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.player.PlayerColorEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class InitChoosePlayerColorSceneTest {

    ArrayList<PlayerColorEnum> availableColors;

    @BeforeEach
    void setUp() {
        availableColors = new ArrayList<>();
    }

    @Test
    @DisplayName("All colors available")
    void testAllColorsAvailable() {
        PlayerColorEnum.stream().forEach(availableColors::add);
        InitChoosePlayerColorScene initChoosePlayerColorScene = new InitChoosePlayerColorScene(null, availableColors);
        initChoosePlayerColorScene.getDrawArea().println();
    }
}
