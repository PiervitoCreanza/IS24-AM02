package it.polimi.ingsw.network.adapters;

import com.google.gson.*;
import it.polimi.ingsw.network.server.message.ErrorServerMessage;
import it.polimi.ingsw.network.server.message.ServerMessage;
import it.polimi.ingsw.network.server.message.successMessage.DeleteGameServerMessage;
import it.polimi.ingsw.network.server.message.successMessage.GetGamesServerMessage;
import it.polimi.ingsw.network.server.message.successMessage.UpdateViewServerMessage;

import java.lang.reflect.Type;

/**
 * This class implements the JsonDeserializer interface for the ClientMessage class.
 * It provides a method to deserialize a JSON element into a ClientMessage object.
 */
public class ServerMessageAdapter implements JsonDeserializer<ServerMessage> {

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
    public ServerMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        // We convert the JsonElement to a JsonObject, we need it to get its properties
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Class<?> serverMessageTypeClass;
        // We get the "ServerAction" property
        String serverMessageType = jsonObject.get("serverAction").getAsString();
        // We decide which is the class type of the side
        switch (serverMessageType) {
            case "UPDATE_VIEW" -> serverMessageTypeClass = UpdateViewServerMessage.class;
            case "DELETE_GAME" -> serverMessageTypeClass = DeleteGameServerMessage.class;
            case "GET_GAMES" -> serverMessageTypeClass = GetGamesServerMessage.class;
            case "ERROR_MSG" -> serverMessageTypeClass = ErrorServerMessage.class;
            default -> throw new JsonParseException("playerAction property is missing or wrong");
        }
        // We deserialize the "serverMessageType" with the class type found above and return it
        return jsonDeserializationContext.deserialize(jsonObject, serverMessageTypeClass);
    }
}