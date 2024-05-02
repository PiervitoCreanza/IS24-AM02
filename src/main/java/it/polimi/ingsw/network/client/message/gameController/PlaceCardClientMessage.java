package it.polimi.ingsw.network.client.message.gameController;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

import java.util.Objects;

/**
 * This class extends the GameControllerClientMessage class and represents a specific type of in-game client message: a request to place a card.
 * It contains the name of the game, the name of the player who is placing the card, the coordinate where the card is to be placed, and the game card to be placed.
 */
public class PlaceCardClientMessage extends GameControllerClientMessage {
    /**
     * The coordinate where the card is to be placed.
     */
    private final Coordinate coordinate;

    /**
     * The game card to be placed.
     */
    private final GameCard card;

    /**
     * Constructor for PlaceCardClientMessage.
     * Initializes the player action with the PLACECARD value, and sets the game name, player name, coordinate, and game card.
     *
     * @param gameName   The name of the game. This cannot be null.
     * @param playerName The name of the player who is placing the card. This cannot be null.
     * @param coordinate The coordinate where the card is to be placed. This cannot be null.
     * @param card       The game card to be placed. This cannot be null.
     */
    public PlaceCardClientMessage(String gameName, String playerName, Coordinate coordinate, GameCard card) {
        super(PlayerActionEnum.PLACE_CARD, gameName, playerName);
        this.coordinate = coordinate;
        this.card = card;
    }

    /**
     * Returns the coordinate where the card is to be placed.
     *
     * @return The coordinate where the card is to be placed.
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Returns the game card to be placed.
     *
     * @return The game card to be placed.
     */
    public GameCard getCard() {
        return card;
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
        if (!(o instanceof PlaceCardClientMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(this.coordinate, that.coordinate) && Objects.equals(this.card, that.card);
    }
}