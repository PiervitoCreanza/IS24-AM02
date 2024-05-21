package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.player.playerBoard.PlayerBoardComponent;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.MenuHandler;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.MenuItem;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.commands.EmptyCommand;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

/**
 * This class represents the scene for when it is another player's turn.
 * It implements the Scene interface, meaning it can be displayed in the UI.
 */
public class OtherPlayerTurnScene implements Scene, PropertyChangeListener {
    /**
     * The DrawArea object where the scene will be drawn.
     */
    private final DrawArea drawArea;

    private final TUIViewController controller;

    private static final Logger logger = LogManager.getLogger(OtherPlayerTurnScene.class);
    private final MenuHandler menuHandler;

    /**
     * Constructs a new OtherPlayerTurnScene.
     *
     * @param playerName  the name of the player whose turn it is
     * @param playerColor the color of the player whose turn it is
     * @param playerBoard the board of the player whose turn it is
     */
    public OtherPlayerTurnScene(TUIViewController controller, String playerName, PlayerColorEnum playerColor, HashMap<Coordinate, GameCard> playerBoard) {
        this.controller = controller;
        this.drawArea = new DrawArea();
        this.drawArea.drawAt(0, 0, "It's");
        this.drawArea.drawAt(this.drawArea.getWidth() + 1, 0, playerName, playerColor.getColor());
        this.drawArea.drawAt(this.drawArea.getWidth(), 0, "'s turn!");
        this.drawArea.drawAt(this.drawArea.getWidth(), this.drawArea.getHeight() + 1, new PlayerBoardComponent(playerBoard));
        this.menuHandler = new MenuHandler(this,
                new MenuItem("c", "chat", new EmptyCommand()),
                new MenuItem("q", "quit", new EmptyCommand())
        );
    }

    /**
     * This method is used to display the object.
     */
    @Override
    public void display() {
        this.drawArea.println();
        menuHandler.print();
    }

    /**
     * @param input
     */
    @Override
    public void handleUserInput(String input) {
        menuHandler.handleInput(input);
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
            case "c" -> controller.showChat();
            case "q" -> controller.selectScene(ScenesEnum.MAIN_MENU);
            default -> logger.error("Invalid property change event");
        }
    }
}
