package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.RepresentedGame;

public class NetworkCommandMapper {
    private MainController mainController;

    public NetworkCommandMapper(MainController mainController) {
        this.mainController = mainController;
    }


    private static ClientCommandMessage jsonToMessageObjBuilder(String jsonString) {

        //Create Game Message
        CreateGameMessage createGameMessage = CreateGameMessage.createGameMessageFromJson(jsonString);
        if (createGameMessage != null) {
            //check if the Create Game Message is effectively a Create Game message, avoiding Gson filling the object with default values when no valid fields are found in the incoming JSON?
            return createGameMessage;
        }
        //Join Game message
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
        System.out.println(parsedMessage); //print Message Type on Console
        if (parsedMessage == null) {
            //out.println(parsedMessage.toString()); //Print to remote
            //out.println("{\"message : 'ok' }\""); //Print to remote
            return "{\"message\" : \"ko, WRONG\"}";
        }

        //using instanceof to check the type of the parsed message, used for RMI also
        //HAVE TO CHECK FIRST IF THEY ARE CHILDREN AND THEN PARENT BECAUSE OF EXTENDS in CREATEGAMEMESSAGE
        if (parsedMessage instanceof CreateGameMessage msg) {
            Game game = null;
            try {
                game = mainController.createGame(msg.gameName, msg.nPlayers, msg.playerName);

                String gameString = new RepresentedGame(game).toJSON();
                return gameString;
            } catch (Exception e) {
                return answerMsgToJSON(e.getMessage());
            }
        } else if (parsedMessage instanceof JoinGameMessage msg) {
            Game game = null;
            try {
                game = mainController.joinGame(msg.gameName, msg.playerName);
            } catch (Exception e) {
                return answerMsgToJSON(e.getMessage());
            }
        } else if (parsedMessage instanceof ChosenCardMessage msg) {
            return answerMsgToJSON("chosenCardMessage");
        }


        return "{\"message\" : \"ok\"}";
    }

    private static String answerMsgToJSON(String answer) {
        return "{\"message\" : \"" + answer + "\"}";
    }
    //Per la CLI produco tutto sulle faccine, su Mac funziona
}
