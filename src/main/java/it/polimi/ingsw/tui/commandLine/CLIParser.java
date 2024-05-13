package it.polimi.ingsw.tui.commandLine;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.tui.controller.TUIViewController;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CLIParser extends Thread {
    private final ClientNetworkControllerMapper clientNetworkControllerMapper;
    private final TUIViewController controller;
    private final PropertyChangeSupport support;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public CLIParser(ClientNetworkControllerMapper clientNetworkControllerMapper, TUIViewController tuiController) {
        this.clientNetworkControllerMapper = clientNetworkControllerMapper;
        this.support = new PropertyChangeSupport(this);
        this.controller = tuiController;
    }

    public void run() {
        printTUIMenu();
//        try {
//            startCLI();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void createGame() throws IOException {
        String numPlayers = "";
        System.out.println("Enter game name to create: ");
        String gameName = reader.readLine();
        System.out.println("Enter number of players: ");
        numPlayers = reader.readLine();
        System.out.println("Enter player name: ");
        String playerName = reader.readLine();
        controller.setPlayerName(playerName);
        controller.setGameName(gameName);
        clientNetworkControllerMapper.createGame(gameName, playerName, Integer.parseInt(numPlayers));
    }

    public void joinGame() throws IOException {
        System.out.println("Enter game name to join: ");
        String gameName = reader.readLine();
        System.out.println("Enter player name: ");
        String playerName = reader.readLine();
        controller.setPlayerName(playerName);
        controller.setGameName(gameName);
        clientNetworkControllerMapper.joinGame(gameName, playerName);
    }

    private void startCLI() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String input = reader.readLine();
            switch (controller.getClientStatus()) {
                case MAIN_MENU -> {
                    parseMainMenu(input);
                }
                case GET_GAMES -> {
                    parseGetGames(input);
                }
            }

        }
    }

    private void parseMainMenu(String input) throws IOException {
        switch (input) {
            case "l" -> {
                clientNetworkControllerMapper.getGames();
            }
            case "c" -> {
                createGame();
            }
            case "j" -> {
                joinGame();
            }
            case "q" -> {
                System.out.println("Exiting...");
                System.exit(0);
            }
            default -> System.out.println("Invalid input");
        }
    }

    private void parseGetGames(String input) throws IOException {
        switch (input) {
            case "r" -> {
                clientNetworkControllerMapper.getGames();
            }
            case "c" -> {
                createGame();
            }
            case "j" -> {
                joinGame();
            }
            case "q" -> {
                System.out.println("Exiting...");
                System.exit(0);
            }
            default -> System.out.println("Invalid input");
        }
    }

    /**
     * Adds a PropertyChangeListener.
     *
     * @param listener the PropertyChangeListener to be added.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener f.
     *
     * @param listener the PropertyChangeListener to be removed.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    /**
     * Notifies of a message.
     *
     * @param message the message to be sent.
     */
    private void notify(String propertyName, Object message) {
        support.firePropertyChange(propertyName, null, message);
    }

    private void printTUIMenu() {
        //TODO: substitute rmiClientAdapter with value coming from mapper
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = null;
        String gameName = "";
        String playerName = "";

        try {
            while (!Objects.equals(input, "13")) {
                if (gameName.isEmpty()) {
                    System.out.print("1) Get games\n");
                    System.out.print("2) Create game\n");
                    System.out.print("3) Join game\n");
                } else {

                    System.out.print("5) Set StarterCard\n");
                    System.out.print("6) Choose player color\n");
                    System.out.print("7) Set ObjectiveCard \n");
                    System.out.print("8) Place card\n");
                    System.out.print("9) Draw card from field\n");
                    System.out.print("10) Draw card from resource deck\n");
                    System.out.print("11) Draw card from gold deck\n");
                    System.out.print("12) Show map\n");
                    System.out.print("13) Exit\n");
                }
                input = reader.readLine();

                String finalPlayerName = playerName;
                switch (input) {
                    case "1" -> {
                        clientNetworkControllerMapper.getGames();
                    }
                    case "2" -> {
                        String numPlayers = "";
                        System.out.println("Enter game name to create: ");
                        gameName = reader.readLine();
                        System.out.println("Enter number of players: ");
                        numPlayers = reader.readLine();
                        System.out.println("Enter player name: ");
                        playerName = reader.readLine();
                        support.firePropertyChange("playerName", null, playerName);
                        support.firePropertyChange("gameName", null, gameName);
                        clientNetworkControllerMapper.createGame(gameName, playerName, Integer.parseInt(numPlayers));
                    }
                    case "3" -> {
                        System.out.println("Enter game name to join: ");
                        gameName = reader.readLine();
                        System.out.println("Enter player name: ");
                        playerName = reader.readLine();
                        support.firePropertyChange("playerName", null, playerName);
                        support.firePropertyChange("gameName", null, gameName);
                        clientNetworkControllerMapper.joinGame(gameName, playerName);
                    }
                    case "5" -> {
                        //TODO: gather arguments on updated VirtualView
                        GameCard starterCard = clientNetworkControllerMapper.getView().gameView().playerViews().stream().filter(playerView -> playerView.playerName().equals(finalPlayerName)).findFirst().get().starterCard();
                        String choice = "0";
                        boolean side = false;
                        while (!choice.equals("2")) {
                            System.out.print("These are your starter card items: ");
                            starterCard.getCurrentSide().getGameItemStore().getNonEmptyKeys().forEach(System.out::print);
                            System.out.println();
                            System.out.println("1) Change side");
                            System.out.println("2) Place card");
                            choice = reader.readLine();
                            switch (choice) {
                                case "1" -> {
                                    side = !side;
                                    starterCard.switchSide();
                                }
                                case "2" -> {
                                    if (side) {
                                        starterCard.switchSide();
                                        clientNetworkControllerMapper.switchCardSide(gameName, playerName, starterCard);
                                    }

                                    clientNetworkControllerMapper.placeCard(gameName, playerName, new Coordinate(0, 0), starterCard);
                                }
                            }
                        }
                        System.out.println("Placed!");
                    }
                    case "6" -> {
                        System.out.println("Choose player color: RED, BLUE, GREEN, YELLOW");
                        String choice = reader.readLine();
                        PlayerColorEnum chosenColor;
                        switch (choice.toLowerCase()) {
                            case "red" -> chosenColor = PlayerColorEnum.RED;
                            case "blue" -> chosenColor = PlayerColorEnum.BLUE;
                            case "green" -> chosenColor = PlayerColorEnum.GREEN;
                            case "yellow" -> chosenColor = PlayerColorEnum.YELLOW;
                            default -> throw new IllegalStateException("Unexpected value: " + choice);
                        }
                        clientNetworkControllerMapper.choosePlayerColor(gameName, playerName, chosenColor);
                    }
                    case "7" -> {
                        ArrayList<ObjectiveCard> choosableObjectives = clientNetworkControllerMapper.getView().gameView().playerViews().stream().filter(playerView -> playerView.playerName().equals(finalPlayerName)).findFirst().get().choosableObjectives();
                        System.out.println("Choose an objective card: ");
                        System.out.println("1) " + choosableObjectives.getFirst());
                        System.out.println("2) " + choosableObjectives.getLast());
                        String choice = reader.readLine();
                        clientNetworkControllerMapper.setPlayerObjective(gameName, playerName, choosableObjectives.get(Integer.parseInt(choice) - 1));
                    }
                    case "8" -> {
                        ArrayList<GameCard> hand = null;
                        String choiceAction = "0";
                        int choiceCard;
                        ArrayList<Boolean> side = new ArrayList<Boolean>(List.of(false, false, false));
                        int x;
                        int y;
                        while (!choiceAction.equals("2")) {
                            //hand = printHand(finalPlayerName);
                            System.out.println("1) Change side");
                            System.out.println("2) Place card");
                            choiceAction = reader.readLine();
                            switch (choiceAction) {
                                case "1" -> {
                                    System.out.println("Of which card do you want to change the side?");
                                    choiceCard = Integer.parseInt(reader.readLine()) - 1;
                                    side.set(choiceCard, !side.get(choiceCard));
                                    hand.get(choiceCard).switchSide();
                                    System.out.println(hand.get(choiceCard).getCurrentSide());
                                }
                                case "2" -> {
                                    System.out.println("Of which card do you want to place?");
                                    choiceCard = Integer.parseInt(reader.readLine()) - 1;
                                    System.out.println("Where do you want to place it?");
                                    System.out.println("Coordniate x:");
                                    x = Integer.parseInt(reader.readLine());
                                    System.out.println("Coordniate y:");
                                    y = Integer.parseInt(reader.readLine());
                                    if (side.get(choiceCard)) {
                                        hand.get(choiceCard).switchSide();
                                        clientNetworkControllerMapper.switchCardSide(gameName, playerName, hand.get(choiceCard));
                                    }

                                    clientNetworkControllerMapper.placeCard(gameName, playerName, new Coordinate(x, y), hand.get(choiceCard));
                                }
                            }
                        }
                        //TODO: gather arguments on updated VirtualView
                    }
                    case "9" -> {
                        ArrayList<GameCard> field = new ArrayList<>(clientNetworkControllerMapper.getView().gameView().globalBoardView().fieldGoldCards());
                        field.addAll(clientNetworkControllerMapper.getView().gameView().globalBoardView().fieldResourceCards());
                        System.out.println("Choose one of the cards on the field: ");
                        System.out.println("1) " + field.get(0));
                        System.out.println("2) " + field.get(1));
                        System.out.println("3) " + field.get(2));
                        System.out.println("4) " + field.get(3));
                        String choice = reader.readLine();
                        clientNetworkControllerMapper.drawCardFromField(gameName, playerName, field.get(Integer.parseInt(choice) - 1));
                    }
                    case "10" -> {
                        GameCard firstResourceCard = clientNetworkControllerMapper.getView().gameView().globalBoardView().resourceFirstCard();
                        System.out.println("The first resource card is: " + firstResourceCard.getCardColor());
                        System.out.println("Do you want to draw it? (y/n)");
                        String choice = reader.readLine();
                        if (choice.equalsIgnoreCase("y")) {
                            clientNetworkControllerMapper.drawCardFromResourceDeck(gameName, playerName);
                        }
                    }
                    case "11" -> {
                        GameCard firstGoldCard = clientNetworkControllerMapper.getView().gameView().globalBoardView().goldFirstCard();
                        System.out.println("The first gold card is: " + firstGoldCard.getCardColor());
                        System.out.println("Do you want to draw it? (y/n)");
                        String choice = reader.readLine();
                        if (choice.equalsIgnoreCase("y")) {
                            clientNetworkControllerMapper.drawCardFromGoldDeck(gameName, playerName);
                        }
                    }
                    case "12" -> {
                        HashMap<Coordinate, GameCard> map = clientNetworkControllerMapper.getView().gameView().playerViews().stream().filter(playerView -> playerView.playerName().equals(finalPlayerName)).findFirst().get().playerBoardView().playerBoard();
                        //printBoardAsMatrix(map);
                    }
                    case "13" -> {
                        System.out.println("Exiting...");
                    }
                    default -> System.out.println("Invalid input");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
