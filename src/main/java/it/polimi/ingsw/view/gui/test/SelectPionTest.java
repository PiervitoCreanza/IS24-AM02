package it.polimi.ingsw.view.gui.test;

import it.polimi.ingsw.view.gui.components.toast.ToastLevels;
import it.polimi.ingsw.view.gui.components.toast.Toaster;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class is a test for the SelectPion scene.
 * It extends the Application class to create a JavaFX application.
 */
public class SelectPionTest extends Application {

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SelectPionScene.fxml"));
        Parent root = loader.load();

        // Create a scene with the PointsPanel
        Scene scene = new Scene(root, 400, 400);

        // Load the CSS
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // Configure and show the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Select Pion Test");
        primaryStage.show();

        // Create a new thread to show toast messages
        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    int finalI = i;
                    Platform.runLater(() -> Toaster.getInstance(primaryStage).showToast(ToastLevels.SUCCESS, String.valueOf(finalI), ""));
                    Thread.sleep(1000);
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }).start();
    }
}