@startuml
    group Join Game [ok]
    ClientA -> Server : joinGame(gameName, playerName)
    Server -> ClientA : {status: "success"}
end

group Join Game [failed due to game full]
    ClientA -> Server : joinGame(gameName, playerName)
    Server -> ClientA : {status: "failed", message: "The game is full"}
end

group Join Game [failed due to player already existing]
    ClientA -> Server : joinGame(gameName, playerName)
    Server -> ClientA : {status: "failed", message: "The game already has the player playerName"}
end

group Join Game [failed due to game not existing]
    ClientA -> Server : joinGame(gameName, playerName)
    Server -> ClientA : {status: "failed", message: "A game with the name gameName doesn't exist"}
end

@enduml
