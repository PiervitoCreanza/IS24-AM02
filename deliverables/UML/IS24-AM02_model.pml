@startuml IS24-AM02

package "Controller"{
    class MainController {
        -ArrayList<GameController> gameControllers
        +getGames()
        +createGame(String gameName, int nPlayers, String playerName)
        +deleteGame(String gameName)
        +joinGame(String gameName, String playerName)
    }
    MainController "1..N" *-- "1" GameController

    class GameController {
        -Game game
        +placeCard(String playerName, GameCard card)
        +drawCardFromField(String playerName, GameCard card)
        +drawCardFromResourceDeck(String playerName)
        +drawCardFromGoldDeck(String playerName)
        +switchCardSide(String playerName, GameCard card)
        +setPlayerObjective(String playerName, GameCard card)
        +getGame()
    }

    note top of MainController
    Interfaccia con client
    end note

     note top of MainController
        I tipi di ritorno del controller
        sono ancora Work In Progress
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
        +void addPlayer(String playerName)
        +Player getCurrentPlayer()
        +void setNextPlayer()
        +boolean isStarted()
        +boolean isOver()
        +void calculateWinners()
        +ArrayList<Player> getWinners()

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
        +void drawCardFromField(GameCard card)
    }

    'GlobalBoard  --  ObjectiveCard
    GlobalBoard "3" *-- "1" Deck

    note top of GlobalBoard
        I mazzi non vengono mischiati,
        vengono estratte carte randomicamente;
      end note

    class Player {
        'Ha la sua PlayerBoard, la sua hand, il suo numero di risorse attuali, la sua posizione sul tabellone e il suo obiettivo.
        -String playerName
        -int playerPos
        -PlayerBoard playerBoard
        -ObjectiveCard objectiveCard
        -ObjectiveCard[] choosableObjectives
        -PlayerHand hand
        -boolean isConnected
        -PlayerColorEnum playerColor
        +String getPlayerName()
        +PlayerBoard getPlayerBoard()
        +int getPlayerPos()
        +PlayerHand getPlayerHand()
        +ObjectiveCard getObjectiveCard()
        +ObjectiveCard[] getChoosableObjectives()
        +void setObjectiveCard(ObjectiveCard)
        +void advancePlayerPos(int steps)
        +boolean setConnected(boolean status)
        +boolean isConnected()
        +PlayerColorEnum getPlayerColor()
        +void setPlayerColor(PlayerColorEnum)
        'Numero di passi di cui avanzare
    }

    Player "1" *-- "1" PlayerBoard

    class Deck<T> {
        -ArrayList<T> deck
        -Random random
        +boolean isEmpty()
        +T draw()
    }


    class PlayerHand {
        -ArrayList<GameCard> hand
        +ArrayList<GameCard> getGameCards()
        +void addCard(GameCard)
        +void removeCard(GameCard)
    }

    Player "1" *-- "1" PlayerHand

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
        +T get(T t)
        +void set(T t, Integer)
        +void increment(T t, Integer)
        +void decrement(T t, Integer)
        +void addStore(Store other)
        +void subtractStore(Store other)
        +ArrayList<T> getNonEmptyKeys()
    }

    class GameItemStore extends Store {
        +GameItemStore()
        +GameItemStore(HashMap<GameItem, Integer> gameItems)
    }

    note bottom of GameItemStore
        Notasi Overloading
        end note

    PlayerBoard "2" *-- "1" Store



    class GameCard {
        -int gameCardId
        -Side currentSide
        -Side otherSide
        -CardColorEnum cardColor
        +int getGameCardId()
        +Side getCurrentSide()
        +void switchSide()
        +CardColorEnum getCardColor()
        +Optional<Corner> getCorner(CornerPosition)
        +GameItemEnum setCornerCovered(CornerPosition)
        +GameItemStore getGameItemStore()
        +int getPoints(Coordinate, PlayerBoard)
        +GameItemStore getNeededItemStore()
    }

    GameCard "2" *-- "1" SideGameCard

    abstract class SideGameCard {
        #Corner topRight
        #Corner topLeft
        #Corner bottomLeft
        #Corner bottomRight
        +Optional<Corner> getCorner(CornerPosition)
        +void setCornerCovered(CornerPosition)
        +GameItemStore getGameItemStore()
        +int getPoints(Coordinate, PlayerBoard)
        +GameItemStore getNeededItemStore()
        +GameItemStore getCornersItems()
    }

    SideGameCard <|-- FrontGameCard
    SideGameCard <|-- BackGameCard
    SideGameCard "1..4" *-- "1" Corner

    class Corner {
          -boolean isCovered
          -GameItemEnum gameItem
          +GameItemEnum getGameItem()
          +GameItemEnum setCovered()
          +boolean isCovered()
      }



      note bottom of Corner
        Documentazione: [[https://bit.ly/3TCavdQ]]
      end note

    'FrontSide Section

    class FrontGameCard {
        #int points
        +GameItemStore getGameItemStore()
        +int getPoints(Coordinate, PlayerBoard)
    }

    FrontGameCard <|-- FrontGoldGameCard

    class FrontGoldGameCard {
        #GameItemStore neededItems
        +GameItemStore getNeededItemStore()
    }

    FrontGoldGameCard <|-- FrontPositionalGoldGameCard
    FrontGoldGameCard <|-- FrontItemGoldGameCard


    class FrontPositionalGoldGameCard {
        +int getPoints(Coordinate, PlayerBoard)

    }


    class FrontItemGoldGameCard {
        -GameItemEnum multiplier
        +int getPoints(Coordinate, PlayerBoard)
    }

    'BackSide Section


    class BackGameCard {
        -GameItemStore resources
        +GameItemStore getGameItemStore()
    }

    'ObjectiveCard Section

    abstract class ObjectiveCard {
        ' Carte obiettivo
        #int pointsWon
        +int getPoints(Coordinate, PlayerBoard)
    }

    Player *-- ObjectiveCard
    GlobalBoard *-- ObjectiveCard
    ObjectiveCard <|-- PositionalObjectiveCard

    class ItemObjectiveCard {
        -GameItemStore multiplier
        +int getPoints(PlayerBoard)
    }

    ObjectiveCard <|-- ItemObjectiveCard

    class PositionalData {
        -Coordinate point
        -CardColorEnum cardColor
        +Coordinate getPoint()
        +CardColorEnum getCardColor()
    }
    PositionalObjectiveCard "1..N" *-- "1" PositionalData

    class PositionalObjectiveCard {
        -ArrayList<PositionalData> positionalData
        +int getPoints(PlayerBoard)
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

    enum CardColorEnum {
        RED
        BLUE
        GREEN
        PURPLE
        NONE
    }

    enum PlayerColorEnum {
        RED
        BLUE
        GREEN
        PURPLE
    }

    note top of PlayerColorEnum
        Per il nero basta vedere il primo player
        nel Game.ArrayList<Player>
    end note
}

@enduml
