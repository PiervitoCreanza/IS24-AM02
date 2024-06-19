package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.server.ServerMessageHandler;
import it.polimi.ingsw.network.server.ServerNetworkControllerMapper;
import it.polimi.ingsw.network.server.message.ServerActionEnum;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

public class ServerNetworkControllerMapperTest {

    private ServerNetworkControllerMapper serverNetworkControllerMapper;

    private ArrayList<ServerMessageHandler> handlers;

    private HashMap<Integer, HashMap<Integer, Integer>> starterCardsIds;

    private HashMap<Integer, HashMap<Integer, Integer>> objectiveCardsIds;

    private HashMap<Integer, HashMap<Integer, HashSet<Coordinate>>> availableCoordinates;

    private HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> hand;

    private HashMap<Integer, ArrayList<Integer>> field;

    private HashMap<Integer, HashMap<Integer, Boolean>> error;

    private HashMap<Integer, HashMap<Integer, Boolean>> isPlayerTurn;

    private HashMap<Integer, Boolean> gameEnded;

    private Random random;

    private final List<Thread> threads = new ArrayList<>();

    private final int nPlayers = 4;

    private final int nGames = 1500;


    @BeforeEach
    void setUp() {
        serverNetworkControllerMapper = new ServerNetworkControllerMapper(new MainController());
        handlers = new ArrayList<>();
        starterCardsIds = new HashMap<>();
        objectiveCardsIds = new HashMap<>();
        availableCoordinates = new HashMap<>();
        hand = new HashMap<>();
        field = new HashMap<>();
        error = new HashMap<>();
        isPlayerTurn = new HashMap<>();
        gameEnded = new HashMap<>();
        random = new Random();
    }

