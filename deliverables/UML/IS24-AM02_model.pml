@startuml IS24-AM02

class GameManager {
    -ArrayList<Game> games
    +createGame(String name, int nPlayers, String playerName)
    +deleteGame(String name)
    +ArrayList<Game> getGames()
    +Game joinGame(String name, String playerName)
}
GameManager "1..N" *-- "1" Game

class Game { 
    'Gestione giocatori e inizio/fine partita
    -String gameName
    -int nPlayers
    -ArrayList<Player> players
    -GlobalBoard globalBoard
    -Player currentPlayer
    +stopGame()
    +addPlayer(Player player)
    +removePlayer(Player player)
    +getPlayers()
    +getPlayer(String playerName)
    +getNextPlayer()
    +getGlobalBoard()
    +isStarted()
}

Game "2..4" *-- "1" Player
Game "1" *-- "1" GlobalBoard

class GlobalBoard {
    -Deck goldDeck
    -Deck resourceDeck
    -Deck objectiveDeck
    -Deck starterDeck
    -ObjectiveCard[2] objectives
    -GameCard[2] fieldGoldCards
    -GameCard[2] fieldResourceCards
    
    +getGoldDeck()
    +getResourceDeck()
    +getObjectiveDeck()
    +getObjectives()
    +getFieldGoldCards()
    +getFieldResourceCards()
    +drawCardFromField(GameCard)
}

'GlobalBoard  --  ObjectiveCard
GlobalBoard "3" *-- "1" Deck

class Player {
    'Ha la sua PlayerBoard, la sua hand, il suo numero di risorse attuali, la sua posizione sul tabellone e il suo obiettivo.
    -String playerName
    -int playerPos
    -PlayerBoard playerBoard
    -ObjectiveCard objectiveCard
    -Hand hand
    +getPlayerName()
    +getPlayerBoard()
    +getPlayerPos()
    +getPlayerHand()
    +getObjectiveCard()
    +setObjectiveCard(ObjectiveCard)
    +advancePlayerPos(int steps)
    'Numero di passi di cui avanzare
}

Player "1" *-- "1" PlayerBoard

class Deck {
    -ArrayList<Card> cards
    +drawCard()
}


class Hand {
    -ArrayList<GameCard> hand
    +ArrayList<GameCard> getGameCards()
    +void addCard(GameCard)
    +void removeCard(GameCard)
}

Player "1" *-- "1" Hand

class PlayerBoard {
    -HashMap<Point, GameCard> playerBoard
    -GameItemStore gameItems
    +Optional<GameCard> getGameCard(Point)
    +Optional<Point> getGameCardPosition(GameCard)
    +ArrayList<GameCard> getGameCards()
    +Integer getGameItemAmount(GameItem)
    +void setGameCard(Point, GameCard)
}

PlayerBoard "1..N" -- "1" GameCard

class Store<T> {
    #HashMap<T, Integer> store
    +get(T t)
    +set(T t, Integer)
    +add(T t, Integer)
    +remove(T t, Integer)
    +getNonEmptyKeys()
}

class GameItemStore extends Store {
    +GameItemStore()
    +GameItemStore(HashMap<GameItem, Integer> gameItems)
}


PlayerBoard "2" *-- "1" Store

interface Card {
    +getPoints(PlayerBoard)
}
Card <|-- GameCard
Card <|-- ObjectiveCard

class GameCard {
    -Side currentSide
    -Side otherSide
    -CardColor cardColor
    +Side getCurrentSide()
    +void switchSide()
    +CardColor getColor()
    +Optional<Corner> getCorner(CornerPosition)
    +GameItem setCornerCovered(CornerPosition)
    +GameItemStore getGameItemStore()
    +Integer getPoints(PlayerBoard)
    +GameItemStore getNeededItemStore()
}

GameCard "2" *-- "1" Side

abstract class Side {
    #Corner topRight
    #Corner topLeft
    #Corner bottomLeft
    #Corner bottomRight
    +getCorner(CornerPosition)
    +setCornerCovered(CornerPosition)
    +GameItemStore getGameItemStore()
    +getPoints(PlayerBoard)
    +GameItemStore getNeededItemStore()
}

Side <|-- Front
Side <|-- Back
Side "1..4" *-- "1" Corner

class Corner {
      -boolean isCovered
      -GameItem gameItem

      +GameItem getGameItem()
      +GameItem setCovered()
      +Boolean isEmpty()
      +Boolean isExisting() {return true}

  }

class NonExistingCorner {
    +Boolean isExisting() {return false}
}
Corner <|-- NonExistingCorner

'FrontSide Section

class Front {
    #int points
    +getGameItemStore()
    +getPoints(PlayerBoard)
}

Front <|-- FrontGoldCard

class FrontGoldCard {
    #GameItemStore neededItems
    +getNeededItemStore()
}

FrontGoldCard <|-- FrontPositionalGoldCard
FrontGoldCard <|-- FrontItemGoldCard


class FrontPositionalGoldCard {
    +getPoints(PlayerBoard)
    
}

class FrontItemGoldCard {
    -GameItem multiplier
    +getPoints(PlayerBoard)
}

'BackSide Section

class Back {
    #GameItemStore resources
    +getGameItemStore()
    +getPoints(PlayerBoard)
}

'ObjectiveCard Section

abstract class ObjectiveCard {
    ' Carte obiettivo
    #Integer pointsWon
    +getPoints(PlayerBoard)
}

Player *-- ObjectiveCard 
GlobalBoard *-- ObjectiveCard
ObjectiveCard <|-- PositionalObjectiveCard
Deck *-- Card

class ItemObjectiveCard {
    -GameItemStore multiplier
    +getPoints(PlayerBoard)
}

ObjectiveCard <|-- ItemObjectiveCard

class PositionalData {
    -Point point
    -CardColor cardColor
    +Point getPoint()
    +CardColor getCardColor()
    +GameItem getGameItem()
    +void setPoint(x, y)
    +void setCardColor(CardColor)
}
PositionalObjectiveCard "1..N" *-- "1" PositionalData

class PositionalObjectiveCard {
    -ArrayList<PositionalData> positionalData
    +getPoints(PlayerBoard)
}

'Enum Section

enum CornerPosition {
    TOP_RIGHT
    TOP_LEFT
    BOTTOM_LEFT
    BOTTOM_RIGHT
}

enum GameItem {
    PLANT
    ANIMAL
    FUNGI
    INSECT
    QUILL
    INKWELL
    MANUSCRIPT
    NULL
}

enum CardColor {
    Red
    Blue
    Green
    Purple
    Neutral
}

@enduml