package it.polimi.ingsw.data;

import com.google.gson.*;
import javafx.beans.property.SimpleBooleanProperty;

import java.lang.reflect.Type;

/**
 * This class is a custom deserializer for Gson to handle JavaFX BooleanProperty.
 * It implements the JsonDeserializer interface provided by Gson.
 */
public class BooleanPropertyDeserializer implements JsonDeserializer<SimpleBooleanProperty>, JsonSerializer<SimpleBooleanProperty> {

    /**
     * This method overrides the deserialize method of the JsonDeserializer interface.
     * It checks if the JsonElement is not null and is a boolean, then returns a new SimpleBooleanProperty with the value of the JsonElement.
     * If the JsonElement is null or not a boolean, it returns a new SimpleBooleanProperty with a default value of false.
     *
     * @param json    the JsonElement being deserialized
     * @param typeOfT the type of the Object to deserialize to
     * @param context context for deserialization
     * @return a new SimpleBooleanProperty with the value of the JsonElement, or a new SimpleBooleanProperty with a default value of false if the JsonElement is null or not a boolean
     * @throws JsonParseException if json is not in the expected format of {@code typeOfT}
     */
    @Override
    public SimpleBooleanProperty deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // Check if the json element is not null and is a boolean
        return new SimpleBooleanProperty(json.getAsBoolean());
    }

    @Override
    public JsonElement serialize(SimpleBooleanProperty booleanProperty, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(booleanProperty.get());
    }
}