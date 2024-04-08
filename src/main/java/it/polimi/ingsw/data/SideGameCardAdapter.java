package it.polimi.ingsw.data;

import com.google.gson.*;
import it.polimi.ingsw.model.card.gameCard.BackGameCard;
import it.polimi.ingsw.model.card.gameCard.SideGameCard;
import it.polimi.ingsw.model.card.gameCard.front.FrontGameCard;
import it.polimi.ingsw.model.card.gameCard.front.goldCard.FrontGoldGameCard;
import it.polimi.ingsw.model.card.gameCard.front.goldCard.FrontItemGoldGameCard;
import it.polimi.ingsw.model.card.gameCard.front.goldCard.FrontPositionalGoldGameCard;
import java.lang.reflect.Type;
import java.util.Map;

public class SideGameCardAdapter implements JsonSerializer<SideGameCard>, JsonDeserializer<SideGameCard> {
    @Override
    public SideGameCard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String sideType = jsonObject.get("sideType").getAsString();
        jsonObject = jsonObject.get("sideContent").getAsJsonObject();
        return switch (sideType) {
            case "FrontGameCard" -> jsonDeserializationContext.deserialize(jsonObject, FrontGameCard.class);
            case "FrontGoldGameCard" -> jsonDeserializationContext.deserialize(jsonObject, FrontGoldGameCard.class);
            case "FrontItemGoldGameCard" ->
                    jsonDeserializationContext.deserialize(jsonObject, FrontItemGoldGameCard.class);
            case "FrontPositionalGoldGameCard" ->
                    jsonDeserializationContext.deserialize(jsonObject, FrontPositionalGoldGameCard.class);
            case "BackGameCard" -> jsonDeserializationContext.deserialize(jsonObject, BackGameCard.class);
            default -> null;
        };
    }

    @Override
    public JsonElement serialize(SideGameCard sideGameCard, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        JsonElement serializedSideGameCard = null;
        if (sideGameCard instanceof FrontPositionalGoldGameCard) {
            jsonObject.addProperty("sideType", "FrontPositionalGoldGameCard");
            serializedSideGameCard = jsonSerializationContext.serialize(sideGameCard, FrontPositionalGoldGameCard.class);
        } else if (sideGameCard instanceof FrontItemGoldGameCard) {
            jsonObject.addProperty("sideType", "FrontItemGoldGameCard");
            serializedSideGameCard = jsonSerializationContext.serialize(sideGameCard, FrontItemGoldGameCard.class);
        } else if (sideGameCard instanceof FrontGoldGameCard) {
            jsonObject.addProperty("sideType", "FrontGoldGameCard");
            serializedSideGameCard = jsonSerializationContext.serialize(sideGameCard, FrontGoldGameCard.class);
        } else if (sideGameCard instanceof FrontGameCard) {
            jsonObject.addProperty("sideType", "FrontGameCard");
            serializedSideGameCard = jsonSerializationContext.serialize(sideGameCard, FrontGameCard.class);
        } else if (sideGameCard instanceof BackGameCard) {
            jsonObject.addProperty("sideType", "BackGameCard");
            serializedSideGameCard = jsonSerializationContext.serialize(sideGameCard, BackGameCard.class);
        }
        jsonObject.add("sideContent", serializedSideGameCard);
        return jsonObject;
    }
}
