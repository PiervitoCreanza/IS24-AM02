@startuml IS24-AM02
class Game {
    'Gestione giocatori e inizio/fine partita
    -ArrayList<Player> players
    -Player currentPlayer
    -GlobalBoard globalBoard
    +startGame()
    +stopGame()
    +addPlayer()
    +removePlayer()
    +getPlayers()
    +getNextPlayer()
}

Game "2..4" *-- "1" Player
Game "1" *-- "1" GlobalBoard

class GlobalBoard {
    -ObjectiveCard[2] objectives
    -ArrayList<GameCard> fieldCards
    -Deck goldDeck
    -Deck resourceDeck
    -Deck objectiveDeck
    +getGoldDeck()
    +getResourceDeck()
    +getObjectiveDeck()
    +getObjectives()
    +setObjectives()
    +initField()
    +getFieldCards()
    +drawCardFromField(GameCard)
}

'GlobalBoard  --  ObjectiveCard
GlobalBoard "3" *-- "1" Deck

class Player {
    'Ha la sua PlayerBoard, la sua hand, il suo numero di risorse attuali, la sua posizione sul tabellone e il suo obiettivo.
    -String playerName
    -int playerPos
    -PlayerBoard playerBoard
    -ObjectiveCard playerObjective
    -Hand playerHand
    +getPlayerBoard()
    +getPlayerPos()
    +getPlayerHand()
    +getPlayerObjective()
    +setPlayerObjective()
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
    +getCard()
    +addCard()
    +removeCard()
}

Player "1" *-- "1" Hand

class PlayerBoard {
    -GameCard[][] boardMatrix
    -int[4] totalResources
    -int[3] totalGameObject
    -int center_x
    -int center_y
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
    +setSide(Side)
    +getColor()
}

GameCard "2" *-- "1" Side

abstract class Side {
    -Optional<Corner> topRight
    -Optional<Corner> topLeft
    -Optional<Corner> bottomLeft
    -Optional<Corner> bottomRight
    +getCorner()
    +getCorners()
    +getGameResource()
    +getPoints()
    +getNeededResources()
}

Side <|-- Front
Side <|-- Back
Side "1..4" *-- "1" Corner

abstract class Corner {
      -boolean isCovered
      +getIsCovered()
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
    -int points
    +getGameResource()
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
    -ArrayList<GameResource> neededResources
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
    -ArrayList<GameResource> resources
    +getGameResource()
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
    -Integer pointsWon
    +getPoints(PlayerBoard)
}

Player *-- ObjectiveCard 
GlobalBoard *-- ObjectiveCard
ObjectiveCard <|-- PositionalObjective
Deck *-- Card

class ObjectObjective {
    +getPoints()
}

ObjectiveCard <|-- ObjectObjective

class PositionalObjective {
    +getPoints()
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