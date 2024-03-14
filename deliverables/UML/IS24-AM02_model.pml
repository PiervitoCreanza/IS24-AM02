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
    -ObjectiveCard playerObjectiveCard
    -Hand playerHand
    +getPlayerBoard()
    +getPlayerPos()
    +getPlayerHand()
    +getPlayerObjective()
    +setPlayerObjective(ObjectiveCard)
    +advance(int steps)
    'Numero di passi di cui avanzare
}

Player "1" *-- "1" PlayerBoard

class Deck {
    -ArrayList<Card> cards
    +drawCard()
}


class Hand {
    -GameCard[3] currentCards
    +ArrayList<GameCard> getCards()
    +addCard(GameCard)
    +removeCard(GameCard)
}

Player "1" *-- "1" Hand

class PlayerBoard {
    -GameCard[][] boardMatrix
    -int[4] totalResources
    -int[3] totalGameObject
    +getCard(x, y)
    +getCardPosition(GameCard)
    +getPlacedCards()
    +getResourceAmount(GameResource)
    +getObjectAmount(Object)
    +placeCard(GameCard, x, y)
    +isPlaceable(GameCard, x, y)
}

PlayerBoard "1..N" -- "1" GameCard

abstract class Card {

}
Card <|-- GameCard
Card <|-- ObjectiveCard

class GameCard {
    -Side currentSide
    -Side otherSide
    -CardColor cardColor
    +getCurrentSide()
    +switchSide()
    +getColor()
    +getCorner(cornerPos)
    +getGameResources()
    +getGameObjects()
    +getPoints()
    +getNeededResources()
}

GameCard "2" *-- "1" Side

abstract class Side {
    #Optional<Corner> topRight
    #Optional<Corner> topLeft
    #Optional<Corner> bottomLeft
    #Optional<Corner> bottomRight
    +getCorner(cornerPos)
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
    #ArrayList<GameResource> neededResources
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
    #ArrayList<GameResource> resources
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
ObjectiveCard <|-- PositionalObjective
ObjectiveCard <|-- ResourceObjective
Deck *-- Card

class ObjectObjective {
    -GameObject[3] multiplier
    +getPoints(PlayerBoard)
}
class ResourceObjective {
    -GameResource resource
    +getPoints(PlayerBoard)
}

ObjectiveCard <|-- ObjectObjective

class PositionalObjective {
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