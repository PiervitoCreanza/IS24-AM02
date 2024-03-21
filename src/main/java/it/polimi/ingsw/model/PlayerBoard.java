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

        // Check if the position is adjacent to at least one other card. We can't place a card isolated from the others (no shared corner).
        boolean isPositionAdjacent = getAdjacentPointCornersPair(point).stream()
                .anyMatch(pair -> (point.getX() == 0 && point.getY() == 0) || playerBoard.containsKey(pair.point()));
        // If the position is not adjacent to any other card, we can't place the card
        if (!isPositionAdjacent) {
            throw new IllegalArgumentException("Position not adjacent to any other card");
        }

        // Iterate over each adjacent point and its respective intersecting corner
        getAdjacentPointCornersPair(point).forEach(pair -> {
            Optional<GameCard> card = getGameCard(pair.point());
            // Check if the placement is compatible with the adjacent cards. If an adjacent card is present and does not have a corner where the two cards will intersect it means that the card cannot be placed.
//            if (card.isPresent() && card.get().getCorner(pair.cornerPosition()).isPresent()) {
//                throw new IllegalArgumentException("Position not compatible with adjacent cards");
//            }
        });

        GameItemStore neededItems = gameCard.getNeededItemStore();
        // For each item check if there are enough resources to place the card.
        if (neededItems.keySet().stream().anyMatch(
                // Check if the items needed by the card (neededItems) < items owned by the player (gameItems)
                gameItem -> neededItems.get(gameItem) < gameItems.get(gameItem)
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
        ArrayList<Optional<Corner>> c1 = new ArrayList<>();
        c1.add(Optional.ofNullable(null));
        Corner test = c1.stream().map(corner -> corner.orElse(null)).findFirst().orElse(null);

        playerBoard.put(point, gameCard);
        return gameCard.getPoints(this);
    }
}
