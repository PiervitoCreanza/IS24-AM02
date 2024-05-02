package it.polimi.ingsw.network.adapters;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.model.card.gameCard.BackGameCard;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.gameCard.front.FrontGameCard;
import it.polimi.ingsw.model.card.objectiveCard.ItemObjectiveCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.card.objectiveCard.PositionalData;
import it.polimi.ingsw.model.card.objectiveCard.PositionalObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import it.polimi.ingsw.network.client.message.ClientMessage;
import it.polimi.ingsw.network.client.message.gameController.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientMessageAdapterTest {
    Parser parser = new Parser();

    @Test
    @DisplayName("Test if serialization and deserialization of GetGamesClientMessage works")
    public void serializeAndDeserializeGetGamesClientMessage() {
        ClientMessage getGamesClientMessage = new GetGamesClientMessage();
        // Serialize
        String jsonGetGamesClientMessage = this.parser.serializeToJson(getGamesClientMessage);
        // Deserialize
        assertEquals(getGamesClientMessage, this.parser.deserializeFromJson(jsonGetGamesClientMessage, ClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of CreateGameClientMessage works")
    public void serializeAndDeserializeCreateGameClientMessage() {
        ClientMessage createGameClientMessage = new CreateGameClientMessage("Game1", 3, "Player1");
        // Serialize
        String jsonCreateGameClientMessage = this.parser.serializeToJson(createGameClientMessage);
        // Deserialize
        assertEquals(createGameClientMessage, this.parser.deserializeFromJson(jsonCreateGameClientMessage, ClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of JoinGameClientMessage works")
    public void serializeAndDeserializeJoinGameClientMessage() {
        ClientMessage joinGameClientMessage = new JoinGameClientMessage("Game1", "Player1");
        // Serialize
        String jsonJoinGameClientMessage = this.parser.serializeToJson(joinGameClientMessage);
        // Deserialize
        assertEquals(joinGameClientMessage, this.parser.deserializeFromJson(jsonJoinGameClientMessage, ClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of ChoosePlayerColorClientMessage works")
    public void serializeAndDeserializeChoosePlayerColorClientMessage() {
        ClientMessage choosePlayerColorClientMessage = new ChoosePlayerColorClientMessage("Red", "Game1", "Player1");
        // Serialize
        String jsonChoosePlayerColorClientMessage = this.parser.serializeToJson(choosePlayerColorClientMessage);
        // Deserialize
        assertEquals(choosePlayerColorClientMessage, this.parser.deserializeFromJson(jsonChoosePlayerColorClientMessage, ClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of SetPlayerObjectiveClientMessage works")
    public void serializeAndDeserializeSetPlayerObjectiveClientMessage() {
        GameItemStore multiplier = new GameItemStore();
        multiplier.set(GameItemEnum.INKWELL, 2);
        ObjectiveCard objectiveCard1 = new ItemObjectiveCard(1, 2, multiplier);
        ClientMessage setPlayerObjectiveClientMessage1 = new SetPlayerObjectiveClientMessage("Game1", "Player1", objectiveCard1);
        // Serialize
        String jsonSetPlayerObjectiveClientMessage1 = this.parser.serializeToJson(setPlayerObjectiveClientMessage1);
        // Deserialize
        assertEquals(setPlayerObjectiveClientMessage1, this.parser.deserializeFromJson(jsonSetPlayerObjectiveClientMessage1, ClientMessage.class));
        ArrayList<PositionalData> positionalDataArrayList = new ArrayList<>();
        positionalDataArrayList.add(new PositionalData(new Coordinate(1, 1), CardColorEnum.RED));
        positionalDataArrayList.add(new PositionalData(new Coordinate(1, 2), CardColorEnum.BLUE));
        positionalDataArrayList.add(new PositionalData(new Coordinate(1, 3), CardColorEnum.BLUE));
        ObjectiveCard objectiveCard2 = new PositionalObjectiveCard(1, 2, positionalDataArrayList);
        ClientMessage setPlayerObjectiveClientMessage2 = new SetPlayerObjectiveClientMessage("Game2", "Player2", objectiveCard2);
        // Serialize
        String jsonSetPlayerObjectiveClientMessage2 = this.parser.serializeToJson(setPlayerObjectiveClientMessage2);
        // Deserialize
        assertEquals(setPlayerObjectiveClientMessage2, this.parser.deserializeFromJson(jsonSetPlayerObjectiveClientMessage2, ClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of PlaceCardClientMessage works")
    public void serializeAndDeserializePlaceCardClientMessage() {
        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.ANIMAL, 1);
        GameCard resourceGameCard = new GameCard(1, new FrontGameCard(new Corner(GameItemEnum.FUNGI), new Corner(GameItemEnum.PLANT), new Corner(GameItemEnum.ANIMAL), new Corner(GameItemEnum.INSECT), 1), new BackGameCard(new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), gameItemStore), CardColorEnum.RED);

        ClientMessage placeCardClientMessage = new PlaceCardClientMessage("Game1", "Player1", new Coordinate(1, 1), resourceGameCard);
        // Serialize
        String jsonPlaceCardClientMessage = this.parser.serializeToJson(placeCardClientMessage);
        // Deserialize
        assertEquals(placeCardClientMessage, this.parser.deserializeFromJson(jsonPlaceCardClientMessage, ClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of DrawCardFromFieldClientMessage works")
    public void serializeAndDeserializeDrawCardFromFieldClientMessage() {
        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.ANIMAL, 1);
        GameCard resourceGameCard = new GameCard(1, new FrontGameCard(new Corner(GameItemEnum.FUNGI), new Corner(GameItemEnum.PLANT), new Corner(GameItemEnum.ANIMAL), new Corner(GameItemEnum.INSECT), 1), new BackGameCard(new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), gameItemStore), CardColorEnum.RED);

        ClientMessage drawCardFromFieldClientMessage = new DrawCardFromFieldClientMessage("Game1", "Player1", resourceGameCard);
        // Serialize
        String jsonDrawCardFromFieldClientMessage = this.parser.serializeToJson(drawCardFromFieldClientMessage);
        // Deserialize
        assertEquals(drawCardFromFieldClientMessage, this.parser.deserializeFromJson(jsonDrawCardFromFieldClientMessage, ClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of DrawCardFromGoldDeckClientMessage works")
    public void serializeAndDeserializeDrawCardFromGoldDeckClientMessage() {
        ClientMessage drawCardFromGoldDeckClientMessage = new DrawCardFromGoldDeckClientMessage("Game1", "Player1");
        // Serialize
        String jsonDrawCardFromGoldDeckClientMessage = this.parser.serializeToJson(drawCardFromGoldDeckClientMessage);
        // Deserialize
        assertEquals(drawCardFromGoldDeckClientMessage, this.parser.deserializeFromJson(jsonDrawCardFromGoldDeckClientMessage, ClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of DrawCardFromResourceDeckClientMessage works")
    public void serializeAndDeserializeDrawCardFromResourceDeckClientMessage() {
        ClientMessage drawCardFromResourceDeckClientMessage = new DrawCardFromResourceDeckClientMessage("Game1", "Player1");
        // Serialize
        String jsonDrawCardFromResourceDeckClientMessage = this.parser.serializeToJson(drawCardFromResourceDeckClientMessage);
        // Deserialize
        assertEquals(drawCardFromResourceDeckClientMessage, this.parser.deserializeFromJson(jsonDrawCardFromResourceDeckClientMessage, ClientMessage.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of SwitchCardSideClientMessage works")
    public void serializeAndDeserializeSwitchCardSideClientMessage() {
        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.ANIMAL, 1);
        GameCard resourceGameCard = new GameCard(1, new FrontGameCard(new Corner(GameItemEnum.FUNGI), new Corner(GameItemEnum.PLANT), new Corner(GameItemEnum.ANIMAL), new Corner(GameItemEnum.INSECT), 1), new BackGameCard(new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), gameItemStore), CardColorEnum.RED);

        ClientMessage switchCardSideClientMessage = new SwitchCardSideClientMessage("Game1", "Player1", resourceGameCard);
        // Serialize
        String jsonSwitchCardSideClientMessage = this.parser.serializeToJson(switchCardSideClientMessage);
        // Deserialize
        assertEquals(switchCardSideClientMessage, this.parser.deserializeFromJson(jsonSwitchCardSideClientMessage, ClientMessage.class));
    }
}