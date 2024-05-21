package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.component.cards.gameCard.GameCardComponent;
import it.polimi.ingsw.tui.view.component.userInputHandler.UserInputHandler;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

/**
 * The InitPlaceStarterCardScene class represents the scene where the player places their starter card.
 * It implements the Scene interface.
 */
public class InitPlaceStarterCardScene implements Scene {
    /**
     * The DrawArea object where the scene will be drawn.
     */
    private final DrawArea drawArea;

    /**
     * The controller that manages the user interface and the game logic.
     */
    private final TUIViewController controller;

    /**
     * The starter card that the player will place.
     */
    private final GameCard starterCard;

    /**
     * The handler for user input.
     */
    private UserInputHandler handler;

    /**
     * Constructs a new InitPlaceStarterCardScene.
     *
     * @param controller  the controller for this scene
     * @param starterCard the starter card that the player will place
     */
    public InitPlaceStarterCardScene(TUIViewController controller, GameCard starterCard) {
        this.starterCard = starterCard;
        this.drawArea = new TitleComponent("Place Starter Card").getDrawArea();
        DrawArea StarterCardArea = new DrawArea();
        GameCardComponent gameCardComponent = new GameCardComponent(starterCard);
        int width = gameCardComponent.getWidth();
        StarterCardArea.drawAt(width / 2, 0, "1");
        StarterCardArea.drawAt(width + (width / 2) + 5, 0, "2");
        StarterCardArea.drawAt(0, 1, gameCardComponent.getDrawArea());
        starterCard.switchSide();
        gameCardComponent = new GameCardComponent(starterCard);
        StarterCardArea.drawAt(gameCardComponent.getWidth() + 5, 1, gameCardComponent.getDrawArea());
        drawArea.drawCenteredX(drawArea.getHeight(), StarterCardArea);
        this.controller = controller;
    }

    /**
     * This method is used to display the scene to the user.
     */
    @Override
    public void display() {
        drawArea.println();
        handler = new UserInputHandler("Choose the side of the Starter Card [1/2]:", input -> input.matches("[1-2]"));
        handler.print();
    }

    /**
     * This method is used to handle user input.
     *
     * @param input the user's input
     */
    public void handleUserInput(String input) {
        if (input.equals("q")) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }
        if (handler.validate(input)) {
            controller.placeStarterCard(starterCard.getCardId(), Integer.parseInt(handler.getInput()) == 2);
            return;
        }
        handler.print();
    }
}