package it.polimi.ingsw.view.gui.components;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class represents a ZoomableScrollPane which extends the ScrollPane class from JavaFX.
 * It allows for zooming and panning on the target node.
 */
public class ZoomableScrollPane extends ScrollPane {
    /**
     * The scale value of the target node.
     */
    private double scaleValue = 0.7;
    /**
     * The intensity of the zoom.
     */
    private final double zoomIntensity = 0.02;
    /**
     * The target node.
     */
    private final Node target;
    /**
     * The zoom node.
     */
    private final Group zoomNode;
    /**
     * Logger for the ZoomableScrollPane class, used to log debug and error messages.
     */
    private static final Logger logger = LogManager.getLogger(ZoomableScrollPane.class);

    /**
     * Constructor for the ZoomableScrollPane class.
     *
     * @param target The node that will be zoomed and panned.
     */
    public ZoomableScrollPane(Node target) {
        super();
        this.target = target;
        this.zoomNode = new Group(target);
        setContent(outerNode(zoomNode));

        setPannable(true);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setFitToHeight(true); //center
        setFitToWidth(true); //center
        setHvalue(0.5);
        setVvalue(0.5);
        updateScale();
    }

    /**
     * This method wraps the node in a VBox and sets the scroll event.
     *
     * @param node The node to be wrapped.
     * @return The wrapped node.
     */
    private Node outerNode(Node node) {
        Node outerNode = centeredNode(node);
        outerNode.setOnScroll(e -> {
            e.consume();
            onScroll(e.getDeltaY(), new Point2D(e.getX(), e.getY()));
        });
        return outerNode;
    }

    /**
     * This method wraps the node in a VBox and centers it.
     *
     * @param node The node to be wrapped.
     * @return The wrapped and centered node.
     */
    private Node centeredNode(Node node) {
        VBox vBox = new VBox(node);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    /**
     * This method updates the scale of the target node.
     */
    private void updateScale() {
        target.setScaleX(scaleValue);
        target.setScaleY(scaleValue);
    }

    /**
     * This method calculates the space cut from the top of the content.
     *
     * @return The space cut from the top.
     */
    private double getSpaceCut() {
        double vvalue = this.getVvalue();
        double contentHeight = this.getContent().getBoundsInLocal().getHeight(); // get the total height of the content

        return vvalue * contentHeight;
    }

    /**
     * This method handles the scroll event and updates the scale of the target node.
     *
     * @param wheelDelta The amount of scroll.
     * @param mousePoint The point where the scroll event occurred.
     */
    private void onScroll(double wheelDelta, Point2D mousePoint) {
        double zoomFactor = Math.exp(wheelDelta * zoomIntensity);

        Bounds innerBounds = target.getLayoutBounds();
        Bounds viewportBounds = getViewportBounds();

        double minScaleValue = Math.min(viewportBounds.getWidth() / innerBounds.getWidth(), viewportBounds.getHeight() / innerBounds.getHeight());

        scaleValue = Math.max(minScaleValue, scaleValue * zoomFactor);
        scaleValue = Math.min(1.5, scaleValue);

        double previousVvalue = getVvalue();
        double previousHvalue = getHvalue();

        updateScale();
        this.layout(); // refresh ScrollPane scroll positions & target bounds

        this.setVvalue(previousVvalue);
        this.setHvalue(previousHvalue);
    }
}