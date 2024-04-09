package it.polimi.ingsw.data;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.gameCard.SideGameCard;
import it.polimi.ingsw.model.card.objectiveCard.ItemObjectiveCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.card.objectiveCard.PositionalObjectiveCard;

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

    private final Gson gson = new GsonBuilder().registerTypeAdapter(SideGameCard.class, new SideGameCardAdapter()).create();

    private <T,R> void parseAndAddCards(JsonObject jsonObject, String cardType, String cardSubType, Class<T> classType, ArrayList<R> cardList) {
        JsonArray jsonArray = jsonObject.getAsJsonObject(cardType).getAsJsonArray(cardSubType);
        Type listType = TypeToken.getParameterized(ArrayList.class, classType).getType();
        cardList.addAll(this.gson.fromJson(jsonArray, listType));
    }

    private void parseResourceCardList(JsonObject jsonObject){
        parseAndAddCards(jsonObject, "GameCard", "ResourceCard", GameCard.class, resourceCardList);
    }

    private void parseGoldCardList(JsonObject jsonObject){
        jsonObject = jsonObject.getAsJsonObject("GameCard");
        parseAndAddCards(jsonObject, "GoldCard", "ItemGoldCard", GameCard.class, goldCardList);
        parseAndAddCards(jsonObject, "GoldCard", "PositionalGoldCard", GameCard.class, goldCardList);
        parseAndAddCards(jsonObject, "GoldCard", "SimpleGoldCard", GameCard.class, goldCardList);
    }

    private void parseStarterCardList(JsonObject jsonObject){
        parseAndAddCards(jsonObject, "GameCard", "StarterCard", GameCard.class, starterCardList);
    }

    private void parseObjectiveCardList(JsonObject jsonObject){
        parseAndAddCards(jsonObject, "ObjectiveCard", "ItemObjectiveCard", ItemObjectiveCard.class, objectiveCardList);
        parseAndAddCards(jsonObject, "ObjectiveCard", "PositionalObjectiveCard", PositionalObjectiveCard.class, objectiveCardList);
    }

    /**
     * Constructs a new Parser object.
     */
    private Parser() {
        try {
            // Create a FileReader to read the JSON file
            Reader reader = new FileReader("src/main/resources/json/CardDB.json");

            // Parse the JSON content using JsonParser
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            // Parse every deck
            parseResourceCardList(jsonObject);
            parseGoldCardList(jsonObject);
            parseStarterCardList(jsonObject);
            parseObjectiveCardList(jsonObject);

            // Debug, remove when everything is done
            resourceCardList.forEach(card -> {
                        System.out.println("CardType: " + card.toString() + " ID: " + card.getGameCardId());
                        System.out.println("Front side: " + card.getCurrentSide().toString());
                        card.switchSide();
                        System.out.println("Back side: " + card.getCurrentSide().toString() + "\n");
                    }
            );
            goldCardList.forEach(card -> {
                        System.out.println("CardType: " + card.toString() + " ID: " + card.getGameCardId());
                        System.out.println("Front side: " + card.getCurrentSide().toString());
                        card.switchSide();
                        System.out.println("Back side: " + card.getCurrentSide().toString() + "\n");
                    }
            );
            starterCardList.forEach(card -> {
                        System.out.println("CardType: " + card.toString() + " ID: " + card.getGameCardId());
                        System.out.println("Front side: " + card.getCurrentSide().toString());
                        card.switchSide();
                        System.out.println("Back side: " + card.getCurrentSide().toString() + "\n");
                    }
            );
            objectiveCardList.forEach(card -> System.out.println(card.toString() + " ID: " + card.getObjectiveCardId()));
            System.out.println("Resource size: " + resourceCardList.size() + " R/G/B/P/N: " + colorSize(resourceCardList));
            System.out.println("Gold size: " + goldCardList.size() + " R/G/B/P/N: " + colorSize(goldCardList));
            System.out.println("Starter size: " + starterCardList.size() + " R/G/B/P/N: " + colorSize(starterCardList));
            System.out.println("Objective size: " + objectiveCardList.size());

            // Close the reader
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException("Parsing failed");
        }
    }

    private ArrayList<Integer> colorSize(ArrayList<GameCard> cardList){
        ArrayList<Integer> sizes = new ArrayList<>();
        sizes.add(cardList.stream().filter(card -> card.getCardColor().equals(CardColorEnum.RED)).toList().size());
        sizes.add(cardList.stream().filter(card -> card.getCardColor().equals(CardColorEnum.GREEN)).toList().size());
        sizes.add(cardList.stream().filter(card -> card.getCardColor().equals(CardColorEnum.BLUE)).toList().size());
        sizes.add(cardList.stream().filter(card -> card.getCardColor().equals(CardColorEnum.PURPLE)).toList().size());
        sizes.add(cardList.stream().filter(card -> card.getCardColor().equals(CardColorEnum.NONE)).toList().size());
        return sizes;
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

    public <T> T deserializeFromJson(String json, Type classType) {
        return this.gson.fromJson(json, classType);
    }
}
