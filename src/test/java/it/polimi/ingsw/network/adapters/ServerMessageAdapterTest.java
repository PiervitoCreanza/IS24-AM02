package it.polimi.ingsw.network.adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.data.ObjectiveCardAdapter;
import it.polimi.ingsw.data.SideGameCardAdapter;
import it.polimi.ingsw.model.card.gameCard.SideGameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.network.server.message.ErrorServerMessage;
import it.polimi.ingsw.network.server.message.ServerMessage;
import it.polimi.ingsw.network.server.message.successMessage.DeleteGameServerMessage;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.server.message.successMessage.GetGamesServerMessage;
import it.polimi.ingsw.network.server.message.successMessage.UpdateViewServerMessage;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ServerMessageAdapterTest")
public class ServerMessageAdapterTest {

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(SideGameCard.class, new SideGameCardAdapter())
            .registerTypeAdapter(ObjectiveCard.class, new ObjectiveCardAdapter())
            .registerTypeAdapter(ServerMessage.class, new ServerMessageAdapter())
            .create();

    MainController mainController = new MainController();

    @Test
    @DisplayName("Test if serialization and deserialization of UpdateViewServerMessage works")
    public void serializeAndDeserializeUpdateViewServerMessage() {
        mainController.createGame("gameName", "playerName", 2);
        GameControllerView gameControllerView = mainController.getVirtualView("gameName");
        ServerMessage updateViewServerMessage = new UpdateViewServerMessage(gameControllerView);
        // Serialize
        String jsonUpdateViewServerMessage = this.gson.toJson(updateViewServerMessage);
        // Deserialize
        assertEquals(updateViewServerMessage, this.gson.fromJson(jsonUpdateViewServerMessage, ServerMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of GetGamesServerMessage works")
    public void serializeAndDeserializeGetGamesServerMessage() {
        HashSet<GameRecord> gameRecords = new HashSet<>();
        gameRecords.add(new GameRecord("gameName", 2, 2));
        gameRecords.add(new GameRecord("gameName2", 1, 2));
        gameRecords.add(new GameRecord("gameName3", 0, 2));
        ServerMessage getGamesServerMessage = new GetGamesServerMessage(gameRecords);
        // Serialize
        String jsonGetGamesServerMessage = this.gson.toJson(getGamesServerMessage);
        // Deserialize
        assertEquals(getGamesServerMessage, this.gson.fromJson(jsonGetGamesServerMessage, ServerMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of DeleteGameServerMessage works")
    public void serializeAndDeserializeDeleteGameServerMessage() {
        ServerMessage deleteGameServerMessage = new DeleteGameServerMessage();
        // Serialize
        String jsonDeleteGameServerMessage = this.gson.toJson(deleteGameServerMessage);
        // Deserialize
        assertEquals(deleteGameServerMessage, this.gson.fromJson(jsonDeleteGameServerMessage, ServerMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of ErrorServerMessage works")
    public void serializeAndDeserializeErrorServerMessage() {
        ServerMessage errorServerMessage = new ErrorServerMessage("error message");
        // Serialize
        String jsonErrorServerMessage = this.gson.toJson(errorServerMessage);
        // Deserialize
        assertEquals(errorServerMessage, this.gson.fromJson(jsonErrorServerMessage, ServerMessage.class));
    }
}
