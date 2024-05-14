package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.tui.utils.Utils;

import java.util.HashSet;

/**
 * This class manages the different scenes in the game.
 */
public class StageManager {

    /**
     * Clears the console screen based on the operating system.
     */
    private static void clearScreen() {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                System.out.print(Utils.ANSI_CLEAR_UNIX);
            } else {
                // Fallback to printing new lines if OS detection failed
                for (int i = 0; i < 100; i++) {
                    System.out.println();
                }
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Displays the scene for getting games.
     *
     * @param games The set of game records to be displayed.
     */
    public void showGetGamesScene(HashSet<GameRecord> games) {
        clearScreen();
        new GetGamesScene(games).display();
    }

    /**
     * Displays the game scene.
     *
     * @param gameControllerView The view of the game controller.
     * @param clientPlayerName   The name of the client player.
     */
    public void showGameScene(GameControllerView gameControllerView, String clientPlayerName) {
        clearScreen();
        new GameScene(gameControllerView, clientPlayerName).display();
    }

    /**
     * Displays the scene for waiting for players.
     *
     * @param gameControllerView The view of the game controller.
     */
    public void showWaitForPlayersScene(GameControllerView gameControllerView) {
        clearScreen();
        new WaitForPlayersScene(gameControllerView).display();
    }

    /**
     * Displays the main menu scene.
     */
    public void showMainMenuScene() {
        clearScreen();
        new MainMenuScene().display();
    }
}