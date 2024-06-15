package it.polimi.ingsw.gui;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Timer;
import java.util.function.Consumer;

public class ToastArea extends Popup {

    private final Stage stage;
    private final VBox container;
    private final Object lock = new Object();
    private Point2D sceneTopLeft;
    private final Consumer<InfoBox> onHideCallback;

    public ToastArea(Stage stage, Consumer<InfoBox> onHideCallback) {
        super();
        this.stage = stage;
        this.onHideCallback = onHideCallback;
        this.getScene().getRoot().setStyle("-fx-background-color: null;");
        stage.widthProperty().addListener((obs, oldVal, newVal) -> setWidth(newVal.doubleValue()));
        stage.heightProperty().addListener((obs, oldVal, newVal) -> setHeight(newVal.doubleValue()));
        stage.xProperty().addListener((obs, oldVal, newVal) -> setX(getSceneTopLeft().getX()));
        stage.yProperty().addListener((obs, oldVal, newVal) -> setY(getRelativeY() + getSceneTopLeft().getY()));

        container = new VBox();
        container.setSpacing(20);
        //container.getChildren().add(new Label("Toast Area"));
        getContent().add(container);
        super.show(stage, getSceneTopLeft().getX(), getSceneTopLeft().getY());
    }

    public void addToast(InfoBox toast) {
        Node toastNode = toast.getToastNode();
        System.out.println("Adding toast to toast area");
        container.getChildren().add(toastNode);


        // Crea un'animazione di transizione che sposta il Popup da sinistra a destra
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.75), toastNode);
        transition.setFromY(toast.getHeight() / 2);
        transition.setToY(0);

        // Crea un'animazione di fade in
        FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(0.75), toastNode);
        fadeInTransition.setFromValue(0.0);
        fadeInTransition.setToValue(1.0);

        transition.play();
        fadeInTransition.play();

        // Crea un'animazione di fade out
        // Create a FadeTransition
        FadeTransition fadeTransition = new FadeTransition();
        // Set the delay of the fade transition to 3 seconds
        //fadeTransition.setDelay(Duration.seconds(3));


// Set the duration of the fade transition to 2 seconds
        fadeTransition.setDuration(Duration.seconds(.2));

// Set the node that the fade transition should be applied to
        fadeTransition.setNode(toastNode);

// Set the end opacity of the fade transition to 0
// This will make the node completely transparent
        fadeTransition.setToValue(0);

        Timer timer = new Timer();
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                synchronized (lock) {
                    fadeTransition.play();
                }

            }
        }, 3000);

// Add a listener to the scale transition to remove the node when the scale transition is finished
        fadeTransition.setOnFinished(event -> {
            synchronized (lock) {
                double toastHeight = toastNode.getBoundsInParent().getHeight();
                System.out.println("Removing toast from toast area " + toastHeight);
                container.getChildren().remove(toastNode);
                container.setTranslateY(container.getTranslateY() + toastHeight - 1);
                TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.75), container);
                translateTransition.setFromY(container.getTranslateY());
                translateTransition.setToY(0);
                translateTransition.play();
                translateTransition.setOnFinished(event1 -> onHideCallback.accept(toast));
            }

        });
    }

    private Point2D getSceneTopLeft() {
        sceneTopLeft = stage.getScene().getRoot().localToScreen(10, 10);
        return sceneTopLeft;
    }

    private int getRelativeY() {
        return (int) this.getY() - (int) sceneTopLeft.getY();
    }
}
