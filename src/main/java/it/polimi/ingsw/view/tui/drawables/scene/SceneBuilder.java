package it.polimi.ingsw.view.tui.drawables.scene;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.server.message.ChatServerToClientMessage;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.network.virtualView.GlobalBoardView;
import it.polimi.ingsw.network.virtualView.PlayerView;
import it.polimi.ingsw.view.tui.controller.TUIViewController;
import it.polimi.ingsw.view.tui.drawables.scene.chatScene.ChatScene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class manages the different scenes in the game.
 * It is responsible for displaying the appropriate scene based on a call from the TUIViewController.
 * It uses an instance of TUIViewController to let the scene interact with the game logic, such as setting the next one.
 */
public class SceneBuilder {

    /**
     * The controller that manages the user interface and the game logic.
     */
    private final TUIViewController controller;

    /**
     * Class constructor.
     *
     * @param controller The controller that manages the user interface and the game logic.
     */
    public SceneBuilder(TUIViewController controller) {
        this.controller = controller;
    }

    /**
     * Instances the GetGamesScene.
     *
     * @param games The list of games to be displayed.
     * @return The GetGamesScene scene.
     */
    public GetGamesScene instanceGetGamesScene(ArrayList<GameRecord> games) {
        return new GetGamesScene(this.controller, games);
    }

    /**
     * Instances the WaitForPlayersScene.
     *
     * @param gameControllerView The game controller view.
     * @return The WaitForPlayersScene scene.
     */
    public WaitForPlayersScene instanceWaitForPlayersScene(GameControllerView gameControllerView) {
        return new WaitForPlayersScene(this.controller, gameControllerView.gameView().playerViews().stream().map(PlayerView::playerName).collect(Collectors.toCollection(ArrayList::new)), 4);
    }

    /**
     * Instances the MainMenuScene.
     *
     * @return The MainMenuScene scene.
     */
    public MainMenuScene instanceMainMenuScene() {
        return new MainMenuScene(this.controller);
    }

    /**
     * Instances the CreateGameScene.
     *
     * @return The CreateGameScene scene.
     */
    public CreateGameScene instanceCreateGameScene() {
        return new CreateGameScene(this.controller);
    }

    /**
     * Instances the InitPlaceStarterCardScene.
     *
     * @param starterCard The starter card to be placed.
     * @return The InitPlaceStarterCardScene scene.
     */
    public InitPlaceStarterCardScene instanceInitPlaceStarterCardScene(GameCard starterCard) {
        return new InitPlaceStarterCardScene(this.controller, starterCard);
    }

    /**
     * Instances the InitChoosePlayerColorScene.
     *
     * @param availableColors The list of available colors.
     * @return The InitChoosePlayerColorScene scene.
     */
    public InitChoosePlayerColorScene instanceInitChoosePlayerColorScene(ArrayList<PlayerColorEnum> availableColors) {
        return new InitChoosePlayerColorScene(this.controller, availableColors);
    }

    /**
     * Instances the InitSetObjectiveCardScene.
     *
     * @param objectiveCards The list of objective cards.
     * @return The InitSetObjectiveCardScene scene.
     */
    public InitSetObjectiveCardScene instanceInitSetObjectiveCardScene(ArrayList<ObjectiveCard> objectiveCards) {
        return new InitSetObjectiveCardScene(this.controller, objectiveCards);
    }

    /**
     * Instances the DrawCardScene.
     *
     * @param playerBoard              The player board.
     * @param globalObjectives         The global objectives.
     * @param playerObjective          The player objective.
     * @param hand                     The player hand.
     * @param globalBoardView          The global board view.
     * @param isLastRound              The flag that indicates if it is the last round.
     * @param remainingRoundsToEndGame The number of remaining rounds to end the game.
     * @param oldViewRemainingRounds   The number of remaining rounds to end the game in the old view.
     * @return The DrawCardScene scene.
     */
    public DrawCardScene instanceDrawCardScene(HashMap<Coordinate, GameCard> playerBoard, ArrayList<ObjectiveCard> globalObjectives, ObjectiveCard playerObjective, ArrayList<GameCard> hand, GlobalBoardView globalBoardView, boolean isLastRound, int remainingRoundsToEndGame, int oldViewRemainingRounds) {
        return new DrawCardScene(this.controller, playerBoard, globalObjectives, playerObjective, hand, globalBoardView, isLastRound, remainingRoundsToEndGame, oldViewRemainingRounds);
    }

    /**
     * Instances the PlaceCardScene.
     *
     * @param playerBoard              The player board.
     * @param globalObjectives         The global objectives.
     * @param playerObjective          The player objective.
     * @param hand                     The player hand.
     * @param playerViews              The list of player views.
     * @param isLastRound              The flag that indicates if it is the last round.
     * @param remainingRoundsToEndGame The number of remaining rounds to end the game.
     * @return The PlaceCardScene scene.
     */
    public PlaceCardScene instancePlaceCardScene(HashMap<Coordinate, GameCard> playerBoard, ArrayList<ObjectiveCard> globalObjectives, ObjectiveCard playerObjective, ArrayList<GameCard> hand, List<PlayerView> playerViews, boolean isLastRound, int remainingRoundsToEndGame) {
        return new PlaceCardScene(this.controller, playerBoard, globalObjectives, playerObjective, hand, playerViews, controller.getPlayerName(), isLastRound, remainingRoundsToEndGame);
    }

    /**
     * Instances the PlayerTurnScene.
     *
     * @param playerName               The player name.
     * @param playerColor              The player color.
     * @param playerBoard              The player board.
     * @param isLastRound              The flag that indicates if it is the last round.
     * @param remainingRoundsToEndGame The number of remaining rounds to end the game.
     * @return The PlayerTurnScene scene.
     */
    public OtherPlayerTurnScene instanceOtherPlayerTurnScene(String playerName, PlayerColorEnum playerColor, HashMap<Coordinate, GameCard> playerBoard, boolean isLastRound, int remainingRoundsToEndGame) {
        return new OtherPlayerTurnScene(this.controller, playerName, playerColor, playerBoard, isLastRound, remainingRoundsToEndGame);
    }

    /**
     * Instances the ChatScene.
     *
     * @param playerName The player name.
     * @param messages   The list of messages.
     * @return The ChatScene scene.
     */
    public ChatScene instanceChatScene(String playerName, ArrayList<ChatServerToClientMessage> messages) {
        return new ChatScene(this.controller, playerName, messages);
    }

    /**
     * Instances the GamePausedScene.
     *
     * @return The GamePausedScene scene.
     */
    public GamePausedScene instanceGamePausedScene() {
        return new GamePausedScene(this.controller);
    }

    /**
     * Instances the WinnerScene.
     *
     * @param updatedView The updated view.
     * @return The WinnerScene scene.
     */
    public Scene instanceWinnerScene(GameControllerView updatedView) {
        return new WinnerScene(this.controller, updatedView.gameView().winners(), updatedView.gameView().playerViews(), 2);
    }

    /**
     * Instances the ConnectionScene.
     *
     * @param message The error message.
        * @param canRetry The flag that indicates if the connection can be retried.
     * @return The ConnectionScene scene.
     */
    public Scene instanceConnectionScene(String message, Boolean canRetry) {
        return new ConnectionScene(this.controller, message, canRetry);
    }
}