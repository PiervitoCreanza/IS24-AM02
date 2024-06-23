package it.polimi.ingsw.gui.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class is a test for the PointsPanel.
 * It extends the Application class to create a JavaFX application.
 */
public class PointsPanelTest extends Application {

    /**
     * The main method that launches the JavaFX application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method that is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * @param primaryStage the primary stage for this application.
     * @throws Exception if an error occurs while loading the FXML file or creating the scene.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file for PointsPanel
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GameScene.fxml"));
        Parent root = loader.load();

        // Create a scene with the PointsPanel
        Scene scene = new Scene(root, 1400, 800);

        // Load the CSS
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // Set the properties for the primary stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Points Panel Test");
        primaryStage.show();
    }
}