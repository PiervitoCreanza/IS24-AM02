package it.polimi.ingsw.model.corner;

import it.polimi.ingsw.model.GameResource;

/**
 * Class for the Corner that contains resource
 */

public class CornerResource extends Corner{
    GameResource gameResource;
    public CornerResource(GameResource gameResource) {
        this.gameResource = gameResource;
    }
    @Override
    public GameResource getGameResource() {
        return gameResource;
    }
}
