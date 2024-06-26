package it.polimi.ingsw.view.tui.drawables.component.playerInventory;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.controller.gameController.GameControllerMiddleware;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.view.tui.drawables.component.player.playerInventory.PlayerInventoryComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class PlayerInventoryComponentTest {

    Game game;

    Player player1;

    @BeforeEach
    void setUp() {
        MainController mainController = new MainController();
        mainController.createGame("TestGame", "Player1", 2);
        mainController.joinGame("TestGame", "Player2");
        game = mainController.getGames().getFirst();
        player1 = game.getPlayer("Player1");
        GameControllerMiddleware gameController = mainController.getGameController("TestGame");
        gameController.setGameStatus(GameStatusEnum.INIT_PLACE_STARTER_CARD);
        gameController.placeCard("Player1", new Coordinate(0, 0), player1.getPlayerBoard().getStarterCard().getCardId());
        player1.setPlayerObjective(player1.getVirtualView().choosableObjectives().getFirst().getCardId());
    }

    @Test
    @DisplayName("Test if every component of the player inventory is drawn correctly")
    void getPlayerInventoryDrawArea() {
        ArrayList<ObjectiveCard> globalObjectives = game.getVirtualView().globalBoardView().globalObjectives();
        ObjectiveCard playerObjective = player1.getVirtualView().objectiveCard();
        ArrayList<GameCard> hand = player1.getVirtualView().playerHandView().hand();
        int spacing = 5;
        System.out.println(new PlayerInventoryComponent(globalObjectives, playerObjective, hand, spacing));
    }
}
