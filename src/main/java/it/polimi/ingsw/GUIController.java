package it.polimi.ingsw;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

import static it.polimi.ingsw.App.DEFAULT_HEIGTH;
import static it.polimi.ingsw.App.DEFAULT_WIDTH;


public class GUIController {

    @FXML
    private StackPane rootPane;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private ImageView foregroundImage;

    @FXML
    private Button createGameButton;

    @FXML
    private Button joinGameButton;

    @FXML
    private Button quitButton;

    private Stage stage;

    private Scene scene;

    private Parent root;

    @FXML
    public void initialize() {
        System.out.println("Initializing GUIController");

        if (backgroundImage == null) {
            System.out.println("backgroundImage is not initialized!");
        } else {
            // Bind the background image size to the size of the StackPane
            backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
            backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());
            System.out.println("backgroundImage initialized successfully");
        }
    }

    public void createGame() {
        // create game
        System.out.println("Game created");
    }

    public void joinGame() {
        // join game
        System.out.println("Game joined");
    }

    public void quit() {
        //quit app
        quitButton.setOnMouseClicked(mouseEvent -> {
            quitButton.getStyleClass().add("button-clicked");
            quitButton.setOnMouseReleased(mouseEvent2 -> quitButton.getStyleClass().remove("button-clicked"));
            System.out.println("App quit");
            Platform.exit();
        });
    }


    // Method to populate the game list
    //TODO
    /*
    public void populateGameList(ArrayList<GameRecord> games) {
        gameListView.getItems().clear();
        gameListView.getItems().addAll(games);
    }
*/

    public void refreshList() {
        //refresh list
        System.out.println("List refreshed");
    }

    public void switchToGamesView(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("fxml2.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGTH);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToHomeView(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("fxml1.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGTH);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}