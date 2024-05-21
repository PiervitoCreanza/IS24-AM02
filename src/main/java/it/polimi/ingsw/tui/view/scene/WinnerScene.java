package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.network.virtualView.PlayerView;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import it.polimi.ingsw.tui.view.component.PlayerComponent;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.MenuHandler;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.MenuItem;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.commands.EmptyCommand;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;

public class WinnerScene implements Scene, PropertyChangeListener {
    /**
     * The DrawArea object where the scene will be drawn.
     */
    private final DrawArea drawArea;

    /**
     * The controller that manages the user interface and the game logic.
     */
    private final TUIViewController controller;

    private static final Logger logger = LogManager.getLogger(WinnerScene.class);

    private final MenuHandler menuHandler;

    /**
     * Constructs a new MainMenuScene.
     *
     * @param controller the controller for this scene
     */
    public WinnerScene(TUIViewController controller, List<String> winners, List<PlayerView> players, int spacing) {
        this.controller = controller;
        this.drawArea = new DrawArea();
        DrawArea titleDrawArea = new DrawArea();
        DrawArea winnersDrawArea = new DrawArea();
        DrawArea losersDrawArea = new DrawArea();

        HashMap<PlayerColorEnum, ColorsEnum> colors = new HashMap<>();
        colors.put(PlayerColorEnum.RED, ColorsEnum.BRIGHT_RED);
        colors.put(PlayerColorEnum.BLUE, ColorsEnum.BRIGHT_BLUE);
        colors.put(PlayerColorEnum.GREEN, ColorsEnum.BRIGHT_GREEN);
        colors.put(PlayerColorEnum.YELLOW, ColorsEnum.BRIGHT_YELLOW);


        for (PlayerView player : players) {
            if (winners.contains(player.playerName())) {
                winnersDrawArea.drawAt((winnersDrawArea.getWidth() == 0) ? 0 : winnersDrawArea.getWidth() + spacing, 0, String.valueOf(new PlayerComponent(player.playerName(), player.playerPos())), colors.get(player.color()));
            } else {
                losersDrawArea.drawAt((losersDrawArea.getWidth() == 0) ? 0 : losersDrawArea.getWidth() + spacing, 0, String.valueOf(new PlayerComponent(player.playerName(), player.playerPos())), colors.get(player.color()));
            }
        }

        int widthMax = Math.max(winnersDrawArea.getWidth(), losersDrawArea.getWidth());
        titleDrawArea.drawAt(0, 0, new TitleComponent("Game Over", widthMax));

        this.drawArea.drawAt(0, 0, titleDrawArea);
        this.drawArea.drawAt(0, drawArea.getHeight(), (winners.size() == 1) ? "The winner is:" : "The winners are:", ColorsEnum.BRIGHT_GREEN);
        this.drawArea.drawCenteredX(drawArea.getHeight(), winnersDrawArea);
        this.drawArea.drawAt(0, drawArea.getHeight() + 1, (players.size() - winners.size() == 1) ? "Other player:" : "Other players:");
        this.drawArea.drawCenteredX(drawArea.getHeight(), losersDrawArea);
        this.menuHandler = new MenuHandler(this,
                new MenuItem("q", "quit", new EmptyCommand())
        );
    }

    /**
     * This method is used to display the scene to the user.
     */
    @Override
    public void display() {
        this.drawArea.println();
        menuHandler.print();
    }

    /**
     * This method is used to handle user input.
     *
     * @param input the user's input
     */
    public void handleUserInput(String input) {
        menuHandler.handleInput(input);
        controller.setIsGameOver(false);
    }

    public DrawArea getDrawArea() {
        return drawArea;
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
        if (changedProperty.equals("q")) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
        } else {
            logger.error("Invalid property change event");
        }
    }
}
