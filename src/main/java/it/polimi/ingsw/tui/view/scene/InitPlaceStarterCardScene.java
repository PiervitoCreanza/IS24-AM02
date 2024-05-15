package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.component.cards.gameCard.GameCardComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InitPlaceStarterCardScene implements Diplayable, UserInputScene {
    private final DrawArea drawArea;
    private final TUIViewController controller;

    public InitPlaceStarterCardScene(TUIViewController controller, GameCard starterCard) {
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
    public void display() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        drawArea.println();
        String side = UserInputScene.getAndValidateInput("Choose the side of the Starter Card [1/2]:", input -> input.matches("[1-2]"), reader);
        // Back to main menu if user quits
        if (side == null) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }
        controller.placeStarterCard(Integer.parseInt(side) == 2);
    }
}
