package it.polimi.ingsw.gui.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatSidebarTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file for the chat sidebar
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChatSidebar.fxml"));
        Parent chatSidebar = loader.load();

        // Create a scene with the chat sidebar
        Scene scene = new Scene(chatSidebar, 300, 600);

        // Set up the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chat Sidebar Test");
        primaryStage.show();
    }
}