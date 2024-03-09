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
  Game "1" -- "2..*" Player
  
  class Board {
      ' Rappresenta l'area di gioco "globale" dove si muovono i giocatori. Necessaria? Possiamo salvare la posizione direttamente nella classe utente.
  
  }
  
  class PlayerBoard {
      ' Rappresenta l'area di gioco del singolo giocatore
      
      +getCard(pos)
      +getResourceAmount(Resource)
      +getObjectAmount(Object)
      +placeCard(GameCard) 
      'placeCard(GameCard) --> GameCard.isPlaceable() --> (sì) --> piazzamento, (no) --> ritorna errore
  }
  PlayerBoard "1" -- "0..*" PlaceableCard
  Player "1" -- "1" PlayerBoard
  
  interface Card {
      'Rappresenta le carte da gioco
  }
  
  enum Side {
      Front
      Back
  }
  Side -- PlaceableCard
  
  enum Resource {
    Plant
    Animal
    Fungi
    Insect
  }
  
  enum Object {
     Quill
     Inkwell
     Manuscript 
  }
  
  class Corner {
      -isPresent
      -isCovered
      -type 
      '(Resource enum or Object enum)
      
      +getType()
  }
  PlaceableCard "1" -- "4" Corner
  
  abstract class PlaceableCard {
      ' Rappresenta le carte oro, le risorse e le starter cards
      -Corner topRight
      -Corner topLeft
      -Corner bottomRight
      -Corner bottomLeft
  
      -Side side
  
      +setSide(Side)
      +isPlaceable(pos, PlayerBoard)
      +getTypes()
  }
  Card <|.. PlaceableCard
  
  abstract class GameCard {
      ' Rappresenta le carte oro e le risorse
      -Resource mainResource
      
      +getGameResource()
  }
  PlaceableCard <|.. GameCard
  
  class GoldCard {
      -ArrayList neededResources[]
      -Integer pointsWon
      -Resource resource
  }
  GameCard <|.. GoldCard
  
  class ResourceCard {
  
  }
  GameCard <|.. ResourceCard
  
  
  class StarterCard {
      ' Rappresenta le carte iniziali
      -ArrayList Resource[] backResources
  }
  PlaceableCard <|.. StarterCard
  
  abstract class ObjectiveCard {
      ' Carte obiettivo
      -Integer pointsWon
      +getPoints()
  }
  Card <|.. ObjectiveCard
  
  class PositionalObjective {
      'Contiene una matrice 2x2 di GameCard
  }
  ObjectiveCard <|.. PositionalObjective
  
  class ObjectObjective {
  
  }
  ObjectiveCard <|.. ObjectObjective
  
  class Hand {
      ' La mano del giocatore
      +addCard()
      +removeCard()
  }
  Hand "0..1" -- "3" PlaceableCard
  
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
      +advance(int steps) 
      'Numero di passi di cui avanzare
  }
}
@enduml