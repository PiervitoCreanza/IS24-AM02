@startuml
    group Place Card [ok]
    ClientA -> Server : setGameCard(coordinate, gameCard)
    Server -> ClientA : {status: "success", []}
    ClientB -> Server : setGameCard(coordinate, gameCard)
    Server -> ClientB : {status: failed, message: "It's not ClientB's turn"}
end

group Choose Objective [failed due to wrong position]
     ClientA -> Server : choosePlayerColor(playerName, playerColor)
     Server -> ClientA : {status: "failed", message: "You can't place a card here"}
end

note right of Server
Quindi rimandiamo indietro tutta la
VirtualView aggiornata?
end note

@enduml
