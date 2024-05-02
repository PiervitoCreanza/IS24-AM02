package it.polimi.ingsw.network.client.message;

/**
 * This class extends the ClientMessage class and represents a specific type of client message: a request to create a game.
 * It contains the name of the game to be created, the number of players, and the name of the player who is creating the game.
 */
public class CreateGameClientMessage extends ClientMessage {
    /**
     * The name of the game to be created.
     */
    private final String gameName;

    /**
     * The number of players in the game to be created.
     */
    private final int nPlayers;

    /**
     * The name of the player who is creating the game.
     */
    private final String playerName;

    /**
     * Constructor for CreateGameClientMessage.
     * Initializes the player action with the CREATEGAME value, and sets the game name, number of players, and player name.
     *
     * @param gameName   The name of the game to be created. This cannot be null.
     * @param nPlayers   The number of players in the game to be created. This cannot be null.
     * @param playerName The name of the player who is creating the game. This cannot be null.
     */
    public CreateGameClientMessage(String gameName, int nPlayers, String playerName) {
        super(PlayerActionEnum.CREATEGAME);
        this.gameName = gameName;
        this.nPlayers = nPlayers;
        this.playerName = playerName;
    }

    /**
     * Returns the name of the game to be created.
     *
     * @return The name of the game to be created.
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * Returns the number of players in the game to be created.
     *
     * @return The number of players in the game to be created.
     */
    public int getnPlayers() {
        return nPlayers;
    }

    /**
     * Returns the name of the player who is creating the game.
     *
     * @return The name of the player who is creating the game.
     */
    public String getPlayerName() {
        return playerName;
    }
}