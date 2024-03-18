package it.polimi.ingsw.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class PlayerBoard {
    private final HashMap<Point, GameCard> playerBoard;
    private final GameItemStore gameItems;

    public PlayerBoard() {
        playerBoard = new HashMap<>();
        gameItems = new GameItemStore();
    }

    /**
     * This method is used to get the card in the player board at a specific position.
     *
     * @param point This is the position of the card.
     * @return Optional<GameCard> This returns the card at the position, if present.
     */
    public Optional<GameCard> getGameCard(Point point) {
        return Optional.ofNullable(playerBoard.get(point));
    }

    /**
     * This method is used to get the position of a card in the player board.
     *
     * @param gameCard This is the card to find.
     * @return Optional<Point> This returns the position of the card, if present.
     */
    public Optional<Point> getGameCardPosition(GameCard gameCard) {
        for (Point point : playerBoard.keySet()) {
            if (playerBoard.get(point).equals(gameCard)) {
                return Optional.of(point);
            }
        }
        return Optional.empty();
    }

    /**
     * This method is used to get the cards in the player board.
     *
     * @return ArrayList<GameCard> This returns the list of cards in the player board.
     */
    public ArrayList<GameCard> getGameCards() {
        return new ArrayList<>(playerBoard.values());
    }

    /**
     * This method is used to get the amount of an item in the player board.
     *
     * @param gameItem This is the item to find.
     * @return Integer This returns the amount of the item.
     */
    public Integer getGameItemAmount(GameItemEnum gameItem) {
        return gameItems.get(gameItem);
    }

    /**
     * This method is used to get a list of pairs of each point and corner position that will intersect the card placed in the center point.
     *
     * @param centerPoint This is the coordinate of the card to intersect.
     * @return ArrayList<PointCornerPositionPair> This returns the list of pairs of each point and corner position.
     */
    private ArrayList<PointCornerPositionPair> getAdjacentPointCornersPair(Point centerPoint) {
        // Get the adjacent points
        Point topLeftPoint = new Point(centerPoint.x - 1, centerPoint.y + 1);
        Point topRightPoint = new Point(centerPoint.x + 1, centerPoint.y + 1);
        Point bottomLeftPoint = new Point(centerPoint.x - 1, centerPoint.y - 1);
        Point bottomRightPoint = new Point(centerPoint.x + 1, centerPoint.y - 1);

        ArrayList<PointCornerPositionPair> pairList = new ArrayList<>();

        // For each point add the corner position that will intersect the card placed in the center point.
        pairList.add(new PointCornerPositionPair(topLeftPoint, CornerPosition.BOTTOM_RIGHT));
        pairList.add(new PointCornerPositionPair(topRightPoint, CornerPosition.BOTTOM_LEFT));
        pairList.add(new PointCornerPositionPair(bottomLeftPoint, CornerPosition.TOP_RIGHT));
        pairList.add(new PointCornerPositionPair(bottomRightPoint, CornerPosition.TOP_LEFT));

        return pairList;
    }

    /**
     * This method is used to place a GameCard in the player board.
     *
     * @param point    This is the position where to place the card.
     * @param gameCard This is the GameCard to place.
     * @return Integer This returns the position to advance the player.
     * @throws IllegalArgumentException This is thrown if the position is already occupied.
     * @throws IllegalArgumentException This is thrown if the position is not adjacent to any other card.
     * @throws IllegalArgumentException This is thrown if the position is not compatible with adjacent cards.
     * @throws IllegalArgumentException This is thrown if there are not enough resources.
     */
    public Integer setGameCard(Point point, GameCard gameCard) {

        if (playerBoard.containsKey(point)) {
            throw new IllegalArgumentException("Position already occupied");
        }

        // Iterate over each adjacent point and its respective intersecting corner
        getAdjacentPointCornersPair(point).forEach(pair -> {
            // If we are not placing the starter card we need to check that exists at least one card adjacent to the one we are placing
            if (point.getX() != 0 || point.getY() != 0 && playerBoard.containsKey(pair.point())) {
                throw new IllegalArgumentException("Position not adjacent to any other card");
            }

            // Check if each intersecting corner of the adjacent cards exist. If not it means that the card cannot be placed as there is at least one card that is missing a corner in the required position
            if (getGameCard(pair.point()).map(card -> !card.getCorner(pair.cornerPosition()).isExisting()).orElse(false)) {
                throw new IllegalArgumentException("Position not compatible with adjacent cards");
            }
        });

        GameItemStore neededItems = gameCard.getNeededItemStore();
        // Check if there are enough resources to place the card
        if (neededItems.keySet().stream().anyMatch(
                gameItem -> neededItems.get(gameItem) < neededItems.get(gameItem)
        )) {
            throw new IllegalArgumentException("Not enough resources");
        }

        // Increment items with the ones on the card to add
        gameCard.getNeededItemStore().keySet().forEach(i -> gameItems.increment(i, 1));

        // Decrement items for each corner of the card
        getAdjacentPointCornersPair(point).forEach(pair -> {
            getGameCard(pair.point()).ifPresent(card -> {
                gameItems.decrement(card.setCornerCovered(pair.cornerPosition()), 1);
            });
        });

        playerBoard.put(point, gameCard);
        return gameCard.getPoints();
    }
}
