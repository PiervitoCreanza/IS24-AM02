package it.polimi.ingsw.network.client.message.gameController;

import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.message.ClientToServerMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

import java.util.Objects;

/**
 * This class extends the GameControllerClientMessage class and represents a specific type of in-game client message: a request to place a card.
 * It contains the name of the game, the name of the player who is placing the card, the coordinate where the card is to be placed, and the game card to be placed.
 */
public class PlaceCardClientToServerMessage extends ClientToServerMessage {
    /**
     * The coordinate where the card is to be placed.
     */
    private final Coordinate coordinate;

    /**
     * The game card to be placed.
     */
    private final int gameCardId;

    /**
     * Weather the card is flipped or not
     */
    private final boolean isFlipped;

    /**
     * Constructor for PlaceCardClientToServerMessage.
     * Initializes the player action with the PLACECARD value, and sets the game name, player name, coordinate, and game card.
     *
     * @param gameName   The name of the game. This cannot be null.
     * @param playerName The name of the player who is placing the card. This cannot be null.
     * @param coordinate The coordinate where the card is to be placed. This cannot be null.
     * @param gameCardId The game card to be placed. This cannot be null.
     */
    public PlaceCardClientToServerMessage(String gameName, String playerName, Coordinate coordinate, int gameCardId, boolean isFlipped) {
        super(PlayerActionEnum.PLACE_CARD, gameName, playerName);
        this.coordinate = coordinate;
        this.gameCardId = gameCardId;
        this.isFlipped = isFlipped;
    }

    /**
     * Returns the coordinate where the card is to be placed.
     *
     * @return The coordinate where the card is to be placed.
     */
    @Override
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Returns the game card to be placed.
     *
     * @return The game card to be placed.
     */
    @Override
    public int getGameCardId() {
        return gameCardId;
    }

    /**
     * Returns the flipped status of the card.
     *
     * @return The flipped status of the card.
     */
    @Override
    public boolean isFlipped() {
        return isFlipped;
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
        if (!(o instanceof PlaceCardClientToServerMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(this.coordinate, that.coordinate) && Objects.equals(this.gameCardId, that.gameCardId);
    }
}