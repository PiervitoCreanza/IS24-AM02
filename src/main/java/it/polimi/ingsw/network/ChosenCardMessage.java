package it.polimi.ingsw.network;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ChosenCardMessage extends ClientCommandMessage {
    private long chosenCard;
    private long playerID;

    public static ChosenCardMessage chosenCardMessageFromJson(String json) {
        try {
            return new Gson().fromJson(json, ChosenCardMessage.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static ChosenCardMessage joinGameFromJson(String json) {
        //return new Gson().fromJson(json, JoinGameMessage.class);
        ChosenCardMessage temp = new Gson().fromJson(json, ChosenCardMessage.class);
        if (temp.isValid())
            return temp;
        return null;
        //check if the Game Message is effectively a JoinGameMessage, avoiding Gson filling the object with default values when no valid fields are found in the incoming JSON?


    } //we supposed its always parsed correctly atm

    private boolean isValid() {
        //We are checking if the message contains valid data, GSON fills the object with default values if the JSON fields are not valid
        return chosenCard != 0 && playerID != 0;
    }
}
