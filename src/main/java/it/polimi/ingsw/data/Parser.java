package it.polimi.ingsw.data;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.gameCard.SideGameCard;
import it.polimi.ingsw.model.card.objectiveCard.ItemObjectiveCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.card.objectiveCard.PositionalObjectiveCard;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * This class is responsible for parsing the JSON file containing the cards of the game.
 * It reads the JSON file and creates the decks of cards.
 */
public class Parser {
    /**
     * The list of resource cards.
     */
    private final ArrayList<GameCard> resourceCardList = new ArrayList<>();

    /**
     * The list of gold cards.
     */
    private final ArrayList<GameCard> goldCardList = new ArrayList<>();

    /**
     * The list of starter cards.
     */
    private final ArrayList<GameCard> starterCardList = new ArrayList<>();

    /**
     * The list of objective cards.
     */
    private final ArrayList<ObjectiveCard> objectiveCardList = new ArrayList<>();

    /**
     * Gson object with custom deserializer for SideGameCard.
     */
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(SideGameCard.class, new SideGameCardAdapter())
            .create();

    /**
     * Generic method to parse and add cards to a list.
     *
     * @param jsonObject  The JSON object containing the cards.
     * @param cardType    The type of the card (e.g., "GameCard").
     * @param cardSubType The subtype of the card (e.g., "ResourceCard").
     * @param classType   The class of the card.
     * @param cardList    The list to add the cards to.
     * @param <T>         The type of the card.
     * @param <R>         The type of the list.
     */
    private <T, R> void parseAndAddCards(JsonObject jsonObject, String cardType, String cardSubType, Class<T> classType, ArrayList<R> cardList) {
        // We get the jsonObject for the specified cardType and then the jsonArray for the specified cardSubType
        JsonArray jsonArray = jsonObject.getAsJsonObject(cardType).getAsJsonArray(cardSubType);
        // We need to create an ArrayList<classType> type (i.e. classType = GameCard.class will generate a listType = ArrayList<GameCard>)
        Type listType = TypeToken.getParameterized(ArrayList.class, classType).getType();
        // We parse the jsonArray with the specified listType and addAll to the cardList
        cardList.addAll(this.gson.fromJson(jsonArray, listType));
    }

    /**
     * Parses the resource cards from the JSON object and adds them to the resource card list.
     *
     * @param jsonObject The JSON object containing the cards.
     */
    private void parseResourceCardList(JsonObject jsonObject) {
        parseAndAddCards(jsonObject, "GameCard", "ResourceCard", GameCard.class, resourceCardList);
    }

    /**
     * Parses the gold cards from the JSON object and adds them to the gold card list.
     *
     * @param jsonObject The JSON object containing the cards.
     */
    private void parseGoldCardList(JsonObject jsonObject) {
        // We need to get inside the GameCard object, gold cards are inside a subsection
        jsonObject = jsonObject.getAsJsonObject("GameCard");
        parseAndAddCards(jsonObject, "GoldCard", "ItemGoldCard", GameCard.class, goldCardList);
        parseAndAddCards(jsonObject, "GoldCard", "PositionalGoldCard", GameCard.class, goldCardList);
        parseAndAddCards(jsonObject, "GoldCard", "SimpleGoldCard", GameCard.class, goldCardList);
    }

    /**
     * Parses the starter cards from the JSON object and adds them to the starter card list.
     *
     * @param jsonObject The JSON object containing the cards.
     */
    private void parseStarterCardList(JsonObject jsonObject) {
        parseAndAddCards(jsonObject, "GameCard", "StarterCard", GameCard.class, starterCardList);
    }

    /**
     * Parses the objective cards from the JSON object and adds them to the objective card list.
     *
     * @param jsonObject The JSON object containing the cards.
     */
    private void parseObjectiveCardList(JsonObject jsonObject) {
        parseAndAddCards(jsonObject, "ObjectiveCard", "ItemObjectiveCard", ItemObjectiveCard.class, objectiveCardList);
        parseAndAddCards(jsonObject, "ObjectiveCard", "PositionalObjectiveCard", PositionalObjectiveCard.class, objectiveCardList);
    }

    /**
     * Constructs a new Parser object.
     * Reads the JSON file, parses the cards, and adds them to the appropriate lists.
     *
     * @throws RuntimeException when parsing fails
     */
    public Parser() {
        try {
            // Create a FileReader to read the JSON file
            InputStream is = getClass().getResourceAsStream("/json/CardDB.json");
            if (is == null) {
                throw new FileNotFoundException("File not found");
            }
            // Create a InputStreamReader to read the InputStream
            Reader reader = new InputStreamReader(is);

            // Parse the JSON content using JsonParser
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            // Parse every deck
            parseResourceCardList(jsonObject);
            parseGoldCardList(jsonObject);
            parseStarterCardList(jsonObject);
            parseObjectiveCardList(jsonObject);

            // Close the reader
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException("Parsing failed");
        }
    }

    /**
     * Returns a new deck of resource cards.
     *
     * @return A new deck of resource cards.
     */
    public Deck<GameCard> getResourceDeck() {
        return new Deck<>(resourceCardList);
    }

    /**
     * Returns a new deck of gold cards.
     *
     * @return A new deck of gold cards.
     */
    public Deck<GameCard> getGoldDeck() {
        return new Deck<>(goldCardList);
    }

    /**
     * Returns a new deck of starter cards.
     *
     * @return A new deck of starter cards.
     */
    public Deck<GameCard> getStarterDeck() {
        return new Deck<>(starterCardList);
    }

    /**
     * Returns a new deck of objective cards.
     *
     * @return A new deck of objective cards.
     */
    public Deck<ObjectiveCard> getObjectiveDeck() {
        return new Deck<>(objectiveCardList);
    }

    /**
     * Serializes an object to a JSON string.
     *
     * @param o The object to serialize.
     * @return The JSON string.
     */
    public String serializeToJson(Object o) {
        return this.gson.toJson(o);
    }

    /**
     * Deserializes a JSON string to an object.
     *
     * @param json      The JSON string.
     * @param classType The type of the object.
     * @param <T>       The type of the object.
     * @return The deserialized object.
     */
    public <T> T deserializeFromJson(String json, Type classType) {
        return this.gson.fromJson(json, classType);
    }
}
