package it.polimi.ingsw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


//TODO: REBASE
public class App extends Application {
    public static int DEFAULT_WIDTH = 1920;
    public static int DEFAULT_HEIGTH = 1080;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxml1.fxml"));
        Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGTH);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}