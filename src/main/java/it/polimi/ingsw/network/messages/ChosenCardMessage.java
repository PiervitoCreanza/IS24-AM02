package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.server.message.ClientCommandMessage;

/**
 * This class represents a message that is sent when a card is chosen.
 * It extends the ClientCommandMessage class.
 *
 * @author mattiacolombomc
 * @version 1.0
 */
public class ChosenCardMessage extends ClientCommandMessage {
    /**
     * The ID of the chosen card.
     */
    private long chosenCard;

    /**
     * The ID of the player who chose the card.
     */
    private long playerID;

    /**
     * This method creates a ChosenCardMessage object from a JSON string.
     *
     * @param json The JSON string to parse
     * @return A ChosenCardMessage object, or null if the JSON string could not be parsed
     * @throws JsonSyntaxException if the JSON string cannot be parsed
     */
    public static ChosenCardMessage chosenCardMessageFromJson(String json) throws JsonSyntaxException {
        return new Gson().fromJson(json, ChosenCardMessage.class);
    }

    /**
     * This method creates a ChosenCardMessage object from a JSON string, but only if the JSON string represents a valid message.
     *
     * @param json The JSON string to parse
     * @return A ChosenCardMessage object if the JSON string represents a valid message, or null otherwise
     * @throws JsonSyntaxException if the JSON string cannot be parsed
     */
    public static ChosenCardMessage joinGameFromJson(String json) throws JsonSyntaxException {
        ChosenCardMessage temp = new Gson().fromJson(json, ChosenCardMessage.class);
        if (temp.isValid()) {
            return temp;
        }
        return null;
    }

    /**
     * This method checks if the message contains valid data.
     *
     * @return true if the message contains valid data, false otherwise
     */
    private boolean isValid() {
        return chosenCard != 0 && playerID != 0;
    }
}