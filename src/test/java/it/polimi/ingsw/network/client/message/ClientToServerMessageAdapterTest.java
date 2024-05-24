package it.polimi.ingsw.network.client.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.data.ObjectiveCardAdapter;
import it.polimi.ingsw.data.SideGameCardAdapter;
import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.model.card.gameCard.BackGameCard;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.gameCard.SideGameCard;
import it.polimi.ingsw.model.card.gameCard.front.FrontGameCard;
import it.polimi.ingsw.model.card.objectiveCard.ItemObjectiveCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.card.objectiveCard.PositionalData;
import it.polimi.ingsw.model.card.objectiveCard.PositionalObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import it.polimi.ingsw.network.client.message.gameController.*;
import it.polimi.ingsw.network.client.message.mainController.CreateGameClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.DeleteGameClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.GetGamesClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.JoinGameClientToServerMessage;
import it.polimi.ingsw.network.server.message.adapter.ClientToServerMessageAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ClientToServerToClientToServerToClientToServerMessageAdapterTest")
public class ClientToServerMessageAdapterTest {
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(SideGameCard.class, new SideGameCardAdapter())
            .registerTypeAdapter(ObjectiveCard.class, new ObjectiveCardAdapter())
            .registerTypeAdapter(ClientToServerMessage.class, new ClientToServerMessageAdapter())
            .create();

