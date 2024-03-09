@startuml IS24-AM02
'TODO find a superclass name for Resource and Object
'TODO find a superclass for gold and resource cards
namespace Model {
  class Game {
      'Gestione giocatori e inizio/fine partita
      +startGame()
      +stopGame()
      +addPlayer()
      +removePlayer()
  }
  Game "1" -- "2..4" Player
  class PlayerBoard {
      ' Rappresenta l'area di gioco del singolo giocatore
      +getCard(pos)
      +getPlacedCards()
      +getResourceAmount(Resource)
      +getObjectAmount(Object)
      +placeCard(GameCardSide)
      'placeCard(GameCardSide) --> GameCardSide.isPlaceable() --> (sì) --> piazzamento, (no) --> ritorna errore
  }
  PlayerBoard "1" -- "0..*" PlaceableSide
  Player "1" -- "1" PlayerBoard
  interface Card {
      'Rappresenta le carte da gioco
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
  abstract class Corner {
      -boolean isCovered
      +getGameObject()
      +getGameResource()
  }
  class CornerObject {
        -GameObject gameObject
        +getGameObject()
  }
  Corner <|-- CornerObject
  class CornerResource {
        -Resource resource
        +getGameResource()
  }
  Corner <|-- CornerResource
  class EmptyCorner {
  }
  Corner <|-- EmptyCorner

  abstract class DoubleSidedCard {
    -PlaceableSide front
    -PlaceableSide back
    -isFront
    +setFront()
  }

  class DoubleSidedGameCard {
    -Resource mainResource
  }
  DoubleSidedCard <|-- DoubleSidedGameCard

  class DoubleSidedStarterCard {
    -ArrayList Resource[] backResources
  }
  DoubleSidedCard <|-- DoubleSidedStarterCard
    
  PlaceableSide "1" -- "4" Corner
  abstract class PlaceableSide {
      ' Rappresenta le carte oro, le risorse e le starter cards
      -Optional<Corner> topRight
      -Optional<Corner> topLeft
      -Optional<Corner> bottomRight
      -Optional<Corner> bottomLeft
      +isPlaceable(pos, PlayerBoard)
  }
  Card <|-- PlaceableSide
  DoubleSidedCard "1"*-- "2" PlaceableSide
  abstract class GameCardSide {
      ' Rappresenta le carte oro e le risorse
      -Integer pointsWon
      +getGameResource()
      +getPoints()
  }
  PlaceableSide <|-- GameCardSide
  abstract class GoldCard {
      -ArrayList<Resource> neededResources
  }
  GameCardSide <|-- GoldCard
  class ResourceCard {
  }
  GameCardSide <|-- ResourceCard
  class SimpleGoldCard {
  }
  GoldCard <|-- SimpleGoldCard
  class PositionalGoldCard {
       +getPoints()
  }
  GoldCard <|-- PositionalGoldCard
  class ObjectGoldCard {
       -GameObject multiplicator
       +getPoints()
  }
  GoldCard <|-- ObjectGoldCard
  class StarterCard {
      ' Rappresenta le carte iniziali
      
  }
  PlaceableSide <|-- StarterCard
  abstract class ObjectiveCard {
      ' Carte obiettivo
      -Integer pointsWon
      +getPoints()
  }
  Card <|-- ObjectiveCard
  class PositionalObjective {
      'Contiene una matrice 2x2 di GameCardSide
  }
  ObjectiveCard <|-- PositionalObjective
  class ObjectObjective {
  }
  ObjectiveCard <|-- ObjectObjective
  class Hand {
      ' La mano del giocatore
      +addCard()
      +removeCard()
  }
  Hand "0..1" -- "3" PlaceableSide
  class Deck {
      ' Mazzo di carte (istanziato una volta per ogni tipo di carta)
      +drawCard()
  }
  Deck "1" -- "1..*" Card
  class Player {
      'Ha la sua PlayerBoard, la sua hand, il suo numero di risorse attuali, la sua posizione sul tabellone e il suo obiettivo.
      +getPlayerBoard()
      +getPlayerPos()
      +getPlayerObjective()
      +setPlayerObjective()
      +advance(int steps)
      'Numero di passi di cui avanzare
  }
}
@enduml