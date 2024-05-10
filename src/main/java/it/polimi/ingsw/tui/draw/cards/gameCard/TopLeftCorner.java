package it.polimi.ingsw.tui.draw.cards.gameCard;

import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.tui.draw.DrawArea;
import it.polimi.ingsw.tui.draw.Drawable;
import it.polimi.ingsw.tui.utils.ColorsEnum;

import java.util.Optional;

public class TopLeftCorner implements Drawable {

    private final DrawArea drawArea;

    public TopLeftCorner(Optional<Corner> topLeftCorner, ColorsEnum color) {
        if (topLeftCorner.isEmpty()) {
            drawArea = new DrawArea("""
                    ┌────
                    │
                    │  
                    """);
            drawArea.setColor(color);
        } else if (topLeftCorner.get().isCovered()) {
            drawArea = new DrawArea("");
        } else {
            drawArea = new DrawArea("""
                    ┌───┬
                    │   │
                    ├───┘
                    """);
            drawArea.setColor(color);
            drawArea.drawAt(2, 1, topLeftCorner.get().getGameItem().getSymbol(), topLeftCorner.get().getGameItem().getColor());
        }
    }

    public DrawArea getDrawArea() {
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
}
