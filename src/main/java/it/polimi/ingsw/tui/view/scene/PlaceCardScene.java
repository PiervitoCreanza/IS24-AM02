package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.virtualView.PlayerBoardView;
import it.polimi.ingsw.network.virtualView.PlayerView;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.utils.Utils;
import it.polimi.ingsw.tui.view.component.EndPhaseComponent;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.component.leaderBoard.LeaderBoardComponent;
import it.polimi.ingsw.tui.view.component.player.playerBoard.PlayerBoardComponent;
import it.polimi.ingsw.tui.view.component.player.playerInventory.PlayerInventoryComponent;
import it.polimi.ingsw.tui.view.component.player.playerItems.PlayerItemsComponent;
import it.polimi.ingsw.tui.view.component.userInputHandler.UserInputHandler;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.MenuHandler;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.MenuItem;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.commands.EmptyCommand;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.commands.UserInputChain;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The PlaceCardScene class represents the scene where the player can place a card on the game board.
 * It implements the Scene and UserInputScene interfaces.
 */
public class PlaceCardScene implements Scene, PropertyChangeListener {

    /**
     * The flag that indicates if this is the last round of the game.
     */
    private final boolean isLastRound;

    /**
     * The draw area of the place card scene.
     */
    private DrawArea drawArea;

    /**
     * The controller for this scene.
     */
    private final TUIViewController controller;

    /**
     * The player's game board.
     */
    private final HashMap<Coordinate, GameCard> playerBoard;

    /**
     * The global objectives for the game.
     */
    private final ArrayList<ObjectiveCard> globalObjectives;

    /**
     * The player's objective.
     */
    private final ObjectiveCard playerObjective;

    /**
     * The player's hand of cards.
     */
    private final ArrayList<GameCard> hand;

    /**
     * The flipped status of the cards in the player's hand.
     */
    private final ArrayList<Boolean> handFlipped = new ArrayList<>(List.of(false, false, false));

    /**
     * The views of the players.
     */
    private final List<PlayerView> playerViews;

    /**
     * The username of the player.
     */
    private final String playerUsername;

    /**
     * The menu handler for the manage of the user input.
     */
    private final MenuHandler menuHandler;

    /**
     * The number of remaining rounds to end the game.
     */
    private final int remainingRoundsToEndGame;

    /**
     * Constructs a new PlaceCardScene.
     * It initializes the scene with the given parameters.
     * It also initializes the menu handler.
     *
     * @param controller               the controller for this scene
     * @param playerBoard              the player's game board
     * @param globalObjectives         the global objectives for the game
     * @param playerObjective          the player's objective
     * @param hand                     the player's hand of cards
     * @param playerViews              the views of the players
     * @param playerUsername           the username of the player
     * @param isLastRound              the flag that indicates if it is the last round
     * @param remainingRoundsToEndGame the number of remaining rounds to end the game
     */
    public PlaceCardScene(TUIViewController controller, HashMap<Coordinate, GameCard> playerBoard, ArrayList<ObjectiveCard> globalObjectives, ObjectiveCard playerObjective, ArrayList<GameCard> hand, List<PlayerView> playerViews, String playerUsername, boolean isLastRound, int remainingRoundsToEndGame) {
        this.controller = controller;
        this.playerBoard = playerBoard;
        this.globalObjectives = globalObjectives;
        this.playerObjective = playerObjective;
        this.hand = hand;
        this.playerViews = playerViews;
        this.playerUsername = playerUsername;
        this.isLastRound = isLastRound;
        this.remainingRoundsToEndGame = remainingRoundsToEndGame;
        this.menuHandler = new MenuHandler(this,
                new MenuItem("s", "switch card",
                        new UserInputHandler("Choose the card to switch:", input -> input.matches("[1-3]")
                        )),
                new MenuItem("p", "place card",
                        new UserInputChain(
                                new UserInputHandler("Choose the card to place:", input -> input.matches("[1-3]")),
                                new UserInputHandler("Select a card on the board (e.g. 1,1):", input -> input.matches("-?\\d{1,2},-?\\d{1,2}")),
                                new UserInputHandler("Select the corner:\n<tl> Top left\n<tr> Top right\n<bl> Bottom left\n<br> Bottom right", input -> input.toLowerCase().matches("tl|tr|bl|br"))
                        )
                ),
                new MenuItem("c", "chat", new EmptyCommand()),
                new MenuItem("q", "quit", new EmptyCommand())
        );
    }

