package it.polimi.ingsw.model.ObjectiveCard;

import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.PlayerBoard;

import java.util.ArrayList;
import java.util.HashSet;
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
     * @param pointsWon The number of points the player can win by fulfilling the objective of this card.
     * @param positionalData The positional data required by the objective card.
     * @throws NullPointerException if positionalData is null.
     */
    public PositionalObjectiveCard(int pointsWon, ArrayList<PositionalData> positionalData) {
        super(pointsWon);
        Objects.requireNonNull(positionalData, "positionalData must be not null");
        this.positionalData = positionalData;
    }

    /**
     * This method calculates and returns the points won by the player.
     * It checks if the player's game cards match the positional data required by the objective card.
     * @param playerboard The player's game board.
     * @return The number of points won by the player.
     */
    @Override
    public int getPoints(PlayerBoard playerboard) {
        int numOfMatch = 0;                          //variable to count how many times player made the configuration
        CardColor firstColor = positionalData.get(0).cardColor();
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
                if (playerboard.getGameCard(temp).get().getCardColor() != position.cardColor()) {
                    break;
                }
                coordinatesMaybeUsed.add(temp);
            }
            if (coordinatesMaybeUsed.size() == 3) {
                coordinatesAlreadyUsed.addAll(coordinatesMaybeUsed);
                numOfMatch++;
            }
            //Respect to coordinates I must move by positionRequired.get(1).
        }
        return numOfMatch * this.pointsWon;
    }
}