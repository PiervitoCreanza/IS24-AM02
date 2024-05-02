package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.network.server.message.ClientCommandMessage;

/**
 * This class represents a message that is sent when a player joins a game.
 * It extends the ClientCommandMessage class.
 *
 * @author mattiacolombomc
 * @version 1.0
 */
public class JoinGameMessage extends ClientCommandMessage {
    /**
     * The name of the game.
     */
    protected String gameName;

    /**
     * The name of the player.
     */
    protected String playerName;

    /**
     * This constructor initializes a new JoinGameMessage object.
     *
     * @param gameName   The name of the game
     * @param playerName The name of the player
     */
    public JoinGameMessage(String gameName, String playerName) {
        this.gameName = gameName;
        this.playerName = playerName;
    }

    /**
     * This method returns the name of the game.
     *
     * @return The name of the game
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * This method returns the name of the player.
     *
     * @return The name of the player
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * This method creates a JoinGameMessage object from a JSON string.
     *
     * @param json The JSON string to parse
     * @return A JoinGameMessage object if the JSON string represents a valid message, or null otherwise
     * @throws Exception if the JSON string cannot be parsed
     */
    public static JoinGameMessage joinGameFromJson(String json) throws Exception {
        JoinGameMessage temp;
        try {
            temp = new Gson().fromJson(json, JoinGameMessage.class);
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
        return gameName != null && playerName != null;
    }
}