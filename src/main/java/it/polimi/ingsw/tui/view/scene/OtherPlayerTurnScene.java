package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.EndPhaseComponent;
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
 * It implements the Scene and UserInputScene interfaces.
 */
public class OtherPlayerTurnScene implements Scene, PropertyChangeListener {

    /**
     * The DrawArea object where the scene will be drawn.
     */
    private final DrawArea drawArea;

    /**
     * The controller that will handle the scene.
     */
    private final TUIViewController controller;

    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(OtherPlayerTurnScene.class);

    /**
     * The menu handler.
     */
    private final MenuHandler menuHandler;

    /**
     * Constructs a new OtherPlayerTurnScene.
     * It initializes the drawArea and the menuHandler.
     * It also initializes the scene with the given parameters.
     *
     * @param controller               the controller for this scene
     * @param playerName               the name of the player whose turn it is
     * @param playerColor              the color of the player whose turn it is
     * @param playerBoard              the board of the player whose turn it is
     * @param isLastRound              whether it is the last round of the game
     * @param remainingRoundsToEndGame the number of rounds remaining to end the game
     */
    public OtherPlayerTurnScene(TUIViewController controller, String playerName, PlayerColorEnum playerColor, HashMap<Coordinate, GameCard> playerBoard, boolean isLastRound, int remainingRoundsToEndGame) {
        this.controller = controller;
        this.drawArea = new DrawArea();
        if (isLastRound) {
            DrawArea endPhaseArea = new EndPhaseComponent(playerName, playerColor, true, remainingRoundsToEndGame).getDrawArea();
            this.drawArea.drawAt(0, 0, endPhaseArea);
        } else {
            this.drawArea.drawAt(0, 0, "It's");
            this.drawArea.drawAt(this.drawArea.getWidth() + 1, 0, playerName, playerColor.getColor());
            this.drawArea.drawAt(this.drawArea.getWidth(), 0, "'s turn!");
        }
        this.drawArea.drawAt(this.drawArea.getWidth(), this.drawArea.getHeight() + 1, new PlayerBoardComponent(playerBoard));
        this.menuHandler = new MenuHandler(this,
                new MenuItem("c", "chat", new EmptyCommand()),
                new MenuItem("q", "quit", new EmptyCommand())
        );
    }

    /**
     * This method is used to display the object.
     * It prints the drawArea and the menuHandler.
     */
    @Override
    public void display() {
        this.drawArea.println();
        menuHandler.print();
    }

    /**
     * @param input the input string
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
            case "q" -> {
                controller.sendDisconnect();
                controller.closeConnection();
            }
            default -> logger.error("Invalid property change event");
        }
    }
}
