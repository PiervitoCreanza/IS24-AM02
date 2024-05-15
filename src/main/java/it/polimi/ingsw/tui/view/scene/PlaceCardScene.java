package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.component.playerBoard.PlayerBoardComponent;
import it.polimi.ingsw.tui.view.component.playerInventory.PlayerInventoryComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class PlaceCardScene implements Diplayable, UserInputScene {
    private final DrawArea drawArea;
    private final TUIViewController controller;
    private final ArrayList<GameCard> hand;

    public PlaceCardScene(TUIViewController controller, HashMap<Coordinate, GameCard> playerBoard, ArrayList<ObjectiveCard> globalObjectives, ObjectiveCard playerObjective, ArrayList<GameCard> hand) {
        this.controller = controller;
        this.hand = hand;
        this.drawArea = new TitleComponent("Place Card").getDrawArea();
        this.drawArea.drawAt(0, this.drawArea.getHeight(), new PlayerBoardComponent(playerBoard));
        this.drawArea.drawAt(0, this.drawArea.getHeight(), new PlayerInventoryComponent(globalObjectives, playerObjective, hand, 0));
        this.drawArea.drawNewLine("""
                Press s <cardNumber> to switch the Nth card.
                Press p <cardNumber> to place the card.
                Press <q> to quit.
                """, 0);

        // TODO: Implement Interface
    }

    /**
     * This method is used to display the object.
     */
    @Override
    public void display() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        this.drawArea.println();
        // TODO: Implement

        String cardToPlace = UserInputScene.getAndValidateInput("Choose the card to place:", input -> input.matches("[1-3]"), reader);
        // Back to main menu if user quits
        if (cardToPlace == null) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }

        String coordinates = UserInputScene.getAndValidateInput("Choose the coordinates to place the card:", input -> input.matches("[-40-40],[-40-40]"), reader);
        // Back to main menu if user quits
        if (coordinates == null) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }
        int choosenCardId = this.hand.get(Integer.parseInt(cardToPlace) - 1).getCardId();
        controller.placeCard(choosenCardId, new Coordinate(Integer.parseInt(coordinates.split(",")[0]), Integer.parseInt(coordinates.split(",")[1])), false);
    }

}
