package it.polimi.ingsw.parsing.adapters;

import com.google.gson.*;
import it.polimi.ingsw.model.card.gameCard.BackGameCard;
import it.polimi.ingsw.model.card.gameCard.SideGameCard;
import it.polimi.ingsw.model.card.gameCard.front.FrontGameCard;
import it.polimi.ingsw.model.card.gameCard.front.goldCard.FrontGoldGameCard;
import it.polimi.ingsw.model.card.gameCard.front.goldCard.FrontItemGoldGameCard;
import it.polimi.ingsw.model.card.gameCard.front.goldCard.FrontPositionalGoldGameCard;

import java.lang.reflect.Type;

/**
 * This class is an adapter for the SideGameCard class, providing methods for serializing and deserializing SideGameCard objects.
 * It implements the JsonSerializer and JsonDeserializer interfaces from the Gson library.
 */
public class SideGameCardAdapter implements JsonSerializer<SideGameCard>, JsonDeserializer<SideGameCard> {

    /**
     * This method is used to deserialize a JSON element into a SideGameCard object.
     *
     * @param jsonElement                The JSON element to deserialize.
     * @param type                       The specific type of the object to deserialize to.
     * @param jsonDeserializationContext Context for deserialization that is used to invoke default deserialization on the specified object.
     * @return The deserialized SideGameCard object.
     * @throws JsonParseException If the json cannot be parsed into a SideGameCard object.
     */
    @Override
    public SideGameCard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        // We convert the JsonElement to a JsonObject, we need it to get its properties
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Class<?> sideTypeClass;
        // We get the "sideType" property
        String sideType = jsonObject.get("sideType").getAsString();
        // We decide which is the class type of the side
        switch (sideType) {
            case "FrontGameCard" -> sideTypeClass = FrontGameCard.class;
            case "FrontGoldGameCard" -> sideTypeClass = FrontGoldGameCard.class;
            case "FrontItemGoldGameCard" -> sideTypeClass = FrontItemGoldGameCard.class;
            case "FrontPositionalGoldGameCard" -> sideTypeClass = FrontPositionalGoldGameCard.class;
            case "BackGameCard" -> sideTypeClass = BackGameCard.class;
            default -> throw new JsonParseException("sideType property is missing or wrong");
        }
        // We deserialize the "sideContent" with the class type found above and return it
        return jsonDeserializationContext.deserialize(jsonObject.get("sideContent"), sideTypeClass);
    }

    /**
     * This method is used to serialize a SideGameCard object into a JSON element.
     *
     * @param sideGameCard             The SideGameCard object to serialize.
     * @param type                     The specific type of the object to serialize from.
     * @param jsonSerializationContext Context for serialization that is used to invoke default serialization on the specified object.
     * @return The serialized JSON element.
     * @throws RuntimeException If the sideGameCard object is of unknown class
     */
    @Override
    public JsonElement serialize(SideGameCard sideGameCard, Type type, JsonSerializationContext jsonSerializationContext) {
        // We create a new JsonObject, it will contain the serialized sideGameCard
        JsonObject jsonObject = new JsonObject();
        Class<?> sideTypeClass;
        String sideType;
        // We check the class of sideGameCard and set the property and class accordingly
        switch (sideGameCard) {
            case FrontPositionalGoldGameCard ignored -> {
                sideType = "FrontPositionalGoldGameCard";
                sideTypeClass = FrontPositionalGoldGameCard.class;
            }
            case FrontItemGoldGameCard ignored -> {
                sideType = "FrontItemGoldGameCard";
                sideTypeClass = FrontItemGoldGameCard.class;
            }
            case FrontGoldGameCard ignored -> {
                sideType = "FrontGoldGameCard";
                sideTypeClass = FrontGoldGameCard.class;
            }
            case FrontGameCard ignored -> {
                sideType = "FrontGameCard";
                sideTypeClass = FrontGameCard.class;
            }
            case BackGameCard ignored -> {
                sideType = "BackGameCard";
                sideTypeClass = BackGameCard.class;
            }
            default -> throw new RuntimeException("sideGameCard is of unknown class");
        }
        // We add the property to the JsonObject
        jsonObject.addProperty("sideType", sideType);
        // We serialize the sideGameCard with the chosen class and add it in the sideContent property
        jsonObject.add("sideContent", jsonSerializationContext.serialize(sideGameCard, sideTypeClass));
        // We return the serialized sideGameCard
        return jsonObject;
    }
}
