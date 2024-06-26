@startuml
    group Choose Objective [ok]
    ClientA -> Server : setPlayerObjective(playerName, card)
    rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].setPlayerObjective(playerName, card)
    Server -> ClientA : {status: "success"}, VirtualViewUpdate
    ClientB -> Server : setPlayerObjective(playerName, card)
    rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].validatePlayerTurn(playerName)
    Server -> ClientB : {status: failed, message: "It's not ClientB's turn"}
end

group Choose Objective [failed due to wrong game status]
     ClientA -> Server : setPlayerObjective(playerName, card)
     rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].setPlayerObjective(playerName, card)
     Server -> ClientA : {status: "failed", message: "Cannot choose Objective Card in current game status"}
end
@enduml
