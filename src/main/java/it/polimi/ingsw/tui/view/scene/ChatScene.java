package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.network.server.message.ChatServerToClientMessage;
import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import static it.polimi.ingsw.tui.utils.Utils.ANSI_BLUE;
import static it.polimi.ingsw.tui.utils.Utils.ANSI_RESET;

public class ChatScene implements Scene {
    private final DrawArea drawArea;
    private final TUIViewController controller;
    private final UserInputHandler receiverHandler = new UserInputHandler("Enter receiver: ", Objects::nonNull);
    private final UserInputHandler messageHandler = new UserInputHandler("Enter message: ", input -> !input.isEmpty());
    private final String playerName;

    private enum ChatStatus {
        HANDLE_INPUT,
        DIRECT,
        GLOBAL,
        SEND_MESSAGE
    }

    private ChatStatus currentStatus = ChatStatus.HANDLE_INPUT;

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
        this.drawArea.drawNewLine("""
                Press <d> to send direct message.
                Press <g> to send global message.
                Press <q> to quit.
                """, 0);
    }

    @Override
    public void display() {
        this.drawArea.println();
    }

    public void handleUserInput(String input) {
        if (currentStatus == ChatStatus.HANDLE_INPUT) {
            switch (input.toLowerCase()) {
                case "d" -> {
                    currentStatus = ChatStatus.DIRECT;
                    receiverHandler.print();
                    return;
                }
                case "g" -> {
                    currentStatus = ChatStatus.GLOBAL;
                    messageHandler.print();
                    return;
                }
                case "q" -> {
                    controller.closeChat();
                    // We need to go back to the previous scene (PlaceCardScene or DrawCardScene)
                    return;
                }
                default -> System.out.println("Invalid input");
            }
        }
        if (currentStatus == ChatStatus.SEND_MESSAGE) {
            handleMessage(input);
        }
        if (currentStatus == ChatStatus.DIRECT) {
            handleReceiver(input);
        }
        if (currentStatus == ChatStatus.GLOBAL) {
            handleGlobalMessage();
        }

    }


    private void handleMessage(String input) {
        if (input.equals("q")) {
            // Go back to action chooser
            currentStatus = ChatStatus.HANDLE_INPUT;
            return;
        }
        if (messageHandler.validate(input)) {
            controller.sendMessage(messageHandler.getInput(), receiverHandler.getInput());
            currentStatus = ChatStatus.HANDLE_INPUT;
        } else {
            receiverHandler.print();
        }
    }


    private void handleGlobalMessage() {
        currentStatus = ChatStatus.SEND_MESSAGE;
        receiverHandler.validate("");
    }


    private void handleReceiver(String input) {
        if (input.equals("q")) {
            // Go back to action chooser
            currentStatus = ChatStatus.HANDLE_INPUT;
            return;
        }
        if (receiverHandler.validate(input)) {
            currentStatus = ChatStatus.SEND_MESSAGE;
        } else {
            receiverHandler.print();
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
