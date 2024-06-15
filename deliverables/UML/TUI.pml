@startuml
skinparam linetype ortho

class Controller {
    -TUIView view
    -SceneEnum currentScene


}

class ClientNetworkControllerMapper {
    -TUIView view
}

class SceneEnum {
    -HomeScene
    -GameScene
}

class TUIView{
    Scene currentScene
    void displayScene(SceneEnum scene)
}
TUIView *-- Scene
interface Scene {
    void display()
}

class HomeScene implements Scene {
    +HomeScene(GameControllerView view)
}
note right of HomeScene: Mostra i giochi disponibili\nPermette di scegliere il game

class GameScene extends DrawArea implements Scene {
    GameControllerView view
}
GameScene *-- PlayerInventoryComponent
GameScene *-- PlayerBoardComponent
note left of GameScene: shows player's board, hand and objective

interface Drawable {
    +int getHeight()
    +int getWidth()
    +Char[][] getDrawedArea()
}

abstract class DrawArea implements Drawable {
    #Char[][] drawArea
    #void drawAt(int x, int y, Char[][] drawArea)
    +Char[][] getDrawableArea()
}
note top of DrawArea: Qui vengono posizionate le varie Drawable da visualizzare

class PlayerInventoryComponent extends DrawArea {
    -DrawArea drawArea
    +public PlayerInventoryComponent(PlayerHandView, playerObjective, globalObjectives)
}
note bottom of PlayerInventoryComponent: Mostra la hand e l'objective

class PlayerBoardComponent extends DrawArea {
    -DrawArea drawArea
    +public PlayerBoardComponent(PlayerView)
}
note bottom of PlayerBoardComponent: Mostra la playerboard, il nome, le risorse e la posizione del giocatore

class PlayerHandComponent extends DrawArea {
    -DrawArea drawArea
    +public PlayerHandComponent(PlayerHandView)
}
PlayerInventoryComponent *-- PlayerHandComponent

class GlobalObjectivesComponent extends DrawArea {
    -DrawArea drawArea
    +public GlobalObjectivesComponent(GameView)
}
PlayerInventoryComponent *-- GlobalObjectivesComponent

class GameCardComponent implements Drawable {
    -Char[][] drawArea
    +public GameCardComponent(GameCard)
}

class ObjectiveCardComponent implements Drawable {
    -Char[][] drawArea
    +public ObjectiveCardComponent(ObjectiveCard)
}
@enduml