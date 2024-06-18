package it.polimi.ingsw.data;

import com.google.gson.*;
import it.polimi.ingsw.model.card.gameCard.SerializableBooleanProperty;

import java.lang.reflect.Type;
// TODO DELETE

/**
 * This class implements JsonDeserializer and JsonSerializer interfaces for SerializableBooleanProperty.
 * It provides methods to convert SerializableBooleanProperty to JSON and vice versa.
 */
public class SerializableBooleanPropertyAdapter implements JsonDeserializer<SerializableBooleanProperty>, JsonSerializer<SerializableBooleanProperty> {

    /**
     * This method is used to convert a JsonElement into a SerializableBooleanProperty.
     * It is overridden from JsonDeserializer interface.
     *
     * @param jsonElement                the JSON element to be deserialized
     * @param type                       the type of the object to be deserialized
     * @param jsonDeserializationContext context for deserialization
     * @return the deserialized SerializableBooleanProperty
     * @throws JsonParseException if jsonElement cannot be converted into a SerializableBooleanProperty
     */
    @Override
    public SerializableBooleanProperty deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new SerializableBooleanProperty(jsonElement.getAsBoolean());
    }

    /**
     * This method is used to convert a SerializableBooleanProperty into a JsonElement.
     * It is overridden from JsonSerializer interface.
     *
     * @param serializableBooleanProperty the SerializableBooleanProperty to be serialized
     * @param type                        the type of the source object
     * @param jsonSerializationContext    context for serialization
     * @return the serialized JsonElement
     */
    @Override
    public JsonElement serialize(SerializableBooleanProperty serializableBooleanProperty, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(serializableBooleanProperty.get());
    }
}