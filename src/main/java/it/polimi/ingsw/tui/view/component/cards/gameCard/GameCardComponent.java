package it.polimi.ingsw.tui.view.component.cards.gameCard;

import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.corner.CornerPosition;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.component.cards.gameCard.corner.BottomLeftCorner;
import it.polimi.ingsw.tui.view.component.cards.gameCard.corner.BottomRightCorner;
import it.polimi.ingsw.tui.view.component.cards.gameCard.corner.TopLeftCorner;
import it.polimi.ingsw.tui.view.component.cards.gameCard.corner.TopRightCorner;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.drawer.Drawable;

import java.util.ArrayList;

/**
 * This class represents a game card component in the game.
 * It implements the Drawable interface, meaning it can be drawn on a DrawArea.
 */
public class GameCardComponent implements Drawable {

    /**
     * The draw area of the game card.
     */
    private final DrawArea drawArea;

    /**
     * Constructor for a GameCardComponent.
     * It initializes the drawArea and draws the game card on it.
     *
     * @param gameCard The game card to be drawn.
     */
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
        drawArea.drawCenteredX(5, neededItemsComponent(gameCard.getNeededItemStore()));
        drawArea.drawAt(9, 2, itemStoreComponent(gameCard.getBackItemStore()));

    }

    /**
     * Constructor for a GameCardComponent.
     * It initializes the drawArea and draws the game card and its coordinates on it.
     *
     * @param gameCard   The game card to be drawn.
     * @param coordinate The coordinates of the game card.
     */
    public GameCardComponent(GameCard gameCard, Coordinate coordinate) {
        GameCardComponent gameCardComponent = new GameCardComponent(gameCard);
        drawArea = gameCardComponent.getDrawArea();
        DrawArea coordinateDrawArea = new DrawArea("[");
        coordinateDrawArea.drawAt(1, 0, (int) coordinate.getX() + ",");
        coordinateDrawArea.drawAt(coordinateDrawArea.getWidth(), 0, (int) coordinate.getY() + "]");
        drawArea.drawCenteredX(0, coordinateDrawArea);
    }

    /**
     * Creates a DrawArea for the items in the item store.
     *
     * @param itemStore The item store containing the items.
     * @return The DrawArea with the items drawn on it.
     */
    private DrawArea itemStoreComponent(GameItemStore itemStore) {
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
                drawArea.drawAt(2, 1, itemsArray.get(1).getSymbol(), itemsArray.get(1).getColor());
                drawArea.drawAt(2, 2, itemsArray.getLast().getSymbol(), itemsArray.getLast().getColor());
            }
        }
        return drawArea;
    }

    /**
     * Creates a DrawArea for the needed items.
     *
     * @param neededItems The item store containing the needed items.
     * @return The DrawArea with the needed items drawn on it.
     */
    private DrawArea neededItemsComponent(GameItemStore neededItems) {
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

    /**
     * Creates a DrawArea for the points component.
     *
     * @param points       The points to be drawn.
     * @param multiplier   The multiplier for the points.
     * @param isPositional Whether the points are positional.
     * @return The DrawArea with the points component drawn on it.
     */
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


    /**
     * Returns the height of the drawable object.
     *
     * @return the height of the drawable object.
     */
    @Override
    public int getHeight() {
        return drawArea.getHeight();
    }

    /**
     * Returns the width of the drawable object.
     *
     * @return the width of the drawable object.
     */
    @Override
    public int getWidth() {
        return drawArea.getWidth();
    }

    /**
     * Returns a string representation of the drawable object.
     *
     * @return a string representation of the drawable object.
     */
    @Override
    public String toString() {
        return drawArea.toString();
    }

    /**
     * Returns the draw area of the drawable object.
     *
     * @return the draw area of the drawable object.
     */
    @Override
    public DrawArea getDrawArea() {
        return drawArea;
    }
}
