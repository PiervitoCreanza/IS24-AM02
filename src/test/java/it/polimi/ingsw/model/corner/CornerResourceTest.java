package it.polimi.ingsw.model.corner;

import it.polimi.ingsw.model.GameResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CornerResourceTest {
    private CornerResource cornerResource;
    private GameResource gameResource;

    @BeforeEach
    public void setup() {
        cornerResource = new CornerResource(gameResource);
    }

    @Test
    public void getGameResourceReturnsCorrectResourceWhenNotCovered() {
        assertEquals(Optional.of(gameResource), cornerResource.getGameResource());
    }

    @Test
    public void getGameResourceReturnsEmptyWhenCovered() {
        cornerResource.setCovered(true);
        assertEquals(Optional.empty(), cornerResource.getGameResource());
    }
}