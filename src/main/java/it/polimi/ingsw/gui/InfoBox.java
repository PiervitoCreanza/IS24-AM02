package it.polimi.ingsw.gui;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class InfoBox extends Popup {

    private final Stage stage;

    private final VBox box;

    private Point2D sceneTopLeft;

    public InfoBox(Stage stage, String color, String toastTitle, String toastDescription) {
        super();
        this.stage = stage;
        // Load the toast FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Toast.fxml"));

        try {
            box = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        // Set the toast color. Supported colors are "green" and "red".
        box.getStyleClass().add(color);

        // Set the title and message of the toast
        Label title = (Label) box.lookup("#toastTitle");
        Label messageLabel = (Label) box.lookup("#toastMessage");
        title.setText(toastTitle);
        messageLabel.setText(toastDescription);

        getContent().add(box);

        stage.widthProperty().addListener((obs, oldVal, newVal) -> setWidth(newVal.doubleValue()));
        stage.heightProperty().addListener((obs, oldVal, newVal) -> setHeight(newVal.doubleValue()));
        stage.xProperty().addListener((obs, oldVal, newVal) -> setX(getSceneTopLeft().getX()));
        stage.yProperty().addListener((obs, oldVal, newVal) -> setY(getRelativeY() + getSceneTopLeft().getY()));
    }

    private void privateShowBox(int y) {
        // Posiziona il Popup in alto a sinistra della scena
        super.show(stage, getSceneTopLeft().getX(), getSceneTopLeft().getY() + y);

        // Crea un'animazione di transizione che sposta il Popup da sinistra a destra
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.75), box);
        transition.setFromY(box.getHeight() / 2);
        transition.setToY(0);

        // Crea un'animazione di fade in
        FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(0.75), box);
        fadeInTransition.setFromValue(0.0);
        fadeInTransition.setToValue(1.0);

        transition.play();
        fadeInTransition.play();

        // Crea un'animazione di fade out
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), box);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);

        // Crea un Timeline per nascondere il Popup dopo 5 secondi
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), evt -> {
            fadeOutTransition.play();
            fadeOutTransition.setOnFinished(e -> hide());
        }));
        timeline.play();
    }

    public void showBox() {
        privateShowBox(0);
    }

    public void showBoxBelow(InfoBox infoBox) {
        privateShowBox((int) (infoBox.getRelativeY() + infoBox.getHeight()));
    }

    private Point2D getSceneTopLeft() {
        sceneTopLeft = stage.getScene().getRoot().localToScreen(10, 10);
        return sceneTopLeft;
    }

    private int getRelativeY() {
        return (int) this.getY() - (int) sceneTopLeft.getY();
    }
}
