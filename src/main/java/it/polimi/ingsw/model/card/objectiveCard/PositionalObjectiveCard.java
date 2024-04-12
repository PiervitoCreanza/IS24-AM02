package it.polimi.ingsw.model.card.objectiveCard;

import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.player.PlayerBoard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class extends the ObjectiveCard class and represents an objective card that rewards points based on the positions of the player's game cards.
 * The points are calculated by checking if the player's game cards match the positional data required by the objective card.
 */
public class PositionalObjectiveCard extends ObjectiveCard {
    /**
     * The positional data required by the objective card.
     */
    private final ArrayList<PositionalData> positionalData;

    /**
     * Constructs a new PositionalObjectiveCard object with the specified number of points and the positional data required by the objective card.
     *
     * @param cardId         The unique identifier of the objective card.
     * @param pointsWon      The number of points the player can win by fulfilling the objective of this card.
     * @param positionalData The positional data required by the objective card.
     * @throws NullPointerException if positionalData is null.
     */
    public PositionalObjectiveCard(int cardId, int pointsWon, ArrayList<PositionalData> positionalData) {
        super(cardId, pointsWon);
        Objects.requireNonNull(positionalData, "positionalData must be not null");
        this.positionalData = positionalData;
    }

    /**
     * This method calculates and returns the points won by the player.
     * It checks if the player's game cards match the positional data required by the objective card.
     *
     * @param playerboard The player's game board.
     * @return The number of points won by the player.
     */
    @Override
    public int getPoints(PlayerBoard playerboard) {
        int numOfMatch = 0;                          //variable to count how many times player made the configuration
        CardColorEnum firstColor = positionalData.getFirst().cardColorEnum();
        ArrayList<Coordinate> coordinatesCanMatch = playerboard.getGameCards().stream()                  //Create a stream of GameCards
                .filter(x -> x.getCardColor() == firstColor)                                             //Filtering by the color that I want
                .map(x -> playerboard.getGameCardPosition(x).get())                                      //and at the end obtain the coordinate of this card
                .collect(Collectors.toCollection(ArrayList::new));

        HashSet<Coordinate> coordinatesAlreadyUsed = new HashSet<>();           //List of coordinates that are used for a match
        for (Coordinate coordinate : coordinatesCanMatch) {                     //For every possible coordinate that is found, try to follow the
            ArrayList<Coordinate> coordinatesMaybeUsed = new ArrayList<>();     //coordinate of Hashmap in positionRequired
            //coordinatesMaybeUsed is an ArrayList that contains the coordinates which, if correct,
            // will be recorded in coordinatesAlreadyUsed
            Coordinate temp = new Coordinate(0, 0);
            //Support variable
            for (PositionalData position : positionalData) {
                temp.x = coordinate.x + position.coordinate().x;
                temp.y = coordinate.y + position.coordinate().y;
                //Move according to the coordinates marked on the hashmap
                if (coordinatesAlreadyUsed.contains(temp)) {
                    break;
                }
                if (playerboard.getGameCard(temp).isEmpty()) {
                    break;
                }
                if (playerboard.getGameCard(temp).get().getCardColor() != position.cardColorEnum()) {
                    break;
                }
                coordinatesMaybeUsed.add(new Coordinate(temp.x, temp.y));
            }
            if (coordinatesMaybeUsed.size() == 3) {
                coordinatesAlreadyUsed.addAll(coordinatesMaybeUsed);
                numOfMatch++;
            }
            //Respect to coordinates I must move by positionRequired.get(1).
        }
        return numOfMatch * this.pointsWon;
    }

    /**
     * Checks if the given object is equal to this PositionalObjectiveCard.
     * Two PositionalObjectiveCards are equal if they have the same cardId, pointsWon, and positionalData.
     *
     * @param o The object to compare this PositionalObjectiveCard against.
     * @return true if the given object represents a PositionalObjectiveCard equivalent to this PositionalObjectiveCard, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PositionalObjectiveCard that)) return false;
        if (!super.equals(o)) return false;
        return this.positionalData.equals(that.positionalData);
    }
}