    @Test
    @DisplayName("Test if serialization and deserialization of GetGamesClientToServerMessage works")
    public void serializeAndDeserializeGetGamesClientToServerMessage() {
        ClientToServerMessage getGamesClientToServerMessage = new GetGamesClientToServerMessage();
        // Serialize
        String jsonGetGamesClientToServerMessage = this.gson.toJson(getGamesClientToServerMessage);
        // Deserialize
        assertEquals(getGamesClientToServerMessage, this.gson.fromJson(jsonGetGamesClientToServerMessage, ClientToServerMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of CreateGameClientToServerMessage works")
    public void serializeAndDeserializeCreateGameClientToServerMessage() {
        ClientToServerMessage createGameClientToServerMessage = new CreateGameClientToServerMessage("Game1", "Player1", 3);
        // Serialize
        String jsonCreateGameClientToServerMessage = this.gson.toJson(createGameClientToServerMessage);
        // Deserialize
        assertEquals(createGameClientToServerMessage, this.gson.fromJson(jsonCreateGameClientToServerMessage, ClientToServerMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of DeleteGameClientToServerMessage works")
    public void serializeAndDeserializeDeleteGameClientToServerMessage() {
        ClientToServerMessage deleteGameClientToServerMessage = new DeleteGameClientToServerMessage("Game1", "Player1");
        // Serialize
        String jsonCreateGameClientToServerMessage = this.gson.toJson(deleteGameClientToServerMessage);
        // Deserialize
        assertEquals(deleteGameClientToServerMessage, this.gson.fromJson(jsonCreateGameClientToServerMessage, ClientToServerMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of JoinGameClientToServerMessage works")
    public void serializeAndDeserializeJoinGameClientToServerMessage() {
        ClientToServerMessage joinGameClientToServerMessage = new JoinGameClientToServerMessage("Game1", "Player1");
        // Serialize
        String jsonJoinGameClientToServerMessage = this.gson.toJson(joinGameClientToServerMessage);
        // Deserialize
        assertEquals(joinGameClientToServerMessage, this.gson.fromJson(jsonJoinGameClientToServerMessage, ClientToServerMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of ChoosePlayerColorClientToServerMessage works")
    public void serializeAndDeserializeChoosePlayerColorClientToServerMessage() {
        ClientToServerMessage choosePlayerColorClientToServerMessage = new ChoosePlayerColorClientToServerMessage("Game1", "Player1", PlayerColorEnum.RED);
        // Serialize
        String jsonChoosePlayerColorClientToServerMessage = this.gson.toJson(choosePlayerColorClientToServerMessage);
        // Deserialize
        assertEquals(choosePlayerColorClientToServerMessage, this.gson.fromJson(jsonChoosePlayerColorClientToServerMessage, ClientToServerMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of SetPlayerObjectiveClientToServerMessage works")
    public void serializeAndDeserializeSetPlayerObjectiveClientToServerMessage() {
        GameItemStore multiplier = new GameItemStore();
        multiplier.set(GameItemEnum.INKWELL, 2);
        ObjectiveCard objectiveCard1 = new ItemObjectiveCard(1, 2, multiplier);
        ClientToServerMessage setPlayerObjectiveClientToServerMessage1 = new SetPlayerObjectiveClientToServerMessage("Game1", "Player1", objectiveCard1.getCardId());
        // Serialize
        String jsonSetPlayerObjectiveClientToServerMessage1 = this.gson.toJson(setPlayerObjectiveClientToServerMessage1);
        // Deserialize
        assertEquals(setPlayerObjectiveClientToServerMessage1, this.gson.fromJson(jsonSetPlayerObjectiveClientToServerMessage1, ClientToServerMessage.class));
        ArrayList<PositionalData> positionalDataArrayList = new ArrayList<>();
        positionalDataArrayList.add(new PositionalData(new Coordinate(1, 1), CardColorEnum.RED));
        positionalDataArrayList.add(new PositionalData(new Coordinate(1, 2), CardColorEnum.CYAN));
        positionalDataArrayList.add(new PositionalData(new Coordinate(1, 3), CardColorEnum.CYAN));
        ObjectiveCard objectiveCard2 = new PositionalObjectiveCard(1, 2, positionalDataArrayList);
        ClientToServerMessage setPlayerObjectiveClientToServerMessage2 = new SetPlayerObjectiveClientToServerMessage("Game2", "Player2", objectiveCard2.getCardId());
        // Serialize
        String jsonSetPlayerObjectiveClientToServerMessage2 = this.gson.toJson(setPlayerObjectiveClientToServerMessage2);
        // Deserialize
        assertEquals(setPlayerObjectiveClientToServerMessage2, this.gson.fromJson(jsonSetPlayerObjectiveClientToServerMessage2, ClientToServerMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of PlaceCardClientToServerMessage works")
    public void serializeAndDeserializePlaceCardClientToServerMessage() {
        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.ANIMAL, 1);
        GameCard resourceGameCard = new GameCard(1, new FrontGameCard(new Corner(GameItemEnum.FUNGI), new Corner(GameItemEnum.PLANT), new Corner(GameItemEnum.ANIMAL), new Corner(GameItemEnum.INSECT), 1), new BackGameCard(new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), gameItemStore), CardColorEnum.RED);

        ClientToServerMessage placeCardClientToServerMessage = new PlaceCardClientToServerMessage("Game1", "Player1", new Coordinate(1, 1), resourceGameCard.getCardId(), false);
        // Serialize
        String jsonPlaceCardClientToServerMessage = this.gson.toJson(placeCardClientToServerMessage);
        // Deserialize
        assertEquals(placeCardClientToServerMessage, this.gson.fromJson(jsonPlaceCardClientToServerMessage, ClientToServerMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of DrawCardFromFieldClientToServerMessage works")
    public void serializeAndDeserializeDrawCardFromFieldClientToServerMessage() {
        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.ANIMAL, 1);
        GameCard resourceGameCard = new GameCard(1, new FrontGameCard(new Corner(GameItemEnum.FUNGI), new Corner(GameItemEnum.PLANT), new Corner(GameItemEnum.ANIMAL), new Corner(GameItemEnum.INSECT), 1), new BackGameCard(new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), gameItemStore), CardColorEnum.RED);

        ClientToServerMessage drawCardFromFieldClientToServerMessage = new DrawCardFromFieldClientToServerMessage("Game1", "Player1", resourceGameCard);
        // Serialize
        String jsonDrawCardFromFieldClientToServerMessage = this.gson.toJson(drawCardFromFieldClientToServerMessage);
        // Deserialize
        assertEquals(drawCardFromFieldClientToServerMessage, this.gson.fromJson(jsonDrawCardFromFieldClientToServerMessage, ClientToServerMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of DrawCardFromGoldDeckClientToServerMessage works")
    public void serializeAndDeserializeDrawCardFromGoldDeckClientToServerMessage() {
        ClientToServerMessage drawCardFromGoldDeckClientToServerMessage = new DrawCardFromGoldDeckClientToServerMessage("Game1", "Player1");
        // Serialize
        String jsonDrawCardFromGoldDeckClientToServerMessage = this.gson.toJson(drawCardFromGoldDeckClientToServerMessage);
        // Deserialize
        assertEquals(drawCardFromGoldDeckClientToServerMessage, this.gson.fromJson(jsonDrawCardFromGoldDeckClientToServerMessage, ClientToServerMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of DrawCardFromResourceDeckClientToServerMessage works")
    public void serializeAndDeserializeDrawCardFromResourceDeckClientToServerMessage() {
        ClientToServerMessage drawCardFromResourceDeckClientToServerMessage = new DrawCardFromResourceDeckClientToServerMessage("Game1", "Player1");
        // Serialize
        String jsonDrawCardFromResourceDeckClientToServerMessage = this.gson.toJson(drawCardFromResourceDeckClientToServerMessage);
        // Deserialize
        assertEquals(drawCardFromResourceDeckClientToServerMessage, this.gson.fromJson(jsonDrawCardFromResourceDeckClientToServerMessage, ClientToServerMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of SwitchCardSideClientToServerMessage works")
    public void serializeAndDeserializeSwitchCardSideClientToServerMessage() {
        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.ANIMAL, 1);
        GameCard resourceGameCard = new GameCard(1, new FrontGameCard(new Corner(GameItemEnum.FUNGI), new Corner(GameItemEnum.PLANT), new Corner(GameItemEnum.ANIMAL), new Corner(GameItemEnum.INSECT), 1), new BackGameCard(new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), gameItemStore), CardColorEnum.RED);

        ClientToServerMessage switchCardSideClientToServerMessage = new SwitchCardSideClientToServerMessage("Game1", "Player1", resourceGameCard.getCardId());
        // Serialize
        String jsonSwitchCardSideClientToServerMessage = this.gson.toJson(switchCardSideClientToServerMessage);
        // Deserialize
        assertEquals(switchCardSideClientToServerMessage, this.gson.fromJson(jsonSwitchCardSideClientToServerMessage, ClientToServerMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of ChatClientToServerMessage works")
    public void serializeAndDeserializeChatClientToServerMessage() {
        ClientToServerMessage chatClientToServerMessage = new ChatClientToServerMessage("Game1", "Player1", "Hello", "Player2");
        // Serialize
        String jsonChatClientToServerMessage = this.gson.toJson(chatClientToServerMessage);
        // Deserialize
        assertEquals(chatClientToServerMessage, this.gson.fromJson(jsonChatClientToServerMessage, ClientToServerMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of DisconnectClientToServerMessage works")
    public void serializeAndDeserializeDisconnectClientToServerMessage() {
        ClientToServerMessage disconnectClientToServerMessage = new DisconnectClientToServerMessage("Game1", "Player1");
        // Serialize
        String jsonDisconnectClientToServerMessage = this.gson.toJson(disconnectClientToServerMessage);
        // Deserialize
        assertEquals(disconnectClientToServerMessage, this.gson.fromJson(jsonDisconnectClientToServerMessage, ClientToServerMessage.class));
    }
}