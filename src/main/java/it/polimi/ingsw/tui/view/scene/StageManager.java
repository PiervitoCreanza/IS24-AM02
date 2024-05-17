package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.network.virtualView.GlobalBoardView;
import it.polimi.ingsw.network.virtualView.PlayerView;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * This class manages the different scenes in the game.
 */
public class StageManager {

    TUIViewController controller;
    ExecutorService executor = Executors.newSingleThreadExecutor();

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

    public GetGamesScene showGetGamesScene(ArrayList<GameRecord> games) {
        clearScreen();
        GetGamesScene scene = new GetGamesScene(this.controller, games);
        scene.display();
        return scene;
    }

    public WaitForPlayersScene showWaitForPlayersScene(GameControllerView gameControllerView) {
        clearScreen();
        WaitForPlayersScene scene = new WaitForPlayersScene(gameControllerView.gameView().playerViews().stream().map(PlayerView::playerName).collect(Collectors.toCollection(ArrayList::new)), 4);
        scene.display();
        return scene;
    }

    public MainMenuScene showMainMenuScene() {
        clearScreen();
        MainMenuScene scene = null;
        try {
            scene = new MainMenuScene(this.controller);
            scene.display();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scene;
    }

    public JoinGameScene showJoinGameScene() {
        clearScreen();
        JoinGameScene scene = null;
        try {
            scene = new JoinGameScene(this.controller);
            scene.display();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scene;
    }

    public CreateGameScene showCreateGameScene() {
        clearScreen();
        CreateGameScene scene = new CreateGameScene(this.controller);
        scene.display();
        return scene;
    }

    public InitPlaceStarterCardScene showInitPlaceStarterCardScene(GameCard starterCard) {
        clearScreen();
        InitPlaceStarterCardScene scene = new InitPlaceStarterCardScene(this.controller, starterCard);
        scene.display();
        return scene;
    }

    public InitChoosePlayerColorScene showInitChoosePlayerColorScene(ArrayList<PlayerColorEnum> availableColors) {
        clearScreen();
        InitChoosePlayerColorScene scene = new InitChoosePlayerColorScene(this.controller, availableColors);
        scene.display();
        return scene;
    }

    public InitSetObjectiveCardScene showInitSetObjectiveCardScene(ArrayList<ObjectiveCard> objectiveCards) {
        clearScreen();
        InitSetObjectiveCardScene scene = new InitSetObjectiveCardScene(this.controller, objectiveCards);
        scene.display();
        return scene;
    }

    public DrawCardScene showDrawCardScene(HashMap<Coordinate, GameCard> playerBoard, ArrayList<ObjectiveCard> globalObjectives, ObjectiveCard playerObjective, ArrayList<GameCard> hand, GlobalBoardView globalBoardView) {
        clearScreen();
        DrawCardScene scene = new DrawCardScene(this.controller, playerBoard, globalObjectives, playerObjective, hand, globalBoardView);
        scene.display();
        return scene;
    }

    public PlaceCardScene showPlaceCardScene(HashMap<Coordinate, GameCard> playerBoard, ArrayList<ObjectiveCard> globalObjectives, ObjectiveCard playerObjective, ArrayList<GameCard> hand, List<PlayerView> playerViews) {
        clearScreen();
        PlaceCardScene scene = new PlaceCardScene(this.controller, playerBoard, globalObjectives, playerObjective, hand, playerViews, controller.getPlayerName());
        scene.display();
        return scene;
    }
}