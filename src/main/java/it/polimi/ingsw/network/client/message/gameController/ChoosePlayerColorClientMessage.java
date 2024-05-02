package it.polimi.ingsw.network.client.message.gameController;

import it.polimi.ingsw.network.client.message.PlayerActionEnum;

/**
 * This class extends the InGameClientMessage class and represents a specific type of in-game client message: a request to choose a player color.
 * It contains the name of the game, the name of the player who is choosing the color, and the chosen color.
 */
public class ChoosePlayerColorClientMessage extends InGameClientMessage {
    /**
     * The chosen player color.
     */
    private final String playerColor;

    /**
     * Constructor for ChoosePlayerColorClientMessage.
     * Initializes the player action with the CHOOSEPLAYERCOLOR value, and sets the game name, player name, and chosen color.
     *
     * @param playerColor The chosen player color. This cannot be null.
     * @param gameName    The name of the game. This cannot be null.
     * @param playerName  The name of the player who is choosing the color. This cannot be null.
     */
    public ChoosePlayerColorClientMessage(String playerColor, String gameName, String playerName) {
        super(PlayerActionEnum.CHOOSEPLAYERCOLOR, gameName, playerName);
        this.playerColor = playerColor;
    }

    /**
     * Returns the chosen player color.
     *
     * @return The chosen player color.
     */
    public String getPlayerColor() {
        return playerColor;
    }
}