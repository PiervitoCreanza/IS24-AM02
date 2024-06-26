package it.polimi.ingsw.parsing.adapters;

import com.google.gson.*;
import it.polimi.ingsw.model.card.objectiveCard.ItemObjectiveCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.card.objectiveCard.PositionalObjectiveCard;

import java.lang.reflect.Type;

/**
 * This class implements the JsonSerializer and JsonDeserializer interfaces for the ObjectiveCard class.
 * It provides methods to serialize and deserialize an ObjectiveCard object to and from JSON.
 */
public class ObjectiveCardAdapter implements JsonSerializer<ObjectiveCard>, JsonDeserializer<ObjectiveCard> {

    /**
     * This method deserializes a JSON element into an ObjectiveCard object.
     * It uses the "objectiveType" property of the JSON object to determine the specific type of ObjectiveCard to create.
     *
     * @param jsonElement                The JSON element to deserialize.
     * @param type                       The specific type of the object to deserialize.
     * @param jsonDeserializationContext Context for deserialization that is passed to a custom deserializer during invocation of its JsonDeserializer.deserialize method.
     * @return The deserialized ObjectiveCard object.
     * @throws JsonParseException If the "objectiveType" property is missing or wrong.
     */
    @Override
    public ObjectiveCard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Class<?> objectiveTypeClass;
        String objectiveType = jsonObject.get("objectiveType").getAsString();
        switch (objectiveType) {
            case "PositionalObjectiveCard" -> objectiveTypeClass = PositionalObjectiveCard.class;
            case "ItemObjectiveCard" -> objectiveTypeClass = ItemObjectiveCard.class;
            default -> throw new JsonParseException("objectiveType property is missing or wrong");
        }
        return jsonDeserializationContext.deserialize(jsonObject.get("objectiveContent"), objectiveTypeClass);
    }

    /**
     * This method serializes an ObjectiveCard object into a JSON element.
     * It uses the class of the ObjectiveCard object to determine the "objectiveType" property of the JSON object.
     *
     * @param objectiveCard            The ObjectiveCard object to serialize.
     * @param type                     The specific type of the object to serialize.
     * @param jsonSerializationContext Context for serialization that is passed to a custom serializer during invocation of its JsonSerializer.serialize method.
     * @return The serialized JSON element.
     * @throws RuntimeException If the ObjectiveCard object is of an unknown class.
     */
    @Override
    public JsonElement serialize(ObjectiveCard objectiveCard, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        Class<?> objectiveTypeClass;
        String objectiveType;
        switch (objectiveCard) {
            case PositionalObjectiveCard ignored -> {
                objectiveType = "PositionalObjectiveCard";
                objectiveTypeClass = PositionalObjectiveCard.class;
            }
            case ItemObjectiveCard ignored -> {
                objectiveType = "ItemObjectiveCard";
                objectiveTypeClass = ItemObjectiveCard.class;
            }
            default -> throw new RuntimeException("objectiveGameCard is of unknown class");
        }
        jsonObject.addProperty("objectiveType", objectiveType);
        jsonObject.add("objectiveContent", jsonSerializationContext.serialize(objectiveCard, objectiveTypeClass));
        return jsonObject;
    }
}