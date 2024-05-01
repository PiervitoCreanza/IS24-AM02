package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.RepresentedGame;
import it.polimi.ingsw.network.messages.ChosenCardMessage;
import it.polimi.ingsw.network.messages.ClientCommandMessage;
import it.polimi.ingsw.network.messages.CreateGameMessage;
import it.polimi.ingsw.network.messages.JoinGameMessage;

/**
 * This class is responsible for mapping network commands.
 * Commands can be sent via netcat using the following syntax:
 * cat request.json | nc 192.168.1.75 12345
 * cat request.json | nc localhost 12345
 */
public class NetworkCommandMapper {
    /**
     * The main controller.
     */
    private MainController mainController;

    /**
     * Constructor for NetworkCommandMapper.
     *
     * @param mainController The main controller.
     */
    public NetworkCommandMapper(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * This method converts a JSON string to a ClientCommandMessage object.
     *
     * @param jsonString The JSON string to convert.
     * @return The converted ClientCommandMessage object, or null if the conversion fails.
     */
    private static ClientCommandMessage jsonToMessageObjBuilder(String jsonString) {
        // Attempt to convert the JSON string to a CreateGameMessage object.
        CreateGameMessage createGameMessage = null;
        try {
            createGameMessage = CreateGameMessage.createGameMessageFromJson(jsonString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (createGameMessage != null) {
            return createGameMessage;
        }

        // Attempt to convert the JSON string to a JoinGameMessage object.
        JoinGameMessage joinGameMessage = null;
        try {
            joinGameMessage = JoinGameMessage.joinGameFromJson(jsonString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (joinGameMessage != null) {
            return joinGameMessage;
        }

        // Attempt to convert the JSON string to a ChosenCardMessage object.
        ChosenCardMessage chosenCardMessage = ChosenCardMessage.chosenCardMessageFromJson(jsonString);
        if (chosenCardMessage != null) {
            return chosenCardMessage;
        }

        return null;
    }

    /**
     * This method parses a JSON string and performs an action based on its content.
     *
     * @param jsonString The JSON string to parse.
     * @return A response message indicating the result of the action.
     */
    public String parse(String jsonString) {
        ClientCommandMessage parsedMessage = jsonToMessageObjBuilder(jsonString);
        System.out.println(parsedMessage); // Print the type of the parsed message to the console.

        if (parsedMessage == null) {
            return "{\"message\" : \"ko, WRONG\"}";
        }

        // Perform an action based on the type of the parsed message.
        // We use 'instanceof' to determine the type of the parsed message. This approach is also used for RMI.
        // It's important to check child classes BEFORE the parent class due to inheritance in 'CreateGameMessage'.
        if (parsedMessage instanceof CreateGameMessage msg) {
            try {
                Game game = mainController.createGame(msg.getGameName(), msg.nPlayers, msg.getPlayerName());
                return new RepresentedGame(game).toJSON();
            } catch (Exception e) {
                return answerMsgToJSON(e.getMessage());
            }
        } else if (parsedMessage instanceof JoinGameMessage msg) {
            try {
                mainController.joinGame(msg.getGameName(), msg.getPlayerName());
            } catch (Exception e) {
                return answerMsgToJSON(e.getMessage());
            }
        } else if (parsedMessage instanceof ChosenCardMessage msg) {
            return answerMsgToJSON("chosenCardMessage");
        }

        return "{\"message\" : \"ok\"}";
    }

    /**
     * This method converts a response message to a JSON string.
     *
     * @param answer The response message to convert.
     * @return The converted JSON string.
     */
    private static String answerMsgToJSON(String answer) {
        return "{\"message\" : \"" + answer + "\"}";
    }
}