@startuml IS24-AM02

package "Controller"{
    class MainController {
        -ArrayList<Game> games
        +ArrayList<Game> getGames()
        +createGame(String gameName, int nPlayers, String playerName)
        +deleteGame(String gameName)
        +Game joinGame(String gameName, String playerName)
    }
    MainController "1..N" *-- "1" GameController

    class GameController {
        -Game game
        +void placeCard(String playerName, GameCard card)
        +drawCardFromField(String playerName, GameCard card)
        +drawCardFromResourceDeck(String playerName)
        +drawCardFromGoldDeck(String playerName)
        +switchCardSide(String playerName, GameCard card)
        +setPlayerObjective(String playerName, GameCard card)
        +Game getGame()

    }

    note top of MainController
    Interfaccia con client
    end note

    note bottom of GameController
        Gestione delle azioni del giocatore
        end note
}

package "Model"{
    class Coordinate extends Point {
    }

    note top of Point
    Built-in java.awt.Point
    end note

    class Game {
        -String gameName
        -int nPlayers
        -ArrayList<Player> players
        -ArrayList<Player> winners
        -GlobalBoard globalBoard
        -Player currentPlayer
        +String getGameName()
        +ArrayList<Player> getPlayers()
        +Player getPlayer(String playerName)
        +GlobalBoard getGlobalBoard()
        +addPlayer(Player player)
        +Player getCurrentPlayer()
        +void setNextPlayer()
        +boolean isStarted()
        +boolean isOver()
        +ArrayList<Player> getWinner()
    }

    note left of Game
    Gestione giocatori e inizio/fine partita
    end note

    Game "2..4" *-- "1" Player
    Game "1" *-- "1" GlobalBoard

    class GlobalBoard {
        -Deck<GameCard> goldDeck
        -Deck<GameCard> resourceDeck
        -Deck<ObjectiveCard> objectiveDeck
        -Deck<GameCard> starterDeck
        -ArrayList<ObjectiveCard> globalObjectives
        -ArrayList<GameCard> fieldGoldCards
        -ArrayList<GameCard> fieldResourceCards
        +Deck<GameCard> getGoldDeck()
        +Deck<GameCard> getResourceDeck()
        +Deck<ObjectiveCard> getObjectiveDeck()
        +Deck<GameCard> getStarterDeck()
        +ArrayList<ObjectiveCard> getGlobalObjectives()
        +ArrayList<GameCard> getFieldGoldCards()
        +ArrayList<GameCard> getFieldResourceCards()
        +boolean isGoldDeckEmpty()
        +boolean isResourceDeckEmpty()
        +drawCardFromField(GameCard card)
    }

    'GlobalBoard  --  ObjectiveCard
    GlobalBoard "3" *-- "1" Deck

    note top of GlobalBoard
        I mazzi non vengono mischiati,
        vengono estratte carte randomicamente;
        potremmo usare Singleton pattern
        per istanziare le carte ed assicurarne
      end note

    class Player {
        'Ha la sua PlayerBoard, la sua hand, il suo numero di risorse attuali, la sua posizione sul tabellone e il suo obiettivo.
        -String playerName
        -int playerPos
        -PlayerBoard playerBoard
        -ObjectiveCard objectiveCard
        - ObjectiveCard[] drawnObjectives
        -Hand hand
        -boolean isConnected
        +getPlayerName()
        +getPlayerBoard()
        +getPlayerPos()
        +getPlayerHand()
        +getObjectiveCard()
        +setObjectiveCard(ObjectiveCard)
        +advancePlayerPos(int steps)
        +boolean setConnected(boolean status)
        +boolean isConnected()
        +boolean isLastRound()
        'Numero di passi di cui avanzare
    }

    Player "1" *-- "1" PlayerBoard

    class Deck<T> {
        -ArrayList<T> deck
        -Random random;
        +boolean isEmpty()
        +T draw()
    }


    class Hand {
        -ArrayList<GameCard> hand
        +ArrayList<GameCard> getGameCards()
        +void addCard(GameCard)
        +void removeCard(GameCard)
    }

    Player "1" *-- "1" Hand

    class PlayerBoard {
        -HashMap<Coordinate, GameCard> playerBoard
        -GameItemStore gameItems
        -GameCard starterCard
        +Optional<GameCard> getGameCard(Coordinate)
        +Coordinate getGameCardPosition(GameCard)
        +ArrayList<GameCard> getGameCards()
        +Integer getGameItemAmount(GameItem)
        +int setGameCard(Coordinate, GameCard)
    }

    PlayerBoard "1..N" *-- "1" GameCard

    class Store<T> {
        #HashMap<T, Integer> store
        +get(T t)
        +set(T t, Integer)
        +increment(T t, Integer)
        +decrement(T t, Integer)
        +getNonEmptyKeys()
    }

    class GameItemStore extends Store {
        +GameItemStore()
        +GameItemStore(HashMap<GameItem, Integer> gameItems)
    }


    PlayerBoard "2" *-- "1" Store



    class GameCard {
        -Side currentSide
        -Side otherSide
        -cardColor cardColor
        +Side getCurrentSide()
        +void switchSide()
        +CardColor getCardColor()
        +Optional<Corner>getCorner(CornerPosition)
        +GameItemEnum setCornerCovered(CornerPosition)
        +GameItemStore getGameItemStore()
        +int getPoints(Coordinate, PlayerBoard)
        +GameItemStore getNeededItemStore()
    }

    GameCard "2" *-- "1" Side

    abstract class Side {
        #Optional<Corner>topRight
        #Optional<Corner>topLeft
        #Optional<Corner>bottomLeft
        #Optional<Corner>bottomRight
        +getCorner(CornerPosition)
        +setCornerCovered(CornerPosition)
        +GameItemStore getGameItemStore()
        +int getPoints(Coordinate, PlayerBoard)
        +GameItemStore getNeededItemStore()
        +GameItemStore getCornersItems()
    }

    Side <|-- Front
    Side <|-- Back
    Side "1..4" *-- "1" Corner

    class Corner {
          -boolean isCovered
          -GameItemEnum gameItem

          +GameItemEnum getGameItem()
          +GameItemEnum setCovered()
          +Boolean isEmpty()

      }



      note bottom of Corner
        Nel caso in cui un Corner è vuoto, allora risulterà avere GameItemEnum = NONE
        Se invece il Corner non esiste, il getter restituirà Optional.empty()
        Se la carta è piazzata, significa che non devo preoccuparmi di accesso a Corner Optional.empty()
      end note

    'FrontSide Section

    class Front {
        #int points
        +getGameItemStore()
        +int getPoints(Coordinate, PlayerBoard)
    }

    Front <|-- FrontGoldCard

    class FrontGoldCard {
        #GameItemStore neededItems
        +getNeededItemStore()
    }

    FrontGoldCard <|-- FrontPositionalGoldCard
    FrontGoldCard <|-- FrontItemGoldCard


    class FrontPositionalGoldCard {
        +int getPoints(Coordinate, PlayerBoard)

    }


    class FrontItemGoldCard {
        -GameItemEnum multiplier
        +int getPoints(Coordinate, PlayerBoard)
    }

    'BackSide Section

    class Back {
        #GameItemStore resources
        +getGameItemStore()
    }

    'ObjectiveCard Section

    abstract class ObjectiveCard {
        ' Carte obiettivo
        #Integer pointsWon
        +int getPoints(Coordinate, PlayerBoard)
    }

    Player *-- ObjectiveCard
    GlobalBoard *-- ObjectiveCard
    ObjectiveCard <|-- PositionalObjectiveCard

    class ItemObjectiveCard {
        -GameItemStore multiplier
        +int getPoints(Coordinate, PlayerBoard)
    }

    ObjectiveCard <|-- ItemObjectiveCard

    class PositionalData {
        -Coordinate point
        -CardColor cardColor
        +Coordinate getPoint()
        +CardColor getCardColor()
        +GameItemEnum getGameItem()
        +void setPoint(x, y)
        +void setCardColor(CardColor)
    }
    PositionalObjectiveCard "1..N" *-- "1" PositionalData

    class PositionalObjectiveCard {
        -ArrayList<PositionalData> positionalData
        +int getPoints(Coordinate, PlayerBoard)
    }

    'Enum Section

    enum CornerPosition {
        TOP_RIGHT
        TOP_LEFT
        BOTTOM_LEFT
        BOTTOM_RIGHT
    }

    enum GameItemEnum {
        PLANT
        ANIMAL
        FUNGI
        INSECT
        QUILL
        INKWELL
        MANUSCRIPT
        NONE
    }

    enum CardColor {
        Red
        Blue
        Green
        Purple
        Neutral
    }
}

@enduml
