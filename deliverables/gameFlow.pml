@startuml

[*] --> WAIT_FOR_PLAYERS
WAIT_FOR_PLAYERS --> WAIT_FOR_PLAYERS: !isGameStarted()
WAIT_FOR_PLAYERS --> initialization_phase: isGameStarted()

state initialization_phase {
    [*] --> INIT_PLACE_STARTER_CARD
    INIT_PLACE_STARTER_CARD --> INIT_DRAW_CARD: Starter card placed
    INIT_DRAW_CARD --> INIT_DRAW_CARD: !(2 Resource cards and 1 Gold card drawn)
    INIT_DRAW_CARD --> INIT_CHOOSE_OBJECTIVE_CARD: (2 Resource cards and 1 Gold card drawn)
    INIT_CHOOSE_OBJECTIVE_CARD --> [*]
}
initialization_phase --> initialization_phase: !(all players have played their turn)
initialization_phase --> player_turn
state player_turn {
    [*] --> PLACE_CARD
    PLACE_CARD --> DRAW_CARD: Card placed
    DRAW_CARD --> [*]: Card drawn
}
player_turn --> player_turn
@enduml
