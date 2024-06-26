package it.polimi.ingsw.view.gui.controllers.menu.credits;

import it.polimi.ingsw.view.gui.controllers.Controller;
import it.polimi.ingsw.view.gui.controllers.ControllersEnum;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * This class is the controller for the credits scene.
 */
public class CreditsSceneController extends Controller {

    /**
     * The logger.
     */
    private final Logger logger = LogManager.getLogger(CreditsSceneController.class);
    /**
     * The ListView for the GitHub links.
     */
    @FXML
    private ListView<String> githubListView;

    /**
     * Returns the name of the controller.
     *
     * @return the name of the controller.
     */
    @Override
    public ControllersEnum getName() {
        return ControllersEnum.CREDITS; // Return appropriate enum value
    }

    /**
     * This method is called before switching to a new scene.
     * It should be overridden by the subclasses to perform any necessary operations before switching to a new scene.
     */
    @Override
    public void beforeUnmount() {
        // Optional: Add any necessary clean-up code here
    }

    /**
     * This method is called before showing the scene.
     * It should be overridden by the subclasses to perform any necessary operations before showing the scene.
     * If the switchScene was caused by a property change, the event is passed as an argument.
     *
     * @param evt the property change event that caused the switch.
     */
    @Override
    public void beforeMount(PropertyChangeEvent evt) {
        // Populate the ListView with GitHub links
        List<String> githubLinks = Arrays.asList(
                "Piervito Creanza - https://github.com/PiervitoCreanza",
                "Mattia Colombo - https://github.com/mattiacolombomc",
                "Simone Curci - https://github.com/simonecurci",
                "Marco Febbo - https://github.com/Krema28"
        );
        Platform.runLater(() -> {
            githubListView.getItems().clear();
            githubListView.getItems().addAll(githubLinks);
        });
    }

    @FXML
    private void initialize() {
        githubListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GitHubListCell.fxml"));
                                HBox hbox = loader.load();
                                GitHubListCellController controller = loader.getController();
                                String[] parts = item.split(" - ");
                                controller.setGitHubInfo(parts[0], parts[1]);
                                setGraphic(hbox);
                            } catch (IOException e) {
                                String error = "Error loading cell";
                                logger.debug(error);
                                setText(error);
                            }
                        }
                    }
                };
            }
        });

        githubListView.setOnMouseClicked(event -> {
            String selectedItem = githubListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String url = selectedItem.split(" - ")[1];
                openUrl(url);
            }
        });
    }

    @FXML
    private void back(ActionEvent actionEvent) {
        switchScene(getPreviousLayoutName());
    }

    @FXML
    private void github(ActionEvent actionEvent) {
        openUrl("https://github.com/PiervitoCreanza/IS24-AM02/");
    }

    private void openUrl(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            logger.debug("Error opening URL: {}", url, e);
        }
    }
}
