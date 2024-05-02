package it.polimi.ingsw.network.client.message.gameController;

import it.polimi.ingsw.network.client.message.PlayerActionEnum;

/**
 * This class extends the GameControllerClientMessage class and represents a specific type of in-game client message: a request to draw a card from the gold deck.
 * It contains the name of the game and the name of the player who is drawing the card.
 */
public class DrawCardFromGoldDeckClientMessage extends GameControllerClientMessage {
    /**
     * Constructor for DrawCardFromGoldDeckClientMessage.
     * Initializes the player action with the DRAWCARDFROMGOLDDECK value, and sets the game name and player name.
     *
     * @param gameName   The name of the game. This cannot be null.
     * @param playerName The name of the player who is drawing the card. This cannot be null.
     */
    public DrawCardFromGoldDeckClientMessage(String gameName, String playerName) {
        super(PlayerActionEnum.DRAWCARDFROMGOLDDECK, gameName, playerName);
    }
}