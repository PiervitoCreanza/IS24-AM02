package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.component.userInputHandler.UserInputHandler;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.util.ArrayList;

/**
 * The InitChoosePlayerColorScene class represents the scene where the player chooses their color.
 * It implements the Scene interface.
 */
public class InitChoosePlayerColorScene implements Scene {
    /**
     * The DrawArea object where the scene will be drawn.
     */
    private final DrawArea drawArea;

    /**
     * The controller that manages the user interface and the game logic.
     */
    private final TUIViewController controller;

    /**
     * The handler for user input.
     */
    private UserInputHandler handler;

    /**
     * Constructs a new InitChoosePlayerColorScene.
     * It initializes the drawArea and populates it with the available colors.
     *
     * @param controller      the controller for this scene
     * @param availableColors the available colors for the player to choose from
     */
    public InitChoosePlayerColorScene(TUIViewController controller, ArrayList<PlayerColorEnum> availableColors) {
        this.drawArea = new TitleComponent("Choose your color").getDrawArea();
        DrawArea colorArea = new DrawArea();
        for (int i = 0; i < availableColors.size(); i++) {
            switch (availableColors.get(i)) {
                case RED -> colorArea.drawAt(0, i, "■ - Red <r>", ColorsEnum.RED);
                case BLUE -> colorArea.drawAt(0, i, "■ - Blue <b>", ColorsEnum.BLUE);
                case GREEN -> colorArea.drawAt(0, i, "■ - Green <g>", ColorsEnum.GREEN);
                case YELLOW -> colorArea.drawAt(0, i, "■ - Yellow <y>", ColorsEnum.YELLOW);
            }
        }
        drawArea.drawCenteredX(this.drawArea.getHeight(), colorArea);
        this.controller = controller;
    }

    /**
     * This method is used to display the scene to the user.
     */
    @Override
    public void display() {
        drawArea.println();
        handler = new UserInputHandler("Choose your color:", input -> input.toLowerCase().matches("red|blue|green|yellow|r|b|g|y"));
        handler.print();
    }

    /**
     * This method is used to handle user input.
     *
     * @param input the user's input
     */
    public void handleUserInput(String input) {
        if (input.equals("q")) {
            controller.sendDisconnect();
            controller.closeConnection();
            return;
        }

        if (handler.validate(input)) {
            PlayerColorEnum chosenColor = null;
            switch (input.toLowerCase()) {
                case "red", "r" -> chosenColor = PlayerColorEnum.RED;
                case "blue", "b" -> chosenColor = PlayerColorEnum.BLUE;
                case "green", "g" -> chosenColor = PlayerColorEnum.GREEN;
                case "yellow", "y" -> chosenColor = PlayerColorEnum.YELLOW;
            }
            controller.choosePlayerColor(chosenColor);
            return;
        }
        handler.print();
    }

    public DrawArea getDrawArea() {
        return drawArea;
    }
}