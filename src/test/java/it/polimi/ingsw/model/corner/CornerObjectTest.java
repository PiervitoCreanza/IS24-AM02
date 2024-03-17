package it.polimi.ingsw.model.corner;

import it.polimi.ingsw.model.GameObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class CornerObjectTest {
    private CornerObject cornerObject;
    private GameObject gameObject;

    @BeforeEach
    public void setup() {
        cornerObject = new CornerObject(gameObject);
    }

    @Test
    public void getGameObjectReturnsCorrectObjectWhenNotCovered() {
        assertEquals(Optional.of(gameObject), cornerObject.getGameObject());
    }

    @Test
    public void getGameObjectReturnsEmptyWhenCovered() {
        cornerObject.setCovered(true);
        assertEquals(Optional.empty(), cornerObject.getGameObject());
    }
}