package it.polimi.ingsw.network.client.message.gameController;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.network.client.message.ClientMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

import java.util.Objects;

/**
 * This class extends the GameControllerClientMessage class and represents a specific type of in-game client message: a request to switch the side of a card.
 * It contains the name of the game, the name of the player who is switching the card side, and the game card to be switched.
 */
public class SwitchCardSideClientMessage extends ClientMessage {
    /**
     * The game card to be switched.
     */
    private final GameCard gameCard;

    /**
     * Constructor for SwitchCardSideClientMessage.
     * Initializes the player action with the SWITCHCARDSIDE value, and sets the game name, player name, and game card.
     *
     * @param gameName   The name of the game. This cannot be null.
     * @param playerName The name of the player who is switching the card side. This cannot be null.
     * @param gameCard   The game card to be switched. This cannot be null.
     */
    public SwitchCardSideClientMessage(String gameName, String playerName, GameCard gameCard) {
        super(PlayerActionEnum.SWITCH_CARD_SIDE, gameName, playerName);
        this.gameCard = gameCard;
    }

    /**
     * Returns the game card to be switched.
     *
     * @return The game card to be switched.
     */
    @Override
    public GameCard getGameCard() {
        return gameCard;
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
        if (!(o instanceof SwitchCardSideClientMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(this.gameCard, that.gameCard);
    }
}