package it.polimi.ingsw.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class PlayerBoard {
    private final HashMap<Point, GameCard> playerBoard;
    private final GameResourceStore gameResources;
    private final GameObjectStore gameObjects;

    public PlayerBoard() {
        playerBoard = new HashMap<Point, GameCard>();
        gameResources = new GameResourceStore();
        gameObjects = new GameObjectStore();
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
        return new ArrayList<GameCard>(playerBoard.values());
    }

    /**
     * This method is used to get the amount of a resource in the player board.
     *
     * @param gameResource This is the resource to find.
     * @return Integer This returns the amount of the resource.
     */
    public Integer getGameResourceAmount(GameResource gameResource) {
        return gameResources.get(gameResource);
    }

    /**
     * This method is used to get the amount of an object in the player board.
     *
     * @param gameObject This is the object to find.
     * @return Integer This returns the amount of the object.
     */
    public Integer getGameObjectAmount(GameObject gameObject) {
        return gameObjects.get(gameObject);
    }

    /**
     * This method is used to check whether a GameCard can be places in the PlayerBoard.
     *
     * @param point    This is the position where to place the card.
     * @param gameCard This is the GameCard to place.
     * @return boolean This returns true if the card can be placed, false otherwise.
     */
    public boolean isPlaceable(Point point, GameCard gameCard) {
        if (playerBoard.containsKey(point)) {
            return false;
        }

        if (!playerBoard.containsKey(new Point(point.x - 1, point.y - 1)) && !playerBoard.containsKey(new Point(point.x + 1, point.y + 1)) && !playerBoard.containsKey(new Point(point.x - 1, point.y + 1)) && !playerBoard.containsKey(new Point(point.x + 1, point.y - 1))) {
            return false;
        }

        if (getGameCard(new Point(point.x + 1, point.y + 1)).map(card -> card.getBottomLeftCorner().isEmpty()).orElse(false) || getGameCard(new Point(point.x - 1, point.y - 1)).map(card -> card.getTopRightCorner().isEmpty()).orElse(false) || getGameCard(new Point(point.x - 1, point.y + 1)).map(card -> card.getBottomRightCorner().isEmpty()).orElse(false) || getGameCard(new Point(point.x + 1, point.y - 1)).map(card -> card.getTopLeftCorner().isEmpty()).orElse(false)) {
            return false;
        }

        return gameCard.getNeededResources().keySet().stream().noneMatch(
                gameResource -> gameResources.get(gameResource) < gameCard.getNeededResources().get(gameResource)
        );
    }

    /**
     * This method is used to place a GameCard in the player board.
     *
     * @param point    This is the position where to place the card.
     * @param gameCard This is the GameCard to place.
     * @throws IllegalArgumentException This is thrown if the position is already occupied.
     * @throws IllegalArgumentException This is thrown if the position is not adjacent to any other card.
     * @throws IllegalArgumentException This is thrown if the position is not compatible with adjacent cards.
     * @throws IllegalArgumentException This is thrown if there are not enough resources.
     */
    public void setGameCard(Point point, GameCard gameCard) {
        if (playerBoard.containsKey(point)) {
            throw new IllegalArgumentException("Position already occupied");
        }

        if ((point.getX() != 0 || point.getY() != 0) && !playerBoard.containsKey(new Point(point.x - 1, point.y - 1)) && !playerBoard.containsKey(new Point(point.x + 1, point.y + 1)) && !playerBoard.containsKey(new Point(point.x - 1, point.y + 1)) && !playerBoard.containsKey(new Point(point.x + 1, point.y - 1))) {
            throw new IllegalArgumentException("Position not adjacent to any other card");
        }

        if (getGameCard(new Point(point.x + 1, point.y + 1)).map(card -> card.getBottomLeftCorner().isEmpty()).orElse(false) || getGameCard(new Point(point.x - 1, point.y - 1)).map(card -> card.getTopRightCorner().isEmpty()).orElse(false) || getGameCard(new Point(point.x - 1, point.y + 1)).map(card -> card.getBottomRightCorner().isEmpty()).orElse(false) || getGameCard(new Point(point.x + 1, point.y - 1)).map(card -> card.getTopLeftCorner().isEmpty()).orElse(false)) {
            throw new IllegalArgumentException("Position not compatible with adjacent cards");
        }

        if (gameCard.getNeededResources().keySet().stream().anyMatch(
                gameResource -> gameResources.get(gameResource) < gameCard.getNeededResources().get(gameResource)
        )) {
            throw new IllegalArgumentException("Not enough resources");
        }

        playerBoard.put(point, gameCard);
    }


}
