package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.util.ArrayList;

/**
 * This class represents the scene where the player chooses their color.
 */
public class InitChoosePlayerColorScene implements Displayable {
    private final DrawArea drawArea;
    private final TUIViewController controller;

    private UserInputHandler handler;

    /**
     * Constructor for the InitChoosePlayerColorScene class.
     * It initializes the drawArea and populates it with the available colors.
     *
     * @param controller The controller of the game.
     */
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

    /**
     * Constructor for the InitChoosePlayerColorScene class.
     * It initializes the drawArea and populates it with the available colors.
     *
     * @param controller      The controller of the game.
     * @param availableColors The available colors for the player to choose from.
     */
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
    public void display() {
        drawArea.println();

        handler = new UserInputHandler("Choose your color:", input -> input.matches("RED|BLUE|GREEN|YELLOW|red|blue|green|yellow|r|b|g|y"));
        handler.print();
    }

    public void handleUserInput(String input) {
        if (input.equals("q")) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }

        if (handler.validate(input)) {
            PlayerColorEnum chosenColor = null;
            switch (input) {
                case "RED", "red", "r" -> chosenColor = PlayerColorEnum.RED;
                case "BLUE", "blue", "b" -> chosenColor = PlayerColorEnum.BLUE;
                case "GREEN", "green", "g" -> chosenColor = PlayerColorEnum.GREEN;
                case "YELLOW", "yellow", "y" -> chosenColor = PlayerColorEnum.YELLOW;
            }
            controller.choosePlayerColor(chosenColor);
            return;
        }
        handler.print();
    }
}
