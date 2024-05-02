package it.polimi.ingsw.network.adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.client.message.ClientMessage;

public class ClientMessageAdapterTest {
    Gson gson = new GsonBuilder().registerTypeAdapter(ClientMessage.class, new ClientMessageAdapter()).create();
/*
    @Test
    @DisplayName("Test if serialization and deserialization of GetGamesClientMessage works")
    public void serializeAndDeserializeResourceCard() {
        ClientMessage getGamesClientMessage = new GetGamesClientMessage();
        // Serialize
        String jsonGetGamesClientMessage = this.gson.toJson(getGamesClientMessage);
        // Deserialize
        assertEquals(getGamesClientMessage, this.gson.fromJson(jsonGetGamesClientMessage, ClientMessage.class));

    }

    // Creazione di una nuova istanza di GetGamesClientMessage
    GetGamesClientMessage getGamesClientMessage = new GetGamesClientMessage();

    // Creazione di una nuova istanza di CreateGameClientMessage
    CreateGameClientMessage createGameClientMessage = new CreateGameClientMessage("Game1", 3, "Player1");

    // Creazione di una nuova istanza di JoinGameClientMessage
    JoinGameClientMessage joinGameClientMessage = new JoinGameClientMessage("Game1", "Player1");

    // Creazione di una nuova istanza di ChoosePlayerColorClientMessage
    ChoosePlayerColorClientMessage choosePlayerColorClientMessage = new ChoosePlayerColorClientMessage("Red", "Game1", "Player1");

    // Creazione di una nuova istanza di SetPlayerObjectiveClientMessage
// Assumendo che abbiamo un'istanza di ObjectiveCard chiamata objectiveCard
    SetPlayerObjectiveClientMessage setPlayerObjectiveClientMessage = new SetPlayerObjectiveClientMessage("Game1", "Player1", objectiveCard);

    // Creazione di una nuova istanza di PlaceCardClientMessage
// Assumendo che abbiamo un'istanza di Coordinate chiamata coordinate e un'istanza di GameCard chiamata gameCard
    PlaceCardClientMessage placeCardClientMessage = new PlaceCardClientMessage("Game1", "Player1", coordinate, gameCard);

    // Creazione di una nuova istanza di DrawCardFromFieldClientMessage
// Assumendo che abbiamo un'istanza di GameCard chiamata gameCard
    DrawCardFromFieldClientMessage drawCardFromFieldClientMessage = new DrawCardFromFieldClientMessage("Game1", "Player1", gameCard);

    // Creazione di una nuova istanza di DrawCardFromGoldDeckClientMessage
    DrawCardFromGoldDeckClientMessage drawCardFromGoldDeckClientMessage = new DrawCardFromGoldDeckClientMessage("Game1", "Player1");

    // Creazione di una nuova istanza di DrawCardFromResourceDeckClientMessage
    DrawCardFromResourceDeckClientMessage drawCardFromResourceDeckClientMessage = new DrawCardFromResourceDeckClientMessage("Game1", "Player1");

    // Creazione di una nuova istanza di SwitchCardSideClientMessage
// Assumendo che abbiamo un'istanza di GameCard chiamata gameCard
    SwitchCardSideClientMessage switchCardSideClientMessage = new SwitchCardSideClientMessage("Game1", "Player1", gameCard);

    @Test
    public void deserializeShouldReturnGetGamesClientMessageWhenPlayerActionIsGETGAMES() {
        String json = "{\"playerAction\":\"GETGAMES\"}";
        ClientMessage message = gson.fromJson(json, ClientMessage.class);
        assertTrue(message instanceof GetGamesClientMessage);
    }

    @Test
    public void deserializeShouldReturnCreateGameClientMessageWhenPlayerActionIsCREATEGAME() {
        String json = "{\"playerAction\":\"CREATEGAME\"}";
        ClientMessage message = gson.fromJson(json, ClientMessage.class);
        assertTrue(message instanceof CreateGameClientMessage);
    }

    // Add similar tests for other message types...

    @Test
    public void deserializeShouldThrowJsonParseExceptionWhenPlayerActionIsMissing() {
        String json = "{}";
        assertThrows(JsonParseException.class, () -> gson.fromJson(json, ClientMessage.class));
    }

    @Test
    public void deserializeShouldThrowJsonParseExceptionWhenPlayerActionIsUnknown() {
        String json = "{\"playerAction\":\"UNKNOWN\"}";
        assertThrows(JsonParseException.class, () -> gson.fromJson(json, ClientMessage.class));
    }
 */
}