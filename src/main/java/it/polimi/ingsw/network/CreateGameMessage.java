package it.polimi.ingsw.network;

import com.google.gson.Gson;

public class CreateGameMessage extends JoinGameMessage {
    protected int nPlayers;

    public CreateGameMessage(String gameName, int nPlayers, String playerName) {
        super(gameName, playerName);
        this.nPlayers = nPlayers;
    }


    public static CreateGameMessage createGameMessageFromJson(String json) {
        CreateGameMessage temp;
        try {
            temp = new Gson().fromJson(json, CreateGameMessage.class);
        } catch (Exception e) {
            return null;
        }
        if (temp.isValid())
            return temp;
        return null;
        //check if the Game Message is effectively a JoinGameMessage, avoiding Gson filling the object with default values when no valid fields are found in the incoming JSON?


    } //we supposed its always parsed correctly atm

    private boolean isValid() {
        //We are checking if the message contains valid data, GSON fills the object with default values if the JSON fields are not valid
        return gameName != null && nPlayers >= 1 && nPlayers <= 4 && playerName != null;
    }
}
