package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.virtualView.GlobalBoardView;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.component.decks.DrawingAreaComponent;
import it.polimi.ingsw.tui.view.component.player.playerBoard.PlayerBoardComponent;
import it.polimi.ingsw.tui.view.component.player.playerInventory.PlayerInventoryComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The DrawCardScene class represents the scene where the player can draw a card.
 * It implements the Scene and UserInputScene interfaces.
 */
public class DrawCardScene implements Scene {

    /**
     * The draw area of the scene.
     */
    private final DrawArea drawArea;

    /**
     * The controller for this scene.
     */
    private final TUIViewController controller;

    /**
     * The resource cards on the field.
     */
    private final ArrayList<GameCard> fieldResourceCards;

    /**
     * The gold cards on the field.
     */
    private final ArrayList<GameCard> fieldGoldCards;

    /**
     * The handler for the user input.
     */
    private final UserInputHandler handler;

    /**
     * The status of the scene.
     */
    private int status = 0;

    /**
     * Constructs a new DrawCardScene.
     *
     * @param controller       the controller for this scene
     * @param playerBoard      the player's game board
     * @param globalObjectives the global objectives for the game
     * @param playerObjective  the player's objective
     * @param hand             the player's hand of cards
     * @param globalBoardView  the global board view
     */
    public DrawCardScene(TUIViewController controller, HashMap<Coordinate, GameCard> playerBoard, ArrayList<ObjectiveCard> globalObjectives, ObjectiveCard playerObjective, ArrayList<GameCard> hand, GlobalBoardView globalBoardView, boolean isLastRound) {
        this.controller = controller;
        this.drawArea = new DrawArea();
        this.fieldResourceCards = globalBoardView.fieldResourceCards();
        this.fieldGoldCards = globalBoardView.fieldGoldCards();

        DrawArea playerBoardArea = new PlayerBoardComponent(playerBoard).getDrawArea();
        DrawArea playerInventoryArea = new PlayerInventoryComponent(globalObjectives, playerObjective, hand, 1).getDrawArea();
        DrawArea drawCardArea = new DrawingAreaComponent(globalBoardView.resourceFirstCard(), globalBoardView.goldFirstCard(), globalBoardView.fieldResourceCards(), globalBoardView.fieldGoldCards(), 5).getDrawArea();

        int widthMax = Math.max(playerBoardArea.getWidth(), playerInventoryArea.getWidth());

        this.drawArea.drawAt(0, 0, new TitleComponent("Draw a Card", widthMax).getDrawArea());
        this.drawArea.drawCenteredX(drawArea.getHeight(), playerBoardArea);
        this.drawArea.drawCenteredX(drawArea.getHeight(), playerInventoryArea);
        this.drawArea.drawCenteredX(drawArea.getHeight(), drawCardArea);
        if (isLastRound) {
            this.drawArea.drawNewLine("""
                    This is the last round of the game.
                    You can place the last card on the board.
                    """, ColorsEnum.BRIGHT_RED, 0);
        }
        this.drawArea.drawNewLine("""
                Press <d> to draw a card.
                Press <c> to see the Chat.
                """, 0);

        handler = new UserInputHandler("Choose the card to draw:", input -> input.matches("[1-6]"));
    }

    /**
     * This method is used to display the object.
     */
    @Override
    public void display() {
        drawArea.println();
    }

    /**
     * This method is used to handle user input in a Scene.
     *
     * @param input The user input.
     */
    public void handleUserInput(String input) {
        if (input.equals("q")) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }
        if (status == 0) {
            switch (input.toLowerCase()) {
                case "d" -> {
                    new TitleComponent("Drawing a Card").getDrawArea().println();
                    status = 1;
                }
                case "c" -> controller.showChat();
            }
        }

        if (status == 1) {
            if (handler.validate(input)) {
                switch (handler.getInput()) {
                    case "1" -> controller.drawCardFromField(fieldResourceCards.getFirst());
                    case "2" -> controller.drawCardFromField(fieldResourceCards.get(1));
                    case "3" -> controller.drawCardFromField(fieldGoldCards.getFirst());
                    case "4" -> controller.drawCardFromField(fieldGoldCards.get(1));
                    case "5" -> controller.drawCardFromResourceDeck();
                    case "6" -> controller.drawCardFromGoldDeck();
                }
            } else {
                handler.print();
            }
        }
    }

    /**
     * This method is used to get the draw area.
     *
     * @return the draw area
     */
    public DrawArea getDrawArea() {
        return drawArea;
    }
}