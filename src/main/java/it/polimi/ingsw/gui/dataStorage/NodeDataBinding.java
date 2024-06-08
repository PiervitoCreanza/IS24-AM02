package it.polimi.ingsw.gui.dataStorage;


import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.beans.PropertyChangeSupport;
import java.util.Objects;

public class NodeDataBinding {
    Node currentlyDisplayedNode;
    Pane boundNode;

    PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public NodeDataBinding(Pane boundNode) {
        this.boundNode = boundNode;
    }

    protected void update(Node newNode) {
        if (!Objects.equals(currentlyDisplayedNode, newNode)) {
            currentlyDisplayedNode = newNode;
            boundNode.getChildren().clear();
            if (newNode != null) {
                boundNode.getChildren().add(newNode);
            }
        }
    }

    public Node getCurrentlyDisplayedNode() {
        return currentlyDisplayedNode;
    }


}
