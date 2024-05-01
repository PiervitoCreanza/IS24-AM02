package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.MainController;

public class NetworkCommandMapper {
    private MainController mainController;

    public NetworkCommandMapper(MainController mainController) {
        this.mainController = mainController;
    }


    private static ClientCommandMessage jsonToMessageObjBuilder(String jsonString) {

        //
        //login message
        JoinGameMessage joinGameMessage = JoinGameMessage.joinGameFromJson(jsonString);
        if (joinGameMessage != null) {
            //check if the Game Message is effectively a join game message, avoiding Gson filling the object with default values when no valid fields are found in the incoming JSON?
            return joinGameMessage;
        }
        //chosen card message
        ChosenCardMessage chosenCardMessage = ChosenCardMessage.chosenCardMessageFromJson(jsonString);
        if (chosenCardMessage != null) {
            return chosenCardMessage;
        }

        return null;
    }

    public String parse(String jsonString) {
        ClientCommandMessage parsedMessage = jsonToMessageObjBuilder(jsonString);
        System.out.println(parsedMessage);
        if (parsedMessage == null) {
            //out.println(parsedMessage.toString()); //Print to remote
            //out.println("{\"message : 'ok' }\""); //Print to remote
            return "{\"message\" : \"ko\"}";
        }
        return "{\"message\" : \"ok\"}";
    }
}