    @Test
    void playGames() {
        for (int i = 0; i < nGames; i++) {
            int finalI = i;
            Thread thread = new Thread(() ->
            {
                String gameName = String.valueOf(finalI);
                String firstPlayerName = String.valueOf(1);
                ServerMessageHandler firstPlayerHandler = initMockServerMessageHandler(gameName, firstPlayerName);
                handlers.add(firstPlayerHandler);
                serverNetworkControllerMapper.getGames(firstPlayerHandler);
                serverNetworkControllerMapper.createGame(firstPlayerHandler, gameName, firstPlayerName, nPlayers);
                String playerName;
                for (int j = 2; j < nPlayers + 1; j++) {
                    playerName = String.valueOf(j);
                    ServerMessageHandler playerHandler = initMockServerMessageHandler(gameName, playerName);
                    handlers.add(playerHandler);
                    serverNetworkControllerMapper.joinGame(playerHandler, gameName, playerName);
                }
                ArrayList<PlayerColorEnum> availableColors = new ArrayList<>(List.of(PlayerColorEnum.values()));
                for (int j = 1; j < nPlayers + 1; j++) {
                    playerName = String.valueOf(j);
                    serverNetworkControllerMapper.placeCard(gameName, playerName, new Coordinate(0, 0), starterCardsIds.get(Integer.parseInt(gameName)).get(Integer.parseInt(playerName)), false);
                    serverNetworkControllerMapper.choosePlayerColor(gameName, playerName, availableColors.get(j - 1));
                    serverNetworkControllerMapper.setPlayerObjective(gameName, playerName, objectiveCardsIds.get(Integer.parseInt(gameName)).get(Integer.parseInt(playerName)));
                }
                while (!gameEnded.get(Integer.parseInt(gameName))) {
                    for (int j = 1; j < nPlayers + 1; j++) {
                        for (int k = j + 1; k < nPlayers + 1; k++) {
                            int finalK = k;
                            Thread thread1 = new Thread(() ->{
                               randomAction(gameName, String.valueOf(finalK));
                            });
                            threads.add(thread1);
                            thread1.start();
                        }
                        playerName = String.valueOf(j);
                        ArrayList<Coordinate> availableCoordinatesList = new ArrayList<>(availableCoordinates.get(Integer.parseInt(gameName)).get(Integer.parseInt(playerName)));
                        ArrayList<Integer> handList = new ArrayList<>(hand.get(Integer.parseInt(gameName)).get(Integer.parseInt(playerName)));
                        do {
                            Coordinate coordinate = availableCoordinatesList.get(random.nextInt(availableCoordinatesList.size()));
                            Integer cardId = handList.get(random.nextInt(handList.size()));
                            serverNetworkControllerMapper.placeCard(gameName, playerName, coordinate, cardId, random.nextBoolean());
                        } while (error.get(Integer.parseInt(gameName)).get(Integer.parseInt(playerName)));
                        if (gameEnded.get(Integer.parseInt(gameName)))
                            break;
                        if (isPlayerTurn.get(Integer.parseInt(gameName)).get(Integer.parseInt(playerName))) {
                            do {
                                switch (random.nextInt(3)) {
                                    case 0 -> {
                                        if (!field.get(Integer.parseInt(gameName)).isEmpty()) {
                                            Integer cardId = field.get(Integer.parseInt(gameName)).get(random.nextInt(field.get(Integer.parseInt(gameName)).size()));
                                            serverNetworkControllerMapper.drawCardFromField(gameName, playerName, cardId);
                                        }
                                    }
                                    case 1 -> serverNetworkControllerMapper.drawCardFromGoldDeck(gameName, playerName);
                                    case 2 -> serverNetworkControllerMapper.drawCardFromResourceDeck(gameName, playerName);
                                }
                            } while (error.get(Integer.parseInt(gameName)).get(Integer.parseInt(playerName)));
                        }
                    }
                }
                for (int j = 1; j < nPlayers + 1; j++) {
                    playerName = String.valueOf(j);
                    serverNetworkControllerMapper.disconnect(gameName, playerName);
                }
            });
            threads.add(thread);
            thread.start();
        }
        for(Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void randomAction(String gameName, String playerName) {
        switch (random.nextInt(10)) {
            case 0 -> serverNetworkControllerMapper.getGames(Mockito.mock(ServerMessageHandler.class));
            case 1 -> serverNetworkControllerMapper.createGame(Mockito.mock(ServerMessageHandler.class), gameName, playerName, random.nextInt(10));
            case 2 -> serverNetworkControllerMapper.joinGame(Mockito.mock(ServerMessageHandler.class), gameName, playerName);
            case 3 -> serverNetworkControllerMapper.placeCard(gameName, playerName, new Coordinate(random.nextInt(100), random.nextInt(100)), random.nextInt(200), random.nextBoolean());
            case 4 -> serverNetworkControllerMapper.choosePlayerColor(gameName, playerName, PlayerColorEnum.values()[random.nextInt(PlayerColorEnum.values().length)]);
            case 5 -> serverNetworkControllerMapper.setPlayerObjective(gameName, playerName, random.nextInt(200));
            case 6 -> serverNetworkControllerMapper.drawCardFromField(gameName, playerName, random.nextInt(200));
            case 7 -> serverNetworkControllerMapper.drawCardFromGoldDeck(gameName, playerName);
            case 8 -> serverNetworkControllerMapper.drawCardFromResourceDeck(gameName, playerName);
            case 9 -> serverNetworkControllerMapper.switchCardSide(gameName, playerName, random.nextInt(200));
        }
    }

    private ServerMessageHandler initMockServerMessageHandler(String gameName, String playerName) {
        ServerMessageHandler serverMessageHandler = Mockito.mock(ServerMessageHandler.class);
        when(serverMessageHandler.getGameName()).thenReturn(gameName);
        when(serverMessageHandler.getPlayerName()).thenReturn(playerName);
        Mockito.doAnswer(invocation -> {
            if (error.get(Integer.parseInt(gameName)) == null)
                initHashMap(Integer.parseInt(gameName));
            ServerToClientMessage message = invocation.getArgument(0);
            if (message.getServerAction().equals(ServerActionEnum.UPDATE_VIEW)) {
                error.get(Integer.parseInt(gameName)).put(Integer.parseInt(playerName), false);
                starterCardsIds.get(Integer.parseInt(gameName)).put(Integer.parseInt(playerName), message.getView().getPlayerViewByName(playerName).starterCard().getCardId());
                objectiveCardsIds.get(Integer.parseInt(gameName)).put(Integer.parseInt(playerName), message.getView().getPlayerViewByName(playerName).choosableObjectives().getFirst().getCardId());
                availableCoordinates.get(Integer.parseInt(gameName)).put(Integer.parseInt(playerName), message.getView().getPlayerViewByName(playerName).playerBoardView().availablePositions());
                hand.get(Integer.parseInt(gameName)).put(Integer.parseInt(playerName), message.getView().getPlayerViewByName(playerName).playerHandView().hand().stream().map(GameCard::getCardId).collect(Collectors.toCollection(ArrayList::new)));
                ArrayList<Integer> fieldCards = message.getView().gameView().globalBoardView().fieldGoldCards().stream().map(GameCard::getCardId).collect(Collectors.toCollection(ArrayList::new));
                fieldCards.addAll(message.getView().gameView().globalBoardView().fieldResourceCards().stream().map(GameCard::getCardId).collect(Collectors.toCollection(ArrayList::new)));
                field.put(Integer.parseInt(gameName), fieldCards);
                isPlayerTurn.get(Integer.parseInt(gameName)).put(Integer.parseInt(playerName), message.getView().isMyTurn(playerName));
                if (message.getView().gameStatus().equals(GameStatusEnum.GAME_OVER)) {
                    gameEnded.put(Integer.parseInt(gameName), true);
                    System.out.println("Game: " + gameName + "ended" + " Winner: " + message.getView().gameView().winners());
                }
                //System.out.println("Game: " + gameName + " Player: " + playerName + " Message: " + message + " Points: " + message.getView().getPlayerViewByName(playerName).playerPos());
            }
            if (message.getServerAction().equals(ServerActionEnum.ERROR_MSG)) {
                error.get(Integer.parseInt(gameName)).put(Integer.parseInt(playerName), true);
                //System.out.println("Game: " + gameName + " Player: " + playerName + " Message: " + message.getErrorMessage());
            }
            // Qui puoi definire il tuo comportamento personalizzato per il metodo sendMessage.
            // Ad esempio, potresti stampare il messaggio:
            //System.out.println("Game: " + gameName + " Player: " + playerName + " Message: " + message);
            return null; // dal momento che il metodo è void, ritorniamo null
        }).when(serverMessageHandler).sendMessage(Mockito.any(ServerToClientMessage.class));

        return serverMessageHandler;
    }

    private void initHashMap(int gameName) {
        error.put(gameName, new HashMap<>());
        starterCardsIds.put(gameName, new HashMap<>());
        objectiveCardsIds.put(gameName, new HashMap<>());
        availableCoordinates.put(gameName, new HashMap<>());
        hand.put(gameName, new HashMap<>());
        gameEnded.put(gameName, false);
        isPlayerTurn.put(gameName, new HashMap<>());
    }
}
