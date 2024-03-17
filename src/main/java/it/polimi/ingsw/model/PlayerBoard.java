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
     * @return Integer This returns the position to advance the player.
     * @throws IllegalArgumentException This is thrown if the position is already occupied.
     * @throws IllegalArgumentException This is thrown if the position is not adjacent to any other card.
     * @throws IllegalArgumentException This is thrown if the position is not compatible with adjacent cards.
     * @throws IllegalArgumentException This is thrown if there are not enough resources.
     */
    public Integer setGameCard(Point point, GameCard gameCard) {
        // Get adjacent cards points
        Point topLeftPoint = new Point(point.x - 1, point.y + 1);
        Point topRightPoint = new Point(point.x + 1, point.y + 1);
        Point bottomLeftPoint = new Point(point.x - 1, point.y - 1);
        Point bottomRightPoint = new Point(point.x + 1, point.y - 1);

        if (playerBoard.containsKey(point)) {
            throw new IllegalArgumentException("Position already occupied");
        }

        if ((point.getX() != 0 || point.getY() != 0)
                && !playerBoard.containsKey(bottomLeftPoint)
                && !playerBoard.containsKey(topRightPoint)
                && !playerBoard.containsKey(topLeftPoint)
                && !playerBoard.containsKey(bottomRightPoint)
        ) {
            throw new IllegalArgumentException("Position not adjacent to any other card");
        }

        if (getGameCard(topRightPoint).map(card -> card.getBottomLeftCorner().isEmpty()).orElse(false)
                || getGameCard(bottomLeftPoint).map(card -> card.getTopRightCorner().isEmpty()).orElse(false)
                || getGameCard(topLeftPoint).map(card -> card.getBottomRightCorner().isEmpty()).orElse(false)
                || getGameCard(bottomRightPoint).map(card -> card.getTopLeftCorner().isEmpty()).orElse(false)
        ) {
            throw new IllegalArgumentException("Position not compatible with adjacent cards");
        }

        if (gameCard.getNeededResources().keySet().stream().anyMatch(
                gameResource -> gameResources.get(gameResource) < gameCard.getNeededResources().get(gameResource)
        )) {
            throw new IllegalArgumentException("Not enough resources");
        }
        
        // Increment resources and objects for each corner of the card
        gameCard.getBottomLeftCorner().ifPresent(this::incrementGameResource);
        gameCard.getBottomLeftCorner().ifPresent(this::incrementGameObject);
        gameCard.getBottomRightCorner().ifPresent(this::incrementGameResource);
        gameCard.getBottomRightCorner().ifPresent(this::incrementGameObject);
        gameCard.getTopLeftCorner().ifPresent(this::incrementGameResource);
        gameCard.getTopLeftCorner().ifPresent(this::incrementGameObject);
        gameCard.getTopRightCorner().ifPresent(this::incrementGameResource);
        gameCard.getTopRightCorner().ifPresent(this::incrementGameObject);

        getGameCard(topLeftPoint).flatMap(GameCard::getBottomRightCorner) // Get bottom right corner of top left card
                .ifPresent(this::setCornerCovered);                       // Set it as covered updating resources and objects

        getGameCard(topRightPoint).flatMap(GameCard::getBottomLeftCorner) // Get bottom left corner of top right card
                .ifPresent(this::setCornerCovered);                        // Set it as covered updating resources and objects

        getGameCard(bottomLeftPoint).flatMap(GameCard::getTopRightCorner) // Get top right corner of bottom left card
                .ifPresent(this::setCornerCovered);                         // Set it as covered updating resources and objects

        getGameCard(bottomRightPoint).flatMap(GameCard::getTopLeftCorner) // Get top left corner of bottom right card
                .ifPresent(this::setCornerCovered);                        // Set it as covered updating resources and objects


        playerBoard.put(point, gameCard);
        return gameCard.getPoints();
    }

    /**
     * This method is used to set a GameCard Side as covered and update GameResources and GameObjects accordingly.
     *
     * @param point This is the position of the corner to cover.
     */
    private void setCornerCovered(Corner corner) {
        corner.getGameResource().ifPresent(gameResource -> gameResources.decrement(corner.getGameResource().get(), 1));
        corner.getGameObject().ifPresent(gameObject -> gameObjects.decrement(corner.getGameObject().get(), 1));
        corner.setCovered(true);
    }

    /**
     * This method is used to increment the amount of a GameResource in the PlayerBoard.
     *
     * @param corner This is the corner to increment the resource of.
     */
    private void incrementGameResource(Corner corner) {
        corner.getGameResource().ifPresent(gameResource -> gameResources.increment(corner.getGameResource().get(), 1));
    }

    /**
     * This method is used to increment the amount of a GameObject in the PlayerBoard.
     *
     * @param corner This is the corner to increment the object of.
     */
    private void incrementGameObject(Corner corner) {
        corner.getGameObject().ifPresent(gameObject -> gameObjects.increment(corner.getGameObject().get(), 1));
    }


}
