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

class Orchestrator{
    'Gestisce i turni e orchestra la partita
}


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
Hand "1" -- "3" GameCard

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

abstract class Card {
    -Side currentSide
    -Side otherSide
    -boolean isFront
    +getCurrentSide()
    +switchSide()
    +isFront()
    +getResource() 
    'abstract
}

Card <|-- GameCard
Card <|-- StarterCard

abstract class GameCard {
    -Resource resource
    -int pointsWon
    +getResource()
    +getPoints()
}

GameCard <|-- ResourceCard
GameCard <|-- GoldCard

class ResourceCard {

}

abstract class GoldCard {
    -ArrayList<Resource> neededResources
    +getNeededResources()
}

GoldCard <|-- SimpleGoldCard
GoldCard <|-- PositionalGoldCard
GoldCard <|-- ObjectGoldCard

class SimpleGoldCard {

} 

class PositionalGoldCard {
    +getPoints(PlayerBoard)
    
}

class ObjectGoldCard {
    -GameObject multiplier
    +getPoints()
}

class StarterCard {
    -ArrayList<Resource> resources
    +getResource()
}

Card "2" -- "1" Side

class Side {
    -Optional<Corner> topRight
    -Optional<Corner> topLeft
    -Optional<Corner> bottomLeft
    -Optional<Corner> bottomRight
    +getCorners()
}

Side "1..4" -- "1" Corner

class Corner<T>{
    -T item
    -boolean isCovered
    +getItem()
    +isCovered()
    +setCovered()
}

class ObjectGoldCard {
       -GameObject multiplicator
       +getPoints()
}

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

'CardBoard 
@enduml
