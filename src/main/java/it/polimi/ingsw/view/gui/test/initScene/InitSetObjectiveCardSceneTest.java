package it.polimi.ingsw.view.gui.test.initScene;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class is a test for the InitSetObjectiveCardScene.
 * It extends the Application class to create a JavaFX application.
 */
public class InitSetObjectiveCardSceneTest extends Application {

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
        // Load the FXML file for InitSetObjectiveCardScene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/initSetObjectiveCardScene.fxml"));
        Parent root = loader.load();

        // Set the properties for the primary stage
        primaryStage.setTitle("Init Set Objective Card Scene");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinHeight(900);
        primaryStage.setMinWidth(1200);
        primaryStage.show();
    }
}