package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.virtualView.GlobalBoardView;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.utils.Utils;
import it.polimi.ingsw.tui.view.component.EndPhaseComponent;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.component.decks.DrawingAreaComponent;
import it.polimi.ingsw.tui.view.component.player.playerBoard.PlayerBoardComponent;
import it.polimi.ingsw.tui.view.component.player.playerInventory.PlayerInventoryComponent;
import it.polimi.ingsw.tui.view.component.userInputHandler.UserInputHandler;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.MenuHandler;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.MenuItem;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.commands.EmptyCommand;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The DrawCardScene class represents the scene where the player can draw a card.
 * It implements the Scene and UserInputScene interfaces.
 */
public class DrawCardScene implements Scene, PropertyChangeListener {

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
     * The logger.
     */
    private static final Logger Logger = LogManager.getLogger(DrawCardScene.class);

    /**
     * The menu handler for the scene.
     */
    private final MenuHandler menuHandler;

    /**
     * Constructs a new DrawCardScene.
     * It initializes the draw area with the player's game board,
     * the global objectives, the player's objective, the player's hand of cards,
     * the global board view, and the end phase component.
     * It also initializes the menu handler with the available commands.
     *
     * @param controller               the controller for this scene
     * @param playerBoard              the player's game board
     * @param globalObjectives         the global objectives for the game
     * @param playerObjective          the player's objective
     * @param hand                     the player's hand of cards
     * @param globalBoardView          the global board view
     * @param isLastRound              the flag that indicates if it is the last round
     * @param remainingRoundsToEndGame the number of remaining rounds to end the game
     */
    public DrawCardScene(TUIViewController controller, HashMap<Coordinate, GameCard> playerBoard, ArrayList<ObjectiveCard> globalObjectives, ObjectiveCard playerObjective, ArrayList<GameCard> hand, GlobalBoardView globalBoardView, boolean isLastRound, int remainingRoundsToEndGame) {
        this.controller = controller;
        this.drawArea = new DrawArea();
        this.fieldResourceCards = globalBoardView.fieldResourceCards();
        this.fieldGoldCards = globalBoardView.fieldGoldCards();

        DrawArea playerBoardArea = new PlayerBoardComponent(playerBoard).getDrawArea();
        DrawArea playerInventoryArea = new PlayerInventoryComponent(globalObjectives, playerObjective, hand, 1).getDrawArea();
        DrawArea drawCardArea = new DrawingAreaComponent(globalBoardView.resourceFirstCard(), globalBoardView.goldFirstCard(), globalBoardView.fieldResourceCards(), globalBoardView.fieldGoldCards(), 5).getDrawArea();
        DrawArea endPhaseArea = new EndPhaseComponent(isLastRound, remainingRoundsToEndGame).getDrawArea();

        int widthMax = Math.max(playerBoardArea.getWidth(), playerInventoryArea.getWidth());

        this.drawArea.drawAt(0, 0, new TitleComponent("Draw a Card", widthMax).getDrawArea());
        this.drawArea.drawCenteredX(drawArea.getHeight(), playerBoardArea);
        this.drawArea.drawCenteredX(drawArea.getHeight(), playerInventoryArea);
        this.drawArea.drawCenteredX(drawArea.getHeight(), drawCardArea);
        this.drawArea.drawAt(0, drawArea.getHeight(), endPhaseArea);

        this.menuHandler = new MenuHandler(this,
                new MenuItem("d", "draw a card", new UserInputHandler("Choose the card to draw:", input -> input.matches("[1-6]"))),
                new MenuItem("c", "show chat", new EmptyCommand()),
                new MenuItem("q", "quit", new EmptyCommand())
        );
    }

    /**
     * This method is used to display the scene.
     * It prompts the user to choose a card to draw.
     * If the user enters a valid input, it calls the controller's drawCard method to draw the card.
     */
    @Override
    public void display() {
        drawArea.println();
        menuHandler.print();
    }

    /**
     * This method is used to handle user input in a Scene.
     *
     * @param input The user input.
     */
    @Override
    public void handleUserInput(String input) {
        menuHandler.handleInput(input);
    }

    /**
     * This method is used to get the draw area.
     *
     * @return the draw area
     */
    public DrawArea getDrawArea() {
        return drawArea;
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
            case "d" -> {
                String input = inputs.getFirst();
                switch (input) {
                    case "1" -> {
                        if (fieldResourceCards.isEmpty())
                            printCardIsMissing(input);
                        controller.drawCardFromField(fieldResourceCards.getFirst().getCardId());
                    }
                    case "2" -> {
                        if (fieldResourceCards.size() < 2)
                            printCardIsMissing(input);
                        controller.drawCardFromField(fieldResourceCards.get(1).getCardId());
                    }
                    case "3" -> {
                        if (fieldGoldCards.isEmpty())
                            printCardIsMissing(input);
                        controller.drawCardFromField(fieldGoldCards.getFirst().getCardId());
                    }
                    case "4" -> {
                        if (fieldGoldCards.size() < 2)
                            printCardIsMissing(input);
                        controller.drawCardFromField(fieldGoldCards.get(1).getCardId());
                    }
                    case "5" -> controller.drawCardFromResourceDeck();
                    case "6" -> controller.drawCardFromGoldDeck();
                }
            }
            case "c" -> controller.showChat();
            case "q" -> controller.closeConnection();
            default -> Logger.error("Invalid property change event");
        }
    }

    private void printCardIsMissing(String input) {
        Utils.clearScreen();
        display();
        new DrawArea("\nYou can't draw card number " + input + ". The field is empty!", ColorsEnum.RED).println();
    }
}