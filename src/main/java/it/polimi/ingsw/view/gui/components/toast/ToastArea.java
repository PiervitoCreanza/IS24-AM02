package it.polimi.ingsw.view.gui.components.toast;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.function.Consumer;

/**
 * This class represents a ToastArea in the application.
 * It extends Popup, which means it can be added to any container that accepts nodes.
 * It manages the display of Toast notifications in a specific area of the application.
 */
public class ToastArea extends Popup {

    /**
     * The stage on which the Toasts will be displayed.
     */
    private final Stage stage;
    /**
     * The container for the Toasts.
     */
    private final VBox container;
    /**
     * The callback to be executed when a Toast is hidden.
     */
    private final Consumer<Toast> onHideCallback;
    /**
     * The top left point of the scene.
     */
    private Point2D sceneTopLeft;

    /**
     * Constructs a new ToastArea.
     *
     * @param stage          the stage on which the Toasts will be displayed.
     * @param onHideCallback the callback to be executed when a Toast is hidden.
     */
    public ToastArea(Stage stage, Consumer<Toast> onHideCallback) {
        super();
        this.stage = stage;
        this.onHideCallback = onHideCallback;
        this.getScene().getRoot().setStyle("-fx-background-color: null;");

        // Add listeners to stage properties to update the size and position of the ToastArea
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            setWidth(newVal.doubleValue());
            redrawToastArea();
        });
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            setHeight(newVal.doubleValue());
            redrawToastArea();
        });
        stage.xProperty().addListener((obs, oldVal, newVal) -> setX(getSceneTopLeft().getX()));
        stage.yProperty().addListener((obs, oldVal, newVal) -> setY(getRelativeY() + getSceneTopLeft().getY()));

        // Initialize the container for the Toasts
        container = new VBox();
        container.setSpacing(20);
        getContent().add(container);
        super.show(stage, getSceneTopLeft().getX(), getSceneTopLeft().getY());
    }

    private void redrawToastArea() {
        super.hide();
        super.show(stage, getSceneTopLeft().getX(), getSceneTopLeft().getY());
    }

    /**
     * Adds a new Toast to the ToastArea and starts its display animation.
     *
     * @param toast the Toast to be added.
     */
    public void addToast(Toast toast) {
        redrawToastArea();
        Node toastNode = toast.getToastNode();
        container.getChildren().add(toastNode);

        // Create a transition animation that moves the Popup from left to right
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.75), toastNode);
        transition.setFromY(toast.getHeight() / 2);
        transition.setToY(0);

        // Create a fade in animation
        FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(0.75), toastNode);
        fadeInTransition.setFromValue(0.0);
        fadeInTransition.setToValue(1.0);

        transition.play();
        fadeInTransition.play();

        // Create a fade out animation
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), toastNode);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);

        // Start the fade out animation after a delay
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), evt -> {
            fadeOutTransition.play();
            fadeOutTransition.setOnFinished(e -> {
                // Remove the Toast from the container when the fade out animation finishes
                container.getChildren().remove(toastNode);
                onHideCallback.accept(toast);
            });
        }));
        timeline.play();
    }

    /**
     * Returns the top left point of the scene.
     *
     * @return the top left point of the scene.
     */
    private Point2D getSceneTopLeft() {
        sceneTopLeft = stage.getScene().getRoot().localToScreen(10, 10);
        return sceneTopLeft;
    }

    /**
     * Returns the relative Y position of the ToastArea.
     *
     * @return the relative Y position of the ToastArea.
     */
    private int getRelativeY() {
        return (int) this.getY() - (int) sceneTopLeft.getY();
    }
}