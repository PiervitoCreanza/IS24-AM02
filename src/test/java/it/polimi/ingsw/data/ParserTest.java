package it.polimi.ingsw.data;

import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.model.card.gameCard.BackGameCard;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.gameCard.front.FrontGameCard;
import it.polimi.ingsw.model.card.gameCard.front.goldCard.FrontGoldGameCard;
import it.polimi.ingsw.model.card.gameCard.front.goldCard.FrontItemGoldGameCard;
import it.polimi.ingsw.model.card.gameCard.front.goldCard.FrontPositionalGoldGameCard;
import it.polimi.ingsw.model.card.objectiveCard.*;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {

    private final Parser parser = Parser.getInstance();

    @Test
    @DisplayName("getInstance returns the same instance for multiple calls")
    public void getInstanceReturnsSameInstance() {
        Parser parser2 = Parser.getInstance();
        assertEquals(parser, parser2);
    }

    @Test
    @DisplayName("Test if serialization and deserialization of resource card works")
    public void serializeAndDeserializeResourceCard() {
        String jsonResourceCard = "{\"cardId\":1,\"currentSideGameCard\":{\"sideType\":\"FrontGameCard\",\"sideContent\":{\"points\":1,\"topRight\":{\"isCovered\":false,\"gameItem\":\"FUNGI\"},\"topLeft\":{\"isCovered\":false,\"gameItem\":\"PLANT\"},\"bottomLeft\":{\"isCovered\":false,\"gameItem\":\"ANIMAL\"},\"bottomRight\":{\"isCovered\":false,\"gameItem\":\"INSECT\"}}},\"otherSideGameCard\":{\"sideType\":\"BackGameCard\",\"sideContent\":{\"resources\":{\"store\":{\"QUILL\":0,\"MANUSCRIPT\":0,\"ANIMAL\":1,\"PLANT\":0,\"INSECT\":0,\"INKWELL\":0,\"FUNGI\":0,\"NONE\":0}},\"topRight\":{\"isCovered\":false,\"gameItem\":\"NONE\"},\"topLeft\":{\"isCovered\":false,\"gameItem\":\"NONE\"},\"bottomLeft\":{\"isCovered\":false,\"gameItem\":\"NONE\"},\"bottomRight\":{\"isCovered\":false,\"gameItem\":\"NONE\"}}},\"cardColorEnum\":\"RED\"}";

        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.ANIMAL, 1);
        GameCard resourceGameCard = new GameCard(1, new FrontGameCard(new Corner(GameItemEnum.FUNGI), new Corner(GameItemEnum.PLANT), new Corner(GameItemEnum.ANIMAL), new Corner(GameItemEnum.INSECT), 1), new BackGameCard(new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), gameItemStore), CardColorEnum.RED);
        // Deserialize
        assertEquals(resourceGameCard, parser.deserializeFromJson(jsonResourceCard, GameCard.class));
        // Serialize
        String serializedCard = parser.serializeToJson(resourceGameCard);
        assertEquals(resourceGameCard, parser.deserializeFromJson(serializedCard, GameCard.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of gold game card works")
    public void serializeAndDeserializeGoldGameCard() {
        String jsonGoldCard = "{\"cardId\":2,\"currentSideGameCard\":{\"sideType\":\"FrontGoldGameCard\",\"sideContent\":{\"neededItems\":{\"store\":{\"QUILL\":1,\"MANUSCRIPT\":1,\"ANIMAL\":0,\"PLANT\":0,\"INSECT\":0,\"INKWELL\":1,\"FUNGI\":0,\"NONE\":0}},\"points\":5,\"topLeft\":{\"isCovered\":false,\"gameItem\":\"FUNGI\"},\"bottomLeft\":{\"isCovered\":false,\"gameItem\":\"ANIMAL\"},\"bottomRight\":{\"isCovered\":false,\"gameItem\":\"PLANT\"}}},\"otherSideGameCard\":{\"sideType\":\"BackGameCard\",\"sideContent\":{\"resources\":{\"store\":{\"QUILL\":0,\"MANUSCRIPT\":0,\"ANIMAL\":0,\"PLANT\":1,\"INSECT\":0,\"INKWELL\":0,\"FUNGI\":0,\"NONE\":0}},\"topRight\":{\"isCovered\":false,\"gameItem\":\"NONE\"},\"topLeft\":{\"isCovered\":false,\"gameItem\":\"NONE\"},\"bottomLeft\":{\"isCovered\":false,\"gameItem\":\"NONE\"},\"bottomRight\":{\"isCovered\":false,\"gameItem\":\"NONE\"}}},\"cardColorEnum\":\"GREEN\"}";

        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.PLANT, 1);
        GameItemStore neededItemStore = new GameItemStore();
        neededItemStore.increment(GameItemEnum.INKWELL, 1);
        neededItemStore.increment(GameItemEnum.MANUSCRIPT, 1);
        neededItemStore.increment(GameItemEnum.QUILL, 1);
        GameCard goldGameCard = new GameCard(2, new FrontGoldGameCard(null, new Corner(GameItemEnum.FUNGI), new Corner(GameItemEnum.ANIMAL), new Corner(GameItemEnum.PLANT), 5, neededItemStore), new BackGameCard(new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), gameItemStore), CardColorEnum.GREEN);

        // Deserialize
        assertEquals(goldGameCard, parser.deserializeFromJson(jsonGoldCard, GameCard.class));
        // Serialize
        String serializedCard = parser.serializeToJson(goldGameCard);
        assertEquals(goldGameCard, parser.deserializeFromJson(serializedCard, GameCard.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of gold item game card works")
    public void serializeAndDeserializeGoldItemGameCard() {
        String jsonItemGoldCard = "{\"cardId\":5,\"currentSideGameCard\":{\"sideType\":\"FrontItemGoldGameCard\",\"sideContent\":{\"multiplier\":\"INKWELL\",\"neededItems\":{\"store\":{\"QUILL\":0,\"MANUSCRIPT\":0,\"ANIMAL\":1,\"PLANT\":1,\"INSECT\":1,\"INKWELL\":0,\"FUNGI\":0,\"NONE\":0}},\"points\":4,\"bottomLeft\":{\"isCovered\":false,\"gameItem\":\"ANIMAL\"},\"bottomRight\":{\"isCovered\":false,\"gameItem\":\"PLANT\"}}},\"otherSideGameCard\":{\"sideType\":\"BackGameCard\",\"sideContent\":{\"resources\":{\"store\":{\"QUILL\":0,\"MANUSCRIPT\":0,\"ANIMAL\":0,\"PLANT\":0,\"INSECT\":1,\"INKWELL\":0,\"FUNGI\":0,\"NONE\":0}},\"topRight\":{\"isCovered\":false,\"gameItem\":\"NONE\"},\"topLeft\":{\"isCovered\":false,\"gameItem\":\"NONE\"},\"bottomLeft\":{\"isCovered\":false,\"gameItem\":\"NONE\"},\"bottomRight\":{\"isCovered\":false,\"gameItem\":\"NONE\"}}},\"cardColorEnum\":\"PURPLE\"}";

        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.INSECT, 1);
        GameItemStore neededItemStore = new GameItemStore();
        neededItemStore.increment(GameItemEnum.PLANT, 1);
        neededItemStore.increment(GameItemEnum.ANIMAL, 1);
        neededItemStore.increment(GameItemEnum.INSECT, 1);
        GameCard itemGoldCard = new GameCard(5, new FrontItemGoldGameCard(null, null, new Corner(GameItemEnum.ANIMAL), new Corner(GameItemEnum.PLANT), 4, neededItemStore, GameItemEnum.INKWELL), new BackGameCard(new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), gameItemStore), CardColorEnum.PURPLE);

        // Deserialize
        assertEquals(itemGoldCard, parser.deserializeFromJson(jsonItemGoldCard, GameCard.class));
        // Serialize
        String serializedCard = parser.serializeToJson(itemGoldCard);
        assertEquals(itemGoldCard, parser.deserializeFromJson(serializedCard, GameCard.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of gold positional game card works")
    public void serializeAndDeserializeGoldPositionalGameCard() {
        String jsonPositionalGoldCard = "{\"cardId\":101,\"currentSideGameCard\":{\"sideType\":\"FrontPositionalGoldGameCard\",\"sideContent\":{\"neededItems\":{\"store\":{\"QUILL\":0,\"MANUSCRIPT\":0,\"ANIMAL\":2,\"PLANT\":2,\"INSECT\":2,\"INKWELL\":0,\"FUNGI\":0,\"NONE\":0}},\"points\":2,\"topLeft\":{\"isCovered\":false,\"gameItem\":\"FUNGI\"},\"bottomLeft\":{\"isCovered\":false,\"gameItem\":\"ANIMAL\"}}},\"otherSideGameCard\":{\"sideType\":\"BackGameCard\",\"sideContent\":{\"resources\":{\"store\":{\"QUILL\":0,\"MANUSCRIPT\":0,\"ANIMAL\":0,\"PLANT\":0,\"INSECT\":0,\"INKWELL\":0,\"FUNGI\":1,\"NONE\":0}},\"topRight\":{\"isCovered\":false,\"gameItem\":\"NONE\"},\"topLeft\":{\"isCovered\":false,\"gameItem\":\"NONE\"},\"bottomLeft\":{\"isCovered\":false,\"gameItem\":\"NONE\"},\"bottomRight\":{\"isCovered\":false,\"gameItem\":\"NONE\"}}},\"cardColorEnum\":\"BLUE\"}";

        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.FUNGI, 1);
        GameItemStore neededItemStore = new GameItemStore();
        neededItemStore.increment(GameItemEnum.PLANT, 2);
        neededItemStore.increment(GameItemEnum.ANIMAL, 2);
        neededItemStore.increment(GameItemEnum.INSECT, 2);
        GameCard positionalGoldCard = new GameCard(101, new FrontPositionalGoldGameCard(null, new Corner(GameItemEnum.FUNGI), new Corner(GameItemEnum.ANIMAL), null, 2, neededItemStore), new BackGameCard(new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), gameItemStore), CardColorEnum.BLUE);

        // Deserialize
        assertEquals(positionalGoldCard, parser.deserializeFromJson(jsonPositionalGoldCard, GameCard.class));
        // Serialize
        String serializedCard = parser.serializeToJson(positionalGoldCard);
        assertEquals(positionalGoldCard, parser.deserializeFromJson(serializedCard, GameCard.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of starter card works")
    public void serializeAndDeserializeStarterCard() {
        String jsonStarterGameCard = "{\"cardId\":10,\"currentSideGameCard\":{\"sideType\":\"FrontGameCard\",\"sideContent\":{\"points\":0,\"topRight\":{\"isCovered\":false,\"gameItem\":\"FUNGI\"},\"topLeft\":{\"isCovered\":false,\"gameItem\":\"ANIMAL\"},\"bottomLeft\":{\"isCovered\":false,\"gameItem\":\"PLANT\"},\"bottomRight\":{\"isCovered\":false,\"gameItem\":\"INSECT\"}}},\"otherSideGameCard\":{\"sideType\":\"BackGameCard\",\"sideContent\":{\"resources\":{\"store\":{\"QUILL\":0,\"MANUSCRIPT\":0,\"ANIMAL\":1,\"PLANT\":0,\"INSECT\":0,\"INKWELL\":0,\"FUNGI\":1,\"NONE\":0}},\"topRight\":{\"isCovered\":false,\"gameItem\":\"NONE\"},\"topLeft\":{\"isCovered\":false,\"gameItem\":\"NONE\"},\"bottomLeft\":{\"isCovered\":false,\"gameItem\":\"NONE\"},\"bottomRight\":{\"isCovered\":false,\"gameItem\":\"NONE\"}}},\"cardColorEnum\":\"NONE\"}";

        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.ANIMAL, 1);
        gameItemStore.set(GameItemEnum.FUNGI, 1);
        GameCard starterGameCard = new GameCard(10, new FrontGameCard(new Corner(GameItemEnum.FUNGI), new Corner(GameItemEnum.ANIMAL), new Corner(GameItemEnum.PLANT), new Corner(GameItemEnum.INSECT), 0), new BackGameCard(new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), gameItemStore), CardColorEnum.NONE);

        // Deserialize
        assertEquals(starterGameCard, parser.deserializeFromJson(jsonStarterGameCard, GameCard.class));
        // Serialize
        String serializedCard = parser.serializeToJson(starterGameCard);
        assertEquals(starterGameCard, parser.deserializeFromJson(serializedCard, GameCard.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of positional objective card works")
    public void serializeAndDeserializePositionalObjectiveCard() {
        String jsonPositionalObjectiveCard = "{\"positionalData\":[{\"coordinate\":{\"x\":0,\"y\":0},\"cardColorEnum\":\"RED\"},{\"coordinate\":{\"x\":-1,\"y\":-1},\"cardColorEnum\":\"RED\"},{\"coordinate\":{\"x\":-2,\"y\":-2},\"cardColorEnum\":\"RED\"}],\"cardId\":2,\"pointsWon\":2}";

        ArrayList<PositionalData> positionalDataArrayList = new ArrayList<>();
        positionalDataArrayList.add(new PositionalData(new Coordinate(0, 0), CardColorEnum.RED));
        positionalDataArrayList.add(new PositionalData(new Coordinate(-1, -1), CardColorEnum.RED));
        positionalDataArrayList.add(new PositionalData(new Coordinate(-2, -2), CardColorEnum.RED));
        ObjectiveCard positionalobjectiveCard = new PositionalObjectiveCard(2, 2, positionalDataArrayList);

        // Deserialize
        assertEquals(positionalobjectiveCard, parser.deserializeFromJson(jsonPositionalObjectiveCard, PositionalObjectiveCard.class));
        // Serialize
        String serializedCard = parser.serializeToJson(positionalobjectiveCard);
        assertEquals(positionalobjectiveCard, parser.deserializeFromJson(serializedCard, PositionalObjectiveCard.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of item objective card works")
    public void serializeAndDeserializeItemObjectiveCard() {
        String jsonItemObjectiveCard = "{\"multiplier\":{\"store\":{\"QUILL\":1,\"MANUSCRIPT\":1,\"ANIMAL\":0,\"PLANT\":0,\"INSECT\":0,\"INKWELL\":1,\"FUNGI\":0,\"NONE\":0}},\"cardId\":1,\"pointsWon\":3}";

        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.increment(GameItemEnum.INKWELL, 1);
        gameItemStore.increment(GameItemEnum.MANUSCRIPT, 1);
        gameItemStore.increment(GameItemEnum.QUILL, 1);
        ObjectiveCard itemObjectiveCard = new ItemObjectiveCard(1, 3, gameItemStore);

        // Deserialize
        assertEquals(itemObjectiveCard, parser.deserializeFromJson(jsonItemObjectiveCard, ItemObjectiveCard.class));
        // Serialize
        String serializedCard = parser.serializeToJson(itemObjectiveCard);
        assertEquals(itemObjectiveCard, parser.deserializeFromJson(serializedCard, ItemObjectiveCard.class));
    }
}
