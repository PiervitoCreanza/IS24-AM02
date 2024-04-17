@startuml
    group Choose Objective [ok]
    ClientA -> Server : chooseObjective(playerName, playerColor)
    Server -> ClientA : {status: "success"}
    ClientB -> Server : choosePlayerColor(gameName, playerName)
    Server -> ClientB : {status: failed, message: "It's not ClientB's turn"}
end

group Choose Objective [failed due to wrong game status]
     ClientA -> Server : choosePlayerColor(playerName, playerColor)
     Server -> ClientA : {status: "failed", message: "Cannot choose objective card in current game status"}
end

@enduml
