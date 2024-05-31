package it.polimi.ingsw.gui;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ZoomableScrollPane extends ScrollPane {
    private double scaleValue = 0.7;
    private final double zoomIntensity = 0.02;
    private final Node target;
    private final Group zoomNode;
    private static final Logger logger = LogManager.getLogger(ZoomableScrollPane.class);

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

    private Node outerNode(Node node) {
        Node outerNode = centeredNode(node);
        outerNode.setOnScroll(e -> {
            e.consume();
            onScroll(e.getDeltaY(), new Point2D(e.getX(), e.getY()));
        });
        return outerNode;
    }

    private Node centeredNode(Node node) {
        VBox vBox = new VBox(node);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private void updateScale() {
        target.setScaleX(scaleValue);
        target.setScaleY(scaleValue);
    }

    private double getSpaceCut() {
        double vvalue = this.getVvalue();
        double contentHeight = this.getContent().getBoundsInLocal().getHeight(); // get the total height of the content

        double topSpaceCut = vvalue * contentHeight;

        System.out.println("Top space cut by the ScrollPane: " + topSpaceCut);
        return topSpaceCut;
    }

    private void onScroll(double wheelDelta, Point2D mousePoint) {
        double zoomFactor = Math.exp(wheelDelta * zoomIntensity);

        Bounds innerBounds = target.getLayoutBounds();
        Bounds viewportBounds = getViewportBounds();

        double minScaleValue = Math.min(viewportBounds.getWidth() / innerBounds.getWidth(), viewportBounds.getHeight() / innerBounds.getHeight());

        scaleValue = Math.max(minScaleValue, scaleValue * zoomFactor);
        scaleValue = Math.min(1.5, scaleValue);
        //System.out.println("minScaleValue: " + minScaleValue + " scaleValue: " + scaleValue);

        double previousVvalue = getVvalue();
        double previousHvalue = getHvalue();
        double initialMouseYPosition = getSpaceCut() + mousePoint.getY();

        updateScale();
        this.layout(); // refresh ScrollPane scroll positions & target bounds

        double finalMouseYPosition = getSpaceCut() + mousePoint.getY();
        double actualDeltaY = finalMouseYPosition - initialMouseYPosition;
        //double deltaY = initialMouseYPosition * scaleValue - initialMouseYPosition;
        this.setVvalue(previousVvalue);
        this.setHvalue(previousHvalue);
        getSpaceCut();

    }
}