    /**
     * Displays the scene to the user.
     * It prints the draw area and the menu handler.
     */
    @Override
    public void display() {
        draw();
        this.drawArea.println();
        this.menuHandler.print();
    }

    /**
     * Handles the user input.
     *
     * @param input the user input
     */
    public void handleUserInput(String input) {
        menuHandler.handleInput(input);
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String changedProperty = evt.getPropertyName();
        @SuppressWarnings("unchecked")
        ArrayList<String> inputs = (ArrayList<String>) evt.getNewValue();
        switch (changedProperty) {
            case "s" -> {
                int cardToSwitchIndex = Integer.parseInt(inputs.getFirst()) - 1;
                hand.get(cardToSwitchIndex).switchSide();
                handFlipped.set(cardToSwitchIndex, !handFlipped.get(cardToSwitchIndex));
                Utils.clearScreen();
                display();
            }
            case "p" -> {
                // The card index in the hand.
                int choosenCardIndex = Integer.parseInt(inputs.get(0)) - 1;
                // The choosen card id.
                int choosenCardId = this.hand.get(choosenCardIndex).getCardId();
                Coordinate coordinate = translateTextToCoordinates(inputs.get(1), inputs.get(2));
                controller.placeCard(choosenCardId, coordinate, handFlipped.get(choosenCardIndex));
            }
            case "c" -> controller.showChat();
            case "q" -> {
                controller.sendDisconnect();
                controller.closeConnection();
            }
        }
    }

    /**
     * Translates the text to the coordinates.
     *
     * @param choosenCardCoordinates the coordinates of the choosen card in the board
     * @param choosenCorner          the corner of the choosen card where to place the new card
     * @return the coordinates of the choosen card
     */
    private Coordinate translateTextToCoordinates(String choosenCardCoordinates, String choosenCorner) {
        Coordinate coordinate = new Coordinate(Integer.parseInt(choosenCardCoordinates.split(",")[0]), Integer.parseInt(choosenCardCoordinates.split(",")[1]));
        switch (choosenCorner.toLowerCase()) {
            case "tl" -> coordinate.setLocation(coordinate.getX() - 1, coordinate.getY() + 1);
            case "tr" -> coordinate.setLocation(coordinate.getX() + 1, coordinate.getY() + 1);
            case "bl" -> coordinate.setLocation(coordinate.getX() - 1, coordinate.getY() - 1);
            case "br" -> coordinate.setLocation(coordinate.getX() + 1, coordinate.getY() - 1);
        }
        return coordinate;
    }

    /**
     * Returns the draw area of the place card scene.
     *
     * @return the draw area of the place card scene
     */
    public DrawArea getDrawArea() {
        return drawArea;
    }

    /**
     * Draws the scene.
     */
    private void draw() {
        this.drawArea = new DrawArea();
        PlayerBoardView playerBoardView = playerViews.stream().filter(playerView -> playerView.playerName().equals(playerUsername)).findFirst().get().playerBoardView();
        int widthMax;

        DrawArea playerBoardArea = new PlayerBoardComponent(playerBoard).getDrawArea();
        DrawArea playerInventoryArea = new PlayerInventoryComponent(globalObjectives, playerObjective, hand, 1).getDrawArea();
        DrawArea leaderBoardArea = new LeaderBoardComponent(playerViews, playerUsername).getDrawArea();
        DrawArea playerItemsArea = new PlayerItemsComponent(playerBoardView.gameItemStore(), 1).getDrawArea();
        DrawArea leaderItemsArea = new DrawArea();
        leaderItemsArea.drawAt(0, 0, leaderBoardArea);
        leaderItemsArea.drawAt(leaderBoardArea.getWidth() + 1, 0, playerItemsArea);
        DrawArea leaderInventoryArea = new DrawArea();
        leaderInventoryArea.drawAt(0, leaderItemsArea.getHeight(), playerInventoryArea);
        leaderInventoryArea.drawCenteredX(0, leaderItemsArea);
        widthMax = Math.max(playerBoardArea.getWidth(), playerInventoryArea.getWidth());
        DrawArea titleArea = new TitleComponent("Place Card", widthMax).getDrawArea();
        DrawArea endPhaseArea = new EndPhaseComponent(isLastRound, remainingRoundsToEndGame).getDrawArea();

        this.drawArea.drawAt(0, 0, titleArea);
        this.drawArea.drawCenteredX(drawArea.getHeight(), playerBoardArea);
        this.drawArea.drawCenteredX(drawArea.getHeight(), leaderInventoryArea);
        this.drawArea.drawAt(0, drawArea.getHeight(), endPhaseArea);

    }
}