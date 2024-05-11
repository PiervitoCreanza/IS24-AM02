package it.polimi.ingsw.network.client.message.adapter;

import com.google.gson.*;
import it.polimi.ingsw.network.server.message.ErrorServerToClientMessage;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import it.polimi.ingsw.network.server.message.chatMessageServerToClientMessage;
import it.polimi.ingsw.network.server.message.successMessage.DeleteGameServerToClientMessage;
import it.polimi.ingsw.network.server.message.successMessage.GetGamesServerToClientMessage;
import it.polimi.ingsw.network.server.message.successMessage.UpdateViewServerToClientMessage;

import java.lang.reflect.Type;

/**
 * This class implements the JsonDeserializer interface for the ClientToServerMessage class.
 * It provides a method to deserialize a JSON element into a ClientToServerMessage object.
 */
public class ServerToClientMessageAdapter implements JsonDeserializer<ServerToClientMessage> {

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
    public ServerToClientMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        // We convert the JsonElement to a JsonObject, we need it to get its properties
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Class<?> serverMessageTypeClass;
        // We get the "ServerAction" property
        String serverMessageType = jsonObject.get("serverAction").getAsString();
        // We decide which is the class type of the side
        switch (serverMessageType) {
            case "UPDATE_VIEW" -> serverMessageTypeClass = UpdateViewServerToClientMessage.class;
            case "DELETE_GAME" -> serverMessageTypeClass = DeleteGameServerToClientMessage.class;
            case "GET_GAMES" -> serverMessageTypeClass = GetGamesServerToClientMessage.class;
            case "ERROR_MSG" -> serverMessageTypeClass = ErrorServerToClientMessage.class;
            case "CHAT_MSG" -> serverMessageTypeClass = chatMessageServerToClientMessage.class;
            default -> throw new JsonParseException("playerAction property is missing or wrong");
        }
        // We deserialize the "serverMessageType" with the class type found above and return it
        return jsonDeserializationContext.deserialize(jsonObject, serverMessageTypeClass);
    }
}