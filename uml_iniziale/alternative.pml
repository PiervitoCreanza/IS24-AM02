@startuml
class Game {
    'Gestione giocatori e inizio/fine partita
    -ArrayList<Player> players
    +startGame()
    +stopGame()
    +addPlayer()
    +removePlayer()
    
}

Game "1" -- "2..4" Player
Game "1" -- "1" GlobalBoard

class GlobalBoard {
    -int[] playerPos
    -ObjectiveCard[2] objectives
    -ArrayList<Card> fieldCards 
    +getPlayerPos()
    +getObjectives()
    +setObjectives()
    +setField()
    +drawCardFromField()
    +updatePlayerPos()
}

'GlobalBoard  --  ObjectiveCard
GlobalBoard "2" -- "1" Deck

class Player {
    'Ha la sua PlayerBoard, la sua hand, il suo numero di risorse attuali, la sua posizione sul tabellone e il suo obiettivo.
    -String playerName
    -int playerPos
    -PlayerBoard playerBoard
    -ObjectiveCard playerObjective
    -Hand playerHand
    +getPlayerBoard()
    +getPlayerPos()
    +getPlayerObjective()
    +setPlayerObjective()
    +advance(int steps)
    'Numero di passi di cui avanzare
}

Player "1" -- "1" PlayerBoard

class Deck {
    -ArrayList<Card> cards
    +drawCard()
    
}

Deck -- Card

class Hand {
    -GameCard[3] currentCards
    +getCard()
    +addCard()
    +removeCard()
}

Player "1" -- "1" Hand

class PlayerBoard {
    -Card[][] map
    -int[4] totalResources
    -int[3] totalGameObject
    -int center_x
    -int center_y
    +getCard(x, y)
    +getPlacedCards()
    +getResourceAmount(Resource)
    +getObjectAmount(Object)
    +placeCard(Card, x, y)
    +isPlaceable(Card, x, y)
}

PlayerBoard "0..N" -- "1" Card

class Card {
    -Side currentSide
    -Side otherSide
    +getCurrentSide()
    +switchSide(Side)
    'switchSide(Side) Così possiamo forzare una side in una sola chaiamta.
}

Card "2" -- "1" Side

abstract class Side {
    -Optional<Corner> topRight
    -Optional<Corner> topLeft
    -Optional<Corner> bottomLeft
    -Optional<Corner> bottomRight
    +getCorner()
    +getCorners()
    +getResource()
    +getPoints()
}

Side <|-- Front
Side <|-- Back
Side "1..4" -r-- "1" Corner


class Corner<T>{
    -T item
    -boolean isCovered
    +getItem()
    +isCovered()
    +setCovered()
}

'FrontSide Section

abstract class Front {
    -int points
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
    -ArrayList<Resource> neededResources
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
    +getPoints()
}

'BackSide Section

abstract class Back {
    -ArrayList<Resource> resources
    +getResource()
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
    +getPoints()
}

Player -- ObjectiveCard 
ObjectiveCard <|-- PositionalObjective

class ObjectObjective {
    +getPoints()
}

ObjectiveCard <|-- ObjectObjective

class PositionalObjective {
    +getPoints()
}

'Enum Section

enum Resource {
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

@enduml
