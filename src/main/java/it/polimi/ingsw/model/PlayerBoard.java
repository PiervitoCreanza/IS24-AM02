package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

/**
 * The PlayerBoard class represents the board of a player in the game.
 * It contains a map of coordinates to game cards, representing the layout of the game cards on the board.
 * It also contains a GameItemStore, representing the items the player has collected.
 * Each player board has a starter card that is set when the board is created.
 */
public class PlayerBoard {
    /**
     * A map from points to game cards, representing the layout of the game cards on the board.
     */
    private final HashMap<Coordinate, GameCard> playerBoard;

    /**
     * The items the player has collected.
     */
    private final GameItemStore gameItems;

    /**
     * The starter card for this player board.
     * This card will be placed in the center of the board at the first turn on the player has chosen the side.
     */
    private final GameCard starterCard;

    /**
     * Constructor for the PlayerBoard class.
     * Initializes the player's board, game items, and starter card.
     *
     * @param starterCard The starter card for this player board. Cannot be null.
     * @throws NullPointerException if the starterCard is null.
     */
    public PlayerBoard(GameCard starterCard) {
        this.starterCard = Objects.requireNonNull(starterCard, "Starter card cannot be null");
        playerBoard = new HashMap<>();
        gameItems = new GameItemStore();
    }

    /**
     * This method is used to get the card in the player board at a specific position.
     *
     * @param coordinate This is the position of the card.
     * @return Optional<GameCard> This returns the card at the position, if present.
     */
    public Optional<GameCard> getGameCard(Coordinate coordinate) {
        return Optional.ofNullable(playerBoard.get(coordinate));
    }

    /**
     * This method is used to get the starter card of the player board.
     *
     * @return GameCard This returns the starter card.
     */
    public GameCard getStarterCard() {
        return starterCard;
    }

    /**
     * This method is used to get the position of a card in the player board.
     *
     * @param gameCard This is the card to find.
     * @return Optional<Coordinate> This returns the position of the card, if present.
     */
    public Optional<Coordinate> getGameCardPosition(GameCard gameCard) {
        for (Coordinate coordinate : playerBoard.keySet()) {
            if (playerBoard.get(coordinate).equals(gameCard)) {
                return Optional.of(coordinate);
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
     * This method is used to get a list of pairs of each coordinate and corner position that will intersect the card placed in the center coordinate.
     *
     * @param centerPoint This is the coordinate of the card to intersect.
     * @return ArrayList<PointCornerPositionPair> This returns the list of pairs of each coordinate and corner position.
     */
    private ArrayList<PointCornerPositionPair> getAdjacentPointCornersPair(Coordinate centerPoint) {
        // Get the adjacent points
        Coordinate topLeftPoint = new Coordinate(centerPoint.x - 1, centerPoint.y + 1);
        Coordinate topRightPoint = new Coordinate(centerPoint.x + 1, centerPoint.y + 1);
        Coordinate bottomLeftPoint = new Coordinate(centerPoint.x - 1, centerPoint.y - 1);
        Coordinate bottomRightPoint = new Coordinate(centerPoint.x + 1, centerPoint.y - 1);

        ArrayList<PointCornerPositionPair> pairList = new ArrayList<>();

        // For each coordinate add the corner position that will intersect the card placed in the center coordinate.
        pairList.add(new PointCornerPositionPair(topLeftPoint, CornerPosition.BOTTOM_RIGHT));
        pairList.add(new PointCornerPositionPair(topRightPoint, CornerPosition.BOTTOM_LEFT));
        pairList.add(new PointCornerPositionPair(bottomLeftPoint, CornerPosition.TOP_RIGHT));
        pairList.add(new PointCornerPositionPair(bottomRightPoint, CornerPosition.TOP_LEFT));

        return pairList;
    }

    /**
     * This method is used to place a GameCard in the player board.
     *
     * @param coordinate This is the position where to place the card.
     * @param gameCard   This is the GameCard to place.
     * @return Integer This returns the position to advance the player.
     * @throws IllegalArgumentException This is thrown if the position is already occupied.
     * @throws IllegalArgumentException This is thrown if the position is not adjacent to any other card.
     * @throws IllegalArgumentException This is thrown if the position is not compatible with adjacent cards.
     * @throws IllegalArgumentException This is thrown if there are not enough resources.
     */
    public int setGameCard(Coordinate coordinate, GameCard gameCard) {

        if (playerBoard.containsKey(coordinate)) {
            throw new IllegalArgumentException("Position already occupied");
        }

        // Check if the position is adjacent to at least one other card. We can't place a card isolated from the others (no shared corner).
        boolean isPositionAdjacent = getAdjacentPointCornersPair(coordinate).stream()
                .anyMatch(pair -> (coordinate.getX() == 0 && coordinate.getY() == 0) || playerBoard.containsKey(pair.coordinate()));
        // If the position is not adjacent to any other card, we can't place the card
        if (!isPositionAdjacent) {
            throw new IllegalArgumentException("Position not adjacent to any other card");
        }

        // Iterate over each adjacent coordinate and its respective intersecting corner
        getAdjacentPointCornersPair(coordinate).forEach(pair -> {
            Optional<GameCard> card = getGameCard(pair.coordinate());
            // Check if the placement is compatible with the adjacent cards. If an adjacent card is present and does not have a corner where the two cards will intersect it means that the card cannot be placed.
            if (card.isPresent() && card.get().getCorner(pair.cornerPosition()).isPresent()) {
                throw new IllegalArgumentException("Position not compatible with adjacent cards");
            }
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
        getAdjacentPointCornersPair(coordinate).forEach(pair -> {
            getGameCard(pair.coordinate()).ifPresent(card -> {
                gameItems.decrement(card.setCornerCovered(pair.cornerPosition()), 1);
            });
        });

        playerBoard.put(coordinate, gameCard);
        return gameCard.getPoints(coordinate, this);
    }
}
