package it.polimi.ingsw.network.server.message.adapter;

import com.google.gson.*;
import it.polimi.ingsw.network.client.message.ClientToServerMessage;
import it.polimi.ingsw.network.client.message.gameController.*;
import it.polimi.ingsw.network.client.message.mainController.CreateGameClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.DeleteGameClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.GetGamesClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.JoinGameClientToServerMessage;

import java.lang.reflect.Type;

/**
 * This class implements the JsonDeserializer interface for the ClientToServerMessage class.
 * It provides a method to deserialize a JSON element into a ClientToServerMessage object.
 */
public class ClientToServerMessageAdapter implements JsonDeserializer<ClientToServerMessage> {

    /**
     * This method deserializes a JSON element into a ClientToServerMessage object.
     * It uses the "playerAction" property of the JSON object to determine the specific type of ClientToServerMessage to create.
     *
     * @param jsonElement                The JSON element to deserialize.
     * @param type                       The specific type of the object to deserialize.
     * @param jsonDeserializationContext Context for deserialization that is passed to a custom deserializer during invocation of its JsonDeserializer.deserialize method.
     * @return The deserialized ClientToServerMessage object.
     * @throws JsonParseException If the "playerAction" property is missing or wrong.
     */
    public ClientToServerMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        // We convert the JsonElement to a JsonObject, we need it to get its properties
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Class<?> clientMessageTypeClass;
        // We get the "playerAction" property
        String clientMessageType = jsonObject.get("playerAction").getAsString();
        // We decide which is the class type of the side
        switch (clientMessageType) {
            case "GET_GAMES" -> clientMessageTypeClass = GetGamesClientToServerMessage.class;
            case "CREATE_GAME" -> clientMessageTypeClass = CreateGameClientToServerMessage.class;
            case "DELETE_GAME" -> clientMessageTypeClass = DeleteGameClientToServerMessage.class;
            case "JOIN_GAME" -> clientMessageTypeClass = JoinGameClientToServerMessage.class;
            case "CHOOSE_PLAYER_COLOR" -> clientMessageTypeClass = ChoosePlayerColorClientToServerMessage.class;
            case "SET_PLAYER_OBJECTIVE" -> clientMessageTypeClass = SetPlayerObjectiveClientToServerMessage.class;
            case "PLACE_CARD" -> clientMessageTypeClass = PlaceCardClientToServerMessage.class;
            case "DRAW_CARD_FROM_FIELD" -> clientMessageTypeClass = DrawCardFromFieldClientToServerMessage.class;
            case "DRAW_CARD_FROM_RESOURCE_DECK" ->
                    clientMessageTypeClass = DrawCardFromResourceDeckClientToServerMessage.class;
            case "DRAW_CARD_FROM_GOLD_DECK" -> clientMessageTypeClass = DrawCardFromGoldDeckClientToServerMessage.class;
            case "SWITCH_CARD_SIDE" -> clientMessageTypeClass = SwitchCardSideClientToServerMessage.class;
            default -> throw new JsonParseException("playerAction property is missing or wrong");
        }
        // We deserialize the "sideContent" with the class type found above and return it
        return jsonDeserializationContext.deserialize(jsonObject, clientMessageTypeClass);
    }
}