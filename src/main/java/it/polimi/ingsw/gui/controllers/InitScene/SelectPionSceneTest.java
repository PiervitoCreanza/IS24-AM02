package it.polimi.ingsw.gui.controllers.InitScene;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SelectPionSceneTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/selectPionScene.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Select Pion Scene");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinHeight(900);
        primaryStage.setMinWidth(1200);
        primaryStage.show();
    }
}