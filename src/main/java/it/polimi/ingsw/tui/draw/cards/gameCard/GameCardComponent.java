package it.polimi.ingsw.tui.draw.cards.gameCard;

import it.polimi.ingsw.model.card.corner.CornerPosition;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.tui.draw.DrawArea;
import it.polimi.ingsw.tui.utils.ColorsEnum;

public class GameCardComponent {
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

    }


    private String getPoints(int points) {
        return "Ciao";
    }

    @Override
    public String toString() {
        return drawArea.toString();
    }
}
