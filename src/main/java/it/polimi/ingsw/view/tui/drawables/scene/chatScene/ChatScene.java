package it.polimi.ingsw.view.tui.drawables.scene.chatScene;

import it.polimi.ingsw.network.server.message.ChatServerToClientMessage;
import it.polimi.ingsw.view.tui.controller.TUIViewController;
import it.polimi.ingsw.view.tui.drawables.component.TitleComponent;
import it.polimi.ingsw.view.tui.drawables.component.userInputHandler.UserInputHandler;
import it.polimi.ingsw.view.tui.drawables.component.userInputHandler.menu.MenuHandler;
import it.polimi.ingsw.view.tui.drawables.component.userInputHandler.menu.MenuItem;
import it.polimi.ingsw.view.tui.drawables.component.userInputHandler.menu.commands.EmptyCommand;
import it.polimi.ingsw.view.tui.drawables.component.userInputHandler.menu.commands.UserInputChain;
import it.polimi.ingsw.view.tui.drawables.scene.Scene;
import it.polimi.ingsw.view.tui.drawer.DrawArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static it.polimi.ingsw.view.tui.utils.Utils.ANSI_BLUE;
import static it.polimi.ingsw.view.tui.utils.Utils.ANSI_RESET;

/**
 * This class represents the scene displayed when the player is in the chat.
 * It implements the Scene and PropertyChangeListener interfaces.
 */
public class ChatScene implements Scene, PropertyChangeListener {

    /**
     * The DrawArea object where the scene will be drawn.
     */
    private final DrawArea drawArea;

    /**
     * The controller that manages the user interface and the game logic.
     */
    private final TUIViewController controller;

    /**
     * The name of the player.
     */
    private final String playerName;

    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(ChatScene.class);

    /**
     * The menu handler for managing user input.
     */
    private final MenuHandler menuHandler;

    /**
     * Constructs a new ChatScene with the specified controller and list of messages.
     * The scene will display the messages in the order they are given.
     *
     * @param controller The controller that manages the user interface and the game logic
     * @param playerName The name of the player
     * @param messages   The list of messages to display
     */
    public ChatScene(TUIViewController controller, String playerName, ArrayList<ChatServerToClientMessage> messages) {
        this.controller = controller;
        this.playerName = playerName;
        this.drawArea = new DrawArea();
        DrawArea chatArea = new DrawArea();
        messages.stream()
                .map(message -> chatPrint(message.getPlayerName(), message.getChatMessage(), message.getTimestamp(), message.isDirectMessage()))
                .forEach(message -> chatArea.drawNewLine(message, 0));
        DrawArea titleArea = new TitleComponent("ChatScene", (chatArea.getWidth() <= 55) ? 55 : chatArea.getWidth()).getDrawArea();
        this.drawArea.drawAt(0, 0, titleArea);
        this.drawArea.drawAt(0, drawArea.getHeight(), chatArea);

        // Create the menu handler
        this.menuHandler = new MenuHandler(this,
                new MenuItem("d", "send" + ANSI_BLUE + " direct " + ANSI_RESET + "message",
                        new UserInputChain(
                                new UserInputHandler("Enter the recipient's username: ", input -> !input.isEmpty()),
                                new UserInputHandler("Enter the message: ", input -> !input.isEmpty())
                        )
                ),
                new MenuItem("g", "send global message",
                        new UserInputHandler("Enter the message: ", input -> !input.isEmpty()
                        )
                ),
                new MenuItem("q", "quit", new EmptyCommand())
        );

        this.drawArea.drawAt(0, drawArea.getHeight(), menuHandler.getDrawArea());
    }

    /**
     * Handles the user input.
     *
     * @param input The user input to handle
     */
    public void handleUserInput(String input) {
        menuHandler.handleInput(input);
    }

    /**
     * This method is used to display the object.
     * It prints the draw area.
     */
    @Override
    public void display() {
        this.drawArea.println();
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
        @SuppressWarnings("unchecked")
        ArrayList<String> inputs = (ArrayList<String>) evt.getNewValue();
        switch (changedProperty) {
            case "d" -> controller.sendMessage(inputs.get(1), inputs.get(0), true);
            case "g" -> controller.sendMessage(inputs.getFirst(), "global", false);
            case "q" -> controller.closeChat();
            default -> logger.error("Invalid property change event");
        }
    }

    /**
     * Returns the draw area of the place card scene.
     *
     * @return the draw area of the place card scene
     */
    public DrawArea getDrawArea() {
        return drawArea;
    }

    /**
     * Prints a chat message with the appropriate colors.
     *
     * @param sender          The sender of the message.
     * @param message         The content of the message.
     * @param timestamp       The timestamp when the message was created.
     * @param isDirectMessage Flag to indicate if the message is a direct message.
     * @return A string representation of the chat message.
     */
    private String chatPrint(String sender, String message, long timestamp, boolean isDirectMessage) {
        // If it's a direct message, print it in blue with the appropriate fields
        if (isDirectMessage)
            return getFormattedTimestamp(timestamp) + " " + ANSI_BLUE + (sender.equals(this.playerName) ? "You" : sender) + ": " + message + ANSI_RESET;
        // If it's not a direct message, print it as a normal message
        return getFormattedTimestamp(timestamp) + " " + (sender.equals(this.playerName) ? "You" : sender) + ": " + message;
    }

    /**
     * Converts a Unix timestamp into a formatted string.
     *
     * @param timestamp The Unix timestamp to convert.
     * @return The formatted string representation of the timestamp.
     */
    private String getFormattedTimestamp(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return dateTime.format(formatter);
    }
}
