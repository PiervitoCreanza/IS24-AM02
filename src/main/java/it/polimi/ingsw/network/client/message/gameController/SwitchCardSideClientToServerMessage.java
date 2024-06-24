package it.polimi.ingsw.network.client.message.gameController;

import it.polimi.ingsw.network.client.message.ClientToServerMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

import java.util.Objects;

/**
 * This class extends the GameControllerClientMessage class and represents a specific type of in-game client message: a request to switch the side of a card.
 * It contains the name of the game, the name of the player who is switching the card side, and the game card to be switched.
 */
public class SwitchCardSideClientToServerMessage extends ClientToServerMessage {
    /**
     * The game card to be switched.
     */
    private final int gameCardId;

    /**
     * Constructor for SwitchCardSideClientToServerMessage.
     * Initializes the player action with the SWITCHCARDSIDE value, and sets the game name, player name, and game card.
     *
     * @param gameName   The name of the game. This cannot be null.
     * @param playerName The name of the player who is switching the card side. This cannot be null.
     * @param gameCardId The game card to be switched. This cannot be null.
     */
    public SwitchCardSideClientToServerMessage(String gameName, String playerName, int gameCardId) {
        super(PlayerActionEnum.SWITCH_CARD_SIDE, gameName, playerName);
        this.gameCardId = gameCardId;
    }

    /**
     * Returns the game card to be switched.
     *
     * @return The game card to be switched.
     */
    @Override
    public int getGameCardId() {
        return gameCardId;
    }

    /**
     * Overrides the equals method from the Object class.
     * Checks if the object passed as parameter is equal to the current instance.
     *
     * @param o The object to compare with the current instance.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SwitchCardSideClientToServerMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(this.gameCardId, that.gameCardId);
    }
}