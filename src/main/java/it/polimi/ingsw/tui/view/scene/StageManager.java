package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;

import java.util.HashSet;

public class StageManager {

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void showGetGamesScene(HashSet<GameRecord> games) {
        clearScreen();
        System.out.flush();
        System.out.println(new GetGamesScene(games));
    }

    public void showGameScene(GameControllerView gameControllerView, String clientPlayerName) {
        clearScreen();
        System.out.println(new GameScene(gameControllerView, clientPlayerName));
    }

    public void showWaitForPlayersScene(GameControllerView gameControllerView) {
        clearScreen();
        System.out.println(new WaitForPlayersScene(gameControllerView));
    }

    public void showMainMenuScene() {
        clearScreen();
        System.out.println(new MainMenuScene());
    }
}
