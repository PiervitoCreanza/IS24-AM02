package it.polimi.ingsw.network.client.message;

/**
 * This class extends the ClientNetworkMessage class and represents a specific type of client message: a request to join a game.
 * It contains the name of the game to be joined and the name of the player who is joining the game.
 */
public class JoinGameClientMessage extends ClientNetworkMessage {
    /**
     * The name of the game to be joined.
     */
    private final String gameName;

    /**
     * The name of the player who is joining the game.
     */
    private final String playerName;

    /**
     * Constructor for JoinGameClientMessage.
     * Initializes the player action with the JOINGAME value, and sets the game name and player name.
     *
     * @param gameName   The name of the game to be joined. This cannot be null.
     * @param playerName The name of the player who is joining the game. This cannot be null.
     */
    public JoinGameClientMessage(String gameName, String playerName) {
        super(PlayerActionEnum.JOINGAME);
        this.gameName = gameName;
        this.playerName = playerName;
    }

    /**
     * Returns the name of the game to be joined.
     *
     * @return The name of the game to be joined.
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * Returns the name of the player who is joining the game.
     *
     * @return The name of the player who is joining the game.
     */
    public String getPlayerName() {
        return playerName;
    }
}