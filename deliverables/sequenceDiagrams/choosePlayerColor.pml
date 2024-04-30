@startuml

group "Choose Player Color [ok & failed due to unallowed player]"
    ClientA -> Server : choosePlayerColor(playerName, playerColor)
    rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].choosePlayerColor(playerName, playerColor)
    Server -> ClientA : {status: "success"}, VirtualViewUpdate
    ClientB -> Server : choosePlayerColor(gameName, playerName)
  rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].validatePlayerTurn(playerName)
    Server -> ClientB : {status: "failed", message: "It's not ClientB's turn"}
end

group "Choose Player Color [failed due to wrong game status]"
     ClientA -> Server : choosePlayerColor(playerName, playerColor)
     rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].choosePlayerColor(playerName, playerColor)
     Server -> ClientA : {status: "failed", message: "Cannot choose Player Color in current game status"}
end

group "Choose Player Color [failed due to color already chosen]"
     ClientA -> Server : choosePlayerColor(playerName, playerColor)
     rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].choosePlayerColor(playerName, playerColor)
     Server -> ClientA : {status: "failed", message: "Color already chosen by another player"}
end


@enduml
