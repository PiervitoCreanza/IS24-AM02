package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

/**
 * The JoinGameScene class represents the scene where a player can join a game.
 * It implements the Scene interface.
 */
public class JoinGameScene implements Scene {
    /**
     * The DrawArea object where the scene will be drawn.
     */
    private final DrawArea drawArea;

    /**
     * The controller that manages the user interface and the game logic.
     */
    private final TUIViewController controller;

    private final UserInputHandler gameIdHandler;
    private final UserInputHandler nicknameHandler;
    private JoinStatus currentStatus;

    /**
     * Constructs a new JoinGameScene.
     *
     * @param controller the controller for this scene
     */
    public JoinGameScene(TUIViewController controller) {
        this.gameIdHandler = new UserInputHandler("Enter game ID:", input -> input.matches("\\d+"));
        this.nicknameHandler = new UserInputHandler("Enter your nickname:", input -> !input.isEmpty());
        this.drawArea = new TitleComponent("Join Game").getDrawArea();
        this.currentStatus = JoinStatus.GAME_ID;
        this.controller = controller;
    }

    /**
     * This method is used to display the scene to the user.
     */
    @Override
    public void display() {
        drawArea.println();
        gameIdHandler.print();
    }

    /**
     * This method is used to handle user input.
     * It performs different actions based on the input and the current status of the scene.
     *
     * @param input the user's input
     */
    public void handleUserInput(String input) {
        if (currentStatus == JoinStatus.GAME_ID) {
            handleGameId(input);
            return;
        }
        if (currentStatus == JoinStatus.NICKNAME) {
            handleNickname(input);
        }
    }

    private void handleGameId(String input) {
        if (input.equals("q")) {
            // Go back to action chooser
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }
        if (gameIdHandler.validate(input)) {
            currentStatus = JoinStatus.NICKNAME;
            nicknameHandler.print();
        } else {
            gameIdHandler.print();
        }
    }

    private void handleNickname(String input) {
        if (input.equals("q")) {
            // Go back to action chooser
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }
        if (nicknameHandler.validate(input)) {
            controller.joinGame(Integer.parseInt(gameIdHandler.getInput()), nicknameHandler.getInput());
            currentStatus = JoinStatus.GAME_ID;
        } else {
            nicknameHandler.print();
        }
    }

    /**
     * The current status of the scene.
     */
    private enum JoinStatus {
        GAME_ID,
        NICKNAME
    }
}