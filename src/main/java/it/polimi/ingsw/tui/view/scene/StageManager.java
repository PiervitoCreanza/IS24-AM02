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
import java.util.stream.Collectors;

/**
 * This class manages the different scenes in the game.
 * It is responsible for displaying the appropriate scene based on a call from the TUIViewController.
 * It uses an instance of TUIViewController to let the scene interact with the game logic, such as setting the next one.
 */
public class StageManager {

    /**
     * The controller that manages the user interface and the game logic.
     */
    private final TUIViewController controller;

    /**
     * Class constructor.
     *
     * @param controller The controller that manages the user interface and the game logic.
     */
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
     * Shows the GetGamesScene.
     *
     * @param games The list of games to be displayed.
     * @return The GetGamesScene scene.
     */
    public GetGamesScene showGetGamesScene(ArrayList<GameRecord> games) {
        clearScreen();
        GetGamesScene scene = new GetGamesScene(this.controller, games);
        scene.display();
        return scene;
    }

    /**
     * Shows the WaitForPlayersScene.
     *
     * @param gameControllerView The game controller view.
     * @return The WaitForPlayersScene scene.
     */
    public WaitForPlayersScene showWaitForPlayersScene(GameControllerView gameControllerView) {
        clearScreen();
        WaitForPlayersScene scene = new WaitForPlayersScene(gameControllerView.gameView().playerViews().stream().map(PlayerView::playerName).collect(Collectors.toCollection(ArrayList::new)), 4);
        scene.display();
        return scene;
    }

    /**
     * Shows the MainMenuScene.
     *
     * @return The MainMenuScene scene.
     */
    public MainMenuScene showMainMenuScene() {
        clearScreen();
        MainMenuScene scene = new MainMenuScene(this.controller);
        scene.display();
        return scene;
    }

    /**
     * Shows the JoinGameScene.
     *
     * @return The JoinGameScene scene.
     */
    public JoinGameScene showJoinGameScene() {
        clearScreen();
        JoinGameScene scene = new JoinGameScene(this.controller);
        scene.display();
        return scene;
    }

    /**
     * Shows the CreateGameScene.
     *
     * @return The CreateGameScene scene.
     */
    public CreateGameScene showCreateGameScene() {
        clearScreen();
        CreateGameScene scene = new CreateGameScene(this.controller);
        scene.display();
        return scene;
    }

    /**
     * Shows the InitPlaceStarterCardScene.
     *
     * @param starterCard The starter card to be placed.
     * @return The InitPlaceStarterCardScene scene.
     */
    public InitPlaceStarterCardScene showInitPlaceStarterCardScene(GameCard starterCard) {
        clearScreen();
        InitPlaceStarterCardScene scene = new InitPlaceStarterCardScene(this.controller, starterCard);
        scene.display();
        return scene;
    }

    /**
     * Shows the InitChoosePlayerColorScene.
     *
     * @param availableColors The list of available colors.
     * @return The InitChoosePlayerColorScene scene.
     */
    public InitChoosePlayerColorScene showInitChoosePlayerColorScene(ArrayList<PlayerColorEnum> availableColors) {
        clearScreen();
        InitChoosePlayerColorScene scene = new InitChoosePlayerColorScene(this.controller, availableColors);
        scene.display();
        return scene;
    }

    /**
     * Shows the InitSetObjectiveCardScene.
     *
     * @param objectiveCards The list of objective cards.
     * @return The InitSetObjectiveCardScene scene.
     */
    public InitSetObjectiveCardScene showInitSetObjectiveCardScene(ArrayList<ObjectiveCard> objectiveCards) {
        clearScreen();
        InitSetObjectiveCardScene scene = new InitSetObjectiveCardScene(this.controller, objectiveCards);
        scene.display();
        return scene;
    }

    /**
     * Shows the DrawCardScene.
     *
     * @param playerBoard      The player board.
     * @param globalObjectives The global objectives.
     * @param playerObjective  The player objective.
     * @param hand             The player hand.
     * @param globalBoardView  The global board view.
     * @return The DrawCardScene scene.
     */
    public DrawCardScene showDrawCardScene(HashMap<Coordinate, GameCard> playerBoard, ArrayList<ObjectiveCard> globalObjectives, ObjectiveCard playerObjective, ArrayList<GameCard> hand, GlobalBoardView globalBoardView) {
        clearScreen();
        DrawCardScene scene = new DrawCardScene(this.controller, playerBoard, globalObjectives, playerObjective, hand, globalBoardView);
        scene.display();
        return scene;
    }

    /**
     * Shows the PlaceCardScene.
     *
     * @param playerBoard      The player board.
     * @param globalObjectives The global objectives.
     * @param playerObjective  The player objective.
     * @param hand             The player hand.
     * @param playerViews      The player views.
     * @return The PlaceCardScene scene.
     */
    public PlaceCardScene showPlaceCardScene(HashMap<Coordinate, GameCard> playerBoard, ArrayList<ObjectiveCard> globalObjectives, ObjectiveCard playerObjective, ArrayList<GameCard> hand, List<PlayerView> playerViews) {
        clearScreen();
        PlaceCardScene scene = new PlaceCardScene(this.controller, playerBoard, globalObjectives, playerObjective, hand, playerViews, controller.getPlayerName());
        scene.display();
        return scene;
    }

    /**
     * Shows the OtherPlayerTurnScene.
     *
     * @param playerName  The player name.
     * @param playerColor The player color.
     * @param playerBoard The player board.
     * @return The OtherPlayerTurnScene scene.
     */
    public OtherPlayerTurnScene showOtherPlayerTurnScene(String playerName, PlayerColorEnum playerColor, HashMap<Coordinate, GameCard> playerBoard) {
        clearScreen();
        OtherPlayerTurnScene scene = new OtherPlayerTurnScene(playerName, playerColor, playerBoard);
        scene.display();
        return scene;
    }

    /**
     * Shows the GamePausedScene.
     *
     * @return The GamePausedScene scene.
     */
    public GamePausedScene showGamePausedScene() {
        clearScreen();
        GamePausedScene scene = new GamePausedScene();
        scene.display();
        return scene;
    }
}