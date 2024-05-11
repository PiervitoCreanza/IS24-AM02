package it.polimi.ingsw.tui.draw;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.tui.draw.cards.gameCard.GameCardComponent;

import java.util.HashMap;

public class PlayerBoardComponent implements Drawable {
    DrawArea drawArea;

    public PlayerBoardComponent(HashMap<Coordinate, GameCard> playerBoard) {
        drawArea = new DrawArea();
        int correctionX = 4;
        int correctionY = 2;
        HashMap<Integer, Coordinate> realCoordinates = new HashMap<>();
        // We save the real coordinates of the card before converting them
        playerBoard.forEach((k, v) -> realCoordinates.put(v.getCardId(), new Coordinate((int) k.getX(), (int) k.getY())));
        // We convert the coordinates of the cards
        playerBoard = drawArea.convertCoordinates(playerBoard);
        playerBoard.forEach((k, v) -> {
            GameCardComponent gameCardComponent = new GameCardComponent(v, realCoordinates.get(v.getCardId()));
            drawArea.drawAt((int) (k.getX() * (gameCardComponent.getWidth() - correctionX)), (int) (k.getY() * (gameCardComponent.getHeight() - correctionY)), gameCardComponent.getDrawArea());
        });
    }

    @Override
    public int getHeight() {
        return drawArea.getHeight();
    }

    @Override
    public int getWidth() {
        return drawArea.getWidth();
    }

    @Override
    public String toString() {
        return drawArea.toString();
    }

    @Override
    public DrawArea getDrawArea() {
        return drawArea;
    }
}
