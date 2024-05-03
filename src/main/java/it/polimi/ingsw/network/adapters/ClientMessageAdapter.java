package it.polimi.ingsw.network.adapters;

import com.google.gson.*;
import it.polimi.ingsw.network.client.message.ClientMessage;
import it.polimi.ingsw.network.client.message.gameController.*;
import it.polimi.ingsw.network.client.message.mainController.CreateGameClientMessage;
import it.polimi.ingsw.network.client.message.mainController.DeleteGameClientMessage;
import it.polimi.ingsw.network.client.message.mainController.GetGamesClientMessage;
import it.polimi.ingsw.network.client.message.mainController.JoinGameClientMessage;

import java.lang.reflect.Type;

/**
 * This class implements the JsonDeserializer interface for the ClientMessage class.
 * It provides a method to deserialize a JSON element into a ClientMessage object.
 */
public class ClientMessageAdapter implements JsonDeserializer<ClientMessage> {

    /**
     * This method deserializes a JSON element into a ClientMessage object.
     * It uses the "playerAction" property of the JSON object to determine the specific type of ClientMessage to create.
     *
     * @param jsonElement                The JSON element to deserialize.
     * @param type                       The specific type of the object to deserialize.
     * @param jsonDeserializationContext Context for deserialization that is passed to a custom deserializer during invocation of its JsonDeserializer.deserialize method.
     * @return The deserialized ClientMessage object.
     * @throws JsonParseException If the "playerAction" property is missing or wrong.
     */
    public ClientMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        // We convert the JsonElement to a JsonObject, we need it to get its properties
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Class<?> clientMessageTypeClass;
        // We get the "playerAction" property
        String clientMessageType = jsonObject.get("playerAction").getAsString();
        // We decide which is the class type of the side
        switch (clientMessageType) {
            case "GET_GAMES" -> clientMessageTypeClass = GetGamesClientMessage.class;
            case "CREATE_GAME" -> clientMessageTypeClass = CreateGameClientMessage.class;
            case "DELETE_GAME" -> clientMessageTypeClass = DeleteGameClientMessage.class;
            case "JOIN_GAME" -> clientMessageTypeClass = JoinGameClientMessage.class;
            case "CHOOSE_PLAYER_COLOR" -> clientMessageTypeClass = ChoosePlayerColorClientMessage.class;
            case "SET_PLAYER_OBJECTIVE" -> clientMessageTypeClass = SetPlayerObjectiveClientMessage.class;
            case "PLACE_CARD" -> clientMessageTypeClass = PlaceCardClientMessage.class;
            case "DRAW_CARD_FROM_FIELD" -> clientMessageTypeClass = DrawCardFromFieldClientMessage.class;
            case "DRAW_CARD_FROM_RESOURCE_DECK" -> clientMessageTypeClass = DrawCardFromResourceDeckClientMessage.class;
            case "DRAW_CARD_FROM_GOLD_DECK" -> clientMessageTypeClass = DrawCardFromGoldDeckClientMessage.class;
            case "SWITCH_CARD_SIDE" -> clientMessageTypeClass = SwitchCardSideClientMessage.class;
            default -> throw new JsonParseException("playerAction property is missing or wrong");
        }
        // We deserialize the "sideContent" with the class type found above and return it
        return jsonDeserializationContext.deserialize(jsonObject, clientMessageTypeClass);
    }
}