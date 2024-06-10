package it.polimi.ingsw.model.card.gameCard;

import javafx.beans.property.SimpleBooleanProperty;

import java.io.Serializable;

/**
 * This class extends SimpleBooleanProperty and implements Serializable interface.
 * It is used to create a serializable version of SimpleBooleanProperty.
 * SimpleBooleanProperty is a part of JavaFX and it does not implement Serializable interface by default.
 * So, this class is a workaround to serialize the boolean property.
 */
public class SerializableBooleanProperty extends SimpleBooleanProperty implements Serializable {

    /**
     * Constructor for SerializableBooleanProperty.
     * It calls the parent constructor with the provided value.
     *
     * @param value the initial value of the SimpleBooleanProperty
     */
    public SerializableBooleanProperty(Boolean value) {
        super(value);
    }
}