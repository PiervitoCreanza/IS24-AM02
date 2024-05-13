package it.polimi.ingsw.tui.view.component.playerItems;

import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.drawer.Drawable;

public class PlayerItemsComponent implements Drawable {

    private final DrawArea drawArea;

    public PlayerItemsComponent(GameItemStore gameItemStore) {
        drawArea = new DrawArea();
        DrawArea playerResourcesDrawArea = new DrawArea("""
                ┌─────────────────────┐
                │  Resources:         │
                │              =      │
                │              =      │
                │              =      │
                │              =      │
                └─────────────────────┘
                """);
        playerResourcesDrawArea.drawAt(4, 2, "F [Fungi]", ColorsEnum.RED);
        playerResourcesDrawArea.drawAt(4, 3, "P [Plant]", ColorsEnum.GREEN);
        playerResourcesDrawArea.drawAt(4, 4, "A [Animal]", ColorsEnum.CYAN);
        playerResourcesDrawArea.drawAt(4, 5, "I [Insect]", ColorsEnum.PURPLE);
        playerResourcesDrawArea.drawCenteredX(18, 20, 2, new DrawArea(gameItemStore.get(GameItemEnum.FUNGI).toString()));
        playerResourcesDrawArea.drawCenteredX(18, 20, 3, new DrawArea(gameItemStore.get(GameItemEnum.PLANT).toString()));
        playerResourcesDrawArea.drawCenteredX(18, 20, 4, new DrawArea(gameItemStore.get(GameItemEnum.ANIMAL).toString()));
        playerResourcesDrawArea.drawCenteredX(18, 20, 5, new DrawArea(gameItemStore.get(GameItemEnum.INSECT).toString()));

        DrawArea playerItemsDrawArea = new DrawArea("""
                ┌─────────────────────────┐
                │  Items:                 │
                │                  =      │
                │                  =      │
                │                  =      │
                │                         │
                └─────────────────────────┘
                """);
        DrawArea itemsNameDrawArea = new DrawArea("""
                Q [Quill]
                I [Inkwell]
                M [Manuscript]
                """);
        itemsNameDrawArea.setColor(ColorsEnum.YELLOW);
        playerItemsDrawArea.drawAt(4, 2, itemsNameDrawArea);
        playerItemsDrawArea.drawCenteredX(22, 24, 2, new DrawArea(gameItemStore.get(GameItemEnum.QUILL).toString()));
        playerItemsDrawArea.drawCenteredX(22, 24, 3, new DrawArea(gameItemStore.get(GameItemEnum.INKWELL).toString()));
        playerItemsDrawArea.drawCenteredX(22, 24, 4, new DrawArea(gameItemStore.get(GameItemEnum.MANUSCRIPT).toString()));
        int spacing = 5;
        drawArea.drawAt(0, 0, playerResourcesDrawArea);
        drawArea.drawAt(drawArea.getWidth() + spacing, 0, playerItemsDrawArea);

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
