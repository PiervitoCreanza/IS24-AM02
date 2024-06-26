package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.components.toast.ToastLevels;
import it.polimi.ingsw.view.gui.controllers.Controller;
import it.polimi.ingsw.view.tui.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * Class representing the GUI application.
 * It extends the JavaFX Application class and implements the View interface.
 */
public class GUIApp extends Application implements View {
    /**
     * Logger for this class.
     */
    private static final Logger logger = LogManager.getLogger(GUIApp.class);

    /**
     * Minimum width of the application window.
     */
    public static int MIN_WIDTH = 1560;

    /**
     * Minimum height of the application window.
     */
    public static int MIN_HEIGHT = 900;

    /**
     * Method to launch the UI.
     * It calls the launch method of the Application class.
     * If the screen size is too small, it sets the property prism.allowhidpi to false.
     * This is done to prevent the application from being rendered too big on small screens.
     */
    @Override
    public void launchUI() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        logger.debug("Screen size is {}x{}", screenSize.width, screenSize.height);
        if (screenSize.width < MIN_WIDTH || screenSize.height < MIN_HEIGHT) {
            System.setProperty("prism.allowhidpi", "false");
        }
        launch();
    }

    /**
     * Method to initialize the application.
     * It logs that the application has started and loads the fonts.
     */
    @Override
    public void init() {
        logger.info("GUIApp started");
        loadFonts();
    }

    /**
     * Method to start the application.
     * It sets up the stage and loads the home scene.
     *
     * @param stage the primary stage
     * @throws IOException if there is an error loading the home scene
     */
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
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icon_512x512.png")));

        // Set the current stage on all controllers.
        Controller.setStage(stage);

        URL fxml1URL = getClass().getResource("/HomeScene.fxml");
        if (fxml1URL == null) {
            throw new IOException("Cannot load resource: HomeScene.fxml");
        }
        FXMLLoader loader = new FXMLLoader(fxml1URL);

        // Load the scene
        Parent root = loader.load();

        Scene scene = new Scene(root, MIN_WIDTH, MIN_HEIGHT);

        String fullScreenButton;
        // Set the full screen button based on the OS.
        if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
            fullScreenButton = "F11";
        } else {
            fullScreenButton = "Cmd+F";
        }
        // Show a toast message when entering full screen.
        stage.fullScreenProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Controller.showToast(ToastLevels.INFO, "Full screen", "Press " + fullScreenButton + " to exit full screen");
            }
        });
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        // Set full screen shortcut
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (System.getProperty("os.name").toLowerCase().contains("mac") && event.isMetaDown() && event.getCode() == KeyCode.F) {
                stage.setFullScreen(!stage.isFullScreen());
            } else if (event.getCode() == KeyCode.F11) {
                stage.setFullScreen(!stage.isFullScreen());
            }
        });

        // Set the stage to the current scene.
        stage.setScene(scene);

        // Set the current scene on all controllers.
        Controller.setScene(scene);

        // Get the controller from the loader.
        Controller controller = loader.getController();

        // Call the beforeShow method of the controller.
        // This method is used to perform actions right before the window is shown.
        controller.beforeMount(null);

        stage.show();
    }

    /**
     * Method to load the fonts.
     * It loads the MedievalSharp-Book and CalSans-SemiBold fonts.
     */
    private void loadFonts() {
        Font.loadFont(getClass().getResourceAsStream("/fonts/MedievalSharp-Book.ttf"), 10);
        Font.loadFont(getClass().getResourceAsStream("/fonts/CalSans-SemiBold.ttf"), 10);
    }
}