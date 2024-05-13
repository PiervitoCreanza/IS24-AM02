package it.polimi.ingsw.tui.view.component.leaderBoard;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LeaderBoardComponentTest {

    MainController mainController;
    Game game;

    @BeforeEach
    void setUp() {
        mainController = new MainController();
        mainController.createGame("Test", "Player1", 4);
        mainController.joinGame("Test", "Player2");
        mainController.joinGame("Test", "Player3");
        game = mainController.getGameController("Test").getGame();
    }

    @Test
    @DisplayName("All players are connected and have 0 points")
    void allConnectedZeroPoints() {
        System.out.println(new LeaderBoardComponent(game.getVirtualView().playerViews(), "Player1"));
    }

    @Test
    @DisplayName("All players are connected and have points")
    void allConnectedPoints() {
        game.getPlayer("Player1").advancePlayerPos(10);
        game.getPlayer("Player2").advancePlayerPos(1);
        game.getPlayer("Player3").advancePlayerPos(5);
        System.out.println(new LeaderBoardComponent(game.getVirtualView().playerViews(), "Player1"));
    }

    @Test
    @DisplayName("Some players are not connected and have points")
    void notConnectedPoints() {
        game.getPlayer("Player1").advancePlayerPos(10);
        game.getPlayer("Player2").advancePlayerPos(1);
        game.getPlayer("Player2").setConnected(false);
        game.getPlayer("Player3").setConnected(false);
        System.out.println(new LeaderBoardComponent(game.getVirtualView().playerViews(), "Player1"));
    }

    @Test
    @DisplayName("A player has a longer name than the maximum allowed")
    void longName() {
        mainController.joinGame("Test", "Player4LongName");
        System.out.println(new LeaderBoardComponent(game.getVirtualView().playerViews(), "Player1"));
    }
}
