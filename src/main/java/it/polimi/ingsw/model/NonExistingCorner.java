package it.polimi.ingsw.model;

/**
 * This class extends the Corner class and represents a non-existing corner in the game.
 * A non-existing corner is always covered and does not contain any game item.
 */
public class NonExistingCorner extends Corner{
    public NonExistingCorner( GameItemEnum gameItem) {
        super(true, gameItem);
    }

    /**
     * This method overrides the isExisting method in the Corner class.
     * It always returns false as the corner does not exist.
     * @return false as the corner does not exist
     */
    public boolean isExisting(){return false;}
}
