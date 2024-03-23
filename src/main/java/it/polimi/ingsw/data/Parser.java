package it.polimi.ingsw.data;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.GameCard;
import it.polimi.ingsw.model.ObjectiveCard.ItemObjectiveCard;
import it.polimi.ingsw.model.ObjectiveCard.ObjectiveCard;

import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * This class is responsible for parsing the JSON file containing the cards of the game.
 * It reads the JSON file and creates the decks of cards.
 * The class is a singleton, so only one instance of it can be created.
 */
public class Parser {

    /**
     * The instance of the Parser class.
     */
    private static Parser instance;

    /**
     * The list of objective cards.
     */
    private final ArrayList<ObjectiveCard> objectiveCardList = new ArrayList<>();

    /**
     * The list of gold cards.
     */
    private final ArrayList<GameCard> goldCardList = new ArrayList<>();

    /**
     * The list of resource cards.
     */
    private final ArrayList<GameCard> resourceCardList = new ArrayList<>();

    /**
     * The list of starter cards.
     */
    private final ArrayList<GameCard> starterCardList = new ArrayList<>();

    private final Gson gson = new Gson();

    /**
     * Constructs a new Parser object.
     */
    private Parser() {
        try {
            // Create a FileReader to read the JSON file
            Reader reader = new FileReader("src/main/resources/json/CardDB.json");

            // Parse the JSON content using JsonParser
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            JsonObject jsonObjectiveCard = jsonObject.getAsJsonObject("ObjectiveCard");
            // Get the specific key from the JSON object
            JsonArray jsonItemObjectiveCard = jsonObjectiveCard.getAsJsonArray("ItemObjectiveCard");

            // Define the TypeToken for ArrayList<YourClass>
            Type listType = new TypeToken<ArrayList<ItemObjectiveCard>>() {
            }.getType();
            this.objectiveCardList.addAll(this.gson.fromJson(jsonItemObjectiveCard, listType));

            // Now the object is ready!
            System.out.println(objectiveCardList.getFirst());

            // Close the reader
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the instance of the Parser class.
     *
     * @return The instance of the Parser class.
     */
    public static Parser getInstance() {
        if (instance == null) {
            instance = new Parser();
        }
        return instance;
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
     * Returns a new deck of gold cards.
     *
     * @return A new deck of gold cards.
     */
    public Deck<GameCard> getGoldDeck() {
        return new Deck<>(goldCardList);
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
     * Returns a new deck of starter cards.
     *
     * @return A new deck of starter cards.
     */
    public Deck<GameCard> getStarterDeck() {
        return new Deck<>(starterCardList);
    }

    public String serializeToJson(Object o) {
        return this.gson.toJson(o);
    }
}
