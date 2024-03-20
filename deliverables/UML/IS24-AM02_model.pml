@startuml IS24-AM02

class GameManager {
    -ArrayList<Game> games
    -ArrayList<GameCard> defaultGoldDeck
    -ArrayList<GameCard> defaultResourceDeck
    -ArrayList<ObjectiveCard> defaultObjectiveDeck
    -ArrayList<GameCard> defaultStarterDeck
    +ArrayList<Game> getGames()
    +Game createGame(String gameName, int nPlayers, String playerName)
    +deleteGame(String gameName)
    +Game joinGame(String gameName, String playerName)
}
GameManager "1..N" *-- "1" Game

class Game {
    -String gameName
    -int nPlayers
    -ArrayList<Player> players
    -GlobalBoard globalBoard
    -Player currentPlayer
    +String getGameName()
    +ArrayList<Player> getPlayers()
    +Player getPlayer(String playerName)
    +GlobalBoard getGlobalBoard()
    +addPlayer(Player player)
    +Player getNextPlayer()
    +boolean isStarted()
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
    -HashMap<Point, GameCard> playerBoard
    -GameItemStore gameItems
    +Optional<GameCard> getGameCard(Point)
    +Point getGameCardPosition(GameCard)
    +ArrayList<GameCard> getGameCards()
    +Integer getGameItemAmount(GameItem)
    +void setGameCard(Point, GameCard)
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
    +int getPoints(Point pos, PlayerBoard)
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
    +int getPoints(Point pos, PlayerBoard)
    +GameItemStore getNeededItemStore()
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
    +int getPoints(Point pos, PlayerBoard)
}

Front <|-- FrontGoldCard

class FrontGoldCard {
    #GameItemStore neededItems
    +getNeededItemStore()
}

FrontGoldCard <|-- FrontPositionalGoldCard
FrontGoldCard <|-- FrontItemGoldCard


class FrontPositionalGoldCard {
    +int getPoints(Point pos, PlayerBoard)
    
}


class FrontItemGoldCard {
    -GameItemEnum multiplier
    +int getPoints(Point pos, PlayerBoard)
}

'BackSide Section

class Back {
    #GameItemStore resources
    +getGameItemStore()
    +int getPoints(Point pos, PlayerBoard)
}

'ObjectiveCard Section

abstract class ObjectiveCard {
    ' Carte obiettivo
    #Integer pointsWon
    +int getPoints(Point pos, PlayerBoard)
}

Player *-- ObjectiveCard 
GlobalBoard *-- ObjectiveCard
ObjectiveCard <|-- PositionalObjectiveCard

class ItemObjectiveCard {
    -GameItemStore multiplier
    +int getPoints(Point pos, PlayerBoard)
}

ObjectiveCard <|-- ItemObjectiveCard

class PositionalData {
    -Point point
    -CardColor cardColor
    +Point getPoint()
    +CardColor getCardColor()
    +GameItemEnum getGameItem()
    +void setPoint(x, y)
    +void setCardColor(CardColor)
}
PositionalObjectiveCard "1..N" *-- "1" PositionalData

class PositionalObjectiveCard {
    -ArrayList<PositionalData> positionalData
    +int getPoints(Point pos, PlayerBoard)
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

@enduml