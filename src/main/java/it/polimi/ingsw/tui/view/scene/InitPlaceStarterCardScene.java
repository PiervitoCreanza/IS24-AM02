package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.component.cards.gameCard.GameCardComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

public class InitPlaceStarterCardScene implements Displayable {
    private final DrawArea drawArea;
    private final TUIViewController controller;
    private final GameCard starterCard;

    private UserInputHandler handler;

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
     * This method is used to display the object.
     */
    @Override
    public void display() {
        drawArea.println();
        handler = new UserInputHandler("Choose the side of the Starter Card [1/2]:", input -> input.matches("[1-2]"));
        handler.print();
    }

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
