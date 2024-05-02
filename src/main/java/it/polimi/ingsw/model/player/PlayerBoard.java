package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.utils.PointCornerPositionPair;
import it.polimi.ingsw.model.card.corner.CornerPosition;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import it.polimi.ingsw.network.virtualView.PlayerBoardView;
import it.polimi.ingsw.network.virtualView.VirtualViewable;

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
public class PlayerBoard implements VirtualViewable<PlayerBoardView> {
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
     * This method is used to get the items in the player board.
     *
     * @return GameItemStore This returns the items in the player board.
     */
    public GameItemStore getGameItems() {
        return gameItems;
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
        validatePlacement(coordinate, gameCard);
        updateGameItems(gameCard, coordinate);
        playerBoard.put(coordinate, gameCard);
        return gameCard.getPoints(coordinate, this);
    }

    private void validatePlacement(Coordinate coordinate, GameCard gameCard) {
        if (playerBoard.containsKey(coordinate)) {
            throw new IllegalArgumentException("Position already occupied");
        }

        if (!isPositionAdjacent(coordinate)) {
            throw new IllegalArgumentException("Position not adjacent to any other card");
        }

        if (!isPlacementCompatible(coordinate)) {
            throw new IllegalArgumentException("Position not compatible with adjacent cards");
        }

        if (!hasEnoughResources(gameCard)) {
            throw new IllegalArgumentException("Not enough resources");
        }
    }

    /**
     * This method is used to check if a position is adjacent to any other card in the player board.
     * If the position is the center of the board, the method will return true as the starter card does not require any adjacent card.
     *
     * @param coordinate The coordinate to check.
     * @return true if the position is adjacent to any other card or the coordinate is the center of the board, false otherwise.
     */
    private boolean isPositionAdjacent(Coordinate coordinate) {
        return getAdjacentPointCornersPair(coordinate).stream()
                .anyMatch(pair -> (coordinate.getX() == 0 && coordinate.getY() == 0) || playerBoard.containsKey(pair.coordinate()));
    }

    /**
     * This method is used to check if the placement of a card is compatible with the adjacent cards.
     * A placement is compatible if there isn't any adjacent card that has an empty intersecting corner with the card to place.
     *
     * @param coordinate The coordinate where the placement is being performed.
     * @return true if the placement is compatible, false otherwise.
     */
    private boolean isPlacementCompatible(Coordinate coordinate) {
        return getAdjacentPointCornersPair(coordinate).stream()
                .noneMatch(pair -> {

                    /*
                    Since the check is performed inside noneMatch, for each adjacent card we will return:
                        - true if the placement is not compatible with the card;
                        - false if the placement is compatible with the card.
                     */
                    Optional<GameCard> card = getGameCard(pair.coordinate());

                    // If the board place is empty, the placement is compatible.
                    if (card.isEmpty()) {
                        return false;
                    }

                    // In the game there are cards that do not have some corners, so the placement is compatible if the card has a corner in the position intersecting the card.
                    // getCorner() returns an Optional, if it is present the card has the corner in the given position.
                    return card.get().getCorner(pair.cornerPosition()).isEmpty();
                });
    }

    /**
     * This method is used to check if the player has enough resources to place a card.
     *
     * @param gameCard The card to place.
     * @return true if the player has enough resources, false otherwise.
     */
    private boolean hasEnoughResources(GameCard gameCard) {
        GameItemStore neededItems = gameCard.getNeededItemStore();
        // Check if all the items needed by the card (neededItems) <= items owned by the player (gameItems)
        // As we are using noneMatch we need to negate the condition
        return neededItems.keySet().stream().noneMatch(gameItem -> neededItems.get(gameItem) > gameItems.get(gameItem));
    }

    /**
     * This method is used to update the player's items after placing a card.
     *
     * @param gameCard   The card placed.
     * @param coordinate The coordinate where the card was placed.
     */
    private void updateGameItems(GameCard gameCard, Coordinate coordinate) {
        // Add the items of the card to the player's items
        gameItems.addStore(gameCard.getGameItemStore());

        // Cover the corners of already present cards that will be covered by the card placed in the coordinate.
        // Remove from the player's items the items of the corners covered by the card.
        getAdjacentPointCornersPair(coordinate).forEach(pair -> {
            Optional<GameCard> card = getGameCard(pair.coordinate());
            if (card.isPresent()) {
                // setCornerCovered() returns the item of the corner covered by the card.
                GameItemEnum coveredItem = card.get().setCornerCovered(pair.cornerPosition());
                gameItems.decrement(coveredItem, 1);
            }
        });
    }

    /**
     * This method is used to get the virtual view of the player's board.
     *
     * @return PlayerBoardView This returns the virtual view of the player's board.
     */
    @Override
    public PlayerBoardView getVirtualView() {
        return new PlayerBoardView(playerBoard, gameItems);
    }
}
