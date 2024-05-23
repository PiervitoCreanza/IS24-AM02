package it.polimi.ingsw.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.model.card.gameCard.BackGameCard;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.gameCard.front.FrontGameCard;
import it.polimi.ingsw.model.card.gameCard.front.goldCard.FrontGoldGameCard;
import it.polimi.ingsw.model.card.gameCard.front.goldCard.FrontItemGoldGameCard;
import it.polimi.ingsw.model.card.gameCard.front.goldCard.FrontPositionalGoldGameCard;
import it.polimi.ingsw.model.card.objectiveCard.ItemObjectiveCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.card.objectiveCard.PositionalData;
import it.polimi.ingsw.model.card.objectiveCard.PositionalObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    private final Parser parser = new Parser();

    private JsonObject jsonObject;

    @BeforeEach
    void setUp() {
        try {
            Reader reader = new FileReader("src/test/java/it/polimi/ingsw/data/CardDBTest.json");
            jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

        } catch (Exception e) {
            throw new IllegalArgumentException("File not found!");
        }
    }

    @Test
    @DisplayName("Test if serialization and deserialization of resource card works")
    public void serializeAndDeserializeResourceCard() {
        String jsonResourceCard = jsonObject.getAsJsonObject("GameCard").getAsJsonObject("ResourceCard").toString();

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
        String jsonGoldCard = jsonObject.getAsJsonObject("GameCard").getAsJsonObject("GoldCard").getAsJsonObject("SimpleGoldCard").toString();

        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.PLANT, 1);
        GameItemStore neededItemStore = new GameItemStore();
        neededItemStore.set(GameItemEnum.INKWELL, 1);
        neededItemStore.set(GameItemEnum.MANUSCRIPT, 1);
        neededItemStore.set(GameItemEnum.QUILL, 1);
        GameCard goldGameCard = new GameCard(2, new FrontGoldGameCard(null, new Corner(GameItemEnum.FUNGI), new Corner(GameItemEnum.ANIMAL), new Corner(GameItemEnum.PLANT), 5, neededItemStore), new BackGameCard(new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), gameItemStore), CardColorEnum.GREEN);

        // Deserialize
        assertEquals(goldGameCard, parser.deserializeFromJson(jsonGoldCard, GameCard.class));
        // Serialize
        String serializedCard = parser.serializeToJson(goldGameCard);
        assertEquals(goldGameCard, parser.deserializeFromJson(serializedCard, GameCard.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of gold item game card works")
    public void serializeAndDeserializeItemGoldGameCard() {
        String jsonItemGoldCard = jsonObject.getAsJsonObject("GameCard").getAsJsonObject("GoldCard").getAsJsonObject("ItemGoldCard").toString();

        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.INSECT, 1);
        GameItemStore neededItemStore = new GameItemStore();
        neededItemStore.set(GameItemEnum.PLANT, 1);
        neededItemStore.set(GameItemEnum.ANIMAL, 1);
        neededItemStore.set(GameItemEnum.INSECT, 1);
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
        String jsonPositionalGoldCard = jsonObject.getAsJsonObject("GameCard").getAsJsonObject("GoldCard").getAsJsonObject("PositionalGoldCard").toString();

        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.FUNGI, 1);
        GameItemStore neededItemStore = new GameItemStore();
        neededItemStore.set(GameItemEnum.PLANT, 2);
        neededItemStore.set(GameItemEnum.ANIMAL, 2);
        neededItemStore.set(GameItemEnum.INSECT, 2);
        GameCard positionalGoldCard = new GameCard(101, new FrontPositionalGoldGameCard(null, new Corner(GameItemEnum.FUNGI), new Corner(GameItemEnum.ANIMAL), null, 2, neededItemStore), new BackGameCard(new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), new Corner(GameItemEnum.NONE), gameItemStore), CardColorEnum.CYAN);

        // Deserialize
        assertEquals(positionalGoldCard, parser.deserializeFromJson(jsonPositionalGoldCard, GameCard.class));
        // Serialize
        String serializedCard = parser.serializeToJson(positionalGoldCard);
        assertEquals(positionalGoldCard, parser.deserializeFromJson(serializedCard, GameCard.class));
    }

    @Test
    @DisplayName("Test if serialization and deserialization of starter card works")
    public void serializeAndDeserializeStarterCard() {
        String jsonStarterGameCard = jsonObject.getAsJsonObject("GameCard").getAsJsonObject("StarterCard").toString();

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
        String jsonPositionalObjectiveCard = jsonObject.getAsJsonObject("ObjectiveCard").getAsJsonObject("PositionalObjectiveCard").toString();

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
        String jsonItemObjectiveCard = jsonObject.getAsJsonObject("ObjectiveCard").getAsJsonObject("ItemObjectiveCard").toString();

        GameItemStore gameItemStore = new GameItemStore();
        gameItemStore.set(GameItemEnum.INKWELL, 1);
        gameItemStore.set(GameItemEnum.MANUSCRIPT, 1);
        gameItemStore.set(GameItemEnum.QUILL, 1);
        ObjectiveCard itemObjectiveCard = new ItemObjectiveCard(1, 3, gameItemStore);

        // Deserialize
        assertEquals(itemObjectiveCard, parser.deserializeFromJson(jsonItemObjectiveCard, ItemObjectiveCard.class));
        // Serialize
        String serializedCard = parser.serializeToJson(itemObjectiveCard);
        assertEquals(itemObjectiveCard, parser.deserializeFromJson(serializedCard, ItemObjectiveCard.class));
    }

    @Test
    @DisplayName("Test if the cards are independent")
    public void test() {
        Parser parser1 = new Parser();
        Deck<GameCard> d1 = parser.getGoldDeck();
        Deck<GameCard> d2 = parser1.getGoldDeck();

        GameCard c1 = d1.getCards().stream().filter(c -> c.getCardId() == 41).findFirst().orElse(null);
        GameCard c2 = d2.getCards().stream().filter(c -> c.getCardId() == 41).findFirst().orElse(null);
        assertEquals(c1, c2);
        assertNotSame(c1, c2);
        c1.switchSide();
        assertNotEquals(c1.getCurrentSide(), c2.getCurrentSide());
    }
}
