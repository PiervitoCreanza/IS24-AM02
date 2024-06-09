package it.polimi.ingsw.gui.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PointsPanelTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carica il file FXML per PointsPanel
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GameScene.fxml"));
        Parent root = loader.load();

        // Crea una scena con il PointsPanel
        Scene scene = new Scene(root, 400, 400);

        // Carica il CSS
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // Configura e mostra lo stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Points Panel Test");
        primaryStage.show();
    }
}