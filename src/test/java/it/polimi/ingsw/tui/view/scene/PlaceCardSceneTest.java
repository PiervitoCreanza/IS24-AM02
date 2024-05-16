package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.controller.gameController.GameControllerMiddleware;
import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.virtualView.PlayerView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceCardSceneTest {

    @Test
    void testToString() {
        Parser parser = new Parser();
        Deck<GameCard> starterDeck = parser.getStarterDeck();
        Deck<GameCard> resourceDeck = parser.getResourceDeck();
        GameCard starterCard = starterDeck.draw();
        PlayerBoard playerBoard = new PlayerBoard(starterCard);
        playerBoard.placeGameCard(new Coordinate(0, 0), starterCard);
        playerBoard.placeGameCard(new Coordinate(1, 1), resourceDeck.draw());
        playerBoard.placeGameCard(new Coordinate(-1, 1), resourceDeck.draw());
        playerBoard.placeGameCard(new Coordinate(1, -1), resourceDeck.draw());
        playerBoard.placeGameCard(new Coordinate(-1, -1), resourceDeck.draw());

        MainController mainController = new MainController();
        mainController.createGame("Test", "Player1", 4);
        mainController.joinGame("Test", "Player2");
        mainController.joinGame("Test", "Player3");
        Game game = mainController.getGameController("Test").getGame();

        Player player1 = game.getPlayer("Player1");
        GameControllerMiddleware gameController = mainController.getGameController("Test");
        gameController.setGameStatus(GameStatusEnum.INIT_PLACE_STARTER_CARD);
        gameController.placeCard("Player1", new Coordinate(0, 0), player1.getPlayerBoard().getStarterCard().getCardId());
        player1.setPlayerObjective(player1.getVirtualView().choosableObjectives().getFirst().getCardId());

        HashMap<Coordinate, GameCard> playerBoardView = playerBoard.getVirtualView().playerBoard();
        ArrayList<ObjectiveCard> globalObjectives = game.getVirtualView().globalBoardView().globalObjectives();
        ObjectiveCard playerObjective = player1.getVirtualView().objectiveCard();
        ArrayList<GameCard> hand = player1.getVirtualView().playerHandView().hand();
        List<PlayerView> playerViews = game.getVirtualView().playerViews();

        PlaceCardScene placeCardScene = new PlaceCardScene(null, playerBoardView, globalObjectives, playerObjective, hand, playerViews, "Player1");
        placeCardScene.getDrawArea().println();
    }
}
