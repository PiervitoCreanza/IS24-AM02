package it.polimi.ingsw.gui;

import it.polimi.ingsw.gui.controllers.Controller;
import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.client.connection.Connection;
import it.polimi.ingsw.tui.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.List;


public class GUIApp extends Application implements View {
    private static final Logger logger = LogManager.getLogger(GUIApp.class);
    public static int MIN_WIDTH = 1560;
    public static int MIN_HEIGHT = 900;
    public static ClientNetworkControllerMapper networkControllerMapper;
    public static Connection connection;

    public void instanceGUI(ClientNetworkControllerMapper networkControllerMapper, Connection connection) {
        GUIApp.networkControllerMapper = networkControllerMapper;
        GUIApp.connection = connection;
    }

    @Override
    public void launchUI() {
        launch();

    }

    @Override
    public void init() {
        logger.info("GUIApp started");
        loadFonts();
    }

    @Override
    public void start(Stage stage) throws IOException {

        // Set the stage to close the application when the window is closed.
        stage.setOnCloseRequest(event -> {
            logger.info("GUIApp quit");
            Platform.exit();
            System.exit(0);
        });
        // Set the stage title and dimensions.
        stage.setTitle("Codex Naturalis");
        stage.setMinHeight(MIN_HEIGHT);
        stage.setMinWidth(MIN_WIDTH);
        //stage.setFullScreen(true);

        // Set the current stage on all controllers.
        Controller.setStage(stage);
        logger.info("Stage set up: {}", stage);

        URL fxml1URL = getClass().getResource("/HomeScene.fxml");
        if (fxml1URL == null) {
            throw new IOException("Cannot load resource: HomeScene.fxml");
        }
        FXMLLoader loader = new FXMLLoader(fxml1URL);

        Parent root = loader.load();

        Scene scene = new Scene(root, MIN_WIDTH, MIN_HEIGHT);

        // Set the stage to the current scene.
        stage.setScene(scene);

        // Make the network controller mapper available to all controllers.
        stage.getScene().getProperties().put("networkControllerMapper", networkControllerMapper);

        // Set the current scene on all controllers.
        Controller.setScene(scene);

        // Get the controller from the loader.
        Controller controller = loader.getController();

        // Call the beforeShow method of the controller.
        // This method is used to perform actions right before the window is shown.
        controller.beforeMount();

        stage.show();

        Platform.runLater(() -> connection.connect());
    }

    private void loadFonts() {
        Font medievalSharpBold = Font.loadFont(getClass().getResourceAsStream("/fonts/MedievalSharp-Bold.ttf"), 10);
        Font medievalSharpBoldOblique = Font.loadFont(getClass().getResourceAsStream("/fonts/MedievalSharp-BoldOblique.ttf"), 10);
        Font medievalSharpBook = Font.loadFont(getClass().getResourceAsStream("/fonts/MedievalSharp-Book.ttf"), 10);
        Font medievalSharpBookOblique = Font.loadFont(getClass().getResourceAsStream("/fonts/MedievalSharp-BookOblique.ttf"), 10);
        Font calSansSemiBold = Font.loadFont(getClass().getResourceAsStream("/fonts/CalSans-SemiBold.otf"), 10);

        List<Font> fonts = List.of(medievalSharpBold, medievalSharpBoldOblique, medievalSharpBook, medievalSharpBookOblique, calSansSemiBold);
        for (Font font : fonts) {
            if (font != null) {
                logger.debug("Font loaded: {}", font.getName());
            }
        }
    }
}