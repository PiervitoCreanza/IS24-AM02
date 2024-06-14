package it.polimi.ingsw.gui;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class InfoBox extends Popup {

    private final Stage stage;

    private final VBox box;

    private Point2D sceneTopLeft;

    public InfoBox(Stage stage, String message) {
        super();
        this.stage = stage;
        // Crea un VBox per contenere il messaggio
        box = new VBox();
        box.getStyleClass().add("popup-box");
        box.getStylesheets().add(getClass().getResource("/alertStyles.css").toExternalForm());
        // Crea un Label per il messaggio
        Label label = new Label(message);
        box.getChildren().add(label);
        // Aggiungi il VBox al Popup
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

        // Crea un Timeline per nascondere il Popup dopo 3 secondi
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> {
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
