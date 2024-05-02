package it.polimi.ingsw.network.client.message.gameController;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

/**
 * This class extends the GameControllerClientMessage class and represents a specific type of in-game client message: a request to draw a card from the field.
 * It contains the name of the game, the name of the player who is drawing the card, and the game card to be drawn.
 */
public class DrawCardFromFieldClientMessage extends GameControllerClientMessage {
    /**
     * The game card to be drawn.
     */
    private final GameCard gameCard;

    /**
     * Constructor for DrawCardFromFieldClientMessage.
     * Initializes the player action with the DRAWCARDFROMFIELD value, and sets the game name, player name, and game card.
     *
     * @param gameName   The name of the game. This cannot be null.
     * @param playerName The name of the player who is drawing the card. This cannot be null.
     * @param gameCard   The game card to be drawn. This cannot be null.
     */
    public DrawCardFromFieldClientMessage(String gameName, String playerName, GameCard gameCard) {
        super(PlayerActionEnum.DRAWCARDFROMFIELD, gameName, playerName);
        this.gameCard = gameCard;
    }

    /**
     * Returns the game card to be drawn.
     *
     * @return The game card to be drawn.
     */
    public GameCard getGameCard() {
        return gameCard;
    }
}