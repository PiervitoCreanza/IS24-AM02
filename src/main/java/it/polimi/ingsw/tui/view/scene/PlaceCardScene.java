package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.virtualView.PlayerBoardView;
import it.polimi.ingsw.network.virtualView.PlayerView;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.component.leaderBoard.LeaderBoardComponent;
import it.polimi.ingsw.tui.view.component.playerBoard.PlayerBoardComponent;
import it.polimi.ingsw.tui.view.component.playerInventory.PlayerInventoryComponent;
import it.polimi.ingsw.tui.view.component.playerItems.PlayerItemsComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The PlaceCardScene class represents the scene where the player can place a card on the game board.
 * It implements the Displayable and UserInputScene interfaces.
 */
public class PlaceCardScene implements Displayable, UserInputScene {

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
    public PlaceCardScene(TUIViewController controller, HashMap<Coordinate, GameCard> playerBoard, ArrayList<ObjectiveCard> globalObjectives, ObjectiveCard playerObjective, ArrayList<GameCard> hand, List<PlayerView> playerViews, String playerUsername) {
        this.controller = controller;
        this.playerBoard = playerBoard;
        this.globalObjectives = globalObjectives;
        this.playerObjective = playerObjective;
        this.hand = hand;
        this.playerViews = playerViews;
        this.playerUsername = playerUsername;

        draw();
    }

    /**
     * Displays the scene to the user.
     */
    @Override
    public void display() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        this.drawArea.println();
        // TODO: Implement
        String input = reader.readLine();
        switch (input) {
            case "s", "S" -> switchCard();
            case "p", "P" -> placeCard();
            case "sb", "SB" -> switchBoard();
            case "c", "C" -> controller.selectScene(ScenesEnum.CHAT);
            default -> System.out.println("Invalid input");
        }
    }

    /**
     * Switches the board.
     */
    private void switchBoard() {
        // TODO: Implement
    }

    /**
     * Switches the card.
     */
    private void switchCard() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        new TitleComponent("Switching Card").getDrawArea().println();
        String cardToSwitch = UserInputScene.getAndValidateInput("Choose the card to switch:", input -> input.matches("[1-3]"), reader);
        if (cardToSwitch == null) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }
        hand.get(Integer.parseInt(cardToSwitch) - 1).switchSide();
        handFlipped.set(Integer.parseInt(cardToSwitch) - 1, !handFlipped.get(Integer.parseInt(cardToSwitch) - 1));
        draw();
        display();
    }

    /**
     * Places a card on the game board.
     */
    private void placeCard() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        new TitleComponent("Placing Card").getDrawArea().println();
        String cardToPlace = UserInputScene.getAndValidateInput("Choose the card to place:", input -> input.matches("[1-3]"), reader);
        // Back to main menu if user quits
        if (cardToPlace == null) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }
        String coordinates = UserInputScene.getAndValidateInput("Choose the coordinates to place the card (x,y) or <q> to quit:", input -> input.matches("-?\\d{1,2},-?\\d{1,2}|q|Q"), reader);
        // Back to main menu if user quits
        if (coordinates == null) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }
        if (coordinates.equals("q") || coordinates.equals("Q")) {
            draw();
            display();
            //TODO: Testing this
        }
        int choosenCardId = this.hand.get(Integer.parseInt(cardToPlace) - 1).getCardId();
        controller.placeCard(choosenCardId, new Coordinate(Integer.parseInt(coordinates.split(",")[0]), Integer.parseInt(coordinates.split(",")[1])), handFlipped.get(Integer.parseInt(cardToPlace) - 1));
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
        leaderItemsArea.drawAt(leaderBoardArea.getWidth(), 0, playerItemsArea);
        DrawArea leaderInventoryArea = new DrawArea();
        leaderInventoryArea.drawAt(0, leaderItemsArea.getHeight(), playerInventoryArea);
        leaderInventoryArea.drawCenteredX(0, leaderItemsArea);
        widthMax = Math.max(playerBoardArea.getWidth(), playerInventoryArea.getWidth());
        DrawArea titleArea = new TitleComponent("Place Card", widthMax).getDrawArea();

        this.drawArea.drawAt(0, 0, titleArea);
        this.drawArea.drawCenteredX(drawArea.getHeight(), playerBoardArea);
        this.drawArea.drawCenteredX(drawArea.getHeight(), leaderInventoryArea);

        this.drawArea.drawNewLine("""
                Press <s> to switch the Nth card.
                Press <p> to place the card.
                Press <c> to see the Chat.
                """, 0);
    }
}