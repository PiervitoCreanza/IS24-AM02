package it.polimi.ingsw.tui.view.component.player.playerItems;

import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.drawer.Drawable;

/**
 * This class represents a player's items component in the game.
 * It implements the Drawable interface, meaning it can be drawn on the screen.
 */
public class PlayerItemsComponent implements Drawable {

    /**
     * The draw area of the player's items component.
     */
    private final DrawArea drawArea;

    /**
     * Constructor for the PlayerItemsComponent class.
     * It initializes the drawArea and populates it with player resources and items.
     *
     * @param gameItemStore A GameItemStore object representing the player's items.
     * @param spacing       The spacing between the player resources and items.
     */
    public PlayerItemsComponent(GameItemStore gameItemStore, int spacing) {
        drawArea = new DrawArea();
        drawArea.drawAt(0, 0, playerResourcesComponent(gameItemStore));
        drawArea.drawAt(drawArea.getWidth() + spacing, 0, playerItemsComponent(gameItemStore));
    }

    /**
     * This method creates a DrawArea for the player's resources.
     *
     * @param gameItemStore A GameItemStore object representing the player's items.
     * @return A DrawArea object representing the player's resources.
     */
    private DrawArea playerResourcesComponent(GameItemStore gameItemStore) {
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
        return playerResourcesDrawArea;
    }

    /**
     * This method creates a DrawArea for the player's items.
     *
     * @param gameItemStore A GameItemStore object representing the player's items.
     * @return A DrawArea object representing the player's items.
     */
    private DrawArea playerItemsComponent(GameItemStore gameItemStore) {
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
        return playerItemsDrawArea;
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
     * Returns the string representation of the drawable object.
     *
     * @return the string representation of the drawable object.
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
