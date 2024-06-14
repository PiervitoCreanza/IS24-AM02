package it.polimi.ingsw.gui.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.InputStream;

public class WinnerSceneTest extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        InputStream is = WinnerSceneTest.class.getResourceAsStream("/MedievalSharp-Book.ttf");
        System.out.println(is);
        Font font = Font.loadFont(is, 20);
        System.out.println(font);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/WinnerScene.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Winner Scene");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinHeight(900);
        primaryStage.setMinWidth(1500);
        primaryStage.show();
    }
}
