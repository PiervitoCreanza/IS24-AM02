package it.polimi.ingsw.network.client.message.gameController;

import it.polimi.ingsw.network.client.message.ClientToServerMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

/**
 * This class extends the GameControllerClientMessage class and represents a specific type of in-game client message: a request to draw a card from the gold deck.
 * It contains the name of the game and the name of the player who is drawing the card.
 */
public class DrawCardFromGoldDeckClientToServerMessage extends ClientToServerMessage {
    /**
     * Constructor for DrawCardFromGoldDeckClientToServerMessage.
     * Initializes the player action with the DRAWCARDFROMGOLDDECK value, and sets the game name and player name.
     *
     * @param gameName   The name of the game. This cannot be null.
     * @param playerName The name of the player who is drawing the card. This cannot be null.
     */
    public DrawCardFromGoldDeckClientToServerMessage(String gameName, String playerName) {
        super(PlayerActionEnum.DRAW_CARD_FROM_GOLD_DECK, gameName, playerName);
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
        if (!(o instanceof DrawCardFromGoldDeckClientToServerMessage)) return false;
        return super.equals(o);
    }
}