package it.polimi.ingsw.network.client.message.gameController;

import it.polimi.ingsw.network.client.message.ClientToServerMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

import java.util.Objects;

/**
 * This class extends the GameControllerClientMessage class and represents a specific type of in-game client message: a request to draw a card from the field.
 * It contains the name of the game, the name of the player who is drawing the card, and the game card to be drawn.
 */
public class DrawCardFromFieldClientToServerMessage extends ClientToServerMessage {
    /**
     * The game card to be drawn.
     */
    private final int gameCard;

    /**
     * Constructor for DrawCardFromFieldClientToServerMessage.
     * Initializes the player action with the DRAWCARDFROMFIELD value, and sets the game name, player name, and game card.
     *
     * @param gameName   The name of the game. This cannot be null.
     * @param playerName The name of the player who is drawing the card. This cannot be null.
     * @param gameCard   The game card to be drawn. This cannot be null.
     */
    public DrawCardFromFieldClientToServerMessage(String gameName, String playerName, int gameCard) {
        super(PlayerActionEnum.DRAW_CARD_FROM_FIELD, gameName, playerName);
        this.gameCard = gameCard;
    }

    /**
     * Returns the game card to be drawn.
     *
     * @return The game card to be drawn.
     */
    @Override
    public int getGameCardId() {
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
        if (!(o instanceof DrawCardFromFieldClientToServerMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(this.gameCard, that.gameCard);
    }
}