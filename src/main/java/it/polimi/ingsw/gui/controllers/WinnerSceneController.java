package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.network.virtualView.PlayerView;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static javafx.geometry.Pos.CENTER_RIGHT;

public class WinnerSceneController extends Controller {
    private static final ControllersEnum NAME = ControllersEnum.WINNER_SCENE;

    @FXML
    private VBox leaderboard;

    /**
     * Returns the name of the controller.
     *
     * @return the name of the controller.
     */
    @Override
    public ControllersEnum getName() {
        return NAME;
    }

    /**
     * This method is called before switching to a new scene.
     * It should be overridden by the subclasses to perform any necessary operations before switching to a new scene.
     */
    @Override
    public void beforeUnmount() {

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
        GameControllerView updatedView = (GameControllerView) evt.getNewValue();
        // Sort the player views by player position
        List<PlayerView> sortedPlayerViews = new ArrayList<>(updatedView.gameView().playerViews());
        sortedPlayerViews.sort(Comparator.comparing(PlayerView::playerPos).reversed());
        sortedPlayerViews.forEach(playerView -> {
            boolean isWinner = updatedView.gameView().winners().contains(playerView.playerName());
            // Create a new player row
            createPlayerRow(playerView.playerPos(), playerView.playerName(), isWinner);
        });

    }

    @FXML
    private void backToMainMenu() {
        networkControllerMapper.sendDisconnect();
        switchScene(ControllersEnum.MAIN_MENU);
    }

    @FXML
    private void quit() {
        networkControllerMapper.sendDisconnect();
        System.exit(0);
    }

    private void createPlayerRow(int playerPos, String playerName, boolean isWinner) {
        /* Expected result:
           <HBox styleClass="winner-row">
               <Text styleClass="winner-name" text="Player Name"/>
               <HBox styleClass="winner-points-container">
                   <Text styleClass="winner-points" text="Player Points"/>
               </HBox>
           </HBox>
         */
        HBox row = new HBox();
        row.getStyleClass().add("winner-row");

        // Add a border to the winner row
        if (isWinner) row.getStyleClass().add("winner-border");

        leaderboard.getChildren().add(row);
        Text name = new Text(playerName);
        name.getStyleClass().add("winner-name");
        row.getChildren().add(name);
        HBox posContainer = new HBox();
        posContainer.setAlignment(CENTER_RIGHT);
        row.getChildren().add(posContainer);

        HBox.setHgrow(posContainer, Priority.ALWAYS);
        Text pos = new Text(playerPos + " points");
        pos.getStyleClass().add("winner-points");
        posContainer.getChildren().add(pos);
    }
}
