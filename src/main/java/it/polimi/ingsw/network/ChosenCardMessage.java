package it.polimi.ingsw.network;

import com.google.gson.Gson;

public class ChosenCardMessage {
    private long chosenCard;
    private long playerID;

    public static ChosenCardMessage chosenCardMessageFromJson(String json) {
        return new Gson().fromJson(json, ChosenCardMessage.class);
    }
}
