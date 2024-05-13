package it.polimi.ingsw.tui.view.component.leaderBoard;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.tui.view.component.playerBoard.GlobalObjectivesComponent;
import it.polimi.ingsw.tui.view.component.playerInventory.PlayerInventoryComponent;
import it.polimi.ingsw.tui.view.component.playerInventory.playerHand.PlayerHandComponent;
import org.junit.jupiter.api.Test;

public class LeaderBoardComponentTest {
    @Test
    void getDrawArea() {
        MainController mainController = new MainController();
        mainController.createGame("Test", "Player1", 4);
        mainController.joinGame("Test", "Player2");
        mainController.joinGame("Test", "Player3");
        mainController.joinGame("Test", "Player4");
        Game game = mainController.getGameController("Test").getGame();
        game.getPlayer("Player1").advancePlayerPos(10);
        game.getPlayer("Player2").advancePlayerPos(1);
        game.getPlayer("Player3").advancePlayerPos(5);
        LeaderBoardComponent leaderBoardComponent = new LeaderBoardComponent(game.getVirtualView().playerViews(), "Player1");
        System.out.println(leaderBoardComponent);
        game.getPlayer("Player2").setConnected(false);
        LeaderBoardComponent leaderBoardComponent2 = new LeaderBoardComponent(game.getVirtualView().playerViews(), "Player1");
        System.out.println(leaderBoardComponent2);
        mainController.getGameController("Test").setGameStatus(GameStatusEnum.INIT_PLACE_STARTER_CARD);
        mainController.getGameController("Test").placeCard("Player1", new Coordinate(0, 0), game.getPlayer("Player1").getVirtualView().starterCard());
        System.out.println(new PlayerHandComponent(game.getPlayer("Player1").getPlayerHand().getCards()));
        System.out.println(new GlobalObjectivesComponent(game.getVirtualView().globalBoardView().globalObjectives(), 5));
        game.getPlayer("Player1").setPlayerObjective(game.getPlayer("Player1").getVirtualView().choosableObjectives().getFirst());
        PlayerInventoryComponent playerInventoryComponent = new PlayerInventoryComponent(game.getVirtualView().globalBoardView().globalObjectives(), game.getPlayer("Player1").getVirtualView().objectiveCard(), game.getPlayer("Player1").getVirtualView().playerHandView().hand());
        System.out.println(playerInventoryComponent);
    }
}
