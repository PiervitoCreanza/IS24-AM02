package it.polimi.ingsw.model.corner;

import it.polimi.ingsw.model.GameResource;
import java.util.Optional;

/**
 * Class for the Corner that contains resource
 */

public class CornerResource extends Corner{
    GameResource gameResource;
    public CornerResource(GameResource gameResource) {
        this.gameResource = gameResource;
    }

    /**
     * This method returns the game resource in the corner.
     * If the corner is covered, it returns an empty Optional.
     * Otherwise, it returns an Optional containing the game resource.
     * @return Optional containing the game resource otherwise an empty Optional.
     */

    @Override
    public Optional<GameResource> getGameResource() {
        return this.isCovered ? Optional.empty() : Optional.ofNullable(this.gameResource);
    }
}
