package it.polimi.ingsw.tui.view.component.cards.gameCard.corner;

import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.drawer.Drawable;

import java.util.Optional;

public class BottomLeftCorner implements Drawable {
    private final DrawArea drawArea;

    public BottomLeftCorner(Optional<Corner> bottomLeftCorner, ColorsEnum color) {
        if (bottomLeftCorner.isEmpty()) {
            drawArea = new DrawArea("""
                    │
                    │
                    └────
                    """);
            drawArea.setColor(color);
        } else if (bottomLeftCorner.get().isCovered()) {
            drawArea = new DrawArea("");
        } else {
            drawArea = new DrawArea("""
                    ├───┐
                    │   │
                    └───┴
                    """);
            drawArea.setColor(color);
            drawArea.drawAt(2, 1, bottomLeftCorner.get().getGameItem().getSymbol(), bottomLeftCorner.get().getGameItem().getColor());
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
