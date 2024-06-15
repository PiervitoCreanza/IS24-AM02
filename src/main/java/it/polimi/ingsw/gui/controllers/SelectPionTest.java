package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.gui.toast.Toaster;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SelectPionTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carica il file FXML per PointsPanel
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SelectPionScene.fxml"));
        Parent root = loader.load();

        // Crea una scena con il PointsPanel
        Scene scene = new Scene(root, 400, 400);

        // Carica il CSS
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // Configura e mostra lo stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Select Pion Test");
        primaryStage.show();

        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    int finalI = i;
                    Platform.runLater(() -> Toaster.getInstance(primaryStage).showToast("green", String.valueOf(finalI), ""));
                    Thread.sleep(1000);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();


    }
}