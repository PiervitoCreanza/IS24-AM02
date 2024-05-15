package it.polimi.ingsw.network.adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.controller.gameController.GameControllerMiddleware;
import it.polimi.ingsw.data.ObjectiveCardAdapter;
import it.polimi.ingsw.data.SideGameCardAdapter;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.gameCard.SideGameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.message.adapter.ServerToClientMessageAdapter;
import it.polimi.ingsw.network.server.message.ErrorServerToClientMessage;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import it.polimi.ingsw.network.server.message.successMessage.DeleteGameServerToClientMessage;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.server.message.successMessage.GetGamesServerToClientMessage;
import it.polimi.ingsw.network.server.message.successMessage.UpdateViewServerToClientMessage;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ServerToClientToServerToClientMessageAdapterTest")
public class ServerToClientToServerToClientMessageAdapterTest {

    Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(SideGameCard.class, new SideGameCardAdapter())
            .registerTypeAdapter(ObjectiveCard.class, new ObjectiveCardAdapter())
            .registerTypeAdapter(ServerToClientMessage.class, new ServerToClientMessageAdapter())
            .create();

    MainController mainController = new MainController();

    @Test
    @DisplayName("Test if serialization and deserialization of UpdateViewServerToClientMessage works")
    public void serializeAndDeserializeUpdateViewServerMessage() {

        mainController.createGame("gameName", "playerName", 2);
        mainController.joinGame("gameName", "playerName2");
        GameControllerMiddleware gameControllerMiddleware = mainController.getGameController("gameName");
        GameControllerView gameControllerView = mainController.getVirtualView("gameName");
        GameCard gameCard = mainController.getVirtualView("gameName").gameView().playerViews().stream().filter(playerView -> playerView.playerName().equals("playerName")).findFirst().get().starterCard();
        gameControllerMiddleware.placeCard("playerName", new Coordinate(0, 0), gameCard.getCardId());
        gameControllerView = mainController.getVirtualView("gameName");

        ServerToClientMessage updateViewServerToClientMessage = new UpdateViewServerToClientMessage(gameControllerView);
        // Serialize
        String jsonUpdateViewServerMessage = this.gson.toJson(updateViewServerToClientMessage);
        // Deserialize
        assertEquals(updateViewServerToClientMessage, this.gson.fromJson(jsonUpdateViewServerMessage, ServerToClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of GetGamesServerToClientMessage works")
    public void serializeAndDeserializeGetGamesServerMessage() {
        ArrayList<GameRecord> gameRecords = new ArrayList<>();
        gameRecords.add(new GameRecord("gameName", 2, 2));
        gameRecords.add(new GameRecord("gameName2", 1, 2));
        gameRecords.add(new GameRecord("gameName3", 0, 2));
        ServerToClientMessage getGamesServerToClientMessage = new GetGamesServerToClientMessage(gameRecords);
        // Serialize
        String jsonGetGamesServerMessage = this.gson.toJson(getGamesServerToClientMessage);
        // Deserialize
        assertEquals(getGamesServerToClientMessage, this.gson.fromJson(jsonGetGamesServerMessage, ServerToClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of DeleteGameServerToClientMessage works")
    public void serializeAndDeserializeDeleteGameServerMessage() {
        ServerToClientMessage deleteGameServerToClientMessage = new DeleteGameServerToClientMessage();
        // Serialize
        String jsonDeleteGameServerMessage = this.gson.toJson(deleteGameServerToClientMessage);
        // Deserialize
        assertEquals(deleteGameServerToClientMessage, this.gson.fromJson(jsonDeleteGameServerMessage, ServerToClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of ErrorServerToClientMessage works")
    public void serializeAndDeserializeErrorServerMessage() {
        ServerToClientMessage errorServerToClientMessage = new ErrorServerToClientMessage("error message");
        // Serialize
        String jsonErrorServerMessage = this.gson.toJson(errorServerToClientMessage);
        // Deserialize
        assertEquals(errorServerToClientMessage, this.gson.fromJson(jsonErrorServerMessage, ServerToClientMessage.class));
    }
}
