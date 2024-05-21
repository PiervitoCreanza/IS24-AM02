package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.virtualView.PlayerBoardView;
import it.polimi.ingsw.network.virtualView.PlayerView;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.utils.Utils;
import it.polimi.ingsw.tui.view.component.InputPromptComponent;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.component.leaderBoard.LeaderBoardComponent;
import it.polimi.ingsw.tui.view.component.player.playerBoard.PlayerBoardComponent;
import it.polimi.ingsw.tui.view.component.player.playerInventory.PlayerInventoryComponent;
import it.polimi.ingsw.tui.view.component.player.playerItems.PlayerItemsComponent;
import it.polimi.ingsw.tui.view.component.userInputHandler.UserInputHandler;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The PlaceCardScene class represents the scene where the player can place a card on the game board.
 * It implements the Scene and UserInputScene interfaces.
 */
public class PlaceCardScene implements Scene {

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
     * The status of the scene.
     */
    private PlaceCardStatus currentStatus = PlaceCardStatus.HANDLE_INPUT;

    /**
     * The user input handler for choosing a card to switch.
     */
    private final UserInputHandler chooseCardHandler = new UserInputHandler("Choose the card to switch:", input -> input.matches("[1-3]"));

    /**
     * The user input handler for choosing a card to place.
     */
    private final UserInputHandler chooseCardToPlaceHandler = new UserInputHandler("Choose the card to place:", input -> input.matches("[1-3]"));

    /**
     * The user input handler for choosing the coordinates to place the card.
     */
    private final UserInputHandler chooseBoardCardHandler = new UserInputHandler("Select a card on the board (e.g. 1,1):", input -> input.matches("-?\\d{1,2},-?\\d{1,2}"));

    private final UserInputHandler chooseBoardCornerHandler = new UserInputHandler(">", input -> input.toLowerCase().matches("topleft|topright|bottomleft|bottomright|top left|top right|bottom left|bottom right|tl|tr|bl|br"));

    private enum PlaceCardStatus {
        HANDLE_INPUT,
        SWITCH_CARD,
        CHOOSE_HAND_CARD,
        CHOOSE_BOARD_CARD,
        CHOOSE_BOARD_CORNER,
    }

    /**
     * Constructs a new PlaceCardScene.
     *
     * @param controller       the controller for this scene
     * @param playerBoard      the player's game board
     * @param globalObjectives the global objectives for the game
     * @param playerObjective  the player's objective
     * @param hand             the player's hand of cards
     * @param playerViews      the views of the players
     * @param playerUsername   the username of the player
     */
    public PlaceCardScene(TUIViewController controller, HashMap<Coordinate, GameCard> playerBoard, ArrayList<ObjectiveCard> globalObjectives, ObjectiveCard playerObjective, ArrayList<GameCard> hand, List<PlayerView> playerViews, String playerUsername, boolean isLastRound) {
        this.controller = controller;
        this.playerBoard = playerBoard;
        this.globalObjectives = globalObjectives;
        this.playerObjective = playerObjective;
        this.hand = hand;
        this.playerViews = playerViews;
        this.playerUsername = playerUsername;
        this.isLastRound = isLastRound;
        draw();
    }

    /**
     * Displays the scene to the user.
     */
    @Override
    public void display() {
        this.drawArea.println();
    }

    /**
     * Handles the user input.
     *
     * @param input the user input
     */
    public void handleUserInput(String input) {

        if (currentStatus == PlaceCardStatus.HANDLE_INPUT) {
            switch (input.toLowerCase()) {
                case "s" -> {
                    currentStatus = PlaceCardStatus.SWITCH_CARD;
                    new TitleComponent("Switching Card").getDrawArea().println();
                    chooseCardHandler.print();
                    return;
                }
                case "p" -> {
                    currentStatus = PlaceCardStatus.CHOOSE_HAND_CARD;
                    new TitleComponent("Placing Card").getDrawArea().println();
                    chooseCardToPlaceHandler.print();
                    return;
                }
                case "c" -> {
                    controller.showChat();
                    return;
                }
                case "q" -> {
                    controller.selectScene(ScenesEnum.MAIN_MENU);
                    return;
                }
                default -> System.out.println("Invalid input");
            }
        }

        if (currentStatus == PlaceCardStatus.SWITCH_CARD) {
            handleSwitchCard(input);
        }
        if (currentStatus == PlaceCardStatus.CHOOSE_HAND_CARD) {
            handleChooseCardToPlace(input);
        }
        if (currentStatus == PlaceCardStatus.CHOOSE_BOARD_CARD) {
            handleChooseBoardCard(input);
        }
        if (currentStatus == PlaceCardStatus.CHOOSE_BOARD_CORNER) {
            handleChooseBoardCorner(input);
        }

    }

