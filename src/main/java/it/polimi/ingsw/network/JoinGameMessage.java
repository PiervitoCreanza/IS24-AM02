package it.polimi.ingsw.network;

import com.google.gson.Gson;

public class JoinGameMessage extends ClientCommandMessage {
    private String gameName;
    private String playerName;

    public JoinGameMessage(String gameName, String playerName) {
        this.gameName = gameName;
        this.playerName = playerName;
    }

    public String getGameName() {
        return gameName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public static JoinGameMessage joinGameFromJson(String json) {
        //return new Gson().fromJson(json, JoinGameMessage.class);
        JoinGameMessage temp = new Gson().fromJson(json, JoinGameMessage.class);
        if (temp.isValid())
            return temp;
        return null;
        //check if the Game Message is effectively a JoinGameMessage, avoiding Gson filling the object with default values when no valid fields are found in the incoming JSON?


    } //we supposed its always parsed correctly atm

    private boolean isValid() {
        //We are checking if the message contains valid data, GSON fills the object with default values if the JSON fields are not valid
        return gameName != null && playerName != null;
    }
}
