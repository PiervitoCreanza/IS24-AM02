package it.polimi.ingsw.network.adapters;

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
import it.polimi.ingsw.network.client.message.ClientMessage;
import it.polimi.ingsw.network.client.message.gameController.*;
import it.polimi.ingsw.network.client.message.mainController.CreateGameClientMessage;
import it.polimi.ingsw.network.client.message.mainController.DeleteGameClientMessage;
import it.polimi.ingsw.network.client.message.mainController.GetGamesClientMessage;
import it.polimi.ingsw.network.client.message.mainController.JoinGameClientMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ClientMessageAdapterTest")
public class ClientMessageAdapterTest {
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(SideGameCard.class, new SideGameCardAdapter())
            .registerTypeAdapter(ObjectiveCard.class, new ObjectiveCardAdapter())
            .registerTypeAdapter(ClientMessage.class, new ClientMessageAdapter())
            .create();

    @Test
    @DisplayName("Test if serialization and deserialization of GetGamesClientMessage works")
    public void serializeAndDeserializeGetGamesClientMessage() {
        ClientMessage getGamesClientMessage = new GetGamesClientMessage();
        // Serialize
        String jsonGetGamesClientMessage = this.gson.toJson(getGamesClientMessage);
        // Deserialize
        assertEquals(getGamesClientMessage, this.gson.fromJson(jsonGetGamesClientMessage, ClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of CreateGameClientMessage works")
    public void serializeAndDeserializeCreateGameClientMessage() {
        ClientMessage createGameClientMessage = new CreateGameClientMessage("Game1", 3, "Player1");
        // Serialize
        String jsonCreateGameClientMessage = this.gson.toJson(createGameClientMessage);
        // Deserialize
        assertEquals(createGameClientMessage, this.gson.fromJson(jsonCreateGameClientMessage, ClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of DeleteGameClientMessage works")
    public void serializeAndDeserializeDeleteGameClientMessage() {
        ClientMessage deleteGameClientMessage = new DeleteGameClientMessage("Game1", "Player1");
        // Serialize
        String jsonCreateGameClientMessage = this.gson.toJson(deleteGameClientMessage);
        // Deserialize
        assertEquals(deleteGameClientMessage, this.gson.fromJson(jsonCreateGameClientMessage, ClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of JoinGameClientMessage works")
    public void serializeAndDeserializeJoinGameClientMessage() {
        ClientMessage joinGameClientMessage = new JoinGameClientMessage("Game1", "Player1");
        // Serialize
        String jsonJoinGameClientMessage = this.gson.toJson(joinGameClientMessage);
        // Deserialize
        assertEquals(joinGameClientMessage, this.gson.fromJson(jsonJoinGameClientMessage, ClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of ChoosePlayerColorClientMessage works")
    public void serializeAndDeserializeChoosePlayerColorClientMessage() {
        ClientMessage choosePlayerColorClientMessage = new ChoosePlayerColorClientMessage(PlayerColorEnum.RED, "Game1", "Player1");
        // Serialize
        String jsonChoosePlayerColorClientMessage = this.gson.toJson(choosePlayerColorClientMessage);
        // Deserialize
        assertEquals(choosePlayerColorClientMessage, this.gson.fromJson(jsonChoosePlayerColorClientMessage, ClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of SetPlayerObjectiveClientMessage works")
    public void serializeAndDeserializeSetPlayerObjectiveClientMessage() {
        GameItemStore multiplier = new GameItemStore();
        multiplier.set(GameItemEnum.INKWELL, 2);
        ObjectiveCard objectiveCard1 = new ItemObjectiveCard(1, 2, multiplier);
        ClientMessage setPlayerObjectiveClientMessage1 = new SetPlayerObjectiveClientMessage("Game1", "Player1", objectiveCard1);
        // Serialize
        String jsonSetPlayerObjectiveClientMessage1 = this.gson.toJson(setPlayerObjectiveClientMessage1);
        // Deserialize
        assertEquals(setPlayerObjectiveClientMessage1, this.gson.fromJson(jsonSetPlayerObjectiveClientMessage1, ClientMessage.class));
        ArrayList<PositionalData> positionalDataArrayList = new ArrayList<>();
        positionalDataArrayList.add(new PositionalData(new Coordinate(1, 1), CardColorEnum.RED));
        positionalDataArrayList.add(new PositionalData(new Coordinate(1, 2), CardColorEnum.BLUE));
        positionalDataArrayList.add(new PositionalData(new Coordinate(1, 3), CardColorEnum.BLUE));
        ObjectiveCard objectiveCard2 = new PositionalObjectiveCard(1, 2, positionalDataArrayList);
        ClientMessage setPlayerObjectiveClientMessage2 = new SetPlayerObjectiveClientMessage("Game2", "Player2", objectiveCard2);
        // Serialize
        String jsonSetPlayerObjectiveClientMessage2 = this.gson.toJson(setPlayerObjectiveClientMessage2);
        // Deserialize
        assertEquals(setPlayerObjectiveClientMessage2, this.gson.fromJson(jsonSetPlayerObjectiveClientMessage2, ClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of PlaceCardClientMessage works")
    public void serializeAndDeserializePlaceCardClientMessage() {
        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.ANIMAL, 1);
        GameCard resourceGameCard = new GameCard(1, new FrontGameCard(new Corner(GameItemEnum.FUNGI), new Corner(GameItemEnum.PLANT), new Corner(GameItemEnum.ANIMAL), new Corner(GameItemEnum.INSECT), 1), new BackGameCard(new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), gameItemStore), CardColorEnum.RED);

        ClientMessage placeCardClientMessage = new PlaceCardClientMessage("Game1", "Player1", new Coordinate(1, 1), resourceGameCard);
        // Serialize
        String jsonPlaceCardClientMessage = this.gson.toJson(placeCardClientMessage);
        // Deserialize
        assertEquals(placeCardClientMessage, this.gson.fromJson(jsonPlaceCardClientMessage, ClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of DrawCardFromFieldClientMessage works")
    public void serializeAndDeserializeDrawCardFromFieldClientMessage() {
        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.ANIMAL, 1);
        GameCard resourceGameCard = new GameCard(1, new FrontGameCard(new Corner(GameItemEnum.FUNGI), new Corner(GameItemEnum.PLANT), new Corner(GameItemEnum.ANIMAL), new Corner(GameItemEnum.INSECT), 1), new BackGameCard(new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), gameItemStore), CardColorEnum.RED);

        ClientMessage drawCardFromFieldClientMessage = new DrawCardFromFieldClientMessage("Game1", "Player1", resourceGameCard);
        // Serialize
        String jsonDrawCardFromFieldClientMessage = this.gson.toJson(drawCardFromFieldClientMessage);
        // Deserialize
        assertEquals(drawCardFromFieldClientMessage, this.gson.fromJson(jsonDrawCardFromFieldClientMessage, ClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of DrawCardFromGoldDeckClientMessage works")
    public void serializeAndDeserializeDrawCardFromGoldDeckClientMessage() {
        ClientMessage drawCardFromGoldDeckClientMessage = new DrawCardFromGoldDeckClientMessage("Game1", "Player1");
        // Serialize
        String jsonDrawCardFromGoldDeckClientMessage = this.gson.toJson(drawCardFromGoldDeckClientMessage);
        // Deserialize
        assertEquals(drawCardFromGoldDeckClientMessage, this.gson.fromJson(jsonDrawCardFromGoldDeckClientMessage, ClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of DrawCardFromResourceDeckClientMessage works")
    public void serializeAndDeserializeDrawCardFromResourceDeckClientMessage() {
        ClientMessage drawCardFromResourceDeckClientMessage = new DrawCardFromResourceDeckClientMessage("Game1", "Player1");
        // Serialize
        String jsonDrawCardFromResourceDeckClientMessage = this.gson.toJson(drawCardFromResourceDeckClientMessage);
        // Deserialize
        assertEquals(drawCardFromResourceDeckClientMessage, this.gson.fromJson(jsonDrawCardFromResourceDeckClientMessage, ClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of SwitchCardSideClientMessage works")
    public void serializeAndDeserializeSwitchCardSideClientMessage() {
        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.ANIMAL, 1);
        GameCard resourceGameCard = new GameCard(1, new FrontGameCard(new Corner(GameItemEnum.FUNGI), new Corner(GameItemEnum.PLANT), new Corner(GameItemEnum.ANIMAL), new Corner(GameItemEnum.INSECT), 1), new BackGameCard(new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), gameItemStore), CardColorEnum.RED);

        ClientMessage switchCardSideClientMessage = new SwitchCardSideClientMessage("Game1", "Player1", resourceGameCard);
        // Serialize
        String jsonSwitchCardSideClientMessage = this.gson.toJson(switchCardSideClientMessage);
        // Deserialize
        assertEquals(switchCardSideClientMessage, this.gson.fromJson(jsonSwitchCardSideClientMessage, ClientMessage.class));
    }
}