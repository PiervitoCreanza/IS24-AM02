package it.polimi.ingsw.tui.draw.cards.gameCard;

import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.corner.CornerPosition;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import it.polimi.ingsw.tui.draw.DrawArea;
import it.polimi.ingsw.tui.draw.Drawable;
import it.polimi.ingsw.tui.utils.ColorsEnum;

import java.util.ArrayList;

public class GameCardComponent implements Drawable {
    private final DrawArea drawArea;

    public GameCardComponent(GameCard gameCard) {
        ColorsEnum color = gameCard.getCardColor().getColor();
        drawArea = new DrawArea("""
                     ─────────────
                                  
                                  
                │                     │ 
                              
                                
                     ─────────────
                """);
        drawArea.setColor(color);
        drawArea.drawAt(0, 0, new TopLeftCorner(gameCard.getCorner(CornerPosition.TOP_LEFT), color).getDrawArea());
        drawArea.drawAt(18, 0, new TopRightCorner(gameCard.getCorner(CornerPosition.TOP_RIGHT), color).getDrawArea());
        drawArea.drawAt(0, 4, new BottomLeftCorner(gameCard.getCorner(CornerPosition.BOTTOM_LEFT), color).getDrawArea());
        drawArea.drawAt(18, 4, new BottomRightCorner(gameCard.getCorner(CornerPosition.BOTTOM_RIGHT), color).getDrawArea());
        drawArea.drawAt(6, 1, pointsComponent(gameCard.getPoints(), gameCard.getMultiplier(), gameCard.isGoldPositional()));
        drawArea.drawCenteredX(6, 17, 5, neededItems(gameCard.getNeededItemStore()));
        drawArea.drawAt(9, 2, itemStore(gameCard.getBackItemStore()));

    }

    public GameCardComponent(GameCard gameCard, Coordinate coordinate) {
        GameCardComponent gameCardComponent = new GameCardComponent(gameCard);
        drawArea = gameCardComponent.getDrawArea();
        DrawArea coordinateDrawArea = new DrawArea("[");
        coordinateDrawArea.drawAt(1, 0, (int) coordinate.getX() + ",");
        coordinateDrawArea.drawAt(coordinateDrawArea.getWidth(), 0, (int) coordinate.getY() + "]");
        drawArea.drawCenteredX(8, 15, 0, coordinateDrawArea);
    }

    private DrawArea itemStore(GameItemStore itemStore) {
        DrawArea drawArea = new DrawArea("");
        ArrayList<GameItemEnum> itemsArray = itemStore.getNonEmptyKeys();
        int size = itemsArray.size();
        switch (size) {
            case 0 -> {
                return drawArea;
            }
            case 1 -> {
                drawArea.drawAt(2, 1, itemsArray.getFirst().getSymbol(), itemsArray.getFirst().getColor());
            }
            case 2 -> {
                drawArea.drawAt(0, 1, itemsArray.getFirst().getSymbol(), itemsArray.getFirst().getColor());
                drawArea.drawAt(4, 1, itemsArray.getLast().getSymbol(), itemsArray.getLast().getColor());
            }
            case 3 -> {
                drawArea.drawAt(2, 0, itemsArray.getFirst().getSymbol(), itemsArray.getFirst().getColor());
                drawArea.drawAt(0, 1, itemsArray.get(1).getSymbol(), itemsArray.get(1).getColor());
                drawArea.drawAt(4, 1, itemsArray.getLast().getSymbol(), itemsArray.getLast().getColor());

            }
        }
        return drawArea;
    }

    private DrawArea neededItems(GameItemStore neededItems) {
        ArrayList<GameItemEnum> itemsArray = neededItems.getNonEmptyKeys();
        DrawArea drawArea = new DrawArea("           ");
        int x = 0;
        for (GameItemEnum item : itemsArray) {
            for (int i = neededItems.get(item); i > 0; i--) {
                drawArea.drawAt(x, 0, item.getSymbol(), item.getColor());
                x += 2;
            }
        }
        return drawArea;
    }

    private DrawArea pointsComponent(int points, GameItemEnum multiplier, boolean isPositional) {
        DrawArea drawArea = new DrawArea("           ");
        if (points == 0) {
            return drawArea;
        } else {
            if (isPositional) {
                drawArea.drawAt(0, 0, points);
                drawArea.drawAt(2, 0, "- Corners");
            } else {
                if (multiplier == GameItemEnum.NONE) {
                    drawArea.drawAt(5, 0, points);
                } else {
                    drawArea.drawAt(0, 0, points);
                    drawArea.drawAt(2, 0, "- Item( )");
                    drawArea.drawAt(9, 0, multiplier.getSymbol(), multiplier.getColor());
                }
            }
        }
        return drawArea;
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
