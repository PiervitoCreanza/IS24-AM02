package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class manages the different scenes in the game.
 */
public class StageManager {

    TUIViewController controller;

    public StageManager(TUIViewController controller) {
        this.controller = controller;
    }

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
    public void showGetGamesScene(ArrayList<GameRecord> games) {
        clearScreen();
        try {
            new GetGamesScene(this.controller, games).display();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        try {
            new MainMenuScene(this.controller).display();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Displays the scene for joining a game.
     */
    public void showJoinGameScene() {
        clearScreen();
        try {
            new JoinGameScene(this.controller).display();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the scene for creating a game.
     */
    public void showCreateGameScene() {
        clearScreen();
        try {
            new CreateGameScene(this.controller).display();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the scene for selecting the starter card.
     */
    public void showInitPlaceStarterCardScene(GameCard starterCard) {
        clearScreen();
        try {
            new InitPlaceStarterCardScene(this.controller, starterCard).display();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showInitChoosePlayerColorScene(ArrayList<PlayerColorEnum> availableColors) {
        clearScreen();
        try {
            new InitChoosePlayerColorScene(this.controller, availableColors).display();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showInitSetObjectiveCardScene(ArrayList<ObjectiveCard> objectiveCards) {
        clearScreen();
        try {
            new InitSetObjectiveCardScene(this.controller, objectiveCards).display();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDrawCardScene(ArrayList<ObjectiveCard> globalObjectives, ObjectiveCard playerObjective, ArrayList<GameCard> hand) {
        clearScreen();
        try {
            new DrawCardScene(this.controller, globalObjectives, playerObjective, hand).display();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPlaceCardScene(HashMap<Coordinate, GameCard> playerBoard, ArrayList<ObjectiveCard> globalObjectives, ObjectiveCard playerObjective, ArrayList<GameCard> hand) {
        clearScreen();
        try {
            new PlaceCardScene(this.controller, playerBoard, globalObjectives, playerObjective, hand).display();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}