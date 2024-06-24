package it.polimi.ingsw.model.card.gameCard;

import javafx.beans.property.SimpleBooleanProperty;

import java.io.*;

/**
 * This class extends SimpleBooleanProperty and implements Serializable interface.
 * It is used to create a serializable version of SimpleBooleanProperty.
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

    @Serial
    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeBoolean(get());
    }

    @Serial
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        set(s.readBoolean());
    }
}