    /**
     * Handles the user input for switching a card.
     *
     * @param input the user input
     */
    public void handleSwitchCard(String input) {
        if (input.equals("q")) {
            Utils.clearScreen();
            display();
            // Go back to action chooser
            currentStatus = PlaceCardStatus.HANDLE_INPUT;
            return;
        }
        if (chooseCardHandler.validate(input)) {
            int cardToSwitchIndex = Integer.parseInt(chooseCardHandler.getInput()) - 1;
            hand.get(cardToSwitchIndex).switchSide();
            handFlipped.set(cardToSwitchIndex, !handFlipped.get(cardToSwitchIndex));
            // Go back to action chooser
            currentStatus = PlaceCardStatus.HANDLE_INPUT;
            draw();
            display();
        } else {
            chooseCardHandler.print();
        }
    }

    /**
     * Handles the user input for choosing a card to place.
     *
     * @param input the user input
     */
    public void handleChooseCardToPlace(String input) {
        if (input.equals("q")) {
            Utils.clearScreen();
            display();
            // Go back to action chooser
            currentStatus = PlaceCardStatus.HANDLE_INPUT;
            return;
        }
        if (chooseCardToPlaceHandler.validate(input)) {
            currentStatus = PlaceCardStatus.CHOOSE_BOARD_CARD;
        } else {
            chooseCardToPlaceHandler.print();
        }
    }

    /**
     * Handles the user input for choosing the coordinates to place the card.
     *
     * @param input the user input
     */
    public void handleChooseBoardCard(String input) {
        if (input.equals("q")) {
            Utils.clearScreen();
            display();
            // Go back to action chooser
            currentStatus = PlaceCardStatus.HANDLE_INPUT;
            return;
        }
        if (chooseBoardCardHandler.validate(input)) {
            currentStatus = PlaceCardStatus.CHOOSE_BOARD_CORNER;
        } else {
            chooseBoardCardHandler.print();
        }
    }

    public void handleChooseBoardCorner(String input) {
        if (input.equals("q")) {
            Utils.clearScreen();
            display();
            // Go back to action chooser
            currentStatus = PlaceCardStatus.HANDLE_INPUT;
            return;
        }
        if (chooseBoardCornerHandler.validate(input)) {
            int chooseCardId = this.hand.get(Integer.parseInt(chooseCardToPlaceHandler.getInput()) - 1).getCardId();
            Coordinate coordinate = getCoordinate();
            currentStatus = PlaceCardStatus.HANDLE_INPUT;
            controller.placeCard(chooseCardId, coordinate, handFlipped.get(Integer.parseInt(chooseCardToPlaceHandler.getInput()) - 1));
        } else {
            InputPromptComponent inputArea = new InputPromptComponent("""
                    Select the corner:
                    <tl> Top left
                    <tr> Top right
                    <bl> Bottom left
                    <br> Bottom right
                    """);
            inputArea.println();
            chooseBoardCornerHandler.print();
        }
    }

    private Coordinate getCoordinate() {
        Coordinate coordinate = new Coordinate(Integer.parseInt(chooseBoardCardHandler.getInput().split(",")[0]), Integer.parseInt(chooseBoardCardHandler.getInput().split(",")[1]));
        switch (chooseBoardCornerHandler.getInput().toLowerCase()) {
            case "topleft", "top left", "tl" -> coordinate.setLocation(coordinate.getX() - 1, coordinate.getY() + 1);
            case "topright", "top right", "tr" -> coordinate.setLocation(coordinate.getX() + 1, coordinate.getY() + 1);
            case "bottomleft", "bottom left", "bl" ->
                    coordinate.setLocation(coordinate.getX() - 1, coordinate.getY() - 1);
            case "bottomright", "bottom right", "br" ->
                    coordinate.setLocation(coordinate.getX() + 1, coordinate.getY() - 1);
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

        this.drawArea.drawAt(0, 0, titleArea);
        this.drawArea.drawCenteredX(drawArea.getHeight(), playerBoardArea);
        this.drawArea.drawCenteredX(drawArea.getHeight(), leaderInventoryArea);
        if (isLastRound) {
            this.drawArea.drawNewLine("""
                    This is the last round of the game.
                    You can place the last card on the board.
                    """, ColorsEnum.BRIGHT_RED, 0);
        }
        this.drawArea.drawNewLine("""
                Press <s> to switch the Nth card.
                Press <p> to place the card.
                Press <c> to see the Chat.
                """, 1);
    }
}