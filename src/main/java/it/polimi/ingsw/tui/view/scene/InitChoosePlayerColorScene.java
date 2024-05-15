package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class InitChoosePlayerColorScene implements Displayable, UserInputScene {
    private final DrawArea drawArea;
    private final TUIViewController controller;

    public InitChoosePlayerColorScene(TUIViewController controller) {
        this.drawArea = new TitleComponent("Choose your color").getDrawArea();
        DrawArea colorArea = new DrawArea();
        colorArea.drawAt(0, 0, "■", ColorsEnum.RED);
        colorArea.drawAt(4, 0, "■", ColorsEnum.BLUE);
        colorArea.drawAt(8, 0, "■", ColorsEnum.GREEN);
        colorArea.drawAt(12, 0, "■", ColorsEnum.YELLOW);
        drawArea.drawCenteredX(drawArea.getHeight(), colorArea);
        this.controller = controller;
    }

    // If controller can pass an array of possible colors, then the following constructor can be used.
    public InitChoosePlayerColorScene(TUIViewController controller, ArrayList<PlayerColorEnum> availableColors) {
        this.drawArea = new TitleComponent("Choose your color").getDrawArea();
        DrawArea colorArea = new DrawArea();
        for (int i = 0; i < availableColors.size(); i++) {
            switch (availableColors.get(i)) {
                case RED -> colorArea.drawAt(0, i, "■", ColorsEnum.RED);
                case BLUE -> colorArea.drawAt(0, i, "■", ColorsEnum.BLUE);
                case GREEN -> colorArea.drawAt(0, i, "■", ColorsEnum.GREEN);
                case YELLOW -> colorArea.drawAt(0, i, "■", ColorsEnum.YELLOW);
            }
        }
        drawArea.drawCenteredX(this.drawArea.getHeight(), colorArea);
        this.controller = controller;
    }

    /**
     * This method is used to display the object.
     */
    @Override
    public void display() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        drawArea.println();

        String color = UserInputScene.getAndValidateInput("Choose your color:", input -> input.matches("RED|BLUE|GREEN|YELLOW|red|blue|green|yellow"), reader);
        // Back to main menu if user quits
        if (color == null) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }
        controller.choosePlayerColor(color);
    }

}
