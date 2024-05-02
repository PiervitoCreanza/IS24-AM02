package it.polimi.ingsw.network.client.message.gameController;

import it.polimi.ingsw.network.client.message.PlayerActionEnum;

/**
 * This class extends the GameControllerClientMessage class and represents a specific type of in-game client message: a request to draw a card from the resource deck.
 * It contains the name of the game and the name of the player who is drawing the card.
 */
public class DrawCardFromResourceDeckClientMessage extends GameControllerClientMessage {
    /**
     * Constructor for DrawCardFromResourceDeckClientMessage.
     * Initializes the player action with the DRAWCARDFROMRESOURCEDECK value, and sets the game name and player name.
     *
     * @param gameName   The name of the game. This cannot be null.
     * @param playerName The name of the player who is drawing the card. This cannot be null.
     */
    public DrawCardFromResourceDeckClientMessage(String gameName, String playerName) {
        super(PlayerActionEnum.DRAW_CARD_FROM_RESOURCE_DECK, gameName, playerName);
    }
}