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
        //setHvalue(0.5);
        //setVvalue(0.5);
        updateScale();
    }

    private Node outerNode(Node node) {
        Node outerNode = centeredNode(node);
        outerNode.setOnScroll(e -> {
            e.consume();
            onScroll(e.getDeltaY(), new Point2D(e.getX(), e.getY()));
        });

        outerNode.setOnDragDetected(e -> {
            System.out.println(getSpaceCut());
        });
        return outerNode;
    }

    private Node centeredNode(Node node) {
        VBox vBox = new VBox(node);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private void updateScale() {
//        System.out.println(this.getWidth() + " " + scaleValue * target.getBoundsInLocal().getWidth() + " " + scaleValue);
//        double maxSide = Math.max(this.getWidth(), this.getHeight());
//        // TODO: Improve zoom constraints handling
//        if (this.getWidth() > scaleValue * maxSide) {
//            scaleValue = maxSide / (target.getBoundsInLocal().getWidth() + 40);
//        }
        target.setScaleX(scaleValue);
        target.setScaleY(scaleValue);
    }

    private double getMouseDistanceFromTop(Point2D mousePoint) {
        Node content = this.getContent(); // get the content of the scroll pane

        // Get the content's bounds in scene coordinates
        Bounds contentBoundsInScene = content.localToScene(content.getBoundsInLocal());

        // Get the y-coordinate of the top corner of the content
        double contentTopY = contentBoundsInScene.getMinY();

        logger.debug("contentTopY: {}. Distance: {}", contentTopY, mousePoint.getY() - contentTopY);
        return mousePoint.getY() - contentTopY;
    }

    private double getSpaceCut() {
        double vvalue = this.getVvalue();
        double contentHeight = this.getContent().getBoundsInLocal().getHeight(); // get the total height of the content

        double topSpaceCut = vvalue * contentHeight;

        System.out.println("Top space cut by the ScrollPane: " + topSpaceCut);
        return topSpaceCut;
    }

    private void onScroll(double wheelDelta, Point2D mousePoint) {
        getSpaceCut();
        double zoomFactor = Math.exp(wheelDelta * zoomIntensity);

        Bounds innerBounds = zoomNode.getBoundsInParent();
        Bounds viewportBounds = getViewportBounds();
        double initialMouseDistance = getMouseDistanceFromTop(mousePoint);
        double initialScaleValue = scaleValue;

        logger.debug("initial mouseDistance: {}. Top: {}", initialMouseDistance, getMouseDistanceFromTop(mousePoint));
//        System.out.println("innerBounds:" + innerBounds.getMinY() + " " + innerBounds.getHeight());
//        System.out.println("viewportBounds:" + viewportBounds.getMinY());

        // calculate pixel offsets from [0, 1] range
        double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
        double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());

        scaleValue = scaleValue * zoomFactor;

        updateScale();
        this.layout(); // refresh ScrollPane scroll positions & target bounds
        double distanceVariation = initialMouseDistance * (scaleValue - initialScaleValue);
        logger.debug("updated mouseDistance: {}. Initial vValue: {}. Final vValue: {}", distanceVariation, getVvalue(), getVvalue() + distanceVariation / viewportBounds.getHeight());
        getSpaceCut();
        double res = getSpaceCut() + distanceVariation;
        System.out.println("Adjusted space cut: " + getSpaceCut() + " " + distanceVariation + " " + res);

        // Get the total pixel height of the content
        double contentHeight = this.getContent().getBoundsInLocal().getHeight();

// Get the pixel height of the viewport
        double viewportHeight = this.getViewportBounds().getHeight();

// Calculate the pixel value of one scroll unit
        double pixelPerScrollUnit = contentHeight / viewportHeight;

        // Calculate the scroll value equivalent to the desired pixel amount
        double scrollValue = distanceVariation * pixelPerScrollUnit;
        System.out.println("Scroll value: " + scrollValue);
        this.setVvalue(this.getVvalue() - scrollValue);

        //logger.debug("goal distance: {}", );
        //this.setVvalue(getVvalue() + distanceVariation / viewportBounds.getHeight());
        logger.debug("Real mouseDistance: {} Top pos: {}", getMouseDistanceFromTop(mousePoint), zoomNode.getBoundsInParent().getMinY());

        // convert target coordinates to zoomTarget coordinates
//        Point2D posInZoomTarget = target.parentToLocal(zoomNode.parentToLocal(mousePoint));
//
//        // calculate adjustment of scroll position (pixels)
//        Point2D adjustment = target.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));
//
//        // convert back to [0, 1] range
//        // (too large/small values are automatically corrected by ScrollPane)
//        Bounds updatedInnerBounds = zoomNode.getBoundsInParent();
//        System.out.println("corrected value" + (mouseDistance * zoomFactor - mouseDistance));
//        this.setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
//        this.setVvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
    }
}

