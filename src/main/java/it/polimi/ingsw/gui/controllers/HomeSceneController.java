package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;

import static it.polimi.ingsw.gui.GUIApp.DEFAULT_HEIGTH;
import static it.polimi.ingsw.gui.GUIApp.DEFAULT_WIDTH;


public class HomeSceneController extends Controller implements PropertyChangeListener {

    public static final ControllersEnum NAME = ControllersEnum.HOME;
    private final static Logger logger = LogManager.getLogger(HomeSceneController.class);
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
    private ClientNetworkControllerMapper networkControllerMapper;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private ProgressIndicator progressIndicator;
    private ArrayList<GameRecord> gamesList;

    @FXML
    private ListView<String> gameListView;

    @FXML
    public void initialize() {
        logger.debug("Initializing HomeSceneController");

        if (backgroundImage == null) {
            logger.debug("backgroundImage is not initialized!");
        } else {
            // Bind the background image size to the size of the StackPane
            backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
            backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());
            logger.debug("backgroundImage initialized successfully");
        }

    }

    public void beforeMount() {
        if (this.networkControllerMapper == null) {
            this.networkControllerMapper = getProperty("networkControllerMapper");
            this.networkControllerMapper.addPropertyChangeListener(this);
        }
    }

    public void createGame() {
        // create game
        logger.debug("Game created");

    }

    public void joinGame() {
        // join game
        logger.debug("Game joined");
    }

    public void quit() {
        //quit app
        quitButton.setOnMouseClicked(mouseEvent -> {
            quitButton.getStyleClass().add("button-clicked");
            quitButton.setOnMouseReleased(mouseEvent2 -> quitButton.getStyleClass().remove("button-clicked"));
            logger.debug("GUIApp quit");
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
        logger.debug("List refreshed");
    }

    public void enterGame() {
        logger.debug("Game entered");
    }

    public void showLoadingScreen(StackPane rootPane) {
        this.progressIndicator = new ProgressIndicator();
        // Add the ProgressIndicator to the root StackPane
        rootPane.getChildren().add(progressIndicator);
    }

    public void hideLoadingScreen(StackPane rootPane) {
        // Remove the ProgressIndicator from the root StackPane
        rootPane.getChildren().remove(this.progressIndicator);
    }

    public void switchToGamesView(ActionEvent event) throws IOException {
        switchScene(ControllersEnum.GAMES_LIST);


    }

    public void switchToHomeView(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("fxml1.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGTH);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void displayDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String changedProperty = evt.getPropertyName();
        switch (changedProperty) {
            case "CONNECTION_ESTABLISHED":
                logger.debug("Connection established");
                break;
            case "CONNECTION_CLOSED":
                displayDialog("Connection closed", "The connection to the server has been closed.");
                logger.debug("Connection closed");
                break;
        }
    }

    @Override
    public ControllersEnum getName() {
        return NAME;
    }

    /**
     *
     */
    @Override
    public void beforeUnmount() {
        // TODO

    }
}