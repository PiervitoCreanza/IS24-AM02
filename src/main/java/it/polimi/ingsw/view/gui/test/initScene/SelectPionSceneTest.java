package it.polimi.ingsw.view.gui.test.initScene;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Test class for loading and displaying the Init Select Pion scene using JavaFX.
 * This class extends Application and serves as an entry point to launch the scene.
 */
public class SelectPionSceneTest extends Application {

    /**
     * The main method to launch the application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start method called by the JavaFX application thread.
     * Loads the FXML file and sets up the primary stage to display the scene.
     *
     * @param primaryStage The primary stage for the application.
     * @throws Exception If there is an error loading the FXML file.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/InitSelectPionScene.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Select Pion Scene");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinHeight(900);
        primaryStage.setMinWidth(1200);
        primaryStage.show();
    }
}