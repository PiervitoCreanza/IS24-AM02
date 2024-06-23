package it.polimi.ingsw.gui.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.InputStream;

/**
 * This class is a test for the WinnerScene.
 * It extends the Application class to create a JavaFX application.
 */
public class WinnerSceneTest extends Application {
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

        // Load the font from the resources
        InputStream is = WinnerSceneTest.class.getResourceAsStream("/MedievalSharp-Book.ttf");
        System.out.println(is);
        Font font = Font.loadFont(is, 20);
        System.out.println(font);

        // Load the FXML file for the WinnerScene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/WinnerScene.fxml"));
        Parent root = loader.load();

        // Set the properties for the primary stage
        primaryStage.setTitle("Winner Scene");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinHeight(900);
        primaryStage.setMinWidth(1500);
        primaryStage.show();
    }
}