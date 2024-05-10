package it.polimi.ingsw.tui.draw.cards.gameCard;

import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.tui.draw.DrawArea;
import it.polimi.ingsw.tui.draw.Drawable;
import it.polimi.ingsw.tui.utils.ColorsEnum;

import java.util.Optional;

public class TopRightCorner implements Drawable {
    private final DrawArea drawArea;

    public TopRightCorner(Optional<Corner> topRightCorner, ColorsEnum color) {
        if (topRightCorner.isEmpty()) {
            drawArea = new DrawArea("""
                    ────┐
                        │
                        │
                    """);
            drawArea.setColor(color);
        } else if (topRightCorner.get().isCovered()) {
            drawArea = new DrawArea("");
        } else {
            drawArea = new DrawArea("""
                    ┬───┐
                    │   │
                    └───┤
                    """);
            drawArea.setColor(color);
            drawArea.drawAt(2, 1, topRightCorner.get().getGameItem().getSymbol(), topRightCorner.get().getGameItem().getColor());
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
