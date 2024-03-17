package it.polimi.ingsw.model.corner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class EmptyCornerTest {
    private EmptyCorner emptyCorner;

    @BeforeEach
    public void setup() {
        emptyCorner = new EmptyCorner();
    }

    @Test
    public void getGameObjectReturnsEmptyOptional() {
        assertFalse(emptyCorner.getGameObject().isPresent());
    }

    @Test
    public void getGameResourceReturnsEmptyOptional() {
        assertFalse(emptyCorner.getGameResource().isPresent());
    }
}