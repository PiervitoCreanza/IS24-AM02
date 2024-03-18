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

class Point {
    -int x
    -int y
    +getX()
    +getY()
}

class PlayerBoard {
    -HashMap<Point, GameCard> playerBoard
    -GameResourceStore gameResources
    -GameObjectStore gameObjects
    +Optional<GameCard> getGameCard(Point)
    +Optional<Point> getGameCardPosition(GameCard)
    +ArrayList<GameCard> getGameCards()
    +Integer getGameResourceAmount(GameResource)
    +Integer getGameObjectAmount(Object)
    +void setGameCard(Point, GameCard)
    +boolean isPlaceable(Point, GameCard)
}

PlayerBoard "1..N" -- "1" GameCard

abstract class Store<T> {
    #HashMap<T, Integer> store
    +get(T t)
    +set(T t, Integer)
    +add(T t, Integer)
    +remove(T t, Integer)
    +getNonEmptyKeys()
}

class GameResourceStore extends Store {
    +GameResourceStore()
    +GameResourceStore(HashMap<GameResource, Integer> gameResources)
}

class GameObjectStore extends Store {
    +GameObjectStore()
    +GameObjectStore(HashMap<GameObject, Integer> gameObjects)
}

PlayerBoard "2" *-- "1" Store
GameResourceStore "1" --* "1" Back
GameResourceStore "1" --* "1" FrontGoldCard

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
    +GameColor getColor()
    +Optional<Corner> getTopRightCorner()
    +Optional<Corner> getTopLeftCorner()
    +Optional<Corner> getBottomLeftCorner()
    +Optional<Corner> getBottomRightCorner()
    +GameResourceStore getGameResources()
    +GameObjectStore getGameObjects()
    +Integer getPoints()
    +GameResourceStore getNeededResources()
}

GameCard "2" *-- "1" Side

abstract class Side {
    #Optional<Corner> topRight
    #Optional<Corner> topLeft
    #Optional<Corner> bottomLeft
    #Optional<Corner> bottomRight
    +getTopRightCorner()
    +getTopLeftCorner()
    +getBottomLeftCorner()
    +getBottomRightCorner()
    +getGameResources()
    +getGameObjects()
    +getPoints()
    +getNeededResources()
}

Side <|-- Front
Side <|-- Back
Side "1..4" *-- "1" Corner

abstract class Corner {
      #boolean isCovered
      +getGameObject()
      +getGameResource()
      +setCovered(Boolean)
  }
  class CornerObject {
        -GameObject gameObject
        +getGameObject()
  }
  Corner <|-- CornerObject
  class CornerResource {
        -GameResource gameResource
        +getGameResource()
  }
  Corner <|-- CornerResource
  class EmptyCorner {
  }
  Corner <|-- EmptyCorner

'FrontSide Section

abstract class Front {
    #int points
    +getGameResources()
    +getPoints()
}

Front <|-- FrontStarterCard
Front <|-- FrontResourceCard
Front <|-- FrontGoldCard

class FrontStarterCard {
    
}

class FrontResourceCard {

}

abstract class FrontGoldCard {
    #GameResourceStore neededResources
    +getNeededResources()
}

FrontGoldCard <|-- FrontSimpleGoldCard
FrontGoldCard <|-- FrontPositionalGoldCard
FrontGoldCard <|-- FrontObjectGoldCard

class FrontSimpleGoldCard {

} 

class FrontPositionalGoldCard {
    +getPoints(PlayerBoard)
    
}

class FrontObjectGoldCard {
    -GameObject multiplier
    +getPoints(PlayerBoard)
}

'BackSide Section

abstract class Back {
    #GameResourceStore resources
    +getGameResources()
    +getPoints()
}

Back <|-- BackStarterCard
Back <|-- BackResourceCard
Back <|-- BackGoldCard

class BackStarterCard {
    
}

class BackResourceCard {

}

class BackGoldCard {
    
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
ObjectiveCard <|-- ResourceObjectiveCard
Deck *-- Card

class ObjectObjectiveCard {
    -GameObjectStore multiplier
    +getPoints(PlayerBoard)
}
ObjectObjectiveCard *-- GameObjectStore
class ResourceObjectiveCard {
    -GameResource resource
    +getPoints(PlayerBoard)
}

ObjectiveCard <|-- ObjectObjectiveCard

class PositionalObjectiveCard {
    -CardColor[3][3] requiredColors
    +getPoints(PlayerBoard)
}

'Enum Section

enum GameResource {
    Plant
    Animal
    Fungi
    Insect
}

enum GameObject {
    Quill
    Inkwell
    Manuscript
}

enum CardColor {
    Red
    Blue
    Green
    Purple
    Neutral
}

@enduml