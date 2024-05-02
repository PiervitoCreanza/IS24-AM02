package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;

/**
 * This class represents a message that is sent when a game is created.
 * It extends the JoinGameMessage class.
 *
 * @author mattiacolombomc
 * @version 1.0
 */
public class CreateGameMessage extends JoinGameMessage {
    /**
     * The number of players in the game.
     */
    public int nPlayers;

    /**
     * This constructor initializes a new CreateGameMessage object.
     *
     * @param gameName   The name of the game
     * @param nPlayers   The number of players in the game
     * @param playerName The name of the player who created the game
     */
    public CreateGameMessage(String gameName, int nPlayers, String playerName) {
        super(gameName, playerName);
        this.nPlayers = nPlayers;
    }

    /**
     * This method creates a CreateGameMessage object from a JSON string.
     *
     * @param json The JSON string to parse
     * @return A CreateGameMessage object if the JSON string represents a valid message, or null otherwise
     * @throws Exception if the JSON string cannot be parsed
     */
    public static CreateGameMessage createGameMessageFromJson(String json) throws Exception {
        CreateGameMessage temp;
        try {
            temp = new Gson().fromJson(json, CreateGameMessage.class); //Avoid possible JSON fragmentation caused by TCP
        } catch (Exception e) {
            return null;
        }
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
        return gameName != null && nPlayers >= 1 && nPlayers <= 4 && playerName != null;
    }